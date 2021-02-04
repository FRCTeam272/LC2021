package frc.robot;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Sensors {

	private AnalogInput	leftRocketIR;
	private AnalogInput	rightRocketIR;

	private DigitalInput frontIR;
	private DigitalInput rearIR;

	private DigitalInput intakePhotoEye;

	private DigitalInput groundIR0;
	private DigitalInput groundIR1;
	private DigitalInput groundIR2;
	private DigitalInput groundIR3;
	private DigitalInput groundIR4;
	private DigitalInput groundIR5;
	private DigitalInput groundIR6;

    private double leftRocketDist;
    private double leftRocketGuidanceValue;
    private	double rightRocketDist;
    private double rightRocketGuidanceValue;
	private boolean frontIRValue = false;
	private boolean rearIRValue = false;	
	private static double COLLISION_THRESHOLD = 0.5;
	private final I2C.Port i2cPort;
	// private final ColorSensorV3 m_colorSensor;
	private boolean intakePhotoEyeValue = false;

	private boolean groundIR0Value = false;
	private boolean groundIR1Value = false;
	private boolean groundIR2Value = false;
	private boolean groundIR3Value = false;
	private boolean groundIR4Value = false;
	private boolean groundIR5Value = false;
	private boolean groundIR6Value = false;

	private AHRS ahrs;
	private boolean isMoving;
	private boolean isRotating;
	private boolean collisionDetected;
	
	private double navxAngle;
	private double navxYaw;
	private double navxPitch;
	private double navxRoll;
	private double navxAccelX;
	private double navxAccelY;
	private double currentJerkX;
	private double currentJerkY;
	private double lastNavxAccelX;
	private double lastNavxAccelY;
	private boolean rightSideGood = false;
	private boolean leftSideGood = false;

	private Color detectedColor;

    private Target target;

	public Sensors(Config config) {

		loadConfig(config);
		this.groundIR0 = new DigitalInput(config.getInt("groundIR0", 0));
		this.groundIR1 = new DigitalInput(config.getInt("groundIR1", 1));
		this.groundIR2 = new DigitalInput(config.getInt("groundIR2", 2));
		this.groundIR3 = new DigitalInput(config.getInt("groundIR3", 3));
		this.groundIR4 = new DigitalInput(config.getInt("groundIR4", 4));
		this.groundIR5 = new DigitalInput(config.getInt("groundIR5", 5));
		this.groundIR6 = new DigitalInput(config.getInt("groundIR6", 6));
		this.frontIR   = new DigitalInput(config.getInt("frontIR",   8));
		this.rearIR    = new DigitalInput(config.getInt("rearIR",    9));

		this.intakePhotoEye    = new DigitalInput(config.getInt("intakePhotoEye",    10));

		this.leftRocketIR  = new AnalogInput(config.getInt("leftRocketIR",  2));
		this.rightRocketIR = new AnalogInput(config.getInt("rightRocketIR", 3));
		
		this.i2cPort = I2C.Port.kOnboard;
		// this.m_colorSensor = new ColorSensorV3(i2cPort);
		
		this.ahrs = null;
		this.isMoving = false;
		this.isRotating = false;
		this.navxAngle = 0.0;
		this.navxYaw = 0.0;
		this.navxPitch = 0.0;
		this.navxRoll = 0.0;
		this.navxAccelX = 0.0;
		this.navxAccelY = 0.0;
		this.collisionDetected = false;
		this.lastNavxAccelX = 0.0;
        this.lastNavxAccelY = 0.0;
        this.target = new Target();
		try {
			/* Communicate w/navX MXP via the MXP SPI Bus. */
			/* Alternatively: I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB */
			/*
			 * See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/ for
			 * details.
			 */
			this.ahrs = new AHRS(SPI.Port.kMXP);
		} catch (RuntimeException ex) {
			DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
		}

	}

	public void readValues() {

		// detectedColor = m_colorSensor.getColor();
		
		this.frontIRValue	 = !this.frontIR.get();
		this.rearIRValue	 = !this.rearIR.get();
		this.groundIR0Value	 = !this.groundIR0.get();
		this.groundIR1Value  = !this.groundIR1.get();
		this.groundIR2Value	 = !this.groundIR2.get();
		this.groundIR3Value  = !this.groundIR3.get();
		this.groundIR4Value  = !this.groundIR4.get();
		this.groundIR5Value  = !this.groundIR5.get();
		this.groundIR6Value	 = !this.groundIR6.get();

		this.intakePhotoEyeValue	 = !this.intakePhotoEye.get();

		this.leftRocketDist  = this.leftRocketIR.getAverageValue();
		this.rightRocketDist = this.rightRocketIR.getAverageValue();
		this.rightSideGood   = (getRightRocketDist() > this.rightRocketGuidanceValue);
		this.leftSideGood    = (getLeftRocketDist() > this.leftRocketGuidanceValue);

		if(this.rightSideGood && this.leftSideGood){
			if(getRightRocketDist() - getLeftRocketDist() > 100 ){
				this.leftSideGood = false;
			} else if (getLeftRocketDist() - getRightRocketDist() > 100) {
				this.rightSideGood = false;
			}
		}

		// navxMXP gyro, accelerometer, compass
    	if (this.ahrs != null && this.ahrs.isConnected() && !this.ahrs.isCalibrating()) {
    		this.isMoving   = this.ahrs.isMoving();
    		this.isRotating = this.ahrs.isRotating();
    		this.navxAngle  = this.ahrs.getAngle();
    		this.navxYaw    = this.ahrs.getYaw();
    		this.navxPitch  = this.ahrs.getPitch();
    		this.navxRoll   = this.ahrs.getRoll();
    		this.navxAccelX = this.ahrs.getWorldLinearAccelX();
    		this.navxAccelY = this.ahrs.getWorldLinearAccelY();
    		
    		this.currentJerkX   = this.navxAccelX - this.lastNavxAccelX;
    		this.lastNavxAccelX = this.navxAccelX;
    		this.currentJerkY   = this.navxAccelY - this.lastNavxAccelY;
    		this.lastNavxAccelY = this.navxAccelY;
            
    		this.collisionDetected = ( ( Math.abs(this.currentJerkX) > COLLISION_THRESHOLD) ||
                                       ( Math.abs(this.currentJerkY) > COLLISION_THRESHOLD) );
        }
//        setTarget(TargetDataProcessor.getInstance().getTargetData());
	
    }

    /**
     * GetGyroRelativeBearing
     * Here we will cook the value of the gyro. If we are turning right we go positive.<br>
     * Turn left and we go negative. Gyro returns 360 on down the more you turn left. We cannot use that as it is.  
     * <p>
     * Here we convert to relative angle. Positive we are going right. Negative left.
     * 
     * <pre>
     * Angle Value     |   Compass Value  | Relative Value	
     * No Turn 0 deg   |          0       |       0
     * Right 5 degrees |        5.0       |     5.0
     * Left  5 degrees |      355.0       |    -5.0 (355-360)
     *   
     * @return
     */
    
    public double getGyroRelativeBearing(){
    	
    	int currYaw = 0;
    	
    	if (this.ahrs != null && this.ahrs.isConnected() && !this.ahrs.isCalibrating())
    		currYaw = (int) this.ahrs.getAngle();
    	currYaw = currYaw % 360;
    	
    	if(currYaw <= 180)							// return positive if < 180
    		return currYaw;
    	else
    		return (currYaw - 360);					// return value offset from 360 if > 180	
    }
    
    /**
     * ZeroGyroBearing()
     * Call this to reset the gyro to 0 degrees. You may need to experiment to get the value correct. 
     *   
     * @return
     */

    public void zeroGyroBearing(){
    	
    	if (this.ahrs != null && this.ahrs.isConnected() && !this.ahrs.isCalibrating())
    		this.ahrs.zeroYaw(); 	
    }

    void loadConfig (Config config) {
			
		this.leftRocketGuidanceValue  = config.getDouble("leftRocketGuidanceValue", 600.0);
		this.rightRocketGuidanceValue = config.getDouble("rightRocketGuidanceValue", 600.0);
    }
    
    void outputToDashboard(Boolean minDisplay) {
		
		SmartDashboard.putBoolean("rightSideGood", this.rightSideGood);
		SmartDashboard.putBoolean("leftSideGood",  this.leftSideGood);
    
		SmartDashboard.putBoolean("gIR0", this.groundIR0Value);
		SmartDashboard.putBoolean("gIR1", this.groundIR1Value);
		SmartDashboard.putBoolean("gIR2", this.groundIR2Value);
		SmartDashboard.putBoolean("gIR3", this.groundIR3Value);
		SmartDashboard.putBoolean("gIR4", this.groundIR4Value);
		SmartDashboard.putBoolean("gIR5", this.groundIR5Value);
		SmartDashboard.putBoolean("gIR6", this.groundIR6Value);
		
        if (!minDisplay) {
			SmartDashboard.putBoolean("frontIRValue",       this.frontIRValue);
            SmartDashboard.putBoolean("rearIRValue",        this.rearIRValue);
            SmartDashboard.putNumber("rightSideDist",       getRightRocketDist());
            SmartDashboard.putNumber("leftSideDist",        getLeftRocketDist());
            SmartDashboard.putNumber("navxAngle",           this.navxAngle);
            SmartDashboard.putNumber("navxPitch",           this.navxPitch);
            SmartDashboard.putNumber("navxRoll",            this.navxRoll);
            SmartDashboard.putNumber("target X",            this.target.getxCoordinate());
            SmartDashboard.putNumber("target width",        this.target.getWidth());
            SmartDashboard.putNumber("attack angle",        this.target.getAttackAngle());
			SmartDashboard.putNumber("target distance",     this.target.getDistance());

			SmartDashboard.putBoolean("intakePhotoEyeValue",        this.intakePhotoEyeValue);
        }
	}

	 
	public boolean isMoving() {
		return this.isMoving;
	}

	public boolean isRotating() {
		return this.isRotating;
	}

	public double getNavxAngle() {
		return this.navxAngle;
	}

	public double getNavxYaw() {
		return this.navxYaw;
	}

	public double getNavxPitch() {
		return this.navxPitch;
	}

	public double getNavxRoll() {
		return this.navxRoll;
	}

	public double getNavxAccelX() {
		return this.navxAccelX;
	}
    
	public double getNavxAccelY() {
		return this.navxAccelY;
	}
    
	public boolean isCollisionDetected() {
		return this.collisionDetected;
	}

	public Target getTarget() {
		return this.target;
    }
    
    public void setTarget (Target target) {
        this.target = target;
	}
	

	public boolean isIntakePhotoEye() {
		return intakePhotoEyeValue;

	}
	
//    public boolean isFrontTouchingFloor() {
//		return frontIRValue;

//    }

    public boolean isRearTouchingFloor() {
		return rearIRValue;
    }
    
	public boolean isGroundIR0() {
        return this.groundIR0Value;
    }
	
    public boolean isGroundIR1() {
        return this.groundIR1Value;
    }
	
    public boolean isGroundIR2() {
        return this.groundIR2Value;
    }
	
    public boolean isGroundIR3() {
        return this.groundIR3Value;
    }
	
    public boolean isGroundIR4() {
        return this.groundIR4Value;
    }
	
    public boolean isGroundIR5() {
        return this.groundIR5Value;
	}

	public boolean isGroundIR6() {
        return this.groundIR6Value;
	}
	
	public double getLeftRocketDist() {
        return this.leftRocketDist;
//		return getIRDistanceFromVoltage(leftRocketVoltage);
	}

	public double getRightRocketDist() {
        return this.rightRocketDist;
//		return getIRDistanceFromVoltage(rightRocketVoltage);
	}

	public double getIRDistanceFromVoltage(double voltage) {
        // double result = 135.094 - 183.7892 * voltage + 88.3524 * (voltage * voltage) 
        //               - 13.79351 * (voltage * voltage * voltage);
        double result = 62.83103 - 0.09359845 * voltage + 0.00003470101 * (voltage * voltage);
		return result;
	}
    public void addTelemetryHeaders(LCTelemetry telem) {
	
		telem.addColumn("LeftIRDist");
        telem.addColumn("RightIRDist");
        telem.addColumn("FrontIR");
        telem.addColumn("RearIR");
    }

    public void writeTelemetryValues(LCTelemetry telem) {
        telem.saveDouble("LeftIRDist", this.leftRocketDist);
        telem.saveDouble("RightIRDist", this.rightRocketDist);
        telem.saveBoolean("FrontIR", this.frontIRValue);
        telem.saveBoolean("RearIR", this.rearIRValue);
    }

	public Color getDetectedColor() {
		return detectedColor;
	}
}

