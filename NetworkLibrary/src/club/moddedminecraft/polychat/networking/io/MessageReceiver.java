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

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public final class MessageReceiver {
    private final Socket socket;
    private final ReceiverCallback receiverCallback;
    private final MessageProcessingQueue messageProcessingQueue = new MessageProcessingQueue();
    private Thread receiverThread;

    public MessageReceiver(Socket socket, ReceiverCallback receiverCallback){
        this.socket = socket;
        this.receiverCallback = receiverCallback;
    }

    public void start(){
        messageProcessingQueue.start();
        receiverThread = new Thread(this::receiverThread);
        receiverThread.start();
    }

    public void stop(){
        receiverThread.stop();
    }

    private void receiverThread(){
        try(
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        ){
            while(true){
                short messageId = dataInputStream.readShort();
                switch(messageId){
                    case ChatMessage.MESSAGE_TYPE_ID:
                        messageProcessingQueue.enqueue(new ChatMessage(dataInputStream));
                        break;
                    case BroadcastMessage.MESSAGE_TYPE_ID:
                        messageProcessingQueue.enqueue(new BroadcastMessage(dataInputStream));
                        break;
                    default:
                        System.err.print("[Polychat] Warning: Illegal message id: " + messageId);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private class MessageProcessingQueue extends ThreadedQueue<Message>{

        @Override protected void init() {}

        @Override
        protected void handle(Message obj) {
            receiverCallback.receive(obj);
        }
    }

}
