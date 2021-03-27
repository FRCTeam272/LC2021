/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.FileWriter;
import java.io.IOException;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.cscore.UsbCamera;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import java.util.ArrayList;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    /**
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */

    static TargetDataProcessor targetDataProcessor = TargetDataProcessor.getInstance();

    private boolean calibrateDrives = false;
    private boolean fieldCentricDrive = false;

    private boolean robotMinDisplay;
    private boolean sensorsMinDisplay;
    private boolean inputsMinDisplay;
    private boolean driveMinDisplay;

    private double cargoRotationDist;
    private double rocketRotationDist;
    private double sixInchShufflePower;

    private int fieldCentricDriveButton;
    private int grabBallButton;
    private int groundPickupButton;
    private int operatorDriveButton;
    private int releaseBallButton;
    private int shuffleLeftButton;
    private int shuffleRightButton;

    private String configFileName;

    private int camHorizRes;
    private int camVertRes;
    private int camFPS;
    private int camBrightness;
    private UsbCamera usbCamera0;
    private UsbCamera usbCamera1;

    private Config config;
    private ControlVars controlVars;
    private GyroNavigate gyroNavigate;
    private Inputs inputs;
    private LCTelemetry telem;
    private RobotMoves robotMoves;
    private Sensors sensors;
    private SwerveDriveTrain driveTrain;
    private LimeLight limelight;
    private Shooter shooter;
    private LCIntake intake;
    private Auton auton;
    NetworkTableEntry cameraSelection;
    private Climber climber;
    private String activeUSBCamera;

    private String attackCode = SmartDashboard.getString("Auto Selector", "11");
	private String computedAttackCode = "11";
    private String gameData;
    private ArrayList<Trajectory> trajectoryList;

    @Override
    public void robotInit() {

        boolean configFileLoaded = false;
        this.calibrateDrives = false;
        this.fieldCentricDrive = false;

        this.configFileName = "/c/FRC272Config.cfg";
        config = new Config();
        try {
            config.load(configFileName);
            configFileLoaded = true;
        } catch (IOException e) {
            System.out.println("Config.load(): ****ERROR: Failed to load the file " + this.configFileName
                    + "   Exception:" + e + "  Reason:" + e.getMessage());
        }

        if (!configFileLoaded) {
            this.configFileName = "/c/FRC272Config_Practice.cfg";
            System.out.println("Config file not found. Attempting to load: " + this.configFileName);
            try {
                config.load(configFileName);
                configFileLoaded = true;
            } catch (IOException e) {
                System.out.println("Config.load(): ****ERROR: Failed to load the file " + this.configFileName
                        + "   Exception:" + e + "  Reason:" + e.getMessage());
            }
        }

        loadConfig(config);
        

        telem = new LCTelemetry();
        telem.loadConfig(config);
        auton = new Auton();
        auton.loadMoves();


        // Set up camera server for driving camera
    	try {
            
            this.usbCamera0 = CameraServer.getInstance().startAutomaticCapture(0);
			this.usbCamera0.setResolution(this.camHorizRes, this.camVertRes);
            //this.usbCamera0.setFPS(this.camFPS);
            this.usbCamera0.setFPS(15);
            this.usbCamera0.setExposureAuto();
            activeUSBCamera=this.usbCamera0.getName();
            cameraSelection = NetworkTableInstance.getDefault().getTable("").getEntry("CameraSelection");
            cameraSelection.setString(activeUSBCamera);
            
            this.usbCamera1 = CameraServer.getInstance().startAutomaticCapture(1);
			this.usbCamera1.setResolution(this.camHorizRes, this.camVertRes);
            //this.usbCamera1.setFPS(this.camFPS);
            this.usbCamera1.setFPS(10);
            this.usbCamera1.setExposureAuto();

            //usbCamera0.setConnectionStrategy(ConnectionStrategy.kForceClose);
            //usbCamera1.setConnectionStrategy(ConnectionStrategy.kKeepOpen);

            
		} catch (Exception e) {
			System.out.println("Camera not connected!" + e);
        }  
        

        controlVars = new ControlVars();
        gyroNavigate = new GyroNavigate();
        inputs = new Inputs(config);
        robotMoves = new RobotMoves();
        sensors = new Sensors(config);
        driveTrain = new SwerveDriveTrain(config);
        limelight = new LimeLight();
        shooter = new Shooter(config);
        intake = new LCIntake(config);
        climber = new Climber(config);
        sensors.addTelemetryHeaders(telem);
        driveTrain.addTelemetryHeaders(telem);



    }

    private void loadConfig(Config config2) {

        this.calibrateDrives = config.getBoolean("calibrateDrives", false);
        this.robotMinDisplay = config.getBoolean("robotMinDisplay", true);
        this.sensorsMinDisplay = config.getBoolean("sensorsMinDisplay", true);
        this.driveMinDisplay = config.getBoolean("driveMinDisplay", true);

        this.fieldCentricDriveButton = config.getInt("fieldCentricDriveButton", 9);
        this.grabBallButton = config.getInt("grabBallButton", 102);
        this.groundPickupButton = config.getInt("groundPickupButton", 103);
        this.operatorDriveButton = config.getInt("operatorDriveButton", 112);
        this.releaseBallButton = config.getInt("releaseBallButton", 101);
        this.shuffleLeftButton = config.getInt("shuffleLeftButton", 3);
        this.shuffleRightButton = config.getInt("shuffleRightButton", 2);

        this.cargoRotationDist = config.getDouble("cargoRotationDist", 600.0);
        this.rocketRotationDist = config.getDouble("rocketRotationDist", 850.0);
        this.sixInchShufflePower = config.getDouble("sixInchShufflePower", 0.3);

        this.camHorizRes = config.getInt("camHorizRez", 320);
        this.camVertRes = config.getInt("camVertRez", 240);
        this.camFPS = config.getInt("camFPS", 25);
        this.camBrightness = config.getInt("camBrightness", 50);

    }

    @Override
    public void disabledInit() {
        telem.saveSpreadSheet();
        telem.restartTimer();
        this.writeRecordedTrajectory();
        auton.loadMoves();
        

    }

    @Override
    public void autonomousInit() {
        this.attackCode = SmartDashboard.getString("AttackCode", "11");
        sensors.zeroGyroBearing();
        auton.resetAuton();

        this.gameData = DriverStation.getInstance().getGameSpecificMessage();
		this.computedAttackCode = auton.computeAttackCode(this.attackCode, this.gameData);
    }

    @Override
    public void autonomousPeriodic() {
       myTeleopPeriodic(true);
    }

    @Override
    public void disabledPeriodic() {

        inputs.readValues();
        sensors.readValues();

        inputs.outputToDashboard(this.inputsMinDisplay);
        sensors.outputToDashboard(this.sensorsMinDisplay);
        driveTrain.outputToDashboard(this.driveMinDisplay);
        this.outputToDashboard(this.robotMinDisplay);
    }

    @Override
    public void teleopInit() {
        this.attackCode = SmartDashboard.getString("AttackCode", "11");
        cameraSelection.setString(this.usbCamera0.getName());
    }

    @Override
    public void teleopPeriodic() {
       
        myTeleopPeriodic(false);
    }

    public void myTeleopPeriodic(boolean isAuton) {

        if (this.calibrateDrives) {
            driveTrain.calibrate(this.inputs);
        } else {
            controlVars.zeroVars();
            inputs.readValues();
            limelight.getDistance(18.73, 28.0);
            limelight.getHorizontialOffset();
            limelight.getSkewRotation();
            sensors.readValues();


            if (inputs.getButton(this.operatorDriveButton)) {
                controlVars.setOperatorDrive(true);
            }
            if (inputs.getButton(101)) {
                controlVars.setshooterShoot(true);
                // System.out.println(controlVars.isshooterShoot());

            }
            if (inputs.getOpZAxisValue() > .5) {
                controlVars.setshooterRight(true);
            }
            if (inputs.getOpZAxisValue() < -.5){
                controlVars.setshooterLeft(true);
            }
            if (inputs.getButton(1)) {
                controlVars.setIntakeUp(true);
                
            }
            if (inputs.getButton(2)) {
                controlVars.setIntakeDown(true);
                
            }
            if (inputs.getButton(111)) {
                controlVars.setIntakeIn(true);
            }
            if (inputs.getButton(112)) {
                controlVars.setIntakeOut(true);
            }
            if (inputs.getButton(103)) {
                controlVars.setshooterTarget(true);
            }
            if (inputs.getOpYAxisValue() > .5) {
                //controlVars.setClimberUp(true);
                controlVars.setShooterHoodAngleUp(true);
            }
            if (inputs.getOpYAxisValue() < -.5) {
                //controlVars.setClimberDown(true);
                controlVars.setShooterHoodAngleDown(true);
            }
            
            if(inputs.getButton(108)){
                controlVars.setClimberDown(true);
            }

            if(inputs.getButton(107)){
                controlVars.setClimberUp(true);
            }

            
            if(inputs.getButton(106)){
                controlVars.setShooterHoodBottom(true);
            }
            if(inputs.getButton(104)){
                controlVars.setShooterHoodMiddle(true);
            }
            if(inputs.getButton(105)){
                controlVars.setShooterHoodTop(true);
            }

            

            if(inputs.getButtonStateChanged(102)){
                
                if(activeUSBCamera.equalsIgnoreCase(this.usbCamera0.getName())){
                    activeUSBCamera=this.usbCamera1.getName();
                    cameraSelection.setString(this.usbCamera1.getName());
                }else{
                    activeUSBCamera=this.usbCamera0.getName();
                    cameraSelection.setString(this.usbCamera0.getName());
                }
            }

            
            if (inputs.getButton(this.fieldCentricDriveButton) &&
                inputs.getButtonStateChanged(this.fieldCentricDriveButton)) {
                this.fieldCentricDrive = !this.fieldCentricDrive; // Skip for now - re-enable
            // later
            }

           
            // Only set field-centric here if we aren't executing any robot moves. None of
            // the
            // robot moves understand how to adjust robot position when field-centric is
            // true
            controlVars.setFieldCentricDrive(this.fieldCentricDrive);
            driveTrain.mapInputsToControlVars(inputs, controlVars);
            
            if(inputs.getButton(107) && inputs.getButtonStateChanged(107)){
                trajectoryList = new ArrayList<Trajectory>();
            
            }

            if(inputs.getButton(107)){
                Trajectory replay = new Trajectory();
                replay.setAngle(controlVars.getRobotAngle());
                replay.setRotation(controlVars.getRobotRotation());
                replay.setSpeed(controlVars.getRobotSpeed());
                trajectoryList.add(replay);
            }
            
            
            if(isAuton){
                auton.dispatcher(controlVars, sensors, gyroNavigate, config,this.computedAttackCode);
            }

            driveTrain.update(controlVars, sensors, gyroNavigate);
            shooter.update(config, sensors, controlVars, limelight, inputs);
            intake.Update(controlVars, sensors);
            climber.update(controlVars,config);

            inputs.outputToDashboard(this.inputsMinDisplay);
            sensors.outputToDashboard(this.sensorsMinDisplay);
            driveTrain.outputToDashboard(this.driveMinDisplay);
            this.outputToDashboard(this.robotMinDisplay);

            sensors.writeTelemetryValues(this.telem);
            driveTrain.writeTelemetryValues(this.telem);
            telem.writeRow();

        }
    }

    @Override
    public void testInit() {
    }

    @Override
    public void testPeriodic() {
    }

    public void updatePreferences()  {

		this.attackCode = Preferences.getInstance().getString("AttackCode", "22");
	}

    public void outputToDashboard(boolean minDisplay) {
        SmartDashboard.putBoolean("CalibrateDrives", this.calibrateDrives);
        SmartDashboard.putBoolean("fieldCentricDrive", this.fieldCentricDrive);

        SmartDashboard.putString("AttackCode", this.attackCode);
		SmartDashboard.putString("ComputedAttackCode", this.computedAttackCode);
    }

    public void writeRecordedTrajectory() {
        if(this.trajectoryList !=null && this.trajectoryList.size()>0) {
            try {
                FileWriter outputFile = new FileWriter("/c/AttachCode_" + this.attackCode + "_replay.csv");
                this.trajectoryList.forEach((replay)->{
                    try {
                        outputFile.write(String.format("%f, %f, %f\n", replay.getAngle(), replay.getRotation(), replay.getSpeed()));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                });
                
                outputFile.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }  
    }
      
}

}
