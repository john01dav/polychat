package club.moddedminecraft.polychat.server;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

import java.util.List;

public class DiscordEvent {
    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) {
        System.out.println("Discord connection initialized!");
        List<IGuild> guilds = event.getClient().getGuilds();
        for (IGuild guild : guilds) {
            if (guild.getName().equals(Main.config.getProperty("guild_name"))) {
                List<IChannel> channels = guild.getChannels();
                for (IChannel channel : channels) {
                    if (channel.getName().equals(Main.config.getProperty("channel_name"))) {
                        Main.channel = channel;
                        System.out.println("Established main message channel!");
                        return;
                    }
                }
            }
        }
        System.out.println("Failed to establish message channel! Will not send messages...");
    }

    @EventSubscriber
    public void onMessageEvent(MessageReceivedEvent event) {
        IGuild guild = event.getGuild();
        IChannel channel = event.getChannel();
        if (guild != null) {
            if (channel != null) {
                if ((guild.getName().equals(Main.config.getProperty("guild_name"))) &&
                        (channel.getName().equals(Main.config.getProperty("channel_name")))) {
                    DiscordCommand.processMessage(event.getAuthor(), event.getMessage());
                }
            }
        }
    }
}
