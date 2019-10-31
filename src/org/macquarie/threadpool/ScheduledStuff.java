/**
 * This file is part of a project entitled ModernConcurrency which is provided
 * as sample code for the following Macquarie University unit of study:
 * 
 * COMP229 "Object Oriented Programming Practices"
 * 
 * Copyright (c) 2019 Dominic Verity and Macquarie University.
 * 
 * ModernConcurrency is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * ModernConcurrency is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ModernConcurrency. (See files COPYING and COPYING.LESSER.) If
 * not, see <http://www.gnu.org/licenses/>.
 */

package org.macquarie.threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduledStuff {
    static int count1 = 1;
    static int count2 = 1;
    static long startTime;

    public static void main(String[] args) {
        ScheduledThreadPoolExecutor pool = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);

        startTime = System.currentTimeMillis();

        pool.schedule(() -> {
            System.out.printf("I'm scheduled to run once after after 10 seconds.(time: %d ms)%n", elapsed());
        }, 10, TimeUnit.SECONDS);

        pool.scheduleWithFixedDelay(() -> {
            System.out.printf("I'm scheduled to run once every second after a 5 second delay (count: %d, time: %d ms)%n",
                    count1++, elapsed());
        }, 5, 1, TimeUnit.SECONDS);

        pool.scheduleWithFixedDelay(() -> {
            System.out.printf(
                    "I'm scheduled to run once every 1.5 seconds after a 8 second delay. (count: %d, time: %d ms)%n",
                    count2++, elapsed());
        }, 8000, 1500, TimeUnit.MILLISECONDS);
    }

    static long elapsed() {
        return (System.currentTimeMillis() - startTime);
    }
}