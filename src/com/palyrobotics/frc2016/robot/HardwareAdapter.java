package com.palyrobotics.frc2016.robot;

import com.ctre.CANTalon;
import com.palyrobotics.frc2016.config.Constants;
import com.palyrobotics.frc2016.util.XboxController;
import com.palyrobotics.frc2016.robot.team254.lib.util.CheesySpeedController;

import edu.wpi.first.wpilibj.*;
//TODO: Set the DPP's somehow

/**
 * Represents all hardware components of the robot.
 * Singleton class. Should only be used in robot package, and 254lib.
 * Subdivides hardware into subsystems.
 * Example call: HardwareAdapter.getInstance().getDrivetrain().getLeftMotor()
 *
 * @author Nihar
 */
public class HardwareAdapter {
	// Hardware components at the top for maintenance purposes, variables and getters at bottom
	/* 
	 * DRIVETRAIN
	 */
	public static class DrivetrainHardware {
		private static DrivetrainHardware instance = new DrivetrainHardware();

		protected static DrivetrainHardware getInstance() {
			return instance;
		}
		public final CANTalon leftSlaveTalon;
		public final CANTalon leftMasterTalon;
		public final CANTalon rightSlaveTalon;
		public final CANTalon rightMasterTalon;

		public final Encoder leftEncoder;
		public final Encoder rightEncoder;
		public final ADXRS450_Gyro gyro;
		public final DoubleSolenoid shifterSolenoid;

		private DrivetrainHardware() {
			if (Constants.kRobotName == Constants.RobotName.TYR) {
				leftSlaveTalon = new CANTalon(Constants.kTyrLeftDriveFrontMotorDeviceID);
				leftMasterTalon = new CANTalon(Constants.kTyrLeftDriveBackMotorDeviceID);
				rightSlaveTalon = new CANTalon(Constants.kTyrRightDriveFrontMotorDeviceID);
				rightMasterTalon = new CANTalon(Constants.kTyrRightDriveBackMotorDeviceID);
//				leftEncoder = new Encoder(
//						Constants.kTyrLeftDriveEncoderDIOA, Constants.kTyrLeftDriveEncoderDIOB);
//				rightEncoder = new Encoder(
//						Constants.kTyrRightDriveEncoderDIOA, Constants.kTyrRightDriveEncoderDIOB);
				// Currently encoders on Tyr are non-existent
				leftEncoder = null;
				rightEncoder = null;
				gyro = new ADXRS450_Gyro();
				shifterSolenoid = new DoubleSolenoid(
						Constants.kTyrDriveSolenoidExtend, Constants.kTyrDriveSolenoidRetract);
			} else {
				leftMasterTalon = new CANTalon(Constants.kDericaLeftDriveMasterDeviceID);
				leftSlaveTalon = new CANTalon(Constants.kDericaLeftDriveFrontMotorDeviceID);
				rightMasterTalon = new CANTalon(Constants.kDericaRightDriveBackMotorDeviceID);
				rightSlaveTalon = new CANTalon(Constants.kDericaRightDriveFrontMotorDeviceID);

				leftMasterTalon.enableBrakeMode(true);
				leftSlaveTalon.enableBrakeMode(true);
				rightSlaveTalon.enableBrakeMode(true);
				rightMasterTalon.enableBrakeMode(true);

				leftMasterTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
				rightMasterTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
				leftMasterTalon.reverseSensor(true);
				rightMasterTalon.reverseOutput(true);

				// Zero encoders
				leftMasterTalon.setEncPosition(0);
				rightMasterTalon.setEncPosition(0);
				leftMasterTalon.configPeakOutputVoltage(Constants.kPeakVoltage, -Constants.kPeakVoltage);
				rightMasterTalon.configPeakOutputVoltage(Constants.kPeakVoltage, -Constants.kPeakVoltage);

				leftEncoder = null;
				rightEncoder = null;
//				leftEncoder = new Encoder(
//						Constants.kDericaLeftDriveEncoderDIOA, Constants.kDericaLeftDriveEncoderDIOB, true);
//				rightEncoder = new Encoder(
//						Constants.kDericaRightDriveEncoderDIOA, Constants.kDericaRightDriveEncoderDIOB);
				gyro = new ADXRS450_Gyro();
				// no shifter solenoid
				shifterSolenoid = null;
			}
		}
	}

	/*
	 * INTAKE - has some null hardware components
	 */
	public static class IntakeHardware {
		private static IntakeHardware instance = new IntakeHardware();

		protected static IntakeHardware getInstance() {
			return instance;
		}
		public final CheesySpeedController leftIntakeMotor;
		public final CheesySpeedController rightIntakeMotor;
		public final CheesySpeedController intakeArmMotor;
		public final AnalogPotentiometer intakeArmPotentiometer;

		private IntakeHardware() {
			if (Constants.kRobotName == Constants.RobotName.TYR) {
				leftIntakeMotor = new CheesySpeedController(
						new VictorSP(Constants.kTyrLeftIntakeMotorDeviceID),
						Constants.kTyrLeftIntakeMotorPDP);
				rightIntakeMotor = new CheesySpeedController(
						new VictorSP(Constants.kTyrRightIntakeMotorDeviceID),
						Constants.kTyrRightIntakeMotorPDP);
				intakeArmMotor = null;
				intakeArmPotentiometer = null;
			} else {
				leftIntakeMotor = new CheesySpeedController(
						new CANTalon(Constants.kDericaIntakeMotorPWM),
						Constants.kDericaIntakeMotorPDP);
				rightIntakeMotor = leftIntakeMotor;
				intakeArmMotor = new CheesySpeedController(
						new CANTalon(Constants.kDericaArmIntakeMotorPWM),
						Constants.kDericaArmIntakeMotorPDP);
				intakeArmPotentiometer = null;
			}
		}
	}

	/*
	 * SHOOTER/CATAPULT
	 * TyrShooter comes with Grabber
	 */
	public static class ShooterHardware {
		private static ShooterHardware instance = new ShooterHardware();

		public static ShooterHardware getInstance() {
			return instance;
		}

		// Pneumatic solenoids, only instantiate if Tyr
		public final DoubleSolenoid pistonSolenoid;
		public final DoubleSolenoid latchSolenoid;
		public final DoubleSolenoid grabberSolenoid;
		public final CheesySpeedController shooterMotor;
		public final AnalogPotentiometer shooterPotentiometer;

		private ShooterHardware() {
			if (Constants.kRobotName == Constants.RobotName.TYR) {
				pistonSolenoid = new DoubleSolenoid(
						Constants.kShooterSolenoidPortExtend, Constants.kShooterSolenoidPortRetract);
				latchSolenoid = new DoubleSolenoid(
						Constants.kLatchSolenoidPortExtend, Constants.kLatchSolenoidPortRetract);
				grabberSolenoid = new DoubleSolenoid(
						Constants.kGrabberSolenoidPortExtend, Constants.kGrabberSolenoidPortRetract);
				shooterMotor = new CheesySpeedController(new CANTalon(Constants.kTyrShooterMotorDeviceID),
						Constants.kTyrShooterMotorPDP);
				shooterPotentiometer = new AnalogPotentiometer(Constants.kTyrShooterPotentiometerPort);
			} else {
				pistonSolenoid = null;
				latchSolenoid = null;
				grabberSolenoid = null;
				shooterMotor = null;
				shooterPotentiometer = null;
			}

		}
	}

	/*
	 * BREACHER
	 */
	public static class BreacherHardware {
		private static BreacherHardware instance = new BreacherHardware();

		public static BreacherHardware getInstance() {
			return instance;
		}
		public final CheesySpeedController breacherMotor;

		private BreacherHardware() {
			if (Constants.kRobotName == Constants.RobotName.TYR) {
				breacherMotor = new CheesySpeedController(new CANTalon(Constants.kBreacherMotorDeviceID), Constants.kBreacherMotorPDP);
			} else {
				breacherMotor = null;
			}
		}
	}
	
	public static class LowGoalShooterHardware {
		private static LowGoalShooterHardware instance = new LowGoalShooterHardware();
		
		public static LowGoalShooterHardware getInstance() {
			return instance;
		}
		public final CheesySpeedController lowGoalShooterMotor;
		
		private LowGoalShooterHardware() {
			if(Constants.kRobotName == Constants.RobotName.DERICA) {
				lowGoalShooterMotor = new CheesySpeedController(new Victor(Constants.kDericaLowGoalShooterPWM), Constants.kDericaLowGoalShooterPDP);
			} else {
				lowGoalShooterMotor = null;
			}
		}
	}
	
	public static class ClimberHardware {
		private static ClimberHardware instance = new ClimberHardware();
		
		public static ClimberHardware getInstance() {
			return instance;
		}
		
		public final CheesySpeedController climbingMotor;
		public final Encoder positionEncoder;
		
		private ClimberHardware() {
			climbingMotor = new CheesySpeedController(new TalonSRX(Constants.kSteikClimberMotorPDP), Constants.kSteikClimberMotorPWM);
			positionEncoder = new Encoder(Constants.kSteikClimberEncoderDIOA, Constants.kSteikClimberEncoderDIOB);
		}
	}
	
	public final static PowerDistributionPanel PDP = new PowerDistributionPanel();

	// Joysticks for operator interface
	protected static class Joysticks {
		private static Joysticks instance = new Joysticks();

		public static Joysticks getInstance() {
			return instance;
		}

		public final Joystick leftStick = new Joystick(0);
		public final Joystick rightStick = new Joystick(1);
		public final Joystick operatorStick;

		public Joysticks() {
			if (Constants.kRobotName == Constants.RobotName.TYR) {
				operatorStick = new XboxController(2);
			} else {
				operatorStick = new Joystick(2);
			}
		}
	}

	// Wrappers to access hardware groups
	public DrivetrainHardware getDrivetrain() {
		return DrivetrainHardware.getInstance();
	}

	public IntakeHardware getIntake() {
		return IntakeHardware.getInstance();
	}

	public ShooterHardware getShooter() {
		return ShooterHardware.getInstance();
	}

	public BreacherHardware getBreacher() {
		return BreacherHardware.getInstance();
	}

	public LowGoalShooterHardware getLowGoalShooter() {
		return LowGoalShooterHardware.getInstance();
	}
	
	public ClimberHardware getClimber() {
		return ClimberHardware.getInstance();
	}
	
	public Joysticks getJoysticks() {
		return Joysticks.getInstance();
	}

	// Singleton set up
	private static final HardwareAdapter instance = new HardwareAdapter();

	public static HardwareAdapter getInstance() {
		return instance;
	}
}