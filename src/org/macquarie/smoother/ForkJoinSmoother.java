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

package org.macquarie.smoother;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ForkJoinSmoother {
    static final int LEN = 100000000;
    static final int RANGE = 5000;
    static final int TEST_ITERATIONS = 100;

    static java.util.Random rand = new java.util.Random();

    // Assertion: the template array has odd length.
    static int[] template = 
        { 1, 2, 4, 8, 16, 32, 64, 16, 0, -32, 0, 16, 64, 32, 16, 8, 4, 2, 1 };
    static int templateRadius = template.length / 2;

    static int[] source = new int[LEN];
    static int[] target = new int[LEN - template.length];

    static ForkJoinPool pool = new ForkJoinPool(8);

    public static void main(String[] args) {

        long timer = 0;

        // initialise with random integers
        for (int i = 0; i < LEN; i++) {
            source[i] = rand.nextInt(2 * RANGE + 1) - RANGE;
        }

        // The following loop runs the smoother function TEST_ITERATIONS times, so that
        // we can get compute an accurate average runtime for solving the supplied
        // smoothing problem

        for (int j = 0; j < TEST_ITERATIONS; j++) {
            long startTime = System.currentTimeMillis();
            parallelSmoother(source, 0, source.length - template.length, target);
            timer += System.currentTimeMillis() - startTime;
            System.out.printf("Iteration %d complete.%n", j);
        }

        System.out.printf("Elapsed time: %d milli-secs%n", timer);
        System.out.printf("Average time per smoother call: %d milli-secs%n", timer / TEST_ITERATIONS);
    }

    /**
     * Function to smooth a 1-d image by taking the average of the values of a a
     * region around each pixel weighted by the values in a fixed template.
     *
     * This function decreases runtime by distributing the proccessor load involved
     * in smothing across the available processors using the fork-join work distribution
     * pattern.
     * 
     * This version of parallel smoothing is implemented using a `ForkJoinPool` and an associated
     * `Smoother` class that extends the `RecursiveAction` class.  
     * 
     * @param in the source array containg the 1-d image to smooth
     * @param start the starting index of the region to smooth
     * @param end the ending index (+1) of the region to smooth
     * @param out the target array in which to write the smoothed image.
     */
    public static void parallelSmoother(int[] in, int start, int end, int[] out) {
        pool.invoke(new Smoother(in, start, end, out));
    }

    /**
     * Function to smooth a 1-d image by taking the average of the values of a a
     * region around each pixel weighted by the values in a fixed template.
     * 
     * @param in the source array containg the 1-d image to smooth
     * @param start the starting index of the region to smooth
     * @param end the ending index (+1) of the region to smooth
     * @param out the target array in which to write the smoothed image.
     */
    public static void smoother(int[] in, int start, int end, int[] out) {
        for (int i = start; i < end; i++) {
            int res = 0;
            for (int j = 0; j < template.length; j++) {
                res += in[i + j] * template[j];
            }
            out[i] = res; 
        }
    }

    /**
     * This class extends the `RecursiveAction` class to break a smoothing problem
     * into pieces and execute those pieces on separate threads in a `ForkJoinPool`.
     * 
     * The `compute()` method here examines the problem it is given to solve, and if
     * it is smaller than some fixed size LIMIT it simply calls the original `smoother()`
     * method to solve that problem. Otherwise it splits to original problem up into to
     * equal size pieces and calls their `fork()` methods to submit them for separate 
     * execution by the `ForkJoinPool`.
     */
    static class Smoother extends RecursiveAction {
        private static final long serialVersionUID = 713289651855068566L;
        private int start, end;
        private int[] in, out;
        static final int LIMIT = LEN / 32;

        Smoother(int[] in, int start, int end, int[] out) {
            this.in = in;
            this.out = out;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - start < LIMIT) {
                smoother(in, start, end, out);
            } else {
                int mid = (start + end) / 2;
                Smoother left = new Smoother(in, start, mid, out);
                Smoother right = new Smoother(in, mid, end, out);
                left.fork();
                right.fork();
                left.join();
                right.join();
            }
        }
    }
}