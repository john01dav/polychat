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
import java.util.ArrayList;

public final class Client {
    private final MessageBus messageBus;
    private ArrayList<Message> receivedMessages;

    public Client(String serverIp, int port) throws IOException {
        messageBus = new MessageBus(new Socket(serverIp, port), this::receiveMessage);
    }

    public void start() {
        messageBus.start();
    }

    public synchronized ArrayList<Message> getReceivedMessages() {
        ArrayList<Message> messages = receivedMessages;
        receivedMessages = null;
        return messages;
    }

    private synchronized void receiveMessage(Message message) {
        if (receivedMessages == null) {
            receivedMessages = new ArrayList<>();
        }
        receivedMessages.add(message);
    }

    public void sendMessage(Message message) {
        messageBus.sendMessage(message);
    }

    public void stop() {
        messageBus.stop();
    }

}
