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

import java.util.Map;

public class CommandHelp extends Command {

    public CommandHelp(String name, Map<String, Object> args) {
        super(name, args);
    }

    @Override
    public String getDescription() {
        return "Show this message";
    }

    public String run(String[] args) {
        StringBuilder help = new StringBuilder();
        for (Command command : Main.discordHandler.getManager().getCommandList()) {
            help.append(command.getName()).append(": ").append(command.getDescription()).append("\n");
        }
        return help.toString();
    }
}
