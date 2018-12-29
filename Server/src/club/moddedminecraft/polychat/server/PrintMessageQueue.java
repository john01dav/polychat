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
package club.moddedminecraft.polychat.server;

import club.moddedminecraft.polychat.networking.io.ChatMessage;
import club.moddedminecraft.polychat.networking.io.Message;
import club.moddedminecraft.polychat.networking.io.MessageData;
import club.moddedminecraft.polychat.networking.util.ThreadedQueue;

public class PrintMessageQueue extends ThreadedQueue<MessageData> {
    @Override
    protected void init() throws Throwable {
        System.out.println("ready to print messages");
    }

    @Override
    protected void handle(MessageData messageData) throws Throwable {
        Message rawMessage = messageData.getMessage();
        if(rawMessage instanceof ChatMessage){
            ChatMessage message = ((ChatMessage) rawMessage);
            System.out.println(message.getPrefix()+message.getUsername()+message.getMessage());
            if (Main.channel != null) {
                Main.channel.sendMessage("**`" + message.getPrefix() + message.getUsername() + "`** " + message.getMessage());
            }
        }
    }
}
