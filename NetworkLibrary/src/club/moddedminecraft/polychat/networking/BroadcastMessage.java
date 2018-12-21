package club.moddedminecraft.polychat.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class BroadcastMessage extends Message{
    protected static final short MESSAGE_TYPE_ID = 0;
    private final String message;

    protected BroadcastMessage(DataInputStream dataInputStream) throws IOException {
        message = dataInputStream.readUTF();
    }

    public BroadcastMessage(String broadcastMessage){
        message = broadcastMessage;
    }

    public String getMessage(){
        return message;
    }

    @Override
    protected void send(DataOutputStream dataOutputStream) throws IOException{
        dataOutputStream.writeShort(MESSAGE_TYPE_ID);
        dataOutputStream.writeUTF(message);
    }

}
