/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3504.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// CTRE magnetic encoder in relative (quadrature) mode has 4096 native units per rotation.
	public static final int UNITS_PER_ROTATION = 4096;
	// There's no gearing between the encoder and output shaft on our test board, so the ratio is 1:1.
	public static final double GEAR_RATIO = 1.0;
	// What is the CAN ID for the Talon SRX motor controller?
	public static final int TALON_ID = 1;
}
