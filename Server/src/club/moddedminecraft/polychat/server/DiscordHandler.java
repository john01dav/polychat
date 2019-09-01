/*
 *  This file is part of PolyChat Server.
 *  *
 *  * Copyright © 2018 DemonScythe45
 *  *
 *  * PolyChat Server is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU Lesser General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * PolyChat Server is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU Lesser General Public License
 *  * along with PolyChat Server. If not, see <https://www.gnu.org/licenses/>.
 *
 */

package club.moddedminecraft.polychat.server;

import club.moddedminecraft.polychat.networking.io.ChatMessage;
import club.moddedminecraft.polychat.server.command.*;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import org.yaml.snakeyaml.Yaml;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.DoubleToIntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiscordHandler {

    private String discordPrefix;
    private CommandManager manager = new CommandManager();

    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) throws InvocationTargetException, FileNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        System.out.println("Discord connection initialized!");
        List<IGuild> guilds = event.getClient().getGuilds();
        for (IGuild guild : guilds) {
            if (guild.getName().equals(Main.config.getProperty("guild_name"))) {
                List<IChannel> channels = guild.getChannels();
                for (IChannel channel : channels) {
                    if (channel.getName().equals(Main.config.getProperty("channel_name"))) {
                        Main.channel = channel;
                        System.out.println("Established main message channel!");
                        Main.startServer();
                        discordPrefix = Main.config.getProperty("discord_prefix", "!");
                        manager.setPrefix(discordPrefix);
//                        try {
                        registerCommands();
//                        } catch (Exception e) {
//                            System.err.println("Error " + e.toString() + " encountered while registering commands, ignoring...");
//                        }
                        return;
                    }
                }
            }
        }
        Main.startServer();
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
                    processMessage(event.getAuthor(), event.getMessage());
                }
            }
        }
    }

    public void registerCommands() throws FileNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        final HashMap<String, Class<? extends Command>> COMMAND_TYPES = new HashMap<String, Class<? extends Command>>() {
            {
                put("online", CommandOnline.class);
                put("help", CommandHelp.class);
                put("minecraft", MinecraftCommand.class);
            }
        };

        Yaml yaml = new Yaml();
        LinkedHashMap yamlObj = yaml.load(new FileInputStream(Main.yamlConfig));

        for (Object entryObj : yamlObj.entrySet()) {

            Map.Entry<String, ArrayList> entry = (Map.Entry<String, ArrayList>) entryObj;
            HashMap<String, String> argMap = new HashMap<>();
            for (Object mapObj : entry.getValue()) {
                argMap.putAll((HashMap<String, String>) mapObj);
            }

            String name = entry.getKey();
            String type = argMap.remove("type");

            Class<? extends Command> element = COMMAND_TYPES.get(type);
            Constructor constructor = element.getConstructor(String.class, Map.class);
            Command commandObj = (Command) constructor.newInstance(name, argMap);

            manager.register(commandObj);
        }
    }

    public CommandManager getManager() {
        return manager;
    }

    public void processMessage(IUser user, IMessage message) {
        if (message.getContent().startsWith(discordPrefix)) {
            String newMessage = manager.run(message);
            if (!newMessage.isEmpty()) {
                Main.channel.sendMessage(newMessage);
            }
        } else {
            ChatMessage discordMessage = new ChatMessage(user.getDisplayName(Main.channel.getGuild()) + ":", formatMessage(message), "empty");
            Main.chatServer.sendMessage(discordMessage);
        }
    }

    private String formatMessage(IMessage message) {
        String messageContent = message.getFormattedContent();
        messageContent = EmojiParser.parseToAliases(messageContent);

        Pattern emojiName = Pattern.compile("<(:\\w+:)\\d+>");
        Matcher nameMatch = emojiName.matcher(messageContent);
        while (nameMatch.find()) {
            for (int i = 0; i <= nameMatch.groupCount(); i++) {
                messageContent = messageContent.replaceFirst("(<:\\w+:\\d+>)", nameMatch.group(i));
            }
        }

        return messageContent;
    }

}
