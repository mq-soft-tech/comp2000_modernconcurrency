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

package org.macquarie.prodcons;

import java.util.concurrent.locks.ReentrantLock;

/**
 * A thread safe bounded buffer class - which uses a very simple implementation
 * which treats a fixed size array of values as a circular buffer.
 * 
 * Demonstrates the use of synchronisation to ensure consistency and
 * {@link #wait()} and {@link #notifyAll()} methods to handle overflow and
 * underflow situations.
 * 
 * Buffers of this type are generic, so can hold values of any (non-primitive)
 * type.
 * 
 * See lecture notes for more information. Also it is worth looking up
 * Producer-Consumer on Wikipedia.
 * 
 * @author Dominic Verity
 *
 */
public class BoundedBuffer<T> {

	private final static int CAPACITY = 3;
	private T[] mValues;
	private int mNext;
	private int mHead;

	private ReentrantLock mLock;

	public BoundedBuffer() {
		mValues = (T[]) new Object[CAPACITY];
		mNext = 0;
		mHead = 0;
		mLock = new ReentrantLock();
	}

	public void put(T pValue) throws InterruptedException {

		// First lock the lock protecting the buffer and check to see if there is 
		// space to put a new entry into. If there isn't then unlock the lock and
		// sleep for a few microseconds, in the hope that some other thread will remove
		// something from the buffer. Then iterate and try again.
		while (!mLock.isHeldByCurrentThread()) {
			mLock.lock();

			if (mNext - mHead >= CAPACITY) {
				mLock.unlock();
				System.out.println("Waiting for some buffer space!");
				Thread.sleep(1000);
			}
		}

		// At this point we hold the lock and there are spaces free in the buffer.

		// We surround the rest of our code in a try..finally, to ensure that the lock is
		// unlocked regardless of the way that we exit from this code.
		
		try {
			// Now we know there is space so add the new value.
			mValues[mNext % CAPACITY] = pValue;
			mNext++;
		} finally {
			mLock.unlock();
		}
	}

	public T get() throws InterruptedException {
		// First lock the lock protecting the buffer and check to see if there is 
		// an entry to remove from it. If there isn't then unlock the lock and
		// sleep for a few microseconds, in the hope that some other thread will put
		// something new into the buffer. Then iterate and try again.
		while (!mLock.isHeldByCurrentThread()) {
			mLock.lock();

			if (mNext - mHead <= 0) {
				mLock.unlock();
				System.out.println("Waiting for a value to become available!");
				Thread.sleep(1000);
			}
		}


		// At this point we hold the lock and we know that a value is present.
		
		// We surround the rest of our code in a try..finally, to ensure that the lock is
		// unlocked regardless of the way that we exit from this code.
		
		try {
			// Get the head value
			T vResult = mValues[mHead++];

			// Adjust if we've wrapped around in the buffer.
			if (mHead >= CAPACITY) {
				mHead -= CAPACITY;
				mNext -= CAPACITY;
			}

			// And return the retrieved value.
			return vResult;
		} finally {
			mLock.unlock();
		}
	}
}
