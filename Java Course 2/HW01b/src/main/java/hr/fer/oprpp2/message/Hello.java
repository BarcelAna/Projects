package hr.fer.oprpp2.message;

import java.io.*;
import java.net.DatagramPacket;

/**
 * Class which represents HELLO message type which can be sent between my custom server and client applications.
 * It extends Message class.
 * Hello message object contains information about sender's name and random key generated by the same client.
 */
public class Hello extends Message {
    /**
     * Name of client who sent HELLO message.
     */
    private String name;

    /**
     * Random key generated by the client who sent HELLO message.
     */
    private long randKey;

    /**
     * Constructor which accepts message number, client name and random key.
     * @param number
     * @param name
     * @param randKey
     */
    public Hello(long number, String name, Long randKey) {
        super((byte)1, number);
        this.name = name;
        this.randKey = randKey;
    }

    /**
     * Returns client name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns random key.
     * @return randKey
     */
    public long getRandKey() {
        return randKey;
    }

    /**
     * Utility method for packing Hello message object to appropriate datagram packet.
     * @param m
     * @return
     * @throws IOException
     */
    public static DatagramPacket packHello(Hello m) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeByte(m.code);
        dos.writeLong(m.number);
        dos.writeUTF(m.name);
        dos.writeLong(m.randKey);
        dos.close();
        byte[] data = bos.toByteArray();

        DatagramPacket packet = new DatagramPacket(data, data.length);

        return packet;
    }


    /**
     * Utility method for unpacking datagram which contains hello message.
     * @param packet
     * @return Hello object
     * @throws IOException
     */
    public static Message unpackHello(DatagramPacket packet) throws IOException {
        byte[] data = packet.getData();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bis);

        byte code = dis.readByte();
        long number = dis.readLong();
        String name = dis.readUTF();
        long randKey = dis.readLong();

        return new Hello(number, name, randKey);
    }
}
