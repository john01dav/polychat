package club.moddedminecraft.polychat.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class ChatMessage extends Message{
    protected static final short MESSAGE_TYPE_ID = 1;
    private final String prefix, username, message;

    protected ChatMessage(DataInputStream dataInputStream) throws IOException {
        prefix = dataInputStream.readUTF();
        username = dataInputStream.readUTF();
        message = dataInputStream.readUTF();
    }

    public ChatMessage(String prefix, String username, String message){
        this.prefix = prefix;
        this.username = username;
        this.message = message;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    @Override
    protected void send(DataOutputStream dataOutputStream) throws IOException{
        dataOutputStream.writeShort(MESSAGE_TYPE_ID);
        dataOutputStream.writeUTF(prefix);
        dataOutputStream.writeUTF(username);
        dataOutputStream.writeUTF(message);
    }

}
