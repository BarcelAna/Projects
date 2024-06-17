package hr.fer.oprpp2.message;

import java.io.*;
import java.net.DatagramPacket;

/**
 * Class which represents OUTMSG message type which can be sent between my custom server and client applications.
 * It extends Message class.
 * OutMsg message object contains information about UID and message sent from some client which has to be transferred to all other clients connected to this server.
 */
public class OutMsg extends Message{
    /**
     * Sender user identifier.
     */
    private long UID;

    /**
     * Content of sent message
     */
    private String msg;

    /**
     * Constructor which accepts message number, UID and message content
     * @param number
     * @param UID
     * @param msg
     */
    public OutMsg(long number, long UID, String msg) {
        super((byte)4, number);
        this.UID = UID;
        this.msg = msg;
    }

    /**
     * Return UID.
     * @return UID
     */
    public long getUID() {
        return UID;
    }

    /**
     * Return message content.
     * @return msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Utility method for packing OutMsg message object to appropriate datagram packet.
     * @param m
     * @return
     * @throws IOException
     */
    public static DatagramPacket packOutMsg(OutMsg m) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeByte(m.code);
        dos.writeLong(m.number);
        dos.writeLong(m.UID);
        dos.writeUTF(m.msg);
        dos.close();
        byte[] data = bos.toByteArray();

        DatagramPacket packet = new DatagramPacket(data, data.length);

        return packet;
    }

    /**
     * Utility method for unpacking datagram which contains OutMsg message.
     * @param packet
     * @return Hello object
     * @throws IOException
     */
    public static Message unpackOutMsg(DatagramPacket packet) throws IOException {
        byte[] data = packet.getData();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bis);

        byte code = dis.readByte();
        long number = dis.readLong();
        long UID = dis.readLong();
        String msg = dis.readUTF();

      return new OutMsg(number, UID, msg);
    }
}
