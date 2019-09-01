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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinecraftCommand extends RoleCommand {

    private final String command;
    private final int argCount;

    public MinecraftCommand(String name, Map<String, Object> args) {
        super(name, args);
        this.command = (String) args.get("command");
        this.argCount = calculateParameters(command);
    }

    public int calculateParameters(String command) {
        Pattern pattern = Pattern.compile("(\\$\\d+)");
        Matcher matcher = pattern.matcher(command);
        return matcher.groupCount();
    }

    public String run(String[] args) {
        String command = this.command;

        if (args.length < 1) {
            return "Error running command: Server prefix required";
        }

        if (args.length < (this.argCount - 1)) {
            return "Expected at least " + this.argCount + " parameters, recieved " + (args.length - 1);
        }

        String serverID = args[0];
        OnlineServer server = Main.serverInfo.getServerNormalized(serverID);
        if (server == null) {
            return "Error running command: server prefix " + serverID + " does not exist.";
        }

        // get the last instance of every unique $(number)
        // ie. /ranks set $1 $2 $1 $3 returns $2 $1 $3
        Pattern pattern = Pattern.compile("(\\$\\d+)(?!.*\\1)");
        Matcher matcher = pattern.matcher(this.command);

        while (matcher.find()) {
            for (int i = 0; i <= matcher.groupCount(); i++) {
                String toBeReplaced = matcher.group(i);
                String replaceWith;
                int argNum = Integer.parseInt(toBeReplaced.substring(1));
                replaceWith = args[argNum];
                command = command.replace(toBeReplaced, replaceWith);
            }
        }
        command = command.replace("$args", String.join(" ", args));

        return "COMMAND ON SERVER " + server.getServerID() + ": " + command;

    }

}
