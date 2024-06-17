package hr.fer.oprpp2;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class Hello extends Message {
    private String name;
    private long randKey;

    public Hello(long number, String name, Long randKey) {
        super((byte)1, number);
        this.name = name;
        this.randKey = randKey;
    }

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

    public String getName() {
        return name;
    }

    public long getRandKey() {
        return randKey;
    }

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
