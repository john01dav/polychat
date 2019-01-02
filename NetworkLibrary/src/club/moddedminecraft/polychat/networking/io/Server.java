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

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Server {
    private final int port;
    private final CopyOnWriteArrayList<ThreadedQueue<MessageData>> messageProcessors;
    private final CopyOnWriteArrayList<MessageBus> messageBuses;
    private Thread listenerThread;

    public Server(int port){
        this.port = port;
        messageProcessors = new CopyOnWriteArrayList<>();
        messageBuses = new CopyOnWriteArrayList<>();
        addMessageProcessor(new MessageDispatcherQueue());
    }

    public void start(){
        listenerThread = new Thread(this::listenerThread);
        listenerThread.start();
    }

    public void stop(){
        listenerThread.interrupt();
        for (MessageBus bus : messageBuses) {
            bus.stop();
        }
    }

    public void sendMessage(Message message){
        ArrayList<MessageBus> toRemove = new ArrayList<>();
        for(MessageBus messageBus : messageBuses){
            if (messageBus.isSocketClosed()) {
                toRemove.add(messageBus);
                messageBus.stop();
            }else{
                messageBus.sendMessage(message);
            }
        }
        messageBuses.removeAll(toRemove);
    }

    public void addMessageProcessor(ThreadedQueue<MessageData> messageProcessor){
        messageProcessors.add(messageProcessor);
        messageProcessor.start();
    }

    private void listenerThread(){
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            while(true){
                try{
                    Socket socket = serverSocket.accept();
                    MessageReceiver messageReceiver = new MessageReceiver();
                    MessageBus messageBus = new MessageBus(socket, messageReceiver);
                    messageReceiver.setMessageBus(messageBus);
                    messageBus.start();
                    messageBuses.add(messageBus);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }catch (InterruptedIOException ignored) {
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Server listener thread exiting!");
    }

    private final class MessageDispatcherQueue extends ThreadedQueue<MessageData>{
        @Override protected void init() {}

        @Override
        protected void handle(MessageData messageData) {
            ArrayList<MessageBus>  toRemove = new ArrayList<>();
            for(MessageBus messageBus : messageBuses){
                if (messageBus.isSocketClosed()) {
                    toRemove.add(messageBus);
                    messageBus.stop();
                }else if(messageBus != messageData.getMessageBus()){
                    messageBus.sendMessage(messageData.getMessage());
                }
            }
        }
    }

    private final class MessageReceiver implements ReceiverCallback{
        private volatile MessageBus messageBus;

        public void setMessageBus(MessageBus messageBus) {
            this.messageBus = messageBus;
        }

        @Override
        public void receive(Message message){
            MessageData messageData = new MessageData(message, messageBus);
            for(ThreadedQueue<MessageData> messageProcessor : messageProcessors){
                messageProcessor.enqueue(messageData);
            }
        }

    }

}
