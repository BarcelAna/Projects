package hr.fer.oprpp2.message;

import java.io.*;
import java.net.DatagramPacket;

/**
 * Class which represents INMSG message type which can be sent between my custom server and client applications.
 * It extends Message class.
 * InMsg message object contains information about sender's name and message content.
 */
public class InMsg extends Message{
    /**
     * Sender name
     */
    private String name;

    /**
     * Message content
     */
    private String msg;

    /**
     * Constructor which accepts message number, sender name and message content.
     * @param number
     * @param name
     * @param msg
     */
    public InMsg(long number, String name, String msg) {
        super((byte)5, number);
        this.name = name;
        this.msg = msg;
    }

    /**
     * Returns sender name.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Return message content.
     * @return message content
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Utility method for packing InMsg message object to appropriate datagram packet.
     * @param m
     * @return
     * @throws IOException
     */
    public static DatagramPacket packInMsg(InMsg m) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeByte(m.code);
        dos.writeLong(m.number);
        dos.writeUTF(m.name);
        dos.writeUTF(m.msg);
        dos.close();
        byte[] data = bos.toByteArray();

        DatagramPacket packet = new DatagramPacket(data, data.length);

        return packet;
    }

    /**
     * Utility method for unpacking datagram which contains InMsg message.
     * @param packet
     * @return Hello object
     * @throws IOException
     */
    public static Message unpackInMsg(DatagramPacket packet) throws IOException {
        byte[] data = packet.getData();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bis);

        byte code = dis.readByte();
        long number = dis.readLong();
        String name = dis.readUTF();
        String msg = dis.readUTF();

        return new InMsg(number, name, msg);
    }
}
