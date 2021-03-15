package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/* Swerve Drive

B3 = Speed
B4 = Angle (Degrees)
B5 = Rotation (-1 to 1 radians / second)

Vtx=IF(B3<-1,1,IF(B3>1,1,B3))*COS(RADIANS(B4+90))
Vty==IF(B3<-1,1,IF(B3>1,1,B3))*SIN(RADIANS(B4+90))
W=IF(B5<-1,-1,IF(B5>1,1,B5))

LF Rx=-1 Ry=1  Vx=F$3-F$5*C11                           == Vtx - (W * Ry)
               Vy=F$4+F$5*B11                           == Vty + (W * Rx)
		    speed=SQRT(D11*D11+E11*E11)                 == SQRT (Vx*Vx+Vy*Vy)
            angle==DEGREES(IF(E11<>0,ATAN2(D11,E11),0)) == DEGREES(IF(Vy<>0,ATAN2(Vx,Vy),0))		   

*/

public class SwerveDrive {
	
	final int WS_ARRAY_SIZE = 15;
	private boolean driveMotorReverse;
	private double xLoc;
	private double yLoc;
	private String locString;
	private int driveMotorChannel;
	private int turnMotorChannel;
	private double turn180Range;
	private double turnForwardPosition;
	private double turnTalonPosition;
	private double averageWheelSpeed;	
	private double[] wheelSpeeds;
	private int wheelSpeedsIdx;
	private int i = 0;
	TalonFX driveMotorFX;
	TalonSRX turnMotor;

	
	// Computed values
	private double wheelSpeed;
	private double wheelAngle;
		
	public SwerveDrive(Config config, String loc) {
		
		double xTrack;
		double yTrack;
		
		this.locString = loc;
		xTrack = config.getDouble("xTrack", 15.0);
		yTrack = config.getDouble("yTrack", 20.0);
		this.xLoc = (loc.charAt(0) == 'l') ? xTrack / -2.0 : xTrack / 2.0;
		this.yLoc = (loc.charAt(1) == 'r') ? yTrack / -2.0 : yTrack / 2.0;
		loadConfig(config);
		this.turnTalonPosition = this.turnForwardPosition;
		this.averageWheelSpeed = 0.0;
		this.wheelSpeeds = new double[WS_ARRAY_SIZE];
		this.wheelSpeedsIdx = 0;
		for (int i = 0; i < WS_ARRAY_SIZE; i++)
			this.wheelSpeeds[i] = 0.0;
		this.driveMotorFX = new TalonFX(this.driveMotorChannel);
		LCTalonFX.configureTalonFX(this.driveMotorFX, config, ControlMode.PercentOutput, "drive");
		this.driveMotorFX.set(ControlMode.PercentOutput, 0.0);
		this.turnMotor = new TalonSRX(this.turnMotorChannel);
		LCTalonSRX.configureTalonSRX(this.turnMotor, config, ControlMode.Position, "turn");
		this.turnMotor.set(ControlMode.Position,this.turnForwardPosition);
	}

	/*
	 *  Use inverse kinematics calculations to calculate the speed and direction of each wheel
	 *  given offsets of wheels from center of robot, a forward/reverse velocity, a side to side 
	 *  velocity, and a rotational velocity.
	 *  robotSpeed = forward/reverse velocity (-1 = full reverse, 1 = full forward)
	 *  robotAngle = side to side velocity (-1 = full left, 1 = full right)
	 *  robotRotation = rotational velocity (-1 = spin left, 1 = spin right)
	 */
	public void computeWheelVector(double robotSpeed, double robotAngle, double robotRotation) {
		
		double ra;
		double rs;
		double vtx;
		double vty;
		double omega;
		double vx;
		double vy;

		rs = rangeCheck(robotSpeed);		
		ra = rangeCheck(robotAngle);
		omega = rangeCheck(robotRotation) * -1.0;
		omega /= 25.0;  // Scale down for driveability
		//System.out.printf("rs: %.4f ra: %.4f omega: %.4f\n", rs, ra, omega);
		
		vtx = ra;
		vty = rs;
        vx = vtx - (omega * this.yLoc);
        vy = vty + (omega * this.xLoc);
		this.wheelSpeed = Math.sqrt(vx * vx + vy * vy);
        this.wheelAngle = Math.atan2(vy, vx);
	}

	/*
	 * Update the motor controllers. 
	 * If angle > 180, then get mod 180 of angle and reverse the motor
	 */
	public void update () {
		
		if (this.wheelAngle < 0.0)
			this.wheelAngle = Math.PI * 2 + this.wheelAngle;
		setDriveWheelAngle();
		setDriveMotorSpeed();
	}
	
	
	private double rangeCheck(double in) {
		double result = 0.0;
		
		if (in < -1.0)
			result = -1.0;
		else if (in > 1.0)
			result = 1.0;
		else 
			result = in;
		return result;
	}
	
    private void setDriveWheelAngle() {
    	
    	double talonRadian;
    	
    	// Convert wheel angle (0 - Pi radians) to talon position
    	talonRadian = this.turn180Range / Math.PI;
    	this.turnTalonPosition = this.wheelAngle * talonRadian;
		this.turnTalonPosition -= (this.turn180Range / 2.0);
		this.turnTalonPosition += this.turnForwardPosition;
		if (this.turnTalonPosition > (this.turn180Range * 2.0))
			this.turnTalonPosition -= (this.turn180Range * 2.0);
		if (this.turnTalonPosition > this.turn180Range * 1.75) {
			this.turnTalonPosition -= this.turn180Range;
			this.wheelSpeed *= -1.0;
		} else if (this.turnTalonPosition < this.turn180Range * .25) {
			this.turnTalonPosition += this.turn180Range;
			this.wheelSpeed *= -1.0;
		}
    	if (Math.abs(this.wheelSpeed) > 0.05) {
    		this.turnMotor.set(ControlMode.Position,this.turnTalonPosition);
    	}
    }
    
    private void setDriveMotorSpeed() {
    	
    	this.averageWheelSpeed = computeAverageWheelSpeed();
    	double spd = (this.driveMotorReverse) ? -this.averageWheelSpeed : this.averageWheelSpeed;
		this.driveMotorFX.set(ControlMode.PercentOutput, spd);
		
		SmartDashboard.putNumber(this.locString + "IntegratedSensorAbPosition:", this.driveMotorFX.getSensorCollection().getIntegratedSensorAbsolutePosition());
		SmartDashboard.putNumber(this.locString + "IntegratedSensorPosition:", this.driveMotorFX.getSensorCollection().getIntegratedSensorPosition());
		
    }
    
    public void setVoltageCompensation(boolean enable, double voltage) {
    	
    	if (enable)
    		LCTalonFX.enableVoltageCompensation(this.driveMotorFX, voltage);
    	else
    		LCTalonFX.disableVoltageCompensation(this.driveMotorFX);
    }
       
    private double computeAverageWheelSpeed() {
    	
    	double average = 0.0;
    	
    	this.wheelSpeeds[this.wheelSpeedsIdx] = this.wheelSpeed;
    	this.wheelSpeedsIdx++;
    	if (this.wheelSpeedsIdx >= WS_ARRAY_SIZE)
    		this.wheelSpeedsIdx = 0;
    	for (int i = 0; i < WS_ARRAY_SIZE; i++)
    		average += this.wheelSpeeds[i];
    	return average / WS_ARRAY_SIZE;
    }
    
    public void calibrateDriveWheelAngle(int position) {

    	if (position < 0) 
    		position = 0;
    	else if (position > 1024)
    		position = 1024;
    	this.turnTalonPosition = position;
    	this.turnMotor.set(ControlMode.Position,this.turnTalonPosition);
    	outputToDashboard(false);
    }
    
    public void calibrateDriveMotorSpeed(double speed) {
    	
    	this.wheelSpeed = speed;
    	double spd = (this.driveMotorReverse) ? -this.wheelSpeed : this.wheelSpeed;
   		this.driveMotorFX.set(ControlMode.PercentOutput, spd);
    	outputToDashboard(false);
    }
    
    void loadConfig(Config config) {
    	
		this.driveMotorChannel = config.getInt(this.locString + "DriveMotorChannel", 0);
		this.turnMotorChannel  = config.getInt(this.locString + "TurnMotorChannel", 0);
		this.turn180Range = config.getDouble("turn180Range", 440);
		this.turnForwardPosition = config.getDouble(this.locString + "TurnForwardPosition", 440);
		this.driveMotorReverse = config.getBoolean(this.locString + "DriveMotorReverse", false);
    }
    
    public void outputToDashboard(boolean minDisplay) {
		
		if (!minDisplay) {
			if (this.i++ > 5) {
				SmartDashboard.putNumber(this.locString + "WheelSpeed", this.wheelSpeed);
				SmartDashboard.putNumber(this.locString + "WheelAngle", this.wheelAngle);
				SmartDashboard.putNumber(this.locString + "ReqPosition", this.turnTalonPosition);
				SmartDashboard.putNumber(this.locString + "AverageWheelSpeed", this.averageWheelSpeed);
//				SmartDashboard.putNumber(this.locString + "ActualPosition", this.turnMotor.getSelectedSensorPosition(0));
//				SmartDashboard.putNumber(this.locString + "ClosedLoopErr", this.turnMotor.getClosedLoopError(0));
				this.i = 0;
			}
		}
	}
	    
	public double getWheelSpeed() {
		return wheelSpeed;
	}

	// Allow swerve drive train class to normalize wheel speeds after calculating all 4 wheels
	public void setWheelSpeed(double wheelSpeed) {
		this.wheelSpeed = wheelSpeed;
	}

	public double getWheelAngle() {
		return wheelAngle;
	}

}
