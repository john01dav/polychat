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

import club.moddedminecraft.polychat.networking.util.ThreadedQueue;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public final class MessageSendQueue extends ThreadedQueue<Message> {
    private final Socket socket;
    private DataOutputStream dataOutputStream;

    public MessageSendQueue(Socket socket) {
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
