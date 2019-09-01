package club.moddedminecraft.polychat.networking.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PlayerListMessage extends Message {

    protected static final short MESSAGE_TYPE_ID = 5;
    private final String serverID;
    private final int listSize;
    private ArrayList<String> playerList;

    public PlayerListMessage(String serverID, ArrayList<String> playerList) {
        this.serverID = serverID;
        this.playerList = playerList;
        this.listSize = playerList.size();
    }

    public PlayerListMessage(DataInputStream istream) throws IOException {
        this.serverID = istream.readUTF();
        this.listSize = istream.readInt();
        this.playerList = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            playerList.add(istream.readUTF());
        }
    }

    public ArrayList<String> getPlayerList() {
        return playerList;
    }

    public String getServerID() {
        return serverID;
    }

    @Override
    protected void send(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(MESSAGE_TYPE_ID);
        dataOutputStream.writeUTF(serverID);
        dataOutputStream.writeInt(this.playerList.size());
        for (String player : this.playerList) {
            dataOutputStream.writeUTF(player);
        }
    }
}
