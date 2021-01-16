package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class holds to variables used by several others to write the current
 * program or class running and the step in that class. The idea is that only
 * one of the classes will be operating at any time. This way we only have to
 * track 1 pair of variables on the screen and in telemetry. Look in the Auton
 * and Breach Class to see how they are used.
 * 
 */

public class AutonDescriptions {

	private String programDescription; // describe the auton programs
	private String stepDescription; // describe the step. Can be saved in
									// telemetry

	/**
	 * Constructor
	 */

	public AutonDescriptions() {

		programDescription = "Init";
		stepDescription = "Init";
	}

	public String getProgramDescription() {
		return programDescription;
	}

	public void setProgramDescription(String desc) {
		programDescription = desc;
	}

	/**
	 * Sets the step description
	 * 
	 * @parm i_Step
	 * @parm s_Desc
	 */

	public String getStepDescription() {
		return stepDescription;
	}

	public void setStepDescription(String desc) {
		stepDescription = desc;
	}

	public void setStepDescription(int step, String desc) {
		stepDescription = "(" + Integer.toString(step) + ") " + desc;
	}

	/**
	 * Sets variables to the telemetry headers
	 */

	public void addTelemetryHeaders(LCTelemetry telem) {
		telem.addColumn("AD Program Desc");
		telem.addColumn("AD Step Desc");
	}

	/**
	 * Writes the variables to the telemetry.
	 */

	public void writeTelemetryValues(LCTelemetry telem) {
		telem.saveString("AD Program Desc", this.programDescription);
		telem.saveString("AD Step Desc", this.stepDescription);
	}

	/**
	 * Displays the variables to the Smart Dashboard.
	 */

	public void outputToDashboard(boolean minDisplay) {

		if (minDisplay == false) {
			SmartDashboard.putString("AD Program Desc", this.programDescription);
			SmartDashboard.putString("AD Step Desc", this.stepDescription);
		}
	}
}
