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

package org.macquarie.threadpool;

import java.util.concurrent.ForkJoinPool;

public class ForkJoinThreadPool {

    private static int PROBLEM_SIZE = 400000000;
    static java.util.Random rand = new java.util.Random();
    private static int RANGE = 5000;

    public static void main(String[] args) {

        ForkJoinPool pool = new ForkJoinPool();
        
        int[] data = new int[PROBLEM_SIZE];
        for (int i = 0; i < PROBLEM_SIZE; i++)
            data[i] = rand.nextInt(RANGE);

        System.out.println("Some selected data values:");
        for (int i = 0; i < PROBLEM_SIZE; i += PROBLEM_SIZE / 20)
            System.out.printf("Entry %d, value %d.%n", i, data[i]);
        System.out.println();

        pool.invoke(new SquareAll(data, 0, data.length));

        System.out.println("Squares of those numbers:");
        for (int i = 0; i < PROBLEM_SIZE; i += PROBLEM_SIZE / 20)
            System.out.printf("Entry %d, value %d.%n", i, data[i]);

    }
}

