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

import java.util.ArrayList;

public class OnlineServer {
    //Server id is the chat prefix and server name is the full name of the server, such as: [REV] / Revelation
    private final String serverID, serverName, serverAddress;
    //For displaying player information
    private final int maxPlayers;
    //List of players currently logged into this server
    protected ArrayList<String> onlinePlayers;
    //Since the mod establishes a socket with the main polychat server in preinit, the listing will report
    //  the server as starting until the server says its fully started
    private boolean started;

    public OnlineServer(String serverID, String serverName, String serverAddress, int maxPlayers) {
        this.serverID = serverID;
        this.serverName = serverName;
        this.serverAddress = serverAddress;
        this.started = false;
        this.maxPlayers = maxPlayers;
        this.onlinePlayers = new ArrayList<>();
    }

    //Sets this server as started
    public void setStarted() {
        this.started = true;
    }

    //Gets a count of online players
    public int playerCount() {
        return this.onlinePlayers.size();
    }

    public ArrayList<String> getOnlinePlayers() {
        return this.onlinePlayers;
    }

    //The maximum players on this server
    public int maxPlayers() {
        return this.maxPlayers;
    }

    //Gets the server ID
    public String getServerID() {
        return this.serverID;
    }

    //Gets the server name
    public String getServerName() {
        return this.serverName;
    }

    //Gets the server address
    public String getServerAddress() {
        return this.serverAddress;
    }

    public void updatePlayerList(ArrayList<String> playerList) {
        this.onlinePlayers = playerList;
    }
}
