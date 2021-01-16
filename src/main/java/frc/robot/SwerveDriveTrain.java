package frc.robot;

import java.util.HashMap;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/* 
 * This class holds 4 SwerveDrive objects which each correspond to one of the swerve drives on the robot
 * 
 */
public class SwerveDriveTrain extends DriveTrain {

    private boolean gyroDrive = false;

    private double driverAngleDivisor;
    private double driverSpeedDivisor;
    private double driverRotationDivisor;
    private int driverAngleExpo;
    private int driverSpeedExpo;
    private int driverRotationExpo;
    private String driverInputCurveString;
    private InputCurve driverInputCurve = null;

    private double operatorAngleDivisor;
    private double operatorSpeedDivisor;
    private double operatorRotationDivisor;
    private int operatorAngleExpo;
    private int operatorSpeedExpo;
    private int operatorRotationExpo;
    private String operatorInputCurveString;
    private InputCurve operatorInputCurve = null;

    private int lfTurnForwardPosition;
    private int lrTurnForwardPosition;
    private int rfTurnForwardPosition;
    private int rrTurnForwardPosition;

    private int calibrateWheelAngle0Button;
    private int calibrateWheelAngle90Button;
    private int calibrateWheelAngle180Button;

    private double robotSpeed;
    private double robotAngle;
    private double robotRotation;

    private int i = 0;
    private String leftRight = "l";
    private String frontRear = "f";

    private HashMap<String, SwerveDrive> swerveDrives;

    public SwerveDriveTrain(Config config) {

        SwerveDrive sd;

        swerveDrives = new HashMap<String, SwerveDrive>(4);
        sd = new SwerveDrive(config, "lf");
        swerveDrives.put("lf", sd);
        sd = new SwerveDrive(config, "lr");
        swerveDrives.put("lr", sd);
        sd = new SwerveDrive(config, "rf");
        swerveDrives.put("rf", sd);
        sd = new SwerveDrive(config, "rr");
        swerveDrives.put("rr", sd);

        robotSpeed = 0.0;
        robotAngle = 0.0;
        robotRotation = 0.0;

        loadConfig(config);
    }

    public void update(ControlVars controlVars, Sensors sensors, GyroNavigate gyroNavigate) {

        double max;
        double wslf, wslr, wsrf, wsrr;

        this.robotAngle = controlVars.getRobotAngle();
        this.robotSpeed = controlVars.getRobotSpeed();
        this.robotRotation = controlVars.getRobotRotation();

        if (controlVars.isFieldCentricDrive()) {
            double temp;
            double yawAngle;
            yawAngle = Math.toRadians(sensors.getNavxYaw());

            // temp = FWD·cos(q) + STR·sin(q);
            temp = this.robotSpeed * Math.cos(yawAngle) + this.robotAngle * Math.sin(yawAngle);
            // STR = -FWD·sin(q) + STR·cos(q);
            this.robotAngle = -this.robotSpeed * Math.sin(yawAngle) + this.robotAngle * Math.cos(yawAngle);
            // FWD = temp;
            this.robotSpeed = temp;
        }

        // Use Gyro to drive when going forward / backward or side to side (e.g.
        // robotSpeed <> 0 && robotRotation == 0)
        // Zero gyro whenever robotRotation <> 0
        /*
         * if (controlVars.isGyroDrive()) { if ((Math.abs(this.robotSpeed) > 0.02 ||
         * Math.abs(this.robotAngle) > 0.02) && this.robotRotation == 0.0) {
         * this.robotRotation = gyroNavigate.getStraightSteeringCorrection(sensors); }
         * else sensors.zeroGyroBearing(); }
         */

        // System.out.printf("RobotSpeed: %.4f RobotAngle: %.4f RobotRotation: %.4f\n",
        // this.robotSpeed, this.robotAngle, this.robotRotation);
        this.swerveDrives.get("lf").computeWheelVector(this.robotSpeed, this.robotAngle, this.robotRotation);
        this.swerveDrives.get("lr").computeWheelVector(this.robotSpeed, this.robotAngle, this.robotRotation);
        this.swerveDrives.get("rf").computeWheelVector(this.robotSpeed, this.robotAngle, this.robotRotation);
        this.swerveDrives.get("rr").computeWheelVector(this.robotSpeed, this.robotAngle, this.robotRotation);

        // Normalize speeds to make largest speed = 1 and others in proportion to it.
        // max=ws1; if(ws2>max)max=ws2; if(ws3>max)max=ws3; if(ws4>max)max=ws4;
        // if(max>1){ws1/=max; ws2/=max; ws3/=max; ws4/=max;}
        wslf = this.swerveDrives.get("lf").getWheelSpeed();
        wslr = this.swerveDrives.get("lr").getWheelSpeed();
        wsrf = this.swerveDrives.get("rf").getWheelSpeed();
        wsrr = this.swerveDrives.get("rr").getWheelSpeed();
        max = wslf;
        if (wslr > max)
            max = wslr;
        if (wsrf > max)
            max = wsrf;
        if (wsrr > max)
            max = wsrr;
        if (max > 1.0) {
            this.swerveDrives.get("lf").setWheelSpeed(wslf / max);
            this.swerveDrives.get("lr").setWheelSpeed(wslr / max);
            this.swerveDrives.get("rf").setWheelSpeed(wsrf / max);
            this.swerveDrives.get("rr").setWheelSpeed(wsrr / max);
        }
        this.swerveDrives.get("lf").update();
        this.swerveDrives.get("lr").update();
        this.swerveDrives.get("rf").update();
        this.swerveDrives.get("rr").update();
    }

    public void setVoltageCompensation(boolean enable) {
        this.swerveDrives.get("lf").setVoltageCompensation(enable, 11.0);
        this.swerveDrives.get("lr").setVoltageCompensation(enable, 11.0);
        this.swerveDrives.get("rf").setVoltageCompensation(enable, 11.0);
        this.swerveDrives.get("rr").setVoltageCompensation(enable, 11.0);
    }

    public void calibrateDriveWheelAngle(String loc, int position) {

        this.swerveDrives.get(loc).calibrateDriveWheelAngle(position);
        // SmartDashboard.putNumber(loc + "CalibrateWheelAngle", position);
    }

    public void calibrateDriveWheelSpeed(String loc, double speed) {

        this.swerveDrives.get(loc).calibrateDriveMotorSpeed(speed);
        // SmartDashboard.putNumber(loc + "CalibrateWheelSpeed", speed);
    }

    void loadConfig(Config config) {
        this.gyroDrive = config.getBoolean("gyroDrive", false);

        this.driverSpeedDivisor = config.getDouble("driverSpeedDivisor", 1.0);
        this.driverAngleDivisor = config.getDouble("driverAngleDivisor", 1.0);
        this.driverRotationDivisor = config.getDouble("driverRotationDivisor", 0.75);
        this.driverSpeedExpo = config.getInt("driverSpeedExpo", 1);
        this.driverAngleExpo = config.getInt("driverAngleExpo", 1);
        this.driverRotationExpo = config.getInt("driverRotationExpo", 1);
        this.driverInputCurveString = config.getString("driverInputCurve", "");
        if (this.driverInputCurveString.length() > 0)
            this.driverInputCurve = new InputCurve(this.driverInputCurveString);

        this.operatorSpeedDivisor = config.getDouble("operatorSpeedDivisor", 0.6);
        this.operatorAngleDivisor = config.getDouble("operatorAngleDivisor", 0.6);
        this.operatorRotationDivisor = config.getDouble("operatorRotationDivisor", 0.5);
        this.operatorSpeedExpo = config.getInt("operatorSpeedExpo", 1);
        this.operatorAngleExpo = config.getInt("operatorAngleExpo", 1);
        this.operatorRotationExpo = config.getInt("operatorRotationExpo", 1);
        this.operatorInputCurveString = config.getString("operatorInputCurve", "");
        if (this.operatorInputCurveString.length() > 0)
            this.operatorInputCurve = new InputCurve(this.operatorInputCurveString);

        this.lfTurnForwardPosition = config.getInt("lfTurnForwardPosition", 440);
        this.lrTurnForwardPosition = config.getInt("lrTurnForwardPosition", 440);
        this.rfTurnForwardPosition = config.getInt("rfTurnForwardPosition", 440);
        this.rrTurnForwardPosition = config.getInt("rrTurnForwardPosition", 440);

        this.calibrateWheelAngle0Button = config.getInt("calibrateWheelAngle0Button", 4);
        this.calibrateWheelAngle90Button = config.getInt("calibrateWheelAngle90Button", 6);
        this.calibrateWheelAngle180Button = config.getInt("calibrateWheelAngle180Button", 3);

        this.swerveDrives.get("lf").loadConfig(config);
        this.swerveDrives.get("lr").loadConfig(config);
        this.swerveDrives.get("rf").loadConfig(config);
        this.swerveDrives.get("rr").loadConfig(config);

    }

    @Override
    public void calibrate(Inputs inputs) {

        String loc;
        int pov;
        int turnForwardPosition;
        double speed;

        inputs.readValues();
        pov = inputs.getDriverPov();
        if (pov == 315 || pov == 0 || pov == 45)
            this.frontRear = "f";
        else if (pov == 135 || pov == 180 || pov == 225)
            this.frontRear = "r";
        else if (pov == 90)
            this.leftRight = "r";
        else if (pov == 270)
            this.leftRight = "l";

        loc = this.leftRight + this.frontRear;

        if (loc.equalsIgnoreCase("lf"))
            turnForwardPosition = this.lfTurnForwardPosition;
        else if (loc.equalsIgnoreCase("lr"))
            turnForwardPosition = this.lrTurnForwardPosition;
        else if (loc.equalsIgnoreCase("rf"))
            turnForwardPosition = this.rfTurnForwardPosition;
        else if (loc.equalsIgnoreCase("rr"))
            turnForwardPosition = this.rrTurnForwardPosition;
        else
            turnForwardPosition = 440;

        if (inputs.getButton(this.calibrateWheelAngle0Button)) {
            turnForwardPosition += 1;
        } else if (inputs.getButton(this.calibrateWheelAngle180Button)) {
            turnForwardPosition -= 1;
        }

        calibrateDriveWheelAngle(loc, turnForwardPosition);

        if (loc.equalsIgnoreCase("lf"))
            this.lfTurnForwardPosition = turnForwardPosition;
        else if (loc.equalsIgnoreCase("lr"))
            this.lrTurnForwardPosition = turnForwardPosition;
        else if (loc.equalsIgnoreCase("rf"))
            this.rfTurnForwardPosition = turnForwardPosition;
        else if (loc.equalsIgnoreCase("rr"))
            this.rrTurnForwardPosition = turnForwardPosition;

        speed = inputs.getYAxisValue();
        if (speed > 0.1 || speed < -0.1)
            calibrateDriveWheelSpeed(loc, speed);
        else
            calibrateDriveWheelSpeed(loc, 0.0);

        SmartDashboard.putNumber("pov", pov);
        SmartDashboard.putString("loc", loc);
        SmartDashboard.putNumber("lfTurnForwardPos", this.lfTurnForwardPosition);
        SmartDashboard.putNumber("lrTurnForwardPos", this.lrTurnForwardPosition);
        SmartDashboard.putNumber("rfTurnForwardPos", this.rfTurnForwardPosition);
        SmartDashboard.putNumber("rrTurnForwardPos", this.rrTurnForwardPosition);
    }

    @Override
    public void mapInputsToControlVars(Inputs inputs, ControlVars controlVars) {
        if (inputs.getXAxisValue() == 0.0 && inputs.getYAxisValue() == 0.0 && inputs.getZAxisValue() == 0.0
                && controlVars.isOperatorDrive()) {
            mapOperatorInputsToControlVars(inputs, controlVars);
        } else {
            mapDriverInputsToControlVars(inputs, controlVars);
        }
    }

    public void mapDriverInputsToControlVars(Inputs inputs, ControlVars controlVars) {

        // Rotation on right joystick, Swerve and throttle on left joystick, throttle on
        // triggers

        double robotAngle = 0.0;
        double robotSpeed = 0.0;
        double robotRotation = 0.0;
        double yAxisValue;

        // Driver Joystick
        robotAngle = inputs.getXAxisValue();
        yAxisValue = inputs.getYAxisValue();
        robotRotation = inputs.getZAxisValue();

        robotSpeed = yAxisValue;
        if (robotSpeed == 0.0)
            robotSpeed = inputs.getThrottleValue();
        // robotSpeed *= this.robotSpeedDivisor;
        // robotSpeed = expo(robotSpeed, this.robotSpeedExpo);
        if (this.driverInputCurve != null && this.driverInputCurve.getPoints() > 0) {
            if (robotSpeed >= 0.0) {
                robotSpeed = this.driverInputCurve.getCurveValue(robotSpeed);
            } else {
                robotSpeed = -this.driverInputCurve.getCurveValue(Math.abs(robotSpeed));
            }
        } else {
            robotSpeed *= this.driverSpeedDivisor;
            robotSpeed = expo(robotSpeed, this.driverSpeedExpo);
        }

        // robotAngle *= this.robotAngleDivisor;
        // robotAngle = expo(robotAngle, this.robotAngleExpo);
        if (this.driverInputCurve != null && this.driverInputCurve.getPoints() > 0) {
            if (robotAngle >= 0.0) {
                robotAngle = this.driverInputCurve.getCurveValue(robotAngle);
            } else {
                robotAngle = -this.driverInputCurve.getCurveValue(Math.abs(robotAngle));
            }
        } else {
            robotAngle *= this.driverAngleDivisor;
            robotAngle = expo(robotAngle, this.driverAngleExpo);
        }

        robotRotation *= this.driverRotationDivisor;
        robotRotation = expo(robotRotation, this.driverRotationExpo);
        controlVars.setRobotAngle(robotAngle);
        controlVars.setRobotSpeed(robotSpeed);
        controlVars.setRobotRotation(robotRotation);
        controlVars.setGyroDrive(this.gyroDrive);
    }

    public void mapOperatorInputsToControlVars(Inputs inputs, ControlVars controlVars) {

        // Rotation on right joystick, Swerve and throttle on left joystick, throttle on
        // triggers

        double robotAngle = 0.0;
        double robotSpeed = 0.0;
        double robotRotation = 0.0;
        double yAxisValue;

        // Operator Joystick
        robotAngle = inputs.getOpXAxisValue();
        yAxisValue = inputs.getOpYAxisValue();
        robotRotation = inputs.getOpZAxisValue();

        robotSpeed = yAxisValue;

        // robotSpeed *= this.operatorSpeedDivisor;
        // robotSpeed = expo(robotSpeed, this.operatorSpeedExpo);
        if (this.operatorInputCurve != null && this.operatorInputCurve.getPoints() > 0) {
            if (robotSpeed >= 0.0) {
                robotSpeed = this.operatorInputCurve.getCurveValue(robotSpeed);
            } else {
                robotSpeed = -this.operatorInputCurve.getCurveValue(Math.abs(robotSpeed));
            }
        } else {
            robotSpeed *= this.operatorSpeedDivisor;
            robotSpeed = expo(robotSpeed, this.operatorSpeedExpo);
        }

        // robotAngle *= this.operatorAngleDivisor;
        // robotAngle = expo(robotAngle, this.operatorAngleExpo);
        if (this.operatorInputCurve != null && this.operatorInputCurve.getPoints() > 0) {
            if (robotAngle >= 0.0) {
                robotAngle = this.operatorInputCurve.getCurveValue(robotAngle);
            } else {
                robotAngle = -this.operatorInputCurve.getCurveValue(Math.abs(robotAngle));
            }
        } else {
            robotAngle *= this.operatorAngleDivisor;
            robotAngle = expo(robotAngle, this.operatorAngleExpo);
        }

        robotRotation *= this.operatorRotationDivisor;
        robotRotation = expo(robotRotation, this.operatorRotationExpo);
        controlVars.setRobotAngle(robotAngle);
        controlVars.setRobotSpeed(robotSpeed);
        controlVars.setRobotRotation(robotRotation);
        controlVars.setGyroDrive(this.gyroDrive);
    }

    @Override
    public void outputToDashboard(Boolean minDisplay) {

        if (!minDisplay) {
            if (this.i++ > 25) {
                SmartDashboard.putNumber("RobotSpeed", this.robotSpeed);
                SmartDashboard.putNumber("RobotAngle", this.robotAngle);
                SmartDashboard.putNumber("RobotRotation", this.robotRotation);
                this.i = 0;
            }
        }
        this.swerveDrives.get("lf").outputToDashboard(minDisplay);
        this.swerveDrives.get("lr").outputToDashboard(minDisplay);
        this.swerveDrives.get("rf").outputToDashboard(minDisplay);
        this.swerveDrives.get("rr").outputToDashboard(minDisplay);
    }

    public void addTelemetryHeaders(LCTelemetry telem) {

        telem.addColumn("RobotSpeed");
        telem.addColumn("RobotAngle");
        telem.addColumn("RobotRotation");
    }

    public void writeTelemetryValues(LCTelemetry telem) {
        telem.saveDouble("RobotSpeed", this.robotSpeed);
        telem.saveDouble("RobotAngle", this.robotAngle);
        telem.saveDouble("RobotRotation", this.robotRotation);
    }

    public double getRobotSpeed() {
        return robotSpeed;
    }

    public double getRobotAngle() {
        return robotAngle;
    }

    public double getRobotRotation() {
        return robotRotation;
    }

}
