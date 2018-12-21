package club.moddedminecraft.polychat.networking;

public interface ChatMessageReciever<T extends Message> {

    void receive(T message);

}
