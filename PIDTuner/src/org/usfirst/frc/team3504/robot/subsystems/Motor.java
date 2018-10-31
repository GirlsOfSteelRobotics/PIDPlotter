/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3504.robot.subsystems;

import org.usfirst.frc.team3504.robot.RobotMap;
import org.usfirst.frc.team3504.robot.commands.ManualControl;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * The motor and position encoder subsystem
 */
public class Motor extends Subsystem {
	// All methods that tell the motor controller what to do are encapsulated in this class, 
	// so keep this variable private! 
	private WPI_TalonSRX talon;
	
	// There are two levels of PID control available in the Talon SRX.
	// We only need to use the primary level, designated as slot zero.
	private final static int PID_SLOT_INDEX = 0;
	
	// Store the most recent target in native units so we can later decide if we've achieved the goal yet
	private int targetNativeUnits;

	// What is the allowable error, as a percentage, before considering the mechanism at the intended position?
	//private final static double ALLOWABLE_ERROR = 0.05;
	
	/**
	 * Construct the Motor object by creating a motor controller object
	 */
	public Motor () {
		talon = new WPI_TalonSRX(RobotMap.TALON_ID);
		// Our test board has a CTRE magnetic encoder as one stage of the VEX VersaPlanetary gear box
		talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
		talon.setSensorPhase(true);
	}

	/** 
	 * Define the command that will run by default. It will run at startup and
	 * when no other command that Requires(Robot.motor) is running.
	 */
	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new ManualControl());
	}

	// Put methods for controlling this subsystem here. Call these from Commands.

	/**
	 * Stop the motor (used when exiting manual control)
	 */
	public void stop() {
		talon.stopMotor();
	}

	/**
	 * Drive the motor at a certain speed (used when running manual joy stick control)
	 * 
	 * @param speed How fast to run the motor; full reverse is -1.0, neutral is 0, full forward is +1.0)
	 */
	public void setSpeed(double speed) {
		talon.set(ControlMode.PercentOutput, speed);
	}

	/**
	 * Zero the encoder position maintained by the Talon SRX motor controller
	 */
	public void zeroPosition() {
		talon.setSelectedSensorPosition(0);
	}
	
	/**
	 * Set the PID parameters (the proportional, integral, and derivative constants).
	 * This sample doesn't use velocity control, only position control, so forward feedback is zeroed. 
	 * The values should be read from the fields on the driver's station, 
	 * making it easy to change them while tuning. 
	 */
	public void updatePIDConstants(double kP, double kI, double kD) {
		talon.config_kP(PID_SLOT_INDEX, kP);
		talon.config_kI(PID_SLOT_INDEX, kI);
		talon.config_kD(PID_SLOT_INDEX, kD);
		talon.config_kF(PID_SLOT_INDEX, 0.0);
	}
	
	/**
	 * Get the current Proportional constant from the motor controller (used as the initial value in the UI)
	 */
	public double getSavedProportional() {
		return talon.configGetParameter(ParamEnum.eProfileParamSlot_P, PID_SLOT_INDEX);
	}

	/**
	 * Get the current Integral constant from the motor controller (used as the initial value in the UI)
	 */
	public double getSavedIntegral() {
		return talon.configGetParameter(ParamEnum.eProfileParamSlot_I, PID_SLOT_INDEX);
	}

	/**
	 * Get the current Derivative constant from the motor controller (used as the initial value in the UI)
	 */
	public double getSavedDerivative() {
		return talon.configGetParameter(ParamEnum.eProfileParamSlot_D, PID_SLOT_INDEX);
	}

	/**
	 * Translate a number of output rotations to the native units of the encoder.
	 * Requires a knowledge of the encoder specifications (native units per rotation)
	 * and of any gearing that's applied between the encoder and final output stage of the gear box.
	 * 
	 * @param rotations Number of rotations of the output shaft
	 * @return Equivalent position in native encoder units
	 */
	private int rotationsToNativeUnits(double rotations) {
		// Perform the math with floating point numbers, then round to the nearest integer number of units.		
		return (int)Math.round(rotations * RobotMap.UNITS_PER_ROTATION * RobotMap.GEAR_RATIO);
	}
	
	/**
	 * Translate a number of native units of the encoder to output rotations.
	 * Requires a knowledge of the encoder specifications (native units per rotation)
	 * and of any gearing that's applied between the encoder and final output stage of the gear box.
	 * 
	 * @param units Encoder position measured in native units of the encoder
	 * @return Equivalent position in output shaft rotations
	 */
	private double nativeUnitsToRotations(int units) {
		// Perform the math with floating point numbers, then round to the nearest integer number of units.		
		return (double)units / RobotMap.UNITS_PER_ROTATION / RobotMap.GEAR_RATIO;
	}
	
	/**
	 * Set the target position and enable the PID controller.
	 * 
	 * @param target The target position, in units of rotations of the output shaft
	 */
	public void setTargetPosition(double target) {
		targetNativeUnits = rotationsToNativeUnits(target);
		talon.set(ControlMode.Position, targetNativeUnits);
	}
	
	/**
	 * Read the encoder to learn the current position of the motor in output rotations.
	 * 
	 * @return Position of the output shaft measured in rotations
	 */
	public double getCurrentRotations() {
		return nativeUnitsToRotations(talon.getSelectedSensorPosition());
	}
	
	/**
	 * Determine if the PID controller has successfully moved the motor to the target position yet.
	 * 
	 * @return True if the current position is within ALLOWABLE_ERROR percentage of the target position 
	 */
	public boolean isPIDFinished() {
		//double error = Math.abs(targetNativeUnits - talon.getSelectedSensorPosition());
		//double percentError = (targetNativeUnits - error) / targetNativeUnits;
		//return percentError <= ALLOWABLE_ERROR;
		return false;
	}
}
