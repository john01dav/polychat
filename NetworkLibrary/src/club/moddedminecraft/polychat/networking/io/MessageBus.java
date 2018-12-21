package club.moddedminecraft.polychat.networking.io;

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
    }

    public void sendMessage(Message message){
        messageSendQueue.enqueue(message);
    }

}
