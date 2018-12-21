package club.moddedminecraft.polychat.networking.io;

public final class MessageData {
    private final Message message;
    private final MessageBus messageBus;

    public MessageData(Message message, MessageBus messageBus){
        this.message = message;
        this.messageBus = messageBus;
    }

    public Message getMessage(){
        return message;
    }

    public MessageBus getMessageBus() {
        return messageBus;
    }
}
