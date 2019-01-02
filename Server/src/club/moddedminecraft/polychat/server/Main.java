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
package club.moddedminecraft.polychat.server;

import club.moddedminecraft.polychat.networking.io.Server;
import club.moddedminecraft.polychat.server.info.OnlineServers;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public final class Main {
    public static Properties config;
    public static File configFile;
    public static Server chatServer;
    public static PrintMessageQueue messageQueue;
    public static IDiscordClient discordClient;
    public static IChannel channel = null;
    public static OnlineServers serverInfo = new OnlineServers();

    public static void main(String[] args) {
        //Reads the config file and exits if something went wrong
        if (!handleConfig()) {
            System.err.println("Reading config failed! Exiting...");
            System.exit(1);
        }

        //Initializes the server
        if (!initServer()) {
            System.err.println("Starting server socket failed! Exiting...");
            System.exit(1);
        }

        //Initializes discord connection if there is an api token
        if ((config.getProperty("api_token").equals("empty")) ||
                (config.getProperty("guild_name").equals("empty")) ||
                (config.getProperty("channel_name").equals("empty"))) {
            System.out.println("Discord parameters are empty, not starting discord connection.");
        }else{
            initDiscord();
        }
    }

    //Manages the configuration file at startup, returning whether to proceed
    public static boolean handleConfig() {
        //Creates the properties object and establishes the path to the config file
        config = new Properties();
        configFile = new File(System.getProperty("user.dir"), "polychat.properties");

        //Checks if the config file exists.  Reads it if it does and creates a default one otherwise.
        if (configFile.exists() && configFile.isFile()) {
            try (FileInputStream fin = new FileInputStream(configFile)) {
                config.load(fin);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            //Establishes an automatically closed output stream which creates the file
            try (FileOutputStream fout = new FileOutputStream(configFile)) {
                //Sets default properties and writes them to disk
                config.setProperty("api_token", "empty");
                config.setProperty("discord_prefix", "--:");
                config.setProperty("use_port", "25566");
                config.setProperty("guild_name", "empty");
                config.setProperty("channel_name", "empty");
                config.setProperty("command_role", "empty");
                config.store(fout, null);
            } catch (IOException e) {
                //Returns false because saving the default config failed
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    //Initializes the connection to the discord API using the API token
    public static void initDiscord() {
        ClientBuilder builder = new ClientBuilder();
        builder.withToken(config.getProperty("api_token"));
        discordClient = builder.login();
        discordClient.getDispatcher().registerListener(new DiscordEvent());
    }

    //Initializes the connection socket for game servers
    public static boolean initServer() {
        //Returns false if an exception is caught, indicating that the server probably shouldn't start
        try {
            //Reads the port from the config file setting
            int port = Integer.parseInt(Main.config.getProperty("use_port"));

            //Initializes the server as the port was correctly specified
            chatServer = new Server(port);
            messageQueue = new PrintMessageQueue();
            chatServer.addMessageProcessor(messageQueue);
            chatServer.start();
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
