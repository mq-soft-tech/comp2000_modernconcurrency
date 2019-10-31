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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SimpleThreadPool 
{
    public static void main(String[] args) 
    {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        CountDownLatch starter = new CountDownLatch(1);
        
        for (int i = 1; i <= 10; i++) 
        {
            int taskNum = i;
            System.out.printf("Create task %d.%n", taskNum);

            executor.execute(() -> {
                try {
                    starter.await();
                    System.out.printf("Executing task %d.%n", taskNum);
                    TimeUnit.SECONDS.sleep((long) (Math.random() * 10));
                    System.out.printf("Task %d completed.%n", taskNum);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        starter.countDown();
        executor.shutdown();
    }
}