/**
 * This file is part of a project entitled ModernConcurrency which is provided as
 * sample code for the following Macquarie University unit of study:
 * 
 * COMP229 "Object Oriented Programming Practices"
 * 
 * Copyright (c) 2019 Dominic Verity and Macquarie University.
 * 
 * ModernConcurrency is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * ModernConcurrency is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ModernConcurrency. (See files COPYING and COPYING.LESSER.) If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.macquarie.atomic_counter;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
	private AtomicInteger value;

	Counter() {
		value = new AtomicInteger();
	}

	public void old_increment() {
		value.incrementAndGet();
	}

	// Atomic incrementer using Compare and Swap instruction.
	public void increment() {
		int count, newCount;
		do {
			count = value.get();
			newCount = count + 1;
		} while (!value.compareAndSet(count, newCount));
	}

	public void decrement() {
		value.decrementAndGet();
	}

	public int get() {
		return value.get();
	}
}