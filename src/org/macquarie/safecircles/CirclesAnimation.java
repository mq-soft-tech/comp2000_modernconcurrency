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

package org.macquarie.safecircles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Simple class implementing a thread safe version of the circle drawing animation
 * from week 8. This implements only the stepping and repainting processes. The 
 * creation and execution of a thread to actually drive the animation process is
 * managed by the {@link Animator} class. Consequently this class implements the
 * {@link Animatable} interface which allows the animator object to communicate
 * with it.
 * 
 * @author Dominic Verity
 *
 */
public class CirclesAnimation implements Animatable {
	
	// Class fields and constants

	/**
	 * Diameter of each circle to be drawn in the animation.
	 */
	private static final int CIRCLE_DIAMETER = 30;
	
	/**
	 * Width of the circle animation.
	 */
	private static final int WIDTH = 200;
	
	/**
	 * Height of the circle animation.
	 */
	private static final int HEIGHT = 300;
	
	// Member variables (fields).
	
	/**
	 * A random number generator for choosing the positions
	 * of each circle.
	 */
	private Random mGenerator;

	/**
	 * The back-buffer image upon which we shall draw our circles.
	 */
	private BufferedImage mImage;
	
	/**
	 * The graphics surface of the back-buffer image.
	 */
	private Graphics2D mCanvas;
	
	// Constructors
	
	/**
	 * Default constructor.
	 */
	public CirclesAnimation() {
		mGenerator = new Random();
		mImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		mCanvas = mImage.createGraphics();
		mCanvas.setColor(Color.BLACK);
	}
	
	// Methods

	/**
	 * Step the animation forward by adding an extra randomly generated circle
	 * to the animation image.
	 * 
	 * @see org.macquarie.safecircles.Animatable#step()
	 */
	@Override
	public synchronized void step() {
		int vXord = mGenerator.nextInt(WIDTH - CIRCLE_DIAMETER);
		int vYord = mGenerator.nextInt(HEIGHT - CIRCLE_DIAMETER);
		mCanvas.drawOval(vXord, vYord, CIRCLE_DIAMETER, CIRCLE_DIAMETER);	
	}

	/**
	 * Paint the current state of the animation onto a specified graphics canvas.
	 * 
	 * @see org.macquarie.safecircles.Animatable#paint(java.awt.Graphics)
	 */
	@Override
	public synchronized void paint(Graphics pGraphics) {
		pGraphics.drawImage(mImage, 0, 0, null);
	}

	/**
	 * @see org.macquarie.safecircles.Animatable#getWidth()
	 */
	@Override
	public int getWidth() {
		return WIDTH;
	}

	/**
	 * @see org.macquarie.safecircles.Animatable#getHeight()
	 */
	@Override
	public int getHeight() {
		return HEIGHT;
	}

}
