package com.palyrobotics.frc2018.config;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.palyrobotics.frc2018.util.Pose;
import com.team254.lib.trajectory.*;

import java.util.Map;

/**
 * Holds all hardware input, such as sensors. <br />
 * Can be simulated
 * @author Nihar
 *
 */
public class RobotState {
	public enum GamePeriod {
		AUTO, TELEOP, DISABLED
	}
	// Updated by autoInit, teleopInit, disabledInit
	public GamePeriod gamePeriod = GamePeriod.DISABLED;

	// Drivetrain
	public ControlMode leftControlMode = ControlMode.Disabled;
	public ControlMode rightControlMode = ControlMode.Disabled;
	
	public double leftSetpoint = 0;
	public double rightSetpoint = 0;
	
	// Tracks total voltage from kPDP
	public double totalVoltageDraw = 0;

	// Pose stores drivetrain sensor data
	public Pose drivePose = new Pose(0,0,0,0,0, 0, 0,0,0,0);

	//Robot position
    public static final int kObservationBufferSize = 100;
    public static final double kMaxTargetAge = 0.4;
    
    // FPGATimestamp -> RigidTransform2d or Rotation2d
    protected RigidTransform2d.Delta vehicle_velocity_;
    protected double differential_height_;
    protected InterpolatingTreeMap<InterpolatingDouble, RigidTransform2d> field_to_vehicle_;
    
    public synchronized void reset(double start_time, RigidTransform2d initial_field_to_vehicle,
            Rotation2d initial_turret_rotation) {
        field_to_vehicle_ = new InterpolatingTreeMap<>(kObservationBufferSize);
        field_to_vehicle_.put(new InterpolatingDouble(start_time), initial_field_to_vehicle);
        vehicle_velocity_ = new RigidTransform2d.Delta(0, 0, 0);
    }

    public synchronized RigidTransform2d getFieldToVehicle(double timestamp) {
        return field_to_vehicle_.getInterpolated(new InterpolatingDouble(timestamp));
    }

    public synchronized Map.Entry<InterpolatingDouble, RigidTransform2d> getLatestFieldToVehicle() {
        return field_to_vehicle_.lastEntry();
    }

    public synchronized RigidTransform2d getPredictedFieldToVehicle(double lookahead_time) {
        return getLatestFieldToVehicle().getValue().transformBy(
                RigidTransform2d.fromVelocity(new RigidTransform2d.Delta(vehicle_velocity_.dx * lookahead_time,
                        vehicle_velocity_.dy * lookahead_time, vehicle_velocity_.dtheta * lookahead_time)));
    }

    public synchronized void addFieldToVehicleObservation(double timestamp, RigidTransform2d observation) {
        field_to_vehicle_.put(new InterpolatingDouble(timestamp), observation);
        //System.out.println("YOU DID IT! number of observations is " + field_to_vehicle_.size());
        //System.out.println("Observation is " + observation);
    }

    public synchronized void addObservations(double timestamp, RigidTransform2d field_to_vehicle,
            RigidTransform2d.Delta velocity) {
        addFieldToVehicleObservation(timestamp, field_to_vehicle);
        vehicle_velocity_ = velocity;
    }

    public RigidTransform2d generateOdometryFromSensors(double left_encoder_delta_distance,
                                                        double right_encoder_delta_distance, Rotation2d current_gyro_angle) {
        RigidTransform2d last_measurement = getLatestFieldToVehicle().getValue();
        return Kinematics.integrateForwardKinematics(last_measurement, left_encoder_delta_distance,
                right_encoder_delta_distance, current_gyro_angle);
    }
    
    public int getNumObservations() {
    	return field_to_vehicle_.size();
    }
}
