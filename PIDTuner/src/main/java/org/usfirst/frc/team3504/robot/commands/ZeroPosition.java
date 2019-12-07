package org.usfirst.frc.team3504.robot.commands;

import org.usfirst.frc.team3504.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ZeroPosition extends Command {

	public ZeroPosition() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.motor);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		System.out.println("Zeroing motor encoder position");
		Robot.motor.zeroPosition();
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		// All the work is done in initialize
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		// This command always finishes immediately, turning it back over to the default command
		return true;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		// No need to stop motors or anything else when zeroing the position.
		// The default command (ManualControl) will run next, 
		// switching the motor to speed controlled by joy stick position.
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		end();
	}
}
