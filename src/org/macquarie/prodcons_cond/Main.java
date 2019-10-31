/**
 * This file is part of a project entitled ModernConcurrency which is provided
 * as sample code for the following Macquarie University unit of study:
 * 
 * COMP229 "Object Oriented Programming Practices"
 * 
 * Copyright (c) 2011 Dominic Verity and Macquarie University.
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

/**
 * An application which sets up a producer and a consumer object which
 * may communicate via a shared bounded buffer. Then it sets them going to see
 * what happens.
 * 
 * @author Dominic Verity
 *
 */
public class Main {

	/**
	 * The main entry point for this application.
	 * @param args - command line arguments, not used in this example.
	 */
	public static void main(String[] args) {
		// Create a bounded buffer.
		BoundedBuffer<Integer> vBuffer = new BoundedBuffer<Integer>();
		
		// Create consumer and producer threads which talk with that buffer
		Producer vProducer = new Producer(vBuffer);
		Consumer vConsumer = new Consumer(vBuffer);
		
		// And start them....
		vProducer.start();
		vConsumer.start();
	}

}
