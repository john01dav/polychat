package club.moddedminecraft.polychat.networking.io;

import club.moddedminecraft.polychat.networking.util.ThreadedQueue;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
        listenerThread.stop();
    }

    public void sendMessage(Message message){
        for(MessageBus messageBus : messageBuses){
            messageBus.sendMessage(message);
        }
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
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private final class MessageDispatcherQueue extends ThreadedQueue<MessageData>{
        @Override protected void init() {}

        @Override
        protected void handle(MessageData messageData) {
            for(MessageBus messageBus : messageBuses){
                if(messageBus != messageData.getMessageBus()){
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
