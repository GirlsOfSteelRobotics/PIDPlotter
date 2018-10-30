/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3504.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team3504.robot.Robot;

/**
 * Start the PID controller to position the motor to the number of rotations requested by the user.
 * Stopping this command will cancel PID control.
 */
public class PIDControl extends Command {
	
	public PIDControl() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.motor);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		double rotations = 3.0;
		System.out.printf("Starting PID control with a target of %.2f rotations\n", rotations);
		Robot.motor.updatePIDConstants();
		Robot.motor.setTargetPosition(rotations);
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		// PID control continues automatically on the motor controller
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return Robot.motor.isPIDFinished();
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		System.out.println("Stopping PID control");
		Robot.motor.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		end();
	}
}
