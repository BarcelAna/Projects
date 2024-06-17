package hr.fer.oprpp2;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.*;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Client class represents simple client which can connect to server and exchange messages with it.
 */
public class Client {
    /**
     * Client's name
     */
    private String name;

    /**
     * Client's IP address
     */
    private InetAddress addr;

    /**
     * Client's port number
     */
    private int port;

    /**
     * Client's socket
     */
    private DatagramSocket dSocket;

    /**
     * User identifier assigned to client from server
     */
    private long UID;

    /**
     * Client's random key
     */
    private long randKey;

    /**
     * Number of packet sent from client
     */
    private long clientNumber = 0l;

    /**
     * Number of packet sent from server
     */
    private long serverNumber = 0l;

    /**
     * Queue of messages received from server
     */
    private BlockingQueue<Message> recQueue = new LinkedBlockingQueue<>();

    /**
     * List model
     */
    private DefaultListModel<String> inbox_model;

    /**
     * JList representing box for displaying messages
     */
    private JList<String> inbox;

    /**
     * Field for entering new messages
     */
    private JTextField tf;

    /**
     * Constructor which creates new Client object and initializes GUI.
     * @param name
     * @param addr
     * @param port
     * @param dSocket
     * @param randKey
     * @param UID
     */
    public Client(String name, InetAddress addr, int port, DatagramSocket dSocket, long randKey, long UID) {
        this.name = name;
        this.addr = addr;
        this.port = port;
        this.dSocket = dSocket;
        this.randKey = randKey;
        this.UID = UID;

        initGUI();
    }

    /**
     * GUI initializer.
     */
    private void initGUI() {
        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setTitle("Chat client: " + name);
        frame.setLocation(20, 20);
        frame.setSize(500, 300);

        frame.setLayout(new BorderLayout());
        tf = new JTextField();
        frame.getContentPane().add(tf, BorderLayout.PAGE_START);
        inbox_model = new DefaultListModel<>();
        inbox = new JList<>(inbox_model);
        frame.getContentPane().add(new JScrollPane(inbox));

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    shutdown();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });

        tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER) {
                    String message = tf.getText().trim();
                    sendMessage(message);
                    tf.setText("");
                }
            }
        });

        new Thread(()->receive()).start();

        frame.setVisible(true);
    }

    /**
     * Main program for reading input arguments, connecting to server and starting client gui app.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if(args.length != 3) {
            System.out.println("Expected input is: ip, port, name");
            return;
        }

        String ip = args[0];
        InetAddress addr;
        try {
            addr = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return;
        }

        int port;
        try{
            port = Integer.parseInt(args[1]);
        } catch(NumberFormatException e) {
            System.out.println("Second argument must be a number.");
            return;
        }

        if(port < 1 || port > 65535) {
            System.out.println("Port number must be a number between 1 and 65535");
            return;
        }

        String name = args[2];

        DatagramSocket dSocket;
        try {
            dSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }

        long randKey = new Random().nextLong();
        long UID = 0L;

        Hello h = new Hello(0l, name, randKey);
        DatagramPacket packet = null;
        try {
            packet = Hello.packHello(h);
        } catch (IOException e) {
            e.printStackTrace();
            dSocket.close();
            return;
        }
        packet.setAddress(addr);
        packet.setPort(port);

        Long result = sendPacket(packet, dSocket);

        if(result == null) {
            System.out.println("Can't make connection to server.");
            dSocket.close();
            return;
        }

        UID = result;

        System.out.println("Connection successfully established!");

        startClient(name, addr, port, dSocket, randKey, UID);

    }

    /**
     * Sends given packet to the server and waits for it's acknowledge.
     * @param packet
     * @param dSocket
     * @return client UID assigned by server or null if packet is not sent successfully.
     * @throws IOException
     */
    private static Long sendPacket(DatagramPacket packet, DatagramSocket dSocket) throws IOException {
        int retransmissions = 10;
        Message m = null;
        boolean statusOK = false;

        while(retransmissions > 0) {

            --retransmissions;

            System.out.println("Sending packet...");

            dSocket.send(packet);

            byte[] recvData = new byte[1024];
            DatagramPacket recvPacket = new DatagramPacket(recvData, recvData.length);

            dSocket.setSoTimeout(5000);

            System.out.println("Waiting for ack...");
            try {
                dSocket.receive(recvPacket);
            } catch(SocketTimeoutException e) {
                if(retransmissions <= 0) {
                    return null;
                }
                continue;
            }

            try{
                m = Util.unpack(recvPacket);
            } catch(RuntimeException e) {
                if(retransmissions <= 0) {
                    return null;
                }
                continue;
            }

            if(m.getCode()==2) {
                System.out.println("Ack packet received...");
                statusOK = true;
                break;
            }
        }
        if(statusOK) {
            return ((Acknowledge)m).getUID();
        }

        else return null;
    }

    /**
     * Starts GUI app.
     * @param name
     * @param addr
     * @param port
     * @param dSocket
     * @param randKey
     * @param UID
     */
    private static void startClient(String name, InetAddress addr, int port, DatagramSocket dSocket, long randKey, long UID) {
        SwingUtilities.invokeLater(()->{
            new Client(name, addr, port, dSocket, randKey, UID);
        });
    }

    /**
     * Sends BYE packet to server.
     * @throws IOException
     */
    private void shutdown() throws IOException {
        Bye b = new Bye(++this.clientNumber, UID);
        DatagramPacket packet = Util.pack(b);
        packet.setAddress(addr);
        packet.setPort(port);

        dSocket.send(packet);
        dSocket.close();
    }

    /**
     * Receives packets from server and process it.
     */
    private void receive() {
        while(true) {
            byte[] recvData = new byte[1024];
            DatagramPacket recvPacket = new DatagramPacket(recvData, recvData.length);

            try {
                dSocket.setSoTimeout(5000);
            }  catch (SocketException e) {
                continue;
            }

            try {
                dSocket.receive(recvPacket);
            } catch(SocketTimeoutException e) {
                continue;
            } catch (IOException e) {
                continue;
            }

            Message m;
            try {
                m = Util.unpack(recvPacket);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            try{
                process(m);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Hands given message to appropriate method based on message type.
     * @param m
     * @throws IOException
     */
    private void process(Message m) throws IOException {
        switch(m.getCode()) {
            case 2:
                processAck((Acknowledge)m);
                return;
            case 5:
                processInMsg((InMsg)m);
                return;
        }
        throw new RuntimeException("Unexpected message type received.");
    }

    /**
     * Sends message to SwingWorker and sends ack packet to the server.
     * @param m
     * @throws IOException
     */
    private void processInMsg(InMsg m) throws IOException {
        boolean statusOK = false;

        if(m.getNumber() != this.serverNumber + 1l) {
            System.out.println("Wrong packet number.");
        } else {
            statusOK = true;
            this.serverNumber++;
        }

        if(statusOK) {
            String message = "<HTML>["+addr+":"+port+"] Poruka od korisnika: " + m.getName() + "<BR>" + m.getMsg();
            SwingUtilities.invokeLater(()->inbox_model.add(inbox_model.size(), message));
        }

        Acknowledge a = new Acknowledge(m.getNumber(), UID);
        DatagramPacket packet = Util.pack(a);
        packet.setAddress(addr);
        packet.setPort(port);
        dSocket.send(packet);
    }

    /**
     * Puts given ACK message to receive queue.
     * @param m
     */
    private void processAck(Acknowledge m) {
        try {
            recQueue.put(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts SwingWorker which displays given message in inbox.
     * @param message
     */
    private void sendMessage(String message) {
        new Sender(message).execute();
    }

    private class Sender extends SwingWorker<Void, Void> {
        String message;
        public Sender(String message) {
            this.message = message;
        }

        @Override
        protected Void doInBackground() throws Exception {
            OutMsg o = new OutMsg(++clientNumber, UID, message);
            DatagramPacket packet = Util.pack(o);
            packet.setAddress(addr);
            packet.setPort(port);

            Long result = sendPacket(packet, dSocket);
            if(result==null) {
                System.out.println("Can't sent OUTMSG");
            }
            return null;
        }
    }


}
