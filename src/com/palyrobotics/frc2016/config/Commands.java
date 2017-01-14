package com.palyrobotics.frc2016.config;

import java.util.Optional;

import com.palyrobotics.frc2016.config.Commands.JoystickInput.XboxInput;
import com.palyrobotics.frc2016.robot.team254.lib.util.DriveSignal;

/**
 * Commands represent the desired setpoints and subsystem states for the robot. <br />
 * Store Requests (enum) for each subsystem and setpoints {@link Setpoints} <br />
 * Directly stores real or mock Joystick input in {@link JoystickInput}
 * @author Nihar
 *
 */
public class Commands {
	private static Commands commands = new Commands();
	
	public static synchronized Commands getInstance() {
		return commands;
	}

	// Store WantedStates for each subsystem state machine
	public enum WantedDriveState {
		NONE, ROUTINE, CHEESY
	}
	public WantedDriveState wantedDriveState = WantedDriveState.NONE;
	/**
	 * Stores numeric setpoints
	 * @author Nihar
	 */
	public static class Setpoints {
		public static final Optional<Double> NULLOPT = Optional.empty();
		
		public Optional<DriveSignal> drivePowerSetpoint = Optional.empty();

		/**
		 * Resets all the setpoints
		 */
		public void reset() {
			drivePowerSetpoint = Optional.empty();
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

	// Allows you to cancel all running routines
	public boolean cancelCurrentRoutine = false;
}