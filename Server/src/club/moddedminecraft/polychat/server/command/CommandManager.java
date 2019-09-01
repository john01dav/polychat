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

import sx.blah.discord.handle.obj.IMessage;

import java.util.ArrayList;

public class CommandManager {
    private ArrayList<Command> cmdList = new ArrayList<>();
    private String prefix;

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void register(Command command) {
        cmdList.add(command);
    }

    public void register(ArrayList<Command> collection) {
        cmdList.addAll(collection);
    }

    public ArrayList<Command> getCommandList() {
        return cmdList;
    }

    public String run(IMessage message) {
        // get message content and remove prefix
        String content = message.getContent().replace(this.prefix, "");
        // get first word (command name)
        String[] rawCommand = content.split(" ", 2);
        String cmdName = rawCommand[0];
        String[] args = new String[0];
        if (rawCommand.length > 1) {
            args = rawCommand[1].split(" ");
        }
        // get everything after first word (args)
        for (Command cmd : cmdList) {
            if (cmd.getName().equals(cmdName)) {
                if (cmd instanceof RoleCommand) {
                    return ((RoleCommand) cmd).verifyAndRun(message.getAuthor(), args);
                } else {
                    return cmd.run(args);
                }
            }
        }
        return "Error running command: Command " + cmdName + " does not exist";
    }
}
