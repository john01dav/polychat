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
package club.moddedminecraft.polychat.client;

import club.moddedminecraft.polychat.networking.io.BroadcastMessage;
import club.moddedminecraft.polychat.networking.io.ChatMessage;
import club.moddedminecraft.polychat.networking.io.Client;
import club.moddedminecraft.polychat.networking.io.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public final class TestClient {

    public static void main(String[] args) {
        new TestClient().run(args[0], Integer.parseInt(args[1]));
    }

    private void run(String ip, int port){
        try {
            System.out.println(ip + " " + port);
            Client client = new Client(ip, port);
            MessageHandler messageHandler = new MessageHandler(client);
            MessageSender messageSender = new MessageSender(client);
            client.start();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static final class MessageSender{
        private final Client client;

        public MessageSender(Client client){
            this.client = client;
            Thread thread = new Thread(this::run);
            thread.start();
        }

        private void run(){
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String prefix = "[" + Math.random() + "] ";
                String username = "user " + Math.random();
                while(true){
                    String message = reader.readLine();
                    client.sendMessage(new ChatMessage(prefix, username, message));
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }

    private static final class MessageHandler{
        private final Client client;

        public MessageHandler(Client client){
            this.client = client;
            Thread thread = new Thread(this::run);
            thread.start();
        }

        private void run() {
            while(true){
                ArrayList<Message> messages = client.getReceivedMessages();
                if(messages != null){
                    for(Message rawMessage : messages){
                        if(rawMessage instanceof ChatMessage){
                            ChatMessage message = ((ChatMessage) rawMessage);
                            System.out.println(message.getPrefix()+message.getUsername()+message.getMessage());
                        }else if(rawMessage instanceof BroadcastMessage){
                            BroadcastMessage message = ((BroadcastMessage) rawMessage);
                            System.out.println(message.getMessage()); //in the real independent server, you can ignore broadcast messages as you do not want those in Discord
                        }
                    }
                }
                //to simulate being run each tick, although you can easily get away with running it 1-2 times per second, as the whole API is very asynchronous so a single server delay like this won't hurt anything
                try{
                    Thread.sleep(50);
                }catch(InterruptedException e){}
            }
        }

    }


}
