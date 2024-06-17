package hr.fer.oprpp2;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Acknowledge extends Message {
    private long UID;

    public Acknowledge(long number, long UID) {
        super((byte)2, number);
        this.UID = UID;
    }

    public static DatagramPacket packAck(Acknowledge m) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeByte(m.getCode());
        dos.writeLong(m.getNumber());
        dos.writeLong(m.getUID());
        dos.close();
        byte[] data = bos.toByteArray();

        DatagramPacket packet = new DatagramPacket(data, data.length);

        return packet;
    }

    public long getUID() {
        return UID;
    }
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
