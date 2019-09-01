/* This file is part of PolyChat.
 *
 * Copyright © 2018 john01dav
 *
 * PolyChat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PolyChat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Polychat. If not, see <https://www.gnu.org/licenses/>.
 */
package club.moddedminecraft.polychat.networking.util;

import java.util.LinkedList;

public abstract class ThreadedQueue<T> {
    private Thread thread;
    private boolean run = true;
    private LinkedList<T> queue = new LinkedList<>();

    public final synchronized void start() {
        thread = new Thread(this::queueThread);
        thread.start();
    }

    public final synchronized void stop() {
        this.run = false;
        thread.interrupt();
    }

    public final synchronized void enqueue(T obj) {
        queue.push(obj);
        if (thread != null) thread.interrupt();
    }

    private final void queueThread() {
        try {
            init();
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                if (!(this.run)) break;
                T nextItem;
                while ((nextItem = getNextItem()) != null) {
                    handle(nextItem);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.out.println("Queue thread exiting!");
    }

    private final synchronized T getNextItem() {
        if (queue.isEmpty()) {
            return null;
        } else {
            return queue.pop();
        }
    }

    protected abstract void init() throws Throwable;

    protected abstract void handle(T obj) throws Throwable;

}
