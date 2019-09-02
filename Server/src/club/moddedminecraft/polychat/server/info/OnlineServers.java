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

import club.moddedminecraft.polychat.networking.io.MessageBus;

import java.util.ArrayList;
import java.util.HashMap;

public class OnlineServers {

    private ArrayList<OnlineServer> onlineServers;
    private HashMap<String, OnlineServer> serverMap;

    public OnlineServers() {
        this.onlineServers = new ArrayList<>();
        this.serverMap = new HashMap<>();
    }

    public ArrayList<OnlineServer> getServers() {
        return onlineServers;
    }

    public OnlineServer getServer(String serverID) {
        System.out.println(serverMap);
        return serverMap.getOrDefault(serverID, null);
    }

    public OnlineServer getServerNormalized(String serverID) {
        for (OnlineServer server : onlineServers) {
            String sID = server.getServerID();
            sID = sID.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "");
            if (sID.equals(serverID.toLowerCase())) {
                return server;
            }
        }
        return null;
    }

    //Adds a server to the list
    public void serverConnected(String serverID, String serverName, String serverAddress, int maxPlayers, MessageBus messageBus) {
        OnlineServer toRemove = serverMap.get(serverID);
        if (toRemove != null) {
            onlineServers.remove(toRemove);
        }
        OnlineServer connected = new OnlineServer(serverID, serverName, serverAddress, maxPlayers, messageBus);
        this.onlineServers.add(connected);
        this.serverMap.put(serverID, connected);
    }

    //Marks a server as online
    public void serverOnline(String serverID) {
        OnlineServer server = serverMap.get(serverID);
        if (server != null) {
            server.setStarted();
        }
    }

    //Removes a server as it went offline
    public void serverOffline(String serverID) {
        OnlineServer toRemove = serverMap.get(serverID);
        onlineServers.remove(toRemove);
        serverMap.remove(serverID);
    }

    public void updatePlayerList(String serverID, ArrayList<String> playerList) {
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
