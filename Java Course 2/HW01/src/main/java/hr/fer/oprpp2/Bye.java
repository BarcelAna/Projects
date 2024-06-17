package hr.fer.oprpp2;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class Bye extends Message{
    private long UID;

    public Bye(long number, long UID) {
        super((byte)3, number);
        this.UID = UID;
    }

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

    public long getUID() {
        return UID;
    }
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
