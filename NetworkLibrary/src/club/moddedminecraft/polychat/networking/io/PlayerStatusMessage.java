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

public class PlayerStatusMessage extends Message {
    protected static final short MESSAGE_TYPE_ID = 3;
    private final String userName, serverID, prefixJson;
    private final boolean joined, silent;

    public PlayerStatusMessage(String userName, String serverID, String prefixJson, boolean joined, boolean silent) {
        this.userName = userName;
        this.serverID = serverID;
        this.prefixJson = prefixJson;
        this.joined = joined;
        this.silent = silent;
    }

    public PlayerStatusMessage(DataInputStream istream) throws IOException {
        this.userName = istream.readUTF();
        this.serverID = istream.readUTF();
        this.prefixJson = istream.readUTF();
        this.joined = istream.readBoolean();
        this.silent = istream.readBoolean();
    }

    public String getUserName() {
        return this.userName;
    }

    public String getServerID() {
        return this.serverID;
    }

    public boolean getJoined() {
        return this.joined;
    }

    //Added to facilitate repopulating player lists on server reconnect
    public boolean getSilent() {
        return this.silent;
    }

    public String getPrefixJson() {
        return this.prefixJson;
    }

    @Override
    protected void send(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(MESSAGE_TYPE_ID);
        dataOutputStream.writeUTF(userName);
        dataOutputStream.writeUTF(serverID);
        dataOutputStream.writeUTF(prefixJson);
        dataOutputStream.writeBoolean(joined);
        dataOutputStream.writeBoolean(silent);
    }
}
