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

import java.util.concurrent.RecursiveAction;

public class SquareAll extends RecursiveAction {
    private static int LIMIT = 10000;

    private int mStart, mEnd;
    private int[] mData;

    SquareAll(int[] pData, int pStart, int pEnd) {
        mStart = pStart;
        mEnd = pEnd;
        mData = pData;
    }

    @Override
    protected void compute() {
        if (mEnd - mStart <= LIMIT) {
            for (int i = mStart; i < mEnd; i++) {
                mData[i] = mData[i] * mData[i];
            }
        } else {
            int vMid = (mEnd + mStart) / 2;
            SquareAll left = new SquareAll(mData, mStart, vMid);
            SquareAll right = new SquareAll(mData, vMid, mEnd);
            left.fork();
            right.fork();
            left.join();
            right.join();

        }
    }
}