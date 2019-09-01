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

package club.moddedminecraft.polychat.server.command;

import club.moddedminecraft.polychat.server.Main;
import club.moddedminecraft.polychat.server.info.OnlineServer;

import java.util.Map;

public class CommandOnline extends Command {

    private boolean deprecated = false;

    public CommandOnline(String name, Map<String, Object> args) {
        super(name, args);
        this.deprecated = (boolean) args.getOrDefault("deprecated", false);
    }

    @Override
    public String getDescription() {
        StringBuilder desc = new StringBuilder();
        desc.append("Returns amount of players online");
        if (this.deprecated) {
            desc.append(" (Deprecated)");
        }
        return desc.toString();
    }

    public String run(String[] args) {
        StringBuilder info = new StringBuilder();
        if (deprecated) {
            String oc = Main.config.getProperty("online_command", "online");
            String prefix = Main.config.getProperty("prefix", "!");
            info.append("WARNING: This command is deprecated and will soon be removed in favor of ").append(prefix).append(oc).append(".\n");
        }
        for (OnlineServer server : Main.serverInfo.getServers()) {
            info.append(String.format(
                    "**%s** [%d/%d]: %s",
                    server.getServerAddress(),
                    server.playerCount(),
                    server.maxPlayers(),
                    String.join(" **|** ", server.getOnlinePlayers())
            ));
        }
        return info.toString();
    }
}
