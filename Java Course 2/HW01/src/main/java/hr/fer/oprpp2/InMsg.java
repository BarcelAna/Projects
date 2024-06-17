package hr.fer.oprpp2;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class InMsg extends Message{
    private String name;
    private String msg;

    public InMsg(long number, String name, String msg) {
        super((byte)5, number);
        this.name = name;
        this.msg = msg;
    }

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

    public String getName() {
        return name;
    }

    public String getMsg() {
        return msg;
    }
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
