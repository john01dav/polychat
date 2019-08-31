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
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class DiscordCommand {
     public static void processMessage(IUser user, IMessage message) {
        String discordPrefix = Main.config.getProperty("discord_prefix");
        if (message.getContent().startsWith(discordPrefix)) {
            String command = message.getContent().replace(discordPrefix, "");
            if (command.startsWith("listall")) Main.serverInfo.serversInfo();
            else if (command.startsWith("listplayers")) {
                String id = command.replace("listplayers ", "");
                try {
                    int index = Integer.parseInt(id);
                    Main.serverInfo.serverInfo(index);
                }catch (NumberFormatException ignored) {}
            }
        }else {
            String content = message.getContent();
            // Turn @<User ID> to @<Display Name>
            for (IUser mention : message.getMentions()) {
                content = content.replace(mention.toString(), "@" + mention.getDisplayName(Main.channel.getGuild()));
            }
            // Turn #<Channel ID> to #channel
            for (IChannel channel : message.getChannelMentions()) {
                content = content.replace(channel.toString(), "#" + channel.getName());
            }
            // Turn @<Role ID> to @<Role>
            for (IRole role : message.getRoleMentions()) {
                content = content.replace(role.toString(), "@" + role.getName());
            }
            ChatMessage discordMessage = new ChatMessage(user.getDisplayName(Main.channel.getGuild()) + ":", content, "empty");
            Main.chatServer.sendMessage(discordMessage);
        }
    }

    public static void runCommand() {
        //TODO run command
    }

    public static void restartServer() {
        //TODO restart the server
    }
}
