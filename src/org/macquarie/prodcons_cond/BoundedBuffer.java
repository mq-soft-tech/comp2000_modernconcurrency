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

package org.macquarie.prodcons_cond;

import java.util.concurrent.locks.Condition;
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

	private ReentrantLock lock;
	private Condition notFull;
	private Condition notEmpty;
	
	public BoundedBuffer () {
		mValues = (T[]) new Object[CAPACITY];
		mNext = 0;
		mHead = 0;
		lock = new ReentrantLock();
		notFull = lock.newCondition();
		notEmpty = lock.newCondition();
	}
	
	public void put(T pValue) throws InterruptedException {
		lock.lock();
		try {

			// First check to see if the buffer is full.
			while (mNext - mHead >= CAPACITY) {
				System.out.println("Full when attempting put!");
				notFull.await();
			}

			// Now we know there is space so add the new value.
			mValues[mNext % CAPACITY] = pValue;
			mNext++;
		} finally {
			notEmpty.signalAll();
			lock.unlock();
		}		
	}
	
	public T get() throws InterruptedException {
		lock.lock();
		try {

			// First check to see if there is anything in the buffer.
			while (mNext - mHead <= 0) {
				System.out.println("Empty when attempting get!");
				notEmpty.await();
			}
			
			// Now we know that a value is present, so get the head value
			T vResult = mValues[mHead++];
			
			// Adjust if we've wrapped around in the buffer.
			if (mHead >= CAPACITY) {
				mHead -= CAPACITY;
				mNext -= CAPACITY;
			}
			
			// And return the retrieved value.
			return vResult;
		} finally {
			notFull.signalAll();
			lock.unlock();
		}
	}
}