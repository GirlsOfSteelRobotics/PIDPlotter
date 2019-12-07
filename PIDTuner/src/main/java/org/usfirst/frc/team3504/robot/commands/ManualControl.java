package org.usfirst.frc.team3504.robot.commands;

import org.usfirst.frc.team3504.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Run the motor under manual joy stick control using the Y axis of the main stick
 */
public class ManualControl extends Command {

	public ManualControl() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.motor);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		System.out.println("Starting manual control");
		Robot.oi.displayMode("Manual Control");
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		// Conveniently, the joy stick position is returned in the range -1.0 to +1.0,
		// same as is needed for setting the speed of the motor.
		Robot.motor.setSpeed(Robot.oi.getSpeedFromJoystick());
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		// As the default command for the Motor subsystem, this command should never finish.
		// Instead, it will be canceled and replaced by other commands when buttons are pressed.
		return false;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		System.out.println("Stopping manual control");
		Robot.oi.displayMode("");
		Robot.motor.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		end();
	}
}
