package com.palyrobotics.frc2016.config;

import java.util.Optional;

import com.palyrobotics.frc2016.config.Commands.JoystickInput.XboxInput;

/**
 * Commands represent the desired setpoints and subsystem states for the robot. <br />
 * Store Requests (enum) for each subsystem and setpoints {@link Setpoints} <br />
 * Directly stores real or mock Joystick input in {@link JoystickInput}
 * @author Nihar
 *
 */
public class Commands {
	static final Commands commands = new Commands();
	
	public static synchronized Commands getInstance() {
		return commands;
	}
	
	/**
	 * Stores all the subsystem setpoints, including what the currently running Routine is
	 * @author Nihar
	 */
	public static class Setpoints {
		public static final Optional<Double> NULLOPT = Optional.empty();
		
		public Routines currentRoutine;
		/**
		 * Resets all the setpoints
		 */
		public void reset() {
		}
	}
	// All robot setpoints
	public Setpoints robotSetpoints = new Setpoints();
	
	/**
	 * Class to store Joystick input
	 * Should eventually be extended to contain all buttons
	 * @author Nihar
	 */
	public static class JoystickInput {
		public static class XboxInput extends JoystickInput {
			public double leftX, leftY, rightX, rightY;
			public XboxInput(double leftX, double leftY, double rightX, double rightY) {
				super(leftX, leftY, false);
			}
		}
		public double x,y;
		public boolean triggerPressed;
		public JoystickInput(double x, double y, boolean triggerPressed) {
			this.x = x; this.y = y; this.triggerPressed = triggerPressed;
		}
		public JoystickInput(double x, double y) {
			this.x = x; this.y = y;
		}
	}
	// Stores Joystick values
	public JoystickInput leftStickInput;
	public JoystickInput rightStickInput;
	public XboxInput operatorStickInput;
	// Routine Request
	public static enum Routines {
		TIMER_DRIVE, ENCODER_DRIVE, TURN_ANGLE, NONE
	}
	// Routine requests
	public Routines routineRequest = Routines.NONE;
	
	// Allows you to cancel routine
	public boolean cancelCurrentRoutine = false;
}