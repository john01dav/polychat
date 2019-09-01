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
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.RateLimitException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrintMessageQueue extends ThreadedQueue<MessageData> {

    @Override
    protected void init() {
        System.out.println("ready to print messages");
    }

    @Override
    protected void handle(MessageData messageData) {
        Message rawMessage = messageData.getMessage();
        boolean limited = false;
        do {
            try {
                limited = false;
                if (Main.channel != null) {
                    if (rawMessage instanceof ChatMessage) {
                        ChatMessage message = ((ChatMessage) rawMessage);
                        System.out.println(message.getUsername() + " " + message.getMessage());
                        Main.channel.sendMessage("**`" + message.getUsername() + "`** " + formatMessage(message.getMessage()));
                    } else if (rawMessage instanceof ServerInfoMessage) {
                        ServerInfoMessage infoMessage = ((ServerInfoMessage) rawMessage);
                        Main.serverInfo.serverConnected(infoMessage.getServerID(),
                                infoMessage.getServerName(),
                                infoMessage.getServerAddress(),
                                infoMessage.getMaxPlayers());
                    } else if (rawMessage instanceof ServerStatusMessage) {
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
                    } else if (rawMessage instanceof PlayerStatusMessage) {
                        String statusString;
                        PlayerStatusMessage playerStatus = ((PlayerStatusMessage) rawMessage);
                        if (playerStatus.getJoined()) {
                            statusString = "**`" + playerStatus.getServerID() + " " + playerStatus.getUserName() + " has joined the game`**";
                            Main.serverInfo.playerJoin(playerStatus.getServerID(), playerStatus.getUserName());
                        } else {
                            statusString = "**`" + playerStatus.getServerID() + " " + playerStatus.getUserName() + " has left the game`**";
                            Main.serverInfo.playerLeave(playerStatus.getServerID(), playerStatus.getUserName());
                        }
                        if (!playerStatus.getSilent()) {
                            Main.channel.sendMessage(statusString);
                        }
                    } else if (rawMessage instanceof PlayerListMessage) {
                        PlayerListMessage plMessage = (PlayerListMessage) rawMessage;
                        Main.serverInfo.updatePlayerList(plMessage.getServerID(), plMessage.getPlayerList());
                    }
                }
            } catch (RateLimitException e) {
                limited = true;
                try {
                    Thread.sleep(e.getRetryDelay());
                } catch (InterruptedException ignored) {
                }
            } catch (Exception e) {
                System.err.println("Error sending message to Discord!");
                e.printStackTrace();
            }
        } while (limited);

    }

    private String formatMessage(String message) {

        IGuild guild = Main.channel.getGuild();

        Pattern roleMentions = Pattern.compile("(@\\w+)");
        Matcher roleMentionMatcher = roleMentions.matcher(message);
        while (roleMentionMatcher.find()) {
            for (int i = 0; i <= roleMentionMatcher.groupCount(); i++) {
                String roleMention = roleMentionMatcher.group(i);
                String name = roleMention.substring(1);

                List<IRole> roleList = guild.getRolesByName(name);
                if (roleList.size() > 0) {
                    message = message.replace(roleMention, String.valueOf(roleList.get(0)));
                }
            }
        }

        Pattern userMentions = Pattern.compile("(@\\(.+\\))");
        Matcher userMentionMatcher = userMentions.matcher(message);
        while (userMentionMatcher.find()) {
            System.out.println("MATCHED!!!!!!!!!!!!!!!");
            for (int i = 0; i <= userMentionMatcher.groupCount(); i++) {
                String userMention = userMentionMatcher.group(i);
                // Remove @ and ()
                String name = userMention.substring(2, (userMention.indexOf(')')));
                System.out.println(userMention);
                System.out.println(name);

                List<IUser> userList = guild.getUsersByName(name);
                if (userList.size() > 0) {
                    message = message.replace(userMention, String.valueOf(userList.get(0)));
                }
            }
        }

        return message;
    }
}
