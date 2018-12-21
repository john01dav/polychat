package club.moddedminecraft.polychat.networking.io;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Message {

    protected abstract void send(DataOutputStream dataOutputStream) throws IOException;

}
