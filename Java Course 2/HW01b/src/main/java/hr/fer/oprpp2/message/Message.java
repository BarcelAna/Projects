package hr.fer.oprpp2.message;

/**
 * Class Message represents message object sent between my custom server and it's clients.
 * Each message has code which goes from 1 to 5 representing type of message:
 *  1 - HELLO
 *  2 - ACK
 *  3 - BYE
 *  4 - OUTMSG
 *  5 - INMSG
 * and number of message.
 */
public class Message {
    protected byte code;
    protected long number;

    public Message(byte code, long number) {
        this.code = code;
        this.number = number;
    }

    public byte getCode() {
        return code;
    }

    public long getNumber() {
        return number;
    }
}
