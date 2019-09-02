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
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;
import java.util.Map;

public abstract class RoleCommand extends Command {

    private final String role;

    public RoleCommand(String name, Map<String, Object> args) {
        super(name, args);
        this.role = (String) args.get("role");
    }

    public boolean verifyRole(IUser user, String role) {
        List<IRole> userRoles = user.getRolesForGuild(Main.channel.getGuild());
        if (role == null) {
            return true;
        }
        List<IRole> rolesByName = Main.channel.getGuild().getRolesByName(role);
        for (IRole test_role : rolesByName) {
            if (userRoles.contains(test_role)) {
                return true;
            }
        }
        return false;
    }

    public String verifyAndRun(IUser user, String[] args) {
        if (!verifyRole(user, role)) {
            return "User does not have permission to perform this command";
        }
        return run(args);
    }

}
