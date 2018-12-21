package club.moddedminecraft.polychat.networking.io;

import club.moddedminecraft.polychat.networking.util.ThreadedQueue;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public final class MessageSendQueue extends ThreadedQueue<Message> {
    private final Socket socket;
    private DataOutputStream dataOutputStream;

    public MessageSendQueue(Socket socket){
        this.socket = socket;
    }

    @Override
    protected void init() throws IOException {
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    protected void handle(Message obj) throws IOException {
        obj.send(dataOutputStream);
    }

}
