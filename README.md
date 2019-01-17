# PolyChat Server and NetworkLibrary

Both modules are built using IDEAs build system.  There are artifacts for creating jar files for both.

Server is a standalone process that uses NetworkLibrary to interface with https://github.com/DemonScythe45/polychat-client running in an MC server.

Chat messages are forwarded from discord to all connected MC servers, and from once MC server to discord and other connected MC servers.

## Config file for Server

`api_token`: This is the discord token that the server will use to log in to the discord api with.

`discord_prefix`: This is the command prefix to be used when running commands from discord.

`use_port`: This is the port that the server will use to listen for connections coming from running MC servers with the client forge mod.

`guild_name`: This is the name of the discord server that contains the channel that will be used for sending/receiving chat messages.

`channel_name`: This is the name of the channel that is used for sending/receiving chat messages.

`command_role`: This is the name of the role that has access for using special commands like restarting MC servers. **Currently not yet implemented.**

`broadcast_delay`: This is the number of minutes between each broadcast message being sent.

`broadcast_prefix`: This is the prefix that is placed in front of broadcast messages.

`prefix_color`: This is the color of the broadcast prefix.  Refer to PolyChat Client README for color codes.

#### Messages.txt

This file contains all of the broadcast messages that are sent out.  Contents are randomized after iterating through the list each time.

Each line corresponds to one individual message, and example being "Go to <link> and vote for the server!" or "Donate at <link>"

The broadcaster does nothing if the file is empty.

## Discord Commands

Currently the commands "listall" and "listplayers" can be used from discord.

Listall prints information about all online MC servers.

Listplayers prints players logged into an MC server specified by server prefix.