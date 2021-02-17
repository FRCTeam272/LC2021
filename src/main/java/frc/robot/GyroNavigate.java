package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/****************************************************************************************************
 * Class used to attack the tower face for the Lansdale Catholic Team 272 2106 Robot in the Stronghold challenge.
 * It must use camera targeting to be sure it is facing the tower face.  
 * When called it is assumed the robot is pointing right at the face.
 * <p> 
 * It has to...<br>
 * 1) Run in straight to the tower face.<br>
 * 2) Determine when it reached it.<br>
 * 3) Position the Balista for the shot when it reaches the tower face.<br>
 * 3) Fire or Not fire at the goal represented in attackCode.i_Goal.<br>
 * 4) Set b_IsDone to true when complete.<br>  
 *<p>			 
 * @author FRC272 - Lansdale Catholic Robotics 
 * @version 1.00
 *   
 */
public class GyroNavigate {

	boolean atAngle;
	boolean onCourse;						// flag to indicate our Attack program is done
	/**
	 * Constructor
	 */
	public GyroNavigate() {

		onCourse = false; 
		atAngle = false;

	}


	public double getStraightSteeringCorrection(Sensors sensors) {

		return getAngleCorrection(sensors, 0.0);
	}
	
	public double getAngleCorrection(Sensors sensors, double desiredAngle){

		double bearingSensitivity;
		double correctionRotation;
		double minCorrectionRotation;
		double maxCorrectionRotation;
		double correctionProportionalAdjustment;
		double currBearing;
		double result;

		result = 0.0;
		bearingSensitivity = 1.0;										// we have to be within this to make a correction
																		// If we are outside -0.5 or 0.5 we will correct
		minCorrectionRotation = .04;									// Minimum rotation to actually cause a turn in the robot
		maxCorrectionRotation = .25;
		correctionProportionalAdjustment = .02;
		currBearing = sensors.getGyroRelativeBearing();
		if(Math.abs(currBearing - desiredAngle) > bearingSensitivity) {
			correctionRotation = Math.abs(currBearing - desiredAngle) * correctionProportionalAdjustment;	
			if (correctionRotation < minCorrectionRotation )			// make sure we have at least minimum power				
				correctionRotation = minCorrectionRotation;
			else if (correctionRotation > maxCorrectionRotation)
				correctionRotation = maxCorrectionRotation;
			if( currBearing < 0 )										// we are drifting left, we need to go right
				result = correctionRotation;							// drifting left, pull us right
			else
				result = -correctionRotation;							// drifting right, pull us left
		}
		return result;
	}
}