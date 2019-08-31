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

package club.moddedminecraft.polychat.server.info;

import club.moddedminecraft.polychat.server.Main;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.RateLimitException;

import java.util.ArrayList;
import java.util.HashMap;

public class OnlineServers {
    private ArrayList<OnlineServer> onlineServers;
    private HashMap<String, OnlineServer> serverMap;

    public OnlineServers() {
        this.onlineServers = new ArrayList<>();
        this.serverMap = new HashMap<>();
    }

    //Prints info about all servers to discord
    public void serversInfo() {
        IChannel messageChannel = Main.channel;
        String info = "**Servers Online [" + onlineServers.size() + "]:**\n";
        for (OnlineServer server : onlineServers) {
            info = String.format(
                    "%s**%s** [%d/%d]: %s",
                    info,
                    server.getServerAddress(),
                    server.playerCount(),
                    server.maxPlayers(),
                    String.join(" **|** ", server.onlinePlayers)
            );
//            info = info + "   " + (i+1) + " : " + server.getServerID() + " : "
//                    + server.getServerName() + " : "
//                    + server.getServerAddress() + " "
//                    + "[" + server.playerCount() + "/" + server.maxPlayers() + "]\n";
        }
        boolean limited;
        do {
            limited = false;
            try {
                messageChannel.sendMessage(info);
            } catch (RateLimitException e) {
                limited = true;
                try {
                    Thread.sleep(e.getRetryDelay());
                } catch (InterruptedException ignored) {
                }
            }
        } while (limited);
    }

    //Prints info about a specific server
    public void serverInfo(int serverIndex) {
        IChannel messageChannel = Main.channel;
        int index = (serverIndex - 1);
        if ((index < 0) || (index > onlineServers.size())) {
            boolean limited;
            do {
                limited = false;
                try {
                    messageChannel.sendMessage("No such server is online");
                } catch (RateLimitException e) {
                    limited = true;
                    try {
                        Thread.sleep(e.getRetryDelay());
                    } catch (InterruptedException ignored) {
                    }
                }
            } while (limited);
            return;
        }
        OnlineServer server = onlineServers.get(index);
        if (server != null) {
            String info = "**`[" + server.playerCount() + "/" + server.maxPlayers()
                    + "] players in " + server.getServerName() + ":`**\n";
            for (String playerName : server.onlinePlayers) {
                info = info + "   " + playerName + "\n";
            }
            boolean limited;
            do {
                limited = false;
                try {
                    messageChannel.sendMessage(info);
                } catch (RateLimitException e) {
                    limited = true;
                    try {
                        Thread.sleep(e.getRetryDelay());
                    } catch (InterruptedException ignored) {
                    }
                }
            } while (limited);
        }
    }

    //Adds a server to the list
    public void serverConnected(String serverID, String serverName, String serverAddress, int maxPlayers) {
        OnlineServer toRemove = serverMap.get(serverID);
        if (toRemove != null) {
            onlineServers.remove(toRemove);
        }
        OnlineServer connected = new OnlineServer(serverID, serverName, serverAddress, maxPlayers);
        this.onlineServers.add(connected);
        this.serverMap.put(serverID, connected);
    }

    //Marks a server as online
    public void serverOnline(String serverID) {
        OnlineServer server = serverMap.get(serverID);
        if (server != null) { server.setStarted(); }
    }

    //Removes a server as it went offline
    public void serverOffline(String serverID) {
        OnlineServer toRemove = serverMap.get(serverID);
        onlineServers.remove(toRemove);
        serverMap.remove(serverID);
    }

    public void updatePlayerList(String serverID, ArrayList<String> playerList) {
//        System.out.println(serverID);
//        System.out.println(serverMap);
//        System.out.println(playerList);
        OnlineServer server = serverMap.get(serverID);
        if (server != null) {
            server.updatePlayerList(playerList);
        }
    }

    public void playerJoin(String serverID, String username) {
        OnlineServer server = serverMap.get(serverID);
        if (server != null) {
            serverMap.get(serverID).onlinePlayers.add(username);
        }
    }

    public void playerLeave(String serverID, String username) {
        OnlineServer server = serverMap.get(serverID);
        if (server != null) {
            serverMap.get(serverID).onlinePlayers.remove(username);
        }
    }

}
