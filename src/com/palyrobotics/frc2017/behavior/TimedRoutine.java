package com.palyrobotics.frc2017.behavior;

import com.palyrobotics.frc2017.config.Commands;
import com.palyrobotics.frc2017.util.Subsystem;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Nihar on 12/27/16.
 */
public class TimedRoutine extends Routine {
	private Routine[] mRoutines;
	private double time, startTime;
	/**
	 *
	 * @param time Time in seconds before routine automatically finishes
	 */
	public TimedRoutine(double time, Routine... routines) {
		mRoutines = routines;
		this.time = time*1000;
	}

	@Override
	public void start() {
		this.startTime = System.currentTimeMillis();
	}

	@Override
	public Commands update(Commands commands) {
		for (Routine r : mRoutines) {
			commands = r.update(commands);
		}
		return commands;
	}

	@Override
	public Commands cancel(Commands commands) {
		for (Routine r : mRoutines) {
			commands = r.cancel(commands);
		}
		return commands;
	}

	@Override
	public boolean finished() {
		if (System.currentTimeMillis() > startTime + time) {
			System.out.println("Timed out routine");
			return true;
		}
		for (Routine r : mRoutines) {
			if (!r.finished()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Subsystem[] getRequiredSubsystems() {
		return RoutineManager.sharedSubsystems(new ArrayList<>(Arrays.asList(mRoutines)));
	}

	@Override
	public String getName() {
		String name = "(Timed"+time+"Routine of ";
		for (Routine r : mRoutines) {
			name += r.getName();
		}
		name+=")";
		return name;
	}
}
