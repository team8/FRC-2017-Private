package com.palyrobotics.frc2016.robot;

import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeExecuter;
import com.palyrobotics.frc2016.auto.AutoModeSelector;
import com.palyrobotics.frc2016.behavior.RoutineManager;
import com.palyrobotics.frc2016.config.RobotState;
import com.palyrobotics.frc2016.subsystems.*;
import com.palyrobotics.frc2016.util.Dashboard;
import com.palyrobotics.frc2016.util.SubsystemLooper;
import com.palyrobotics.frc2016.robot.team254.lib.util.DriveSignal;
import com.palyrobotics.frc2016.robot.team254.lib.util.RobotData;
import com.palyrobotics.frc2016.robot.team254.lib.util.SystemManager;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Robot extends IterativeRobot {
	// Instantiate singleton classes
	private static RobotState robotState = new RobotState();

	public static RobotState getRobotState() {
		return robotState;
	}

	private static HardwareAdapter hardwareAdapter = HardwareAdapter.getInstance();
	private static OperatorInterface operatorInterface = OperatorInterface.getInstance();
	// Instantiate separate thread controls
	private SubsystemLooper mSubsystemLooper = new SubsystemLooper();
	private RoutineManager mRoutineManager = new RoutineManager();
	private AutoModeExecuter mAutoModeRunner = new AutoModeExecuter(mRoutineManager);

	// Subsystem controllers
	private Drive mDrive = Drive.getInstance();
	
	//hardware updater
	private HardwareUpdater mHardwareUpdater;

	private Dashboard mDashboard = Dashboard.getInstance();
	NetworkTable sensorTable;

	static {
		SystemManager.getInstance().add(new RobotData());
	}

	@Override
	public void robotInit() {
		System.out.println("Start robotInit()");
		// Gyro initialization
		HardwareAdapter.getInstance().getDrivetrain().gyro.calibrate();
		mSubsystemLooper.register(mDrive);
		mHardwareUpdater = new HardwareUpdater(mDrive);
		//        SystemManager.getInstance().add(routineManager);
		sensorTable = NetworkTable.getTable("Sensor");
		mDashboard.init();
		System.out.println("End robotInit()");
	}

	@Override
	public void autonomousInit() {
		System.out.println("Start autonomousInit()");
		robotState.gamePeriod = RobotState.GamePeriod.AUTO;

		mDrive.resetController();
		
		AutoMode mode = AutoModeSelector.getInstance().getAutoMode();
		mAutoModeRunner.setAutoMode(mode);
		// Prestart auto mode
		mode.prestart();
		mAutoModeRunner.start();
		// Start control loops
		mSubsystemLooper.start();
		System.out.println("End autonomousInit()");
	}

	@Override
	public void autonomousPeriodic() {
		mRoutineManager.update();
		mDashboard.update();
		mHardwareUpdater.updateSensors();
		mHardwareUpdater.updateSubsystems();
	}

	@Override
	public void teleopInit() {
		System.out.println("Start teleopInit()");
		robotState.gamePeriod = RobotState.GamePeriod.TELEOP;
		mRoutineManager.reset();
		operatorInterface.updateCommands();
		mSubsystemLooper.start();
		System.out.println("End teleopInit()");
	}

	@Override
	public void teleopPeriodic() {
		// Update RobotState
		mHardwareUpdater.updateSensors();
		// Gets joystick commands
		operatorInterface.updateCommands();
		
		// Updates commands based on routines
		mRoutineManager.update();

		//Update the hardware
		mHardwareUpdater.updateSubsystems();
		
		// Update sensorTable with encoder distances
		sensorTable.putString("left", String.valueOf(robotState.drivePose.getLeftDistance()));
		sensorTable.putString("right", String.valueOf(robotState.drivePose.getRightDistance()));
		mDashboard.update();
	}

	@Override
	public void disabledInit() {
		System.out.println("Start disabledInit()");
		System.out.println("Current Auto Mode: " + AutoModeSelector.getInstance().getAutoMode().toString());
		robotState.gamePeriod = RobotState.GamePeriod.DISABLED;
		// Stop auto mode
		mAutoModeRunner.stop();

		// Stop routine_request
		mRoutineManager.reset();

		// Stop control loops
		mSubsystemLooper.stop();

		// Stop controllers
		mDrive.setOpenLoop(DriveSignal.NEUTRAL);

		// Manually run garbage collector
		System.gc();

		System.out.println("End disabledInit()");
	}

	@Override
	public void disabledPeriodic() {
		if(Dashboard.getInstance().getSelectedAutoMode() != "-1") {
			AutoModeSelector.getInstance().setFromDashboard();
		}
	}
}