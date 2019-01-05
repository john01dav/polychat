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

import club.moddedminecraft.polychat.networking.io.BroadcastMessage;

import java.util.ArrayList;
import java.util.Collections;

public class BroadcastManager {
    private long delay;
    private Thread broadcastThread;
    private ArrayList<BroadcastMessage> messages;

    public BroadcastManager(ArrayList<BroadcastMessage> messages) {
        this.messages = messages;
        int timeDelay = Integer.parseInt(Main.config.getProperty("broadcast_delay"));
        this.delay = (timeDelay * 60000); //Time delay is in minutes, so this converts to the right millisecond value
        this.broadcastThread = new Thread(this::broadcastThread);
        this.broadcastThread.start();
    }

    public void stop() {
        this.broadcastThread.interrupt();
    }

    public void broadcastThread() {
        try {
            while (true) {
                Collections.shuffle(messages);
                for (BroadcastMessage message : messages) {
                    Thread.sleep(delay);
                    Main.chatServer.sendMessage(message);
                }
            }
        }catch (InterruptedException ignored) {
            System.out.println("Broadcast thread exiting");
        }
    }
}
