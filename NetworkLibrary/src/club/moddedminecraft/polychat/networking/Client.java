package club.moddedminecraft.polychat.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public final class Client {
    private Socket socket;
    private Thread listenerThread, senderThread;
    private LinkedList<Message> messageSendQueue = new LinkedList<>();
    private ArrayList<Message> messageProcessQueue;
    private final Object sendLock = new Object(), processLock = new Object();

    public Client(String serverIp, int port) throws IOException {
        socket = new Socket(serverIp, port);
    }

    public void start(){
        listenerThread = new Thread(this::listenerThread);
        listenerThread.start();

        senderThread = new Thread(this::senderThread);
        senderThread.start();
    }

    public ArrayList<Message> getReceivedMessages(){
        synchronized(processLock){
            if(messageProcessQueue == null){
                return null;
            }else{
                ArrayList<Message> receivedMessages = messageProcessQueue;
                messageProcessQueue = null;
                return receivedMessages;
            }
        }
    }

    public void sendMessage(Message message){
        synchronized(sendLock){
            messageSendQueue.push(message);
            senderThread.interrupt();
        }
    }

    public void stop(){
        listenerThread.stop();
        listenerThread.start();
    }

    private void listenerThread(){
        try(
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        ){
            while(true){
                short messageId = dataInputStream.readShort();
                switch(messageId){
                    case ChatMessage.MESSAGE_TYPE_ID:
                        queueMessageForProcessing(new ChatMessage(dataInputStream));
                        break;
                    case BroadcastMessage.MESSAGE_TYPE_ID:
                        queueMessageForProcessing(new BroadcastMessage(dataInputStream));
                        break;
                    default:
                        System.err.print("[Polychat] Warning: Illegal message id: " + messageId);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void senderThread(){
        try(
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        ){
            while(true){
                try{
                    Thread.sleep(5000);
                }catch(InterruptedException e){}
                Message message;
                while((message = getNextMessage()) != null){
                    message.send(dataOutputStream);
                    dataOutputStream.flush();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private Message getNextMessage(){
        synchronized(sendLock){
            if(messageSendQueue.isEmpty()){
                return null;
            }else{
                return messageSendQueue.pop();
            }
        }
    }

    private synchronized void queueMessageForProcessing(Message message){
        if(messageProcessQueue == null){
            messageProcessQueue = new ArrayList<>();
        }
        messageProcessQueue.add(message);
    }

}
