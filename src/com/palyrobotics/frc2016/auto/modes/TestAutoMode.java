package com.palyrobotics.frc2016.auto.modes;

import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeEndedException;

/**
 * Created by Nihar on 1/11/17.
 * An AutoMode for running test autonomous
 */
public class TestAutoMode extends AutoMode {
	// Current configured to test gyro turn angle

	@Override
	protected void routine() throws AutoModeEndedException {
		drive.setBangBangTurnAngleSetpoint(90);
//		drive.setDistanceSetpoint(1);
	}

	@Override
	public String toString() {
		return "TestAutoMode";
	}

	@Override
	public void prestart() {
		System.out.println("Starting TestAutoMode");
	}
}
