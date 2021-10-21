/**
 * This file is part of a project entitled ModernConcurrency which is provided
 * as sample code for the following Macquarie University unit of study:
 * 
 * COMP2000 "Object Oriented Programming Practices"
 * 
 * Copyright (c) 2019-2021 Dominic Verity and Macquarie University.
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

package org.macquarie.latch;

import java.util.concurrent.CountDownLatch;

public class CountDown {
    private static int THREADS = 5;
    static java.util.Random rand = new java.util.Random();

    public static void main(String[] args) {
        CountDownLatch starter = new CountDownLatch(1);
        CountDownLatch stopper = new CountDownLatch(THREADS);

        for (int i = 0; i < THREADS; i++) {
            int threadID = i;
            new Thread(() -> {
                int maxCount = rand.nextInt(20);

                try {
                    starter.await();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                System.out.printf("Counter %d started.%n", threadID);

                for (int count = 0; count < maxCount; count++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.printf("Counter %d, value %d.%n", threadID, count);
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                System.out.printf("Counter %d finished.%n", threadID);
                stopper.countDown();

            }).start();
        }

        starter.countDown();
        try {
            stopper.await();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("All done!");
    }
}