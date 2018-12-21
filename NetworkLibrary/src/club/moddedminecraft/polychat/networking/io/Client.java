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

    public void start(){
        messageBus.start();
    }

    public synchronized ArrayList<Message> getReceivedMessages(){
        ArrayList<Message> messages = receivedMessages;
        receivedMessages = null;
        return messages;
    }

    private synchronized void receiveMessage(Message message){
        if(receivedMessages == null){
            receivedMessages = new ArrayList<>();
        }
        receivedMessages.add(message);
    }

    public void sendMessage(Message message){
        messageBus.sendMessage(message);
    }

    public void stop(){
        messageBus.stop();
    }

}
