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

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	// Create the joy stick object in slot 0, the top-most listed in the driver's station UI
	private Joystick joystick = new Joystick(0);

	/**
	 * Create the operator interface (OI), the definition of joy sticks and assignment of buttons to Commands.
	 * Call this from Robot.RobotInit() only after creating all subsystems to avoid NullPointerExceptions.
	 */
	public OI() {
		// Define a couple of buttons to run the motor under PID control for testing PID parameters

		// Toggle between manual drive with the joy stick versus automatic control via the PID controller
		Button startStopPID = new JoystickButton(joystick, 1);
		startStopPID.toggleWhenPressed(new PIDControl());
		
		Button zeroPosition = new JoystickButton(joystick, 2);
		zeroPosition.whenPressed(new ZeroPosition());
	}

	/**
	 * Read and return the joy stick position to be used to set the motor speed 
	 */
	public double getStickPosition() {
		// Negate the Y axis value because pushing the stick forward returns negative values.
		// (This makes sense when joy sticks are used in airplanes, with forward pointing the plane downward)
		return -joystick.getY();
	}
}
