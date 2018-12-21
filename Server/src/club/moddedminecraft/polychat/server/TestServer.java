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

import club.moddedminecraft.polychat.networking.io.*;
import club.moddedminecraft.polychat.networking.util.ThreadedQueue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class TestServer{

    public static void main(String[] args) {
        new TestServer().run(Integer.parseInt(args[0]));
    }

    private void run(int port){
        System.out.println(port);
        Server server = new Server(port);
        server.addMessageProcessor(new PrintMessageQueue());
        MessageSender messageSender = new MessageSender(server);
        server.start();
    }

    private static final class MessageSender{
        private final Server server;

        public MessageSender(Server server){
            this.server = server;
            Thread thread = new Thread(this::run);
            thread.start();
        }

        private void run(){
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                while(true){
                    String message = reader.readLine();
                    server.sendMessage(new BroadcastMessage(message));
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }

    private class PrintMessageQueue extends ThreadedQueue<MessageData>{
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
            }else if(rawMessage instanceof BroadcastMessage){
                BroadcastMessage message = ((BroadcastMessage) rawMessage);
                System.out.println(message.getMessage()); //in the real independent server, you can ignore broadcast messages as you do not want those in Discord
            }
        }
    }

}
