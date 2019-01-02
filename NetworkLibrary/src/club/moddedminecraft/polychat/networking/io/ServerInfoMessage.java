/*
 *  This file is part of PolyChat Server.
 *  *
 *  * Copyright © 2018 DemonScythe45
 *  *
 *  * PolyChat Server is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU Lesser General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * PolyChat Server is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU Lesser General Public License
 *  * along with PolyChat Server. If not, see <https://www.gnu.org/licenses/>.
 *
 */

package club.moddedminecraft.polychat.networking.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

//Sends data to main polychat server when the game launches
//Data is used when displaying info about all servers in discord
public class ServerInfoMessage extends Message {
    protected static final short MESSAGE_TYPE_ID = 4;
    private final String serverID, serverName, serverAddress;
    private final int maxPlayers;

    public ServerInfoMessage(String serverID, String serverName, String serverAddress, int maxPlayers) {
        this.serverID = serverID;
        this.serverName = serverName;
        this.serverAddress = serverAddress;
        this.maxPlayers = maxPlayers;
    }

    public ServerInfoMessage(DataInputStream istream) throws IOException {
        this.serverID = istream.readUTF();
        this.serverName = istream.readUTF();
        this.serverAddress = istream.readUTF();
        this.maxPlayers = istream.readInt();
    }

    public String getServerID() {
        return this.serverID;
    }

    public String getServerName() {
        return this.serverName;
    }

    public String getServerAddress() {
        return this.serverAddress;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    @Override
    protected void send(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(MESSAGE_TYPE_ID);
        dataOutputStream.writeUTF(serverID);
        dataOutputStream.writeUTF(serverName);
        dataOutputStream.writeUTF(serverAddress);
        dataOutputStream.writeInt(maxPlayers);
    }
}
