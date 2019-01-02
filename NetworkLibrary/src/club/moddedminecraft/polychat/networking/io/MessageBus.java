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

import java.io.IOException;
import java.net.Socket;

public final class MessageBus {
    private final Socket socket;
    private final MessageReceiver messageReceiver;
    private final MessageSendQueue messageSendQueue;

    public MessageBus(Socket socket, ReceiverCallback receiverCallback){
        this.socket = socket;
        messageReceiver = new MessageReceiver(socket, receiverCallback);
        messageSendQueue = new MessageSendQueue(socket);
    }

    public void start(){
        messageReceiver.start();
        messageSendQueue.start();
    }

    public void stop(){
        messageReceiver.stop();
        messageSendQueue.stop();
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Exception closing socket!");
            e.printStackTrace();
        }
    }

    public boolean isSocketClosed() {
        return this.socket.isClosed();
    }

    public void sendMessage(Message message){
        messageSendQueue.enqueue(message);
    }

}
