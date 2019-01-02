/* This file is part of PolyChat.
 *
 * Copyright © 2018 john01dav
 *
 * PolyChat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PolyChat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Polychat. If not, see <https://www.gnu.org/licenses/>.
 */
package club.moddedminecraft.polychat.server;

import club.moddedminecraft.polychat.networking.io.*;
import club.moddedminecraft.polychat.networking.util.ThreadedQueue;

public class PrintMessageQueue extends ThreadedQueue<MessageData> {
    @Override
    protected void init() {
        System.out.println("ready to print messages");
    }

    @Override
    protected void handle(MessageData messageData) throws Throwable {
        Message rawMessage = messageData.getMessage();
        if (Main.channel != null) {
            if (rawMessage instanceof ChatMessage){
                ChatMessage message = ((ChatMessage) rawMessage);
                System.out.println(message.getUsername() + " " + message.getMessage());
                Main.channel.sendMessage("**`" + message.getUsername() + "`** " + message.getMessage());
            }else if (rawMessage instanceof ServerInfoMessage) {
                ServerInfoMessage infoMessage = ((ServerInfoMessage) rawMessage);
                Main.serverInfo.serverConnected(infoMessage.getServerID(),
                        infoMessage.getServerName(),
                        infoMessage.getServerAddress(),
                        infoMessage.getMaxPlayers());
            }else if (rawMessage instanceof ServerStatusMessage) {
                ServerStatusMessage serverStatus = ((ServerStatusMessage) rawMessage);
                switch (serverStatus.getState()) {
                    case 1:
                        Main.channel.sendMessage("**`" + serverStatus.getServerID() + " Server Online`**");
                        Main.serverInfo.serverOnline(serverStatus.getServerID());
                        break;
                    case 2:
                        Main.channel.sendMessage("**`" + serverStatus.getServerID() + " Server Offline`**");
                        Main.serverInfo.serverOffline(serverStatus.getServerID());
                        break;
                    case 3:
                        Main.channel.sendMessage("**`" + serverStatus.getServerID() + " Server Crashed`**");
                        Main.serverInfo.serverOffline(serverStatus.getServerID());
                        break;
                    default:
                        System.err.println("Unrecognized server state " + serverStatus.getState() + " received from " + serverStatus.getServerID());
                }
            }else if (rawMessage instanceof PlayerStatusMessage) {
                String statusString;
                PlayerStatusMessage playerStatus = ((PlayerStatusMessage) rawMessage);
                if (playerStatus.getJoined()) {
                    statusString = "**`" + playerStatus.getServerID() + " " + playerStatus.getUserName() + " has joined the game`**";
                    Main.serverInfo.playerJoin(playerStatus.getServerID(), playerStatus.getUserName());
                }else {
                    statusString = "**`" + playerStatus.getServerID() + " " + playerStatus.getUserName() + " has left the game`**";
                    Main.serverInfo.playerLeave(playerStatus.getServerID(), playerStatus.getUserName());
                }
                Main.channel.sendMessage(statusString);
            }
        }
    }
}
