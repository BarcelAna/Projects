package hr.fer.oprpp2.message;

import java.io.*;
import java.net.DatagramPacket;

/**
 * Class which represents ACK message type which can be sent between my custom server and client applications.
 * It extends Message class.
 * Acknowledge message object contains information about UID to which this message will be sent.
 */
public class Acknowledge extends Message {
    /**
     * Recipient user identifier.
     */
    private long UID;

    /**
     * Constructor which accepts message number and UID
     * @param number
     * @param UID
     */
    public Acknowledge(long number, long UID) {
        super((byte)2, number);
        this.UID = UID;
    }

    /**
     * Returns UID.
     * @return UID
     */
    public long getUID() {
        return UID;
    }

    /**
     * Utility method for packing Acknowledge message object to appropriate datagram packet.
     * @param m
     * @return
     * @throws IOException
     */
    public static DatagramPacket packAck(Acknowledge m) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeByte(m.getCode());
        dos.writeLong(m.getNumber());
        dos.writeLong(m.getUID());
        dos.close();
        byte[] data = bos.toByteArray();
        //bos.close();

        DatagramPacket packet = new DatagramPacket(data, data.length);

        return packet;
    }

    /**
     * Utility method for unpacking datagram which contains acknowledge message.
     * @param packet
     * @return Hello object
     * @throws IOException
     */
    public static Message unpackAck(DatagramPacket packet) throws IOException {
        byte[] data = packet.getData();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bis);

        byte code = dis.readByte();
        long number = dis.readLong();
        long UID = dis.readLong();

        return new Acknowledge(number, UID);
    }
}
