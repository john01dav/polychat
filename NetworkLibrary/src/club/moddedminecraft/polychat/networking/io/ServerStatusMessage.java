/*
 *  This file is part of PolyChat.
 *  *
 *  * Copyright © 2018 DemonScythe45
 *  *
 *  * PolyChat is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU Lesser General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * PolyChat is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU Lesser General Public License
 *  * along with PolyChat. If not, see <https://www.gnu.org/licenses/>.
 *
 */

package club.moddedminecraft.polychat.networking.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

//Used for broadcasting server online/offline events
public class ServerStatusMessage extends Message {
    protected static final short MESSAGE_TYPE_ID = 2;
    private final String serverID;
    private final short state;

    public ServerStatusMessage(String serverID, short state) {
        this.serverID = serverID;
        this.state = state;
    }

    public ServerStatusMessage(DataInputStream istream) throws IOException {
        this.serverID = istream.readUTF();
        this.state = istream.readShort();
    }

    public String getServerID() {
        return this.serverID;
    }

    public short getState() {
        return this.state;
    }

    @Override
    protected void send(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(MESSAGE_TYPE_ID);
        dataOutputStream.writeUTF(serverID);
        dataOutputStream.writeShort(state);
    }
}
