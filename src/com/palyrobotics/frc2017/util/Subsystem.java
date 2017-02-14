package com.palyrobotics.frc2017.util;

import com.palyrobotics.frc2017.config.Commands;
import com.palyrobotics.frc2017.config.RobotState;
import com.palyrobotics.frc2017.util.archive.SubsystemLoop;

public abstract class Subsystem implements SubsystemLoop {
	protected Dashboard dashboard = Dashboard.getInstance();
	private String mName;

	public Subsystem(String name) {
		this.mName = name;
//		SystemManager.getInstance().add(this);
	}
	// Updates the subsystem with current commands and state
	public abstract void update(Commands commands, RobotState robotState);

	public String getName() {
		return mName;
	}

	@Override
	public String toString() {
		return mName;
	}
//	public abstract void reloadConstants();
}
