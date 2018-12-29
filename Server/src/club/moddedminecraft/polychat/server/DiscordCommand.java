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
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class DiscordCommand {
    public static void processMessage(IUser user, IMessage message) {
        ChatMessage discordMessage = new ChatMessage("[D]", user.getName(), message.getContent());
        Main.chatServer.sendMessage(discordMessage);
    }

    public static void listServers() {
        //TODO list servers online
    }

    public static void setChatChannel() {
        //TODO set chat channel
    }

    public static void runCommand() {
        //TODO run command
    }

    public static void restartServer() {
        //TODO restart the server
    }

    public static void onlinePlayers() {
        //TODO list online players
    }
}
