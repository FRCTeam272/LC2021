package frc.robot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
	private boolean isDone = false; // flag to indicate our auton program is done
	private boolean moveIsDone = false;
	private boolean stepIsSetup = false;
	private String stepDescription;
	private autonSteps autonStep;

	private int curr_step = 0;
	private AutonSteps[] steps;

	//Auton Moves
	private HashMap<String, Move> listMoves = new HashMap<String, Move>();
	private HashMap<String, String> attackCodeMatrix;
	private int totalMoves = 0;
	private int moveStep = 0;
	private int idx = 0;
	private String moveCode = "";
	private String currMove = "";
	private Double currMoveValue1 = 0.0;
	private Double currMoveValue2 = 0.0;
	private String currMoveStringValue1 = "";
	private String currMoveStringValue2 = "";
	private BufferedReader in = null;
	private FileReader inFr = null;
	private RobotMoves robotMoves;
	private ArrayList<Trajectory> trajectoryList;


	


	/**
	 * Constructor
	 */
	public Auton() {

		getSteps();

		autonTimer = new Timer(); // timer used to show how long we are in auton
		stepTimer = new Timer(); // timer used by the steps for various things

		loadAttackCodeMatrix();
		loadMoves();
		robotMoves = new RobotMoves();
		resetAuton();
	}

	private void getSteps() {
		// TODO update the Smart Dashboard
		// get the auton type
		boolean is_gsc = true; // replace with the proper call
		if(is_gsc){
			
			Alliance temp = Alliance.BLUE_A; // figure out how to pull from RADIO Buttons, smart dashboard
			steps = new Auton_GSC(temp).getSteps();
		}

	}

	public void resetAuton() {

		this.started = false;
		this.autonStep = autonSteps.TARGET;
		this.stepIsSetup = false;
		this.isDone = false;
		this.moveIsDone = false;
		this.stepDescription = "Reset";
		this.autonTimer.reset();
		this.stepTimer.reset();

	}

	
	public void dispatcher(ControlVars controlVars, Sensors sensors, GyroNavigate gyronav, Config config,String attackCode) {

		// Motion Magic
		//TODO: previous auton
		/*if (!this.started) {
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

		curr_step++; */
		
		//New Auton from file
		if (this.isDone )
			return;

		if (!this.started) {
			this.autonTimer.start();
			this.autonTimer.reset();
			this.moveStep = 1;
			this.moveIsDone = false;
			if (this.totalMoves == 0) {
	
				this.isDone = true;
				stepDescription = "TotalMoves==0, no moves, failed.";
				return;				
			}
			this.started = true;
		}

		// Combine these to get the move to look up in the list
		this.moveCode = String.format("%s%02d", attackCode, moveStep);
		getMoveValues(this.moveCode);
		if (this.currMove.equalsIgnoreCase("motionProfile")) {
			stepDescription = String.format("Calling: motionProfile mp-%d-%d.csv",  this.currMoveValue1.intValue(),  this.currMoveValue2.intValue());
		} else if (this.currMove.equalsIgnoreCase("recordedProfile")) {
			stepDescription = "Calling: recordedProfile " + this.currMoveStringValue1;
		} else
			stepDescription = "Calling: [" + this.currMove + "] (" + this.currMoveStringValue1 + ") (" + this.currMoveStringValue2 + ")";


		switch (this.currMove) {
			
			case "motionProfile":  //                  ignored  ignored  filename of recorded profile
				moveIsDone = motionProfile(controlVars, sensors, 0.0, 0.0, this.currMoveStringValue1);
				break;

			default:
				stepDescription = "Default: Unknown move [" + this.currMove + "]";
				this.isDone = true;
		}

		if (this.moveIsDone == true) {
			
			this.stepIsSetup = false;
			this.moveStep++;
		}

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

	private void loadMotionProfile(String fileName) {
    	
		try {
			
			String row;
			String[] fields;
			
				this.trajectoryList = new ArrayList<Trajectory>();
				this.inFr = new FileReader("/c/" + fileName );
			this.in = new BufferedReader(this.inFr);

			while ((row = in.readLine()) != null) {
				fields = row.split(",");
				if (fields.length == 3) {				
					try {
						Trajectory trajectory= new Trajectory();
						trajectory.setAngle(Double.parseDouble(fields[0].trim()));
						trajectory.setRotation(Double.parseDouble(fields[1].trim()));
						trajectory.setSpeed(Double.parseDouble(fields[2].trim()));
						trajectory.setIntakeIn(Boolean.parseBoolean(fields[3].trim()));
						trajectory.setIntakeUp(Boolean.parseBoolean(fields[4].trim()));
						trajectory.setIntakeDown(Boolean.parseBoolean(fields[5].trim()));
						this.trajectoryList.add(trajectory);
					} catch (NumberFormatException e) {
						System.out.println("Motion profile " + fileName + " Numberformat Exception !!");
					}
				}					
			}
		} catch (FileNotFoundException e) {
			System.out.println("Motion profile " + fileName + " not found.");
			
		} catch (IOException e) {
			System.out.println("Motion profile " + fileName + " I/O error.");
			
		} finally {
			try {
				if (this.in != null)
					this.in.close();
				if (this.inFr != null)
					this.inFr.close();
			} catch (IOException e) {
			}
		}
	
	}

	private boolean motionProfile(ControlVars controlVars, Sensors sensors, Double distance, Double angle, String rpFilename) {
		
		boolean replay;
		double  spd;
		double  ang;
		
		replay = false;
		if (this.stepIsSetup == false) {
			if (rpFilename != null && rpFilename.length() > 0) {
				System.out.println("loading Motion profile for replay");
				replay = true;
				this.loadMotionProfile(rpFilename);
			} 
			
			if(this.trajectoryList==null || this.trajectoryList.isEmpty()) {
				return true;
			}
			
			this.stepTimer.reset();
			this.stepTimer.start();
			this.stepIsSetup = true;
			this.idx = 0;
			
		}
		//this.idx = (int) Math.round(this.stepTimer.get() / 0.022);
		if (this.idx < this.trajectoryList.size()) {
			
			controlVars.setRobotAngle((double)this.trajectoryList.get(idx).getAngle());
			controlVars.setRobotSpeed((double)this.trajectoryList.get(idx).getSpeed());
			controlVars.setRobotRotation((double)this.trajectoryList.get(idx).getRotation());
			controlVars.setIntakeIn(this.trajectoryList.get(idx).isIntakeIn());
			controlVars.setIntakeUp(this.trajectoryList.get(idx).isIntakeUp());
			controlVars.setIntakeDown(this.trajectoryList.get(idx).isIntakeDown());
			controlVars.setGyroDrive(false);
				
			
			System.out.println("Motion Profile Distance : " + distance + " ,Angle : " + angle);
			System.out.println("Step: "+ idx + " ,Speed: " + controlVars.getRobotSpeed() + " ,Angle: " + controlVars.getRobotAngle() + " ,Rotation: " + controlVars.getRobotRotation());
			
			
			this.idx++;
			return false;
		} else {
			controlVars.setRobotSpeed(0.0); 
			controlVars.setRobotAngle(0.0);
			controlVars.setRobotRotation(0.0);	
			controlVars.setIntakeIn(false);
			controlVars.setIntakeUp(false);
			controlVars.setIntakeDown(false);	
			controlVars.setGyroDrive(false);
			return true;
		}			
	}

	public void loadMoves() {

		String fileName = "/c/AutonMoves.txt";

		try {
			totalMoves = 0;

			BufferedReader in = new BufferedReader(new FileReader(fileName));

			String line;
			while ((line = in.readLine()) != null) {

				line = line.trim();

				if (line.length() == 0 || // skip empty line
						line.charAt(0) == '#') // skip commented line
					continue;
				
				Move move = this.extractMoveDetails(line);
				listMoves.put(move.getCode(), move);
				totalMoves++;

			}

			in.close();

			System.out.println("Auton.loadMoves(): Completed with " + Integer.toString(totalMoves) + " loaded...");
			return;

		} catch (IOException e) {
			System.out.println("Auton.loadMoves(): ****ERROR: Failed to load the file " + fileName
					+ "   Exception:" + e + "  Reason:" + e.getMessage());
		}

		// if we get here then we indicate a failed load
		totalMoves = 0;

		return; // no moves
	}
	
	private Move extractMoveDetails(String line) {
		String[] parts = line.split("\\|");
		Move move = new Move();
		move.setCode(parts[0].toString().trim());
		move.setDescription(parts[1].toString().trim());

		if (parts.length > 2) {
			move.setValue1(parts[2].trim());
		}
		
		if (parts.length > 3) {
			move.setValue2(parts[3].trim());
		}
		return move;
			
	}
	
	private void getMoveValues(String moveCode) {

		this.currMove = "noop";
		this.currMoveValue1 = 0.0;
		this.currMoveValue2 = 0.0;
		this.currMoveStringValue1 = "";
		this.currMoveStringValue2 = "";
		
		if(listMoves.containsKey(moveCode)) {
			this.currMove = listMoves.get(moveCode).getDescription();
			this.currMoveStringValue1 = listMoves.get(moveCode).getStringValue1();
			this.currMoveValue1 = listMoves.get(moveCode).getValue1();
			this.currMoveStringValue2 = listMoves.get(moveCode).getStringValue2();
			this.currMoveValue2 = listMoves.get(moveCode).getValue2();
		}		
	}

	public String computeAttackCode (String attackCode, String fieldRandomization) {
		
		String key;
		String result;
		
		key = attackCode + fieldRandomization;
		result = attackCodeMatrix.get(key);
		if (result == null || result.length() == 0)
			return attackCode;
		else
			return result;
	}
	
	private void loadAttackCodeMatrix() {
	
		this.attackCodeMatrix = new HashMap<String, String>(40);
		this.attackCodeMatrix.put("11LLL", "11");
		this.attackCodeMatrix.put("11LRL", "11");
		this.attackCodeMatrix.put("11RLR", "11");
		this.attackCodeMatrix.put("11RRR", "11");
		this.attackCodeMatrix.put("12LLL", "15");
		this.attackCodeMatrix.put("12LRL", "15");
		this.attackCodeMatrix.put("12RLR", "11");
		this.attackCodeMatrix.put("12RRR", "11");
		this.attackCodeMatrix.put("13LLL", "17");
		this.attackCodeMatrix.put("13LRL", "15");
		this.attackCodeMatrix.put("13RLR", "17");
		this.attackCodeMatrix.put("13RRR", "11");
	
		this.attackCodeMatrix.put("21LLL", "22");
		this.attackCodeMatrix.put("21LRL", "22");
		this.attackCodeMatrix.put("21RLR", "21");
		this.attackCodeMatrix.put("21RRR", "21");
		this.attackCodeMatrix.put("22LLL", "23");
		this.attackCodeMatrix.put("22LRL", "23");
		this.attackCodeMatrix.put("22RLR", "24");
		this.attackCodeMatrix.put("22RRR", "24");
		this.attackCodeMatrix.put("23LLL", "23");
		this.attackCodeMatrix.put("23LRL", "23");
		this.attackCodeMatrix.put("23RLR", "24");
		this.attackCodeMatrix.put("23RRR", "24");

		this.attackCodeMatrix.put("31LLL", "32");
		this.attackCodeMatrix.put("31LRL", "32");
		this.attackCodeMatrix.put("31RLR", "32");
		this.attackCodeMatrix.put("31RRR", "32");
		this.attackCodeMatrix.put("32LLL", "32");
		this.attackCodeMatrix.put("32LRL", "32");
		this.attackCodeMatrix.put("32RLR", "36");
		this.attackCodeMatrix.put("32RRR", "36");
		this.attackCodeMatrix.put("33LLL", "32");
		this.attackCodeMatrix.put("33LRL", "38");
		this.attackCodeMatrix.put("33RLR", "36");
		this.attackCodeMatrix.put("33RRR", "38");
	
	}

	public HashMap<String, Move> getListMoves() {
		return listMoves;
	}

	

   

}
