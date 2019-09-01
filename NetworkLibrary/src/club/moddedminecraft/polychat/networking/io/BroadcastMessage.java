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

public final class BroadcastMessage extends Message {
    protected static final short MESSAGE_TYPE_ID = 0;
    private final String prefix, message;
    private final int prefix_color;

    protected BroadcastMessage(DataInputStream dataInputStream) throws IOException {
        prefix = dataInputStream.readUTF();
        message = dataInputStream.readUTF();
        prefix_color = dataInputStream.readInt();
    }

    public BroadcastMessage(String prefix, String broadcastMessage, int prefix_color) {
        this.prefix = prefix;
        this.message = broadcastMessage;
        this.prefix_color = prefix_color;
    }

    public String getMessage() {
        return message;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public int prefixColor() {
        return this.prefix_color;
    }

    @Override
    protected void send(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(MESSAGE_TYPE_ID);
        dataOutputStream.writeUTF(prefix);
        dataOutputStream.writeUTF(message);
        dataOutputStream.writeInt(prefix_color);
    }

}
