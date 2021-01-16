package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Inputs {

	// declare your variable up here
	private LCJoystick	joyDriverStick;
	private LCJoystick 	joyOperatorStick;
	
	private boolean[]	driverButtonState = new boolean[12];
	private boolean[]   driverButtonStateChanged = new boolean[12];
	private boolean[]	operatorButtonState = new boolean[12];
	private boolean[]   operatorButtonStateChanged = new boolean[12];

	private double		xAxisValue = 0.0;
	private double		rightXAxisValue = 0.0;
	private double		yAxisValue = 0.0;
	private double		rightYAxisValue = 0.0;
	private double		zAxisValue = 0.0;
	private double		throttleValue = 0.0;
	
	private double		opXAxisValue = 0.0;
	private double		opRightXAxisValue = 0.0;
	private double		opYAxisValue = 0.0;
	private double		opRightYAxisValue = 0.0;
	private double		opZAxisValue = 0.0;
	private double		opThrottleValue = 0.0;
	
	
	// class Constructor initialize your variables here
	public Inputs( Config config ) {

		joyDriverStick  = new LCJoystick(config, "driver");
		joyOperatorStick = new LCJoystick(config, "operator");

		zeroInputs();
		for (int i = 0; i < 12; i++) {
			driverButtonState[i] = false;
			driverButtonStateChanged[i] = false;
			operatorButtonState[i] = false;
			operatorButtonStateChanged[i] = false;
		}
	}

	public void readValues() {
		boolean buttonState;
		
		this.xAxisValue      = this.joyDriverStick.getX();
		this.rightXAxisValue = this.joyDriverStick.getRightX();
		this.yAxisValue      = -this.joyDriverStick.getY();     					
		this.rightYAxisValue = -this.joyDriverStick.getRightY();
		this.zAxisValue      = this.joyDriverStick.getZ();
		this.throttleValue   = this.joyDriverStick.getThrottle();
		
		this.opXAxisValue	    = this.joyOperatorStick.getX();
		this.opRightXAxisValue	= this.joyOperatorStick.getRightX();
		this.opYAxisValue		= -this.joyOperatorStick.getY();
		this.opRightYAxisValue	= -this.joyOperatorStick.getRightY();
		this.opZAxisValue		= this.joyOperatorStick.getZ();
		this.opThrottleValue    = this.joyOperatorStick.getThrottle();
		
		for (int i = 1; i <= this.joyDriverStick.getButtonCount(); i++) {
			buttonState = this.joyDriverStick.getRawButton(i);
			this.driverButtonStateChanged[i-1] = (buttonState ^ this.driverButtonState[i-1]);
			this.driverButtonState[i-1] = buttonState;			
		}

		for (int i = 1; i <= this.joyOperatorStick.getButtonCount(); i++) {
			buttonState = this.joyOperatorStick.getRawButton(i);
			this.operatorButtonStateChanged[i-1] = (buttonState ^ this.operatorButtonState[i-1]);
			this.operatorButtonState[i-1] = buttonState;			
		}
		
		if( Math.abs(this.xAxisValue) < .05)
			this.xAxisValue = 0.0;
		if( Math.abs(this.rightXAxisValue) < .05)
			this.rightXAxisValue = 0.0;
		if( Math.abs(this.yAxisValue) < .05)
			this.yAxisValue = 0.0;
		if( Math.abs(this.rightYAxisValue) < .05)
			this.rightYAxisValue = 0.0;
		if( Math.abs(this.zAxisValue) < .05)
			this.zAxisValue = 0.0;
		if( Math.abs(this.throttleValue) < .05)
			this.throttleValue = 0.0;
		
		if( Math.abs(this.opXAxisValue) < .05)
			this.opXAxisValue = 0.0;
		if( Math.abs(this.opRightXAxisValue) < .05)
			this.opRightXAxisValue = 0.0;
		if( Math.abs(this.opYAxisValue) < .05)
			this.opYAxisValue = 0.0;
		if( Math.abs(this.opRightYAxisValue) < .05)
			this.opRightYAxisValue = 0.0;
		if( Math.abs(this.opZAxisValue) < .05)
			this.opZAxisValue = 0.0;
		if( Math.abs(this.opThrottleValue) < .05)
			this.opThrottleValue = 0.0;
	}
	
	// Button > 100 indicates operator controller. Use mod 100 to get button number
	public boolean getButton(int button) {
		
		if (button >= 100) {
			return getOperatorButton(button % 100);
		} else {
			return getDriverButton(button);
		}
	}
	
	private boolean getDriverButton(int button) {
		
		if (button < 1 || button > joyDriverStick.getButtonCount())
			return false;
		else
			return this.driverButtonState[button-1];
	}
	
	private boolean getOperatorButton(int button) {
		
		if (button < 1 || button > joyOperatorStick.getButtonCount())
			return false;
		else
			return this.operatorButtonState[button-1];
	}
	
	// Button > 100 indicates operator controller. Use mod 100 to get button number
	public boolean getButtonStateChanged(int button) {
		
		if (button >= 100) {
			return getOperatorButtonStateChanged(button % 100);
		} else {
			return getDriverButtonStateChanged(button);
		}
	}
	
	private boolean getDriverButtonStateChanged(int button) {
		
		if (button < 1 || button > joyDriverStick.getButtonCount())
			return false;
		else
			return this.driverButtonStateChanged[button-1];
	}
	
	private boolean getOperatorButtonStateChanged(int button) {
		
		if (button < 1 || button > joyOperatorStick.getButtonCount())
			return false;
		else
			return this.operatorButtonStateChanged[button-1];
	}
	
	public boolean isOperatorSticksNonZero() {
		
		double opAddedStickValue = this.opYAxisValue + this.opRightYAxisValue + this.opXAxisValue + this.opRightXAxisValue;
		if (opAddedStickValue == 0.0) {
			return false;
		} else {
			return true;
		}	
	}
	
	public int getDriverPov() {
		return joyDriverStick.getPov();
	}
	
	public int getOperatorPov() {
		return joyOperatorStick.getPov();
	}
	
	public void outputToDashboard(boolean minDisplay){
		
		if (!minDisplay) {
			SmartDashboard.putNumber("Driver Y", this.yAxisValue);
			SmartDashboard.putNumber("Driver RightY", this.rightYAxisValue);
			SmartDashboard.putNumber("Driver X", this.xAxisValue);
			SmartDashboard.putNumber("Driver RightX", this.rightXAxisValue);
			SmartDashboard.putNumber("Driver Z", this.zAxisValue);
			SmartDashboard.putNumber("Driver Throttle", this.throttleValue);
			
			SmartDashboard.putNumber("Operator Y", this.opYAxisValue);
			SmartDashboard.putNumber("Operator RightY", this.opRightYAxisValue);
			SmartDashboard.putNumber("Operator X", this.opXAxisValue);
			SmartDashboard.putNumber("Operator RightX", this.opRightXAxisValue);
			SmartDashboard.putNumber("Operator Z", this.opZAxisValue);
			SmartDashboard.putNumber("Operator Throttle", this.opThrottleValue);
		}
	}


	public void zeroInputs(){

		this.xAxisValue = 0.0;
		this.rightXAxisValue = 0.0;
		this.yAxisValue = 0.0;
		this.rightYAxisValue = 0.0;
		this.zAxisValue = 0.0;
		this.throttleValue = 0.0;
		
		this.opXAxisValue = 0.0;
		this.opRightXAxisValue = 0.0;
		this.opYAxisValue = 0.0;
		this.opRightYAxisValue = 0.0;
		this.opZAxisValue = 0.0;
		this.opThrottleValue = 0.0;
	}

	public double getXAxisValue() {
		return this.xAxisValue;
	}

	public double getRightXAxisValue() {
		return this.rightXAxisValue;
	}

	public double getYAxisValue() {
		return this.yAxisValue;
	}

	public double getRightYAxisValue() {
		return this.rightYAxisValue;
	}

	public double getZAxisValue() {
		return this.zAxisValue;
	}
	
	public double getOpXAxisValue() {
		return opXAxisValue;
	}

	public double getOpRightXAxisValue() {
		return opRightXAxisValue;
	}

	public double getOpYAxisValue() {
		return opYAxisValue;
	}

	public double getOpRightYAxisValue() {
		return opRightYAxisValue;
	}

	public double getOpZAxisValue() {
		return opZAxisValue;
	}

	public double getThrottleValue() {
		return throttleValue;
	}

	public double getOpThrottleValue() {
		return opThrottleValue;
	}
	public void setRumble() {
		this.joyDriverStick.setRumble();
	}
	
}