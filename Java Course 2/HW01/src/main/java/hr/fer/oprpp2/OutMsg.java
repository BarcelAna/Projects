package hr.fer.oprpp2;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class OutMsg extends Message{
    private long UID;
    private String msg;

    public OutMsg(long number, long UID, String msg) {
        super((byte)4, number);
        this.UID = UID;
        this.msg = msg;
    }

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

    public long getUID() {
        return UID;
    }

    public String getMsg() {
        return msg;
    }
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
