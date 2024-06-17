package hr.fer.oprpp2;

import hr.fer.oprpp2.message.*;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * Utility class for server-client based communication.
 * It contains methods for packing Message objects to datagram packets and unpacking such packets back to Message objects.
 */
public class Util {

    /**
     * Unpacks given datagram packet and returns appropriate Message object
     * @param packet - datagram packet
     * @return - Message object representations of packet's data
     * @throws IOException - if error occurs while creating certain Message object.
     * @throws RuntimeException - if method is given packet with unsupported data.
     */
    public static Message unpack(DatagramPacket packet) throws IOException {
        int code = packet.getData()[0];
        //int code = packet.getData()[packet.getOffset()];
        switch(code) {
            case 1:
                return Hello.unpackHello(packet);
            case 2:
                return Acknowledge.unpackAck(packet);
            case 3:
                return Bye.unpackBye(packet);
            case 4:
                return OutMsg.unpackOutMsg(packet);
            case 5:
                return InMsg.unpackInMsg(packet);
        }
        throw new RuntimeException("Unsupported message received.");
    }

    /**
     * Packs the given Message object to appropriate datagram packet and returns it.
     * @param m - message
     * @return - datagram packet
     * @throws IOException - if error occurs while trying to create packet.
     * @throws RuntimeException - if given message is not supported
     */
    public static DatagramPacket pack(Message m) throws IOException {
        int code = m.getCode();
        switch(code) {
            case 1:
                return Hello.packHello((Hello)m);
            case 2:
                return Acknowledge.packAck((Acknowledge)m);
            case 3:
                return Bye.packBye((Bye)m);
            case 4:
                return OutMsg.packOutMsg((OutMsg)m);
            case 5:
                return InMsg.packInMsg((InMsg)m);
        }
        throw new RuntimeException("Unsupported message type.");
    }


}
