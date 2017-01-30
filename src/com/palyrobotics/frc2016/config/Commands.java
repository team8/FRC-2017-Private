package com.palyrobotics.frc2016.config;

import java.util.ArrayList;
import java.util.Optional;

import com.palyrobotics.frc2016.behavior.Routine;
import com.palyrobotics.frc2016.config.Commands.JoystickInput.XboxInput;
import com.palyrobotics.frc2016.util.DriveSignal;
import com.palyrobotics.frc2016.subsystems.Climber;
import com.palyrobotics.frc2016.subsystems.Drive;
import com.palyrobotics.frc2016.subsystems.Flippers;
import com.palyrobotics.frc2016.subsystems.Intake;
import com.palyrobotics.frc2016.subsystems.Spatula;

import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * Commands represent the desired setpoints and subsystem states for the robot. <br />
 * Store Requests (enum) for each subsystem and setpoints {@link Setpoints} <br />
 * Directly stores real or mock Joystick input in {@link JoystickInput} <br />
 * Variables are public and have default values to prefer NullPointerExceptions
 * @author Nihar
 *
 */
public class Commands {
	public ArrayList<Routine> wantedRoutines = new ArrayList<Routine>();

	// Store WantedStates for each subsystem state machine
	public Drive.DriveState wantedDriveState = Drive.DriveState.NEUTRAL;
	public Climber.ClimberState wantedClimbState = Climber.ClimberState.IDLE;
	public Flippers.FlipperSignal wantedFlipperSignal = new Flippers.FlipperSignal(
			DoubleSolenoid.Value.kForward, DoubleSolenoid.Value.kForward);
	public Spatula.SpatulaState wantedSpatulaState = Spatula.SpatulaState.UP;
	public Intake.IntakeState wantedIntakeState = Intake.IntakeState.IDLE;

	public void addWantedRoutine(Routine wantedRoutine) {
		for(Routine routine : wantedRoutines) {
			if(routine.getClass().equals(wantedRoutine.getClass())) {
				System.out.println("tried to add duplicate routine!");
				return;
			}
		}
		wantedRoutines.add(wantedRoutine);
	}
	
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
	 * @author Nihar
	 */
	public static class JoystickInput {
		public static class XboxInput extends JoystickInput {
			public double leftX, leftY, rightX, rightY;
			public XboxInput(double leftX, double leftY, double rightX, double rightY) {
				super(leftX, leftY, false);
				this.leftX = leftX;
				this.leftY = leftY;
				this.rightX = rightX;
				this.rightY = rightY;
			}
		}
		public double x,y;
		public boolean triggerPressed;
		public JoystickInput(double x, double y, boolean triggerPressed) {
			this.x = x; this.y = y; this.triggerPressed = triggerPressed;
		}
	}
	// Stores Joystick values
	public JoystickInput leftStickInput = new JoystickInput(0,0, false);
	public JoystickInput rightStickInput = new JoystickInput(0,0, false);
	public JoystickInput operatorStickInput = new JoystickInput(0, 0, false);

	// Allows you to cancel all running routines
	public boolean cancelCurrentRoutines = false;
}