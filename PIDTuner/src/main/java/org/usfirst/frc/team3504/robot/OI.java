/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3504.robot;

import org.usfirst.frc.team3504.robot.commands.PIDControl;
import org.usfirst.frc.team3504.robot.commands.ZeroPosition;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	// Set the default target as the number of rotations
	private static final double DEFAULT_TARGET = 5.0;
	
	// Create the joystick object in slot 0, the top-most listed in the driver's station UI
	private Joystick joystick = new Joystick(0);
	Button startStopPID;
	Button zeroPosition;

	/**
	 * Create the operator interface (OI), the definition of joysticks and assignment of buttons to Commands.
	 * Call this from Robot.RobotInit() only after creating all subsystems to avoid NullPointerExceptions.
	 */
	public OI() {
		// Define a couple of buttons to run the motor under PID control for testing PID parameters

		// Toggle between manual drive with the joy stick versus automatic control via the PID controller
		startStopPID = new JoystickButton(joystick, 1);
		startStopPID.toggleWhenPressed(new PIDControl());
		
		// Set the current position reading back to zero
		zeroPosition = new JoystickButton(joystick, 2);
		zeroPosition.whenPressed(new ZeroPosition());
	}

	/**
	 * Read and return the joystick position to be used to set the motor speed 
	 */
	public double getSpeedFromJoystick() {
		// Negate the Y axis value because pushing the stick forward returns negative values.
		// (This makes sense when joysticks are used in airplanes, with forward pointing the plane downward)
		double rawValue = -joystick.getY();
		// Scale the joystick values to make the controls less touchy.
		// With this curve in place, the joystick needs to move to about 0.8 to result in a speed of 0.5.
		// That provides more range of motion for the slower speeds.
		// Take the absolute value of the first factor to ensure negative joystick values result in negative speeds.
		return Math.abs(rawValue)*rawValue*rawValue*rawValue;
	}
	
	/**
	 * Initialize the user interface by populating all the input/output fields with the provided starting values
	 * or some reasonable default
	 */
	public void initInterface(double kP, double kI, double kD) {
		displayMode("Disabled");
		displayPosition(0.0);
		displaySpeed(0);
		displayError(0);
		displayInstructions();
		setTarget(DEFAULT_TARGET);
		setProportional(kP);
		setIntegral(kI);
		setDerivative(kD);
	}
	
	/**
	 * Display the current position in rotations on the smart dashboard
	 */
	public void displayPosition(double rotations) {
		SmartDashboard.putString("Current Rotations", String.format("%.3f", rotations));
	}
	
	/**
	 * Display the current speed in native sensor units per 100 milliseconds
	 */
	public void displaySpeed(int speed) {
		SmartDashboard.putNumber("Current Speed", speed);
	}
	
	/**
	 * Display the error (target - current position) in rotations
	 */
	public void displayError(double error) {
		SmartDashboard.putNumber("Current Error", error);
	}
	
	/**
	 * Display the current mode of the UI, either Manual or PID Control
	 */
	public void displayMode(String mode) {
		SmartDashboard.putString("Mode", mode);
	}
	
	/**
	 * Give the user some tips on using the Smart Dashboard interface
	 */
	public void displayInstructions() {
		SmartDashboard.putString("Reminder:", "Must press Tab after entering a number to submit the change!");
	}
	
	/**
	 * Create an input field for the target number of rotations and populate it with the default value
	 */
	public void setTarget(double rotations) {
		SmartDashboard.putNumber("Target Rotations", rotations);
	}

	/**
	 * Get the target number of rotations from the user, reading it off Smart Dashboard
	 */
	public double getTarget() {
		return SmartDashboard.getNumber("Target Rotations", Double.NaN);
	}

	/**
	 * Create an input field for the Proportional constant
	 */
	public void setProportional(double kP) {
		SmartDashboard.putNumber("Proportional", kP);
	}

	/**
	 * Get the Proportional constant from the user, reading it off Smart Dashboard
	 */
	public double getProportional() {
		return SmartDashboard.getNumber("Proportional", 0.0);
	}

	/**
	 * Create an input field for the Integral constant
	 */
	public void setIntegral(double kI) {
		SmartDashboard.putNumber("Integral", kI);
	}

	/**
	 * Get the Integral constant from the user, reading it off Smart Dashboard
	 */
	public double getIntegral() {
		return SmartDashboard.getNumber("Integral", 0.0);
	}

	/**
	 * Create an input field for the Derivative constant
	 */
	public void setDerivative(double kP) {
		SmartDashboard.putNumber("Derivative", kP);
	}

	/**
	 * Get the Derivative constant from the user, reading it off Smart Dashboard
	 */
	public double getDerivative() {
		return SmartDashboard.getNumber("Derivative", 0.0);
	}
}
