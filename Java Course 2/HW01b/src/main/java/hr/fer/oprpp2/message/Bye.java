package hr.fer.oprpp2.message;

import java.io.*;
import java.net.DatagramPacket;

/**
 * Class which represents BYE message type which can be sent between my custom server and client applications.
 * It extends Message class.
 * Bye message object contains information about sender's UID.
 */
public class Bye extends Message{
    /**
     * Sender user identifier.
     */
    private long UID;

    /**
     * Constructor which accepts message number and sender UID.
     * @param number
     * @param UID
     */
    public Bye(long number, long UID) {
        super((byte)3, number);
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
     * Utility method for packing Bye message object to appropriate datagram packet.
     * @param m
     * @return
     * @throws IOException
     */
    public static DatagramPacket packBye(Bye m) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeByte(m.code);
        dos.writeLong(m.number);
        dos.writeLong(m.UID);
        dos.close();
        byte[] data = bos.toByteArray();

        DatagramPacket packet = new DatagramPacket(data, data.length);

        return packet;
    }

    /**
     * Utility method for unpacking datagram which contains bye message.
     * @param packet
     * @return Hello object
     * @throws IOException
     */
    public static Message unpackBye(DatagramPacket packet) throws IOException {
        byte[] data = packet.getData();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bis);

        byte code = dis.readByte();
        long number = dis.readLong();
        long UID = dis.readLong();

        return new Bye(number, UID);
    }
}
