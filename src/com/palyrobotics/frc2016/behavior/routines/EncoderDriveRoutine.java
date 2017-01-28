package com.palyrobotics.frc2016.behavior.routines;

import com.palyrobotics.frc2016.behavior.Routine;
import com.palyrobotics.frc2016.config.Commands;

import com.palyrobotics.frc2016.subsystems.Drive;
import com.palyrobotics.frc2016.util.Subsystem;

/**
 * Drives forward a specified distance
 * Uses right encoder to determine if distance is reached
 * Times out after specified seconds, default m_default_timeout
 * @author Nihar
 */
public class EncoderDriveRoutine extends Routine {
	@Override
	public Subsystem[] getRequiredSubsystems() {
		return new Subsystem[]{drive};
	}
	/*
	 * START = Set new drive setpoint
	 * DRIVING = Waiting to reach drive setpoint
	 * DONE = reached target or not operating
	 */
	private enum EncoderDriveRoutineStates {
		START, DRIVING, DONE
	}

	EncoderDriveRoutineStates state = EncoderDriveRoutineStates.START;
	private double mDistance;
	private double mVelocitySetpoint;
	private final double kDefaultVelocitySetpoint = 0.5;
	
	// Timeout after x seconds
	private double mTimeout;
	private final double kDefaultTimeout = 5;
	private long mStartTime;

	private boolean mIsNewState = true;

	/**
	 * Constructs with target distance
	 * Uses default timeout and default velocity setpoint
	 * @param distance Target distance to travel
	 */
	public EncoderDriveRoutine(double distance) {
		this.mDistance = distance;
		this.mTimeout = kDefaultTimeout;
		setVelocity(kDefaultVelocitySetpoint);
	}
	
	/**
	 * Constructs with specified timeout
	 * @param distance Target distance to travel
	 * @param timeout Time (seconds) before timeout
	 */
	public EncoderDriveRoutine(double distance, int timeout) {
		this.mDistance = distance;
		this.mTimeout = timeout;
		setVelocity(kDefaultVelocitySetpoint);
	}
	
	/**
	 * 
	 * @param distance Target distance to travel
	 * @param timeout Time (seconds) before timeout
	 * @param velocity Target velocity
	 */
	public EncoderDriveRoutine(double distance, double timeout, double velocity) {
		this.mDistance = distance;
		this.mTimeout = timeout;
		setVelocity(velocity);
	}
	
	/**
	 * Sets the velocity setpoint
	 * @param velocity target velocity to drive at (0 to 1)
	 * @return true if valid setspeed
	 */
	public boolean setVelocity(double velocity) {
		if(velocity > 0) {
			this.mVelocitySetpoint = velocity;
			return true;
		}
		return false;
	}

	//Routines just change the states of the robotsetpoints, which the behavior manager then moves the physical subsystems based on.
	@Override
	public Commands update(Commands commands) {
		EncoderDriveRoutineStates newState = state;
		switch (state) {
		case START:
			mStartTime = System.currentTimeMillis();
			// Only set the setpoint the first time the state is START
			if(mIsNewState) {
				drive.setDistanceSetpoint(mDistance, mVelocitySetpoint);
			}
			commands.wantedDriveState = Drive.DriveState.CONTROLLER;
			newState = EncoderDriveRoutineStates.DRIVING;
			break;
		case DRIVING:
			if(drive.controllerOnTarget()) {
				newState = EncoderDriveRoutineStates.DONE;
			}
			if((System.currentTimeMillis()-mStartTime) > mTimeout*1000) {
				newState = EncoderDriveRoutineStates.DONE;
			}
			break;
		case DONE:
			drive.resetController();
			break;
		}
		
		mIsNewState = false;
		if(newState != state) {
			state = newState;
			mIsNewState = true;
		}
		
		return commands;
	}

	@Override
	public Commands cancel(Commands commands) {
		state = EncoderDriveRoutineStates.DONE;
		commands.wantedDriveState = Drive.DriveState.NEUTRAL;
		drive.resetController();
		return commands;
	}

	@Override
	public void start() {
		drive.resetController();
		mStartTime = System.currentTimeMillis();
	}

	@Override
	public boolean finished() {
		return state == EncoderDriveRoutineStates.DONE;
	}

	@Override
	public String getName() {
		return "EncoderDriveRoutine";
	}

}
