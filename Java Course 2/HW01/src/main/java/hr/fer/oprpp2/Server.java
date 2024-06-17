package hr.fer.oprpp2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Class Server represents simple server program which listens through the given port.
 */
public class Server {
    /**
     * Datagram socket for this server
     */
    private DatagramSocket dSocket;

    /**
     * Port number on which the server listens for messages from clients
     */
    private int port;

    /**
     * List of clients connected to this server and it's data
     */
    private List<ClientData> clients;

    /**
     * UID for the next client
     */
    private AtomicLong nextUID = new AtomicLong(new Random().nextInt() & 0xFFFFFFFFL);


    /**
     * Constructor which accepts port number and creates Server object on given port.
     * @param port
     */
    public Server(int port) {
        this.port = port;
        this.clients = new ArrayList<>();
    }

    /**
     * Main program for reading input arguments and starting server on given port.
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Expected input is: port");
            return;
        }

        int port;

        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Argument must be a number.");
            return;
        }

        if (port < 1 || port > 65535) {
            System.out.println("Port must be a number between 1 and 65535");
            return;
        }

        Server server = new Server(port);
        server.listen();
    }

    /**
     * Receives new message, reads it and process it according to the message type.
     */
    private void listen() {
        try{
            this.dSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            System.out.println("Can't open server socket.");
            return;
        }

        System.out.println("Listening on port " + port + "...");
        while(true) {
            byte[] recvData = new byte[1024];
            DatagramPacket recvPacket = new DatagramPacket(recvData, recvData.length);

            try{
                dSocket.receive(recvPacket);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            Message m = null;
            try {
                m = Util.unpack(recvPacket);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            try {
                process(m, recvPacket.getAddress(), recvPacket.getPort());
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
    }

    /**
     * Hands given Message object to appropriate process method based on message code.
     * @param m - message
     * @param addr - sender's IP address
     * @param port - sender's port
     * @throws IOException - if error occurs while processing given message
     * @throws RuntimeException - if given message is not supported
     */
    private void process(Message m, InetAddress addr, int port) throws IOException {
        switch(m.getCode()) {
            case 1:
                processHello((Hello)m, addr, port);
                return;
            case 2:
                processAck((Acknowledge)m, addr, port);
                return;
            case 3:
                processBye((Bye)m, addr, port);
                return;
            case 4:
                processOutMsg((OutMsg)m, addr, port);
                return;
        }
        throw new RuntimeException("Unsupported message type.");
    }

    /**
     * Method for handling received HELLO message from client.
     * First it checks whether connection to that client is already established.
     * If so, it just returns ACK packet to the client. If not, it creates new ClientData record, saves it to it's list of clients and then returns ACK packet.
     * @param m - hello message
     * @param addr - client's IP address
     * @param port - client's port
     * @throws IOException - if error occurs while processing hello message
     */
    private void processHello(Hello m, InetAddress addr, int port) throws IOException {
        ClientData clientData = null;
        //ZAŠTO MORA BITI SYNCHRONIZED
        synchronized (clients) {
            clientData = findClientByKey(m.getRandKey(), addr, port);
            if(clientData==null) {
                clientData = new ClientData();
                clientData.port = port;
                clientData.address = addr;
                clientData.randKey = m.getRandKey();
                clientData.name = m.getName();
                clientData.UID = this.nextUID.getAndIncrement();

                clients.add(clientData);

                ClientData workerData = clientData;
                clientData.worker = new Thread(()->worker(workerData));
                clientData.worker.start();
            } else if(clientData.inCounter > 0L || clientData.outCounter > 0l) {
                System.out.println("ERROR: Hello packet sent from this client does not have correct number");
                return;
            }
        }

        Acknowledge a = new Acknowledge(m.getNumber(), clientData.UID);
        DatagramPacket packet = Util.pack(a);
        packet.setAddress(addr);
        packet.setPort(port);
        dSocket.send(packet);
    }

    /**
     * Method for handling received ACK message from client.
     * It first finds client from which this message is sent.
     * If such client exists, ack message is added to the queue of received messages.
     * @param m - received ack message
     * @param addr - client's IP address
     * @param port - client's port number
     */
    private void processAck(Acknowledge m, InetAddress addr, int port) {
        ClientData clientData = null;
        //ZAŠTO SYNCHRONIZED?
        synchronized(clients) {
            clientData = findClientByUID(m.getUID(), addr, port);
            if(clientData == null) {
                return;
            }
        }
        clientData.recQueue.add(m);
    }

    /**
     * Method for handling received BYE message from client.
     * It first finds client from which this message is being sent from.
     * If such client exists, connection to this client is set to invalid and ACK packet is sent to it.
     * @param m - received bye message
     * @param addr - client's IP address
     * @param port - client's port number
     * @throws IOException - if error occurs while sending ack packet back to the client.
     */
    private void processBye(Bye m, InetAddress addr, int port) throws IOException {
        ClientData clientData = null;
        synchronized (clients) {
            clientData = findClientByUID(m.getUID(), addr, port);
            if(clientData==null) {
                return;
            }
        }
        if(m.getNumber()!=clientData.inCounter+1L) {
            System.out.println("Bye packet does not have expected number. Sending acknowledge but ignoring packet.");
        } else {
            clientData.inCounter++;
            clientData.invalid=true;
        }

        //šta bi bilo kad bi ovo samo stavili u red poruka za slanje klijentu?
        Acknowledge a = new Acknowledge(m.getNumber(), m.getUID());
        DatagramPacket packet = Util.pack(a);
        packet.setAddress(addr);
        packet.setPort(port);
        dSocket.send(packet);
    }

    /**
     * Handles OUTMSG received from client.
     * First it checks if such client exists. Then server sends INMSG packet to all it's clients and ACK to this client.
     * @param m - OUTMSG message
     * @param addr - client's IP address
     * @param port - client's port number
     * @throws IOException - if error ocurrs while sending messages to clients
     */
    private void processOutMsg(OutMsg m, InetAddress addr, int port) throws IOException {
        ClientData clientData = null;
        synchronized (clients) {
            clientData = findClientByUID(m.getUID(), addr, port);
            if(clientData == null) {
                return;
            }
        }

        if(clientData.invalid) {
            return;
        }

        boolean numberOK = true;

        if(m.getNumber()!=clientData.inCounter+1L) {
            System.out.println("OutMsg packet does not have expected number. Sending acknowledge but ignoring packet.");
            numberOK = false;
        } else {
            clientData.inCounter++;
        }

        if(numberOK) {
            sendInMsg(clientData, m.getMsg());
        }

        Acknowledge a = new Acknowledge(m.getNumber(), m.getUID());
        DatagramPacket packet = Util.pack(a);
        packet.setAddress(addr);
        packet.setPort(port);
        dSocket.send(packet);

    }

    /**
     * Sends INMSG to all connected clients.
     * @param clientData - connection's data
     * @param msg - message sent from the client
     */
    private void sendInMsg(ClientData clientData, String msg) {
        synchronized (clients) {
            for(ClientData c : clients) {
                try {
                    c.sendQueue.put(new InMsg(++c.outCounter, clientData.name, msg));
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Utility method for finding client by the given key, IP address and port number.
     * @param randKey - key
     * @param addr - client's IP address
     * @param port - client's port number
     * @return ClientData object if such client exists, null otherwise
     */
    private ClientData findClientByKey(long randKey, InetAddress addr, int port) {
        for(ClientData c : clients) {
            if(c.randKey==randKey && c.address.equals(addr) && c.port == port) {
                return c;
            }
        }
        return null;
    }

    /**
     * Utility method for finding client by the given UID, IP address and port number.
     * @param uid - user identifier
     * @param addr - client's IP address
     * @param port - client's port number
     * @return ClientData object if such client exists, null otherwise
     */
    private ClientData findClientByUID(long uid, InetAddress addr, int port) {
        for(ClientData c: clients) {
            if(c.UID==uid && c.address.equals(addr) && c.port==port) {
                return c;
            }
        }
        return null;
    }

    /**
     * Worker thread's method for reading messages from the queue and sending them to the client.
     * @param workerData - connection data
     */
    private void worker(ClientData workerData) {
        try{
            while(true) {
                try {
                    do{
                        Message m = workerData.sendQueue.take();
                        sendToClient(m, workerData);
                    } while(!workerData.invalid);
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        synchronized (clients) {
            clients.remove(workerData);
        }
    }

    /**
     * Method for sending given message to the given client and receiving ACK message back from the client.
     * @param m - message to be sent
     * @param workerData - connection data
     * @throws IOException
     */
    private void sendToClient(Message m, ClientData workerData) throws IOException {
        DatagramPacket packet = Util.pack(m);
        packet.setAddress(workerData.address);
        packet.setPort(workerData.port);

        int retransmissions = 10;
        while(true) {
            --retransmissions;

            try{
                dSocket.send(packet);
            } catch(IOException e) {
                e.printStackTrace();
                if(retransmissions <= 0) {
                    workerData.invalid = true;
                    return;
                }
                continue;
            }

            Message recvM = null;
            try {
                recvM = workerData.recQueue.poll(5L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {

                if(retransmissions <= 0) {
                    workerData.invalid = true;
                    return;
                }

                e.printStackTrace();
                continue;
            }

            if(recvM==null) {
                //jel ne bi ovdje trebala ići provjera jel ratransmissions <= 0
                if(retransmissions <= 0) {
                    workerData.invalid = true;
                    return;
                }
                continue;
            }

            if(recvM.getCode()==(byte)2) {
                if(recvM.getNumber() != m.getNumber()) {
                    System.out.println("Received packet number is not the one expected.");
                    if(retransmissions <= 0) {
                        workerData.invalid = true;
                        return;
                    }
                    //workerData.recQueue.add(recvM);
                    continue;
                }
                break;
            }
            System.out.println("Unexpected message type received.");

        }

    }

    /**
     * Class ClientData represents all important information about connection between server and some client.
     */
    public static class ClientData {
        /**
         * flag which shows if this connection is invalid
         */
        private boolean invalid = false;

        /**
         * number of packets sent from the client
         */
        private long inCounter = 0l;

        /**
         * number of packets sent from the server
         */
        private long outCounter = 0l;

        /**
         * client's key
         */
        private long randKey;

        /**
         * client's user identifier
         */
        private long UID;

        /**
         * client's name
         */
        private String name;

        /**
         * client's IP address
         */
        private InetAddress address;

        /**
         * client's port number
         */
        private int port;

        /**
         * Queue of received acknowledgments.
         */
        private BlockingQueue<Message> recQueue = new LinkedBlockingQueue<>();

        /**
         * Queue of messages which should be sent to the client
         */
        private BlockingQueue<Message> sendQueue = new LinkedBlockingQueue<>();

        /**
         * Thread which handles message delivery from server to client.
         */
        private Thread worker;
    }

}
