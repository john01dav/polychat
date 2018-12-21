/* This file is part of PolyChat.
 *
 * Copyright © 2018 john01dav
 *
 * PolyChat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PolyChat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Polychat. If not, see <https://www.gnu.org/licenses/>.
 */
package club.moddedminecraft.polychat.networking.io;

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
