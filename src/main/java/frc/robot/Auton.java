package frc.robot;

/*
GSC Steps
step 1 access smart dashboard
	what type of auto gsc or loop
		if gsc:
			A mode, Alliance (RedA, BlueA, RedB, BlueB)
			gsc_run method
		if loop:
			A layout
			loop_run method

	gsc:
		A sensor info, encoder
		motors
		method to set based on angle
			1, -1
			.5, -.5
		method to reset motors front facing
		
		
*/

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/** 
 */

public class Auton {

	private enum autonSteps {
		INTAKE_DOWN, TARGET, WAIT, SHOOT, DRIVE, END
	};

	private Timer autonTimer;
	private Timer stepTimer;
	private boolean started; // tells us that we have started auton
	private boolean stepIsSetup = false;
	private String stepDescription;
	private autonSteps autonStep;

	private int curr_step = 0;
	private AutonSteps[] steps;


	/**
	 * Constructor
	 */
	public Auton() {

		getSteps();

		autonTimer = new Timer(); // timer used to show how long we are in auton
		stepTimer = new Timer(); // timer used by the steps for various things
		resetAuton();
	}

	private void getSteps() {
		// get the auton type
		boolean is_gsc = true; // replace with the proper call
		if(is_gsc){
			Alliance temp = Alliance.BLUE_A; // figurre out how to pull from RADIO Buttons, smart dashboard
			steps = new Auton_GSC(temp).steps;
		}
	}

	public void resetAuton() {

		this.started = false;
		this.autonStep = autonSteps.TARGET;
		this.stepIsSetup = false;
		this.stepDescription = "Reset";
		this.autonTimer.reset();
		this.stepTimer.reset();

	}

	
	public void dispatcher(ControlVars controlVars, Sensors sensors, GyroNavigate gyronav, Config config) {

		if (!this.started) {
			this.autonTimer.reset();
			this.autonTimer.start();
			controlVars.setIntakeDown(true);
			this.stepIsSetup = false;
			this.started = true;
		}

		if(curr_step >= steps.length){
			return;
		}

		controlVars.moveForward(steps[curr_step].rotations);
		controlVars.setRobotAngle(steps[curr_step].adjust_angle);

		curr_step++;
		

		// switch (this.autonStep) {

		// case INTAKE_DOWN:

		// 	if (!this.stepIsSetup) {
		// 		this.stepTimer.reset();
		// 		this.stepTimer.start();
		// 		this.stepIsSetup = true;
		// 		this.stepDescription = "IntakeDown";

		// 	}

		// 	if (stepTimer.get() > 1) {
		// 		this.autonStep = autonSteps.TARGET;
		// 		this.stepIsSetup = false;
		// 		this.stepTimer.reset();
		// 	} else {
		// 		controlVars.setIntakeDown(true);
		// 	}

		// 	break;

		// case TARGET:
		// 	if (!this.stepIsSetup) {
		// 		this.stepTimer.reset();
		// 		this.stepTimer.start();
		// 		this.stepIsSetup = true;
		// 		this.stepDescription = "Target";
				
		// 	}

		// 	if (stepTimer.get() > 1) {
		// 		this.autonStep = autonSteps.WAIT;
		// 		this.stepIsSetup = false;
		// 		this.stepTimer.reset();
		// 	}else{
		// 		controlVars.setshooterTarget(true);
		// 	}

		// 	break;

		// case WAIT:
		// 	if (!this.stepIsSetup) {
		// 		this.stepIsSetup = true;
		// 		this.stepDescription = "Wait";
		// 		this.stepTimer.reset();
		// 		this.stepTimer.start();
		// 	}

		// 	if (this.stepTimer.get() > 5) {
		// 		this.autonStep = autonSteps.SHOOT;
		// 		this.stepIsSetup = false;
		// 		this.stepTimer.reset();
		// 	}

		// 	break;

		// case SHOOT:
		// 	if (!this.stepIsSetup) {
		// 		this.stepIsSetup = true;
		// 		this.stepDescription = "Shoot";
		// 		this.stepTimer.reset();
		// 		this.stepTimer.start();
		// 	}

		// 	if (this.stepTimer.get() > 4) {
		// 		this.autonStep = autonSteps.DRIVE;
		// 		this.stepIsSetup = false;
		// 		this.stepTimer.reset();
		// 	} else {
		// 		controlVars.setshooterShoot(true);
		// 	}

		// 	break;

		// case DRIVE:
		// 	if (!this.stepIsSetup) {
		// 		this.stepIsSetup = true;
		// 		this.stepDescription = "Drive";
		// 		this.stepTimer.reset();
		// 		this.stepTimer.start();
		// 	}

		// 	if (this.stepTimer.get() > .5) {
		// 		controlVars.setRobotSpeed(0);
		// 		this.autonStep = autonSteps.END;
		// 		this.stepIsSetup = false;
		// 		this.stepTimer.reset();
		// 	} else {
		// 		controlVars.setRobotSpeed(-0.25);
		// 	}

		// 	break;

		// case END:
		// 	this.stepDescription = "End";
		// 	break;
		// }

	}

	public void addTelemetryHeaders(LCTelemetry telem) {
		telem.addColumn("A Auton Timer");
		telem.addColumn("A Step Timer");
		telem.addColumn("A Step");

	}

	public void writeTelemetryValues(LCTelemetry telem) {
		telem.saveDouble("A Auton Timer", this.autonTimer.get());
		telem.saveDouble("A Step Timer", this.stepTimer.get());
		telem.saveString("A Step", this.stepDescription);

	}

	public void outputToDashboard(boolean b_MinDisplay) {
		SmartDashboard.putString("A_Step", this.stepDescription);

		if (b_MinDisplay == false) {
			SmartDashboard.putNumber("A_StepTime", this.stepTimer.get());
			SmartDashboard.putNumber("A_AutonTime", this.autonTimer.get());
		}
	}

}
