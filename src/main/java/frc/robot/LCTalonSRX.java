package frc.robot;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.*;

public class LCTalonSRX {


    public static void configureTalonSRX(TalonSRX talonSRX, Config config, ControlMode controlMode, String type) {

    	talonSRX.selectProfileSlot(0, 0);

		if(type.equalsIgnoreCase("ShooterHood") && ControlMode.Position == controlMode){
			//Timeout configured for 100ms to wait for configFeedbackSensor. if timeout error
	    	ErrorCode error = talonSRX.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 100);
	    	if(error.value!=0) {
	    		System.out.println("!!!!!!!! Error updating Config for Feedback Senor while configuring CAN Talon" + error.toString());
	    	}
	    		
	    	// Don't accumulate position information e.g. position is absolute
	    	talonSRX.configSetParameter(ParamEnum.eFeedbackNotContinuous, 1, 0x00, 0x00, 0x00);
	    	
			// Choose to ensure sensor is positive when output is positive
			talonSRX.setSensorPhase(false);
	
			

			// Choose based on what direction you want forward/positive to be.
			// This does not affect sensor phase. 
			talonSRX.setInverted(false);

			// Set the peak and nominal outputs
			talonSRX.configNominalOutputForward(0, 100);
			talonSRX.configNominalOutputReverse(0, 100);
			talonSRX.configPeakOutputForward(1, 100);
			talonSRX.configPeakOutputReverse(-1, 100);
			
			talonSRX.setNeutralMode(NeutralMode.Brake);

			talonSRX.configAllowableClosedloopError(0,config.getInt(type + "AllowableClosedLoopErr", 0), 100);

			// Set the PID loop values
			talonSRX.config_kP(0, config.getDouble(type + "_PID_P", 10), 100);
	    	talonSRX.config_kI(0, config.getDouble(type + "_PID_I", 0.0), 100);
	    	talonSRX.config_kD(0, config.getDouble(type + "_PID_D", 0.0), 100);
	    	talonSRX.config_kF(0, config.getDouble(type + "_PID_F", 0.0002), 100);
			talonSRX.set(ControlMode.Position, talonSRX.getSelectedSensorPosition());
			
		}else if (ControlMode.Position == controlMode) {
	    	//Timeout configured for 100ms to wait for configFeedbackSensor. if timeout error
	    	ErrorCode error = talonSRX.configSelectedFeedbackSensor(FeedbackDevice.Analog, 0, 100);
	    	if(error.value!=0) {
	    		System.out.println("!!!!!!!! Error updating Config for Feedback Senor while configuring CAN Talon" + error.toString());
	    	}
	    		
	    	// Don't accumulate position information e.g. position is absolute
	    	talonSRX.configSetParameter(ParamEnum.eFeedbackNotContinuous, 1, 0x00, 0x00, 0x00);
	    	
			// Choose to ensure sensor is positive when output is positive
			talonSRX.setSensorPhase(false);

			// Choose based on what direction you want forward/positive to be.
			// This does not affect sensor phase. 
			talonSRX.setInverted(false);

			// Set the peak and nominal outputs
			talonSRX.configNominalOutputForward(0, 100);
			talonSRX.configNominalOutputReverse(0, 100);
			talonSRX.configPeakOutputForward(1, 100);
			talonSRX.configPeakOutputReverse(-1, 100);
			
			talonSRX.setNeutralMode(NeutralMode.Brake);

			talonSRX.configAllowableClosedloopError(0,config.getInt(type + "AllowableClosedLoopErr", 0), 100);

			// Set the PID loop values
			talonSRX.config_kP(0, config.getDouble(type + "_PID_P", 10.0), 100);
	    	talonSRX.config_kI(0, config.getDouble(type + "_PID_I", 0.0), 100);
	    	talonSRX.config_kD(0, config.getDouble(type + "_PID_D", 100.0), 100);
	    	talonSRX.config_kF(0, config.getDouble(type + "_PID_F", 0.2), 100);

	    	talonSRX.set(ControlMode.Position, 0.0);
    	} else if (ControlMode.PercentOutput == controlMode) {
			// Set the peak and nominal outputs
			talonSRX.configNominalOutputForward(0, 100);
			talonSRX.configNominalOutputReverse(0, 100);
			talonSRX.configPeakOutputForward(1, 100);
			talonSRX.configPeakOutputReverse(-1, 100);
    		//talonSRX.configOpenloopRamp(0.3, 100);
			talonSRX.setNeutralMode(NeutralMode.Brake);

    		talonSRX.set(ControlMode.PercentOutput, 0.0);    		
    	}
 
//    	if (type.equalsIgnoreCase("drive")) {
//    		// Set max voltage out to drive motors to 10 V. to compensate for battery level
//    		talonSRX.configVoltageCompSaturation(10.0, 100);
//    		talonSRX.enableVoltageCompensation(true);
//    		talonSRX.configVoltageMeasurementFilter(32, 100);
//    	}

		if (type.equalsIgnoreCase("ShooterPosition")) {
			// Choose to ensure sensor is positive when output is positive
			

			// Choose based on what direction you want forward/positive to be.
			// This does not affect sensor phase. 
			//talonSRX.setInverted(true);
			
			// If elevator moves too fast, limit it here
			talonSRX.configPeakOutputForward(config.getDouble("elevatorMotorUpPower", 0.5), 100);
			talonSRX.configPeakOutputReverse(config.getDouble("elevatorMotorDownPower", -0.5), 100);
		//            talonSRX.configPeakOutputReverse(getReversePower(config, "elevatorMotorDownPower", -0.5), 100);
			
			// PID tunings for elevator
			talonSRX.config_kP(0, config.getDouble(type + "_PID_P", 10.0), 100);
	    	talonSRX.config_kI(0, config.getDouble(type + "_PID_I", 0.0), 100);
	    	talonSRX.config_kD(0, config.getDouble(type + "_PID_D", 100.0), 100);
	    	talonSRX.config_kF(0, config.getDouble(type + "_PID_F", 0.2), 100);
			
		
		}
    	
    	if (type.equalsIgnoreCase("elevator")) {
			// Choose to ensure sensor is positive when output is positive
			talonSRX.setSensorPhase(true);

			// Choose based on what direction you want forward/positive to be.
			// This does not affect sensor phase. 
            talonSRX.setInverted(true);
            
    		talonSRX.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, 
    				LimitSwitchNormal.NormallyOpen, 0);
    		talonSRX.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, 
    				LimitSwitchNormal.NormallyOpen, 0);
    		// If elevator moves too fast, limit it here
			talonSRX.configPeakOutputForward(config.getDouble("elevatorMotorUpPower", 0.5), 100);
            talonSRX.configPeakOutputReverse(config.getDouble("elevatorMotorDownPower", -0.5), 100);
//            talonSRX.configPeakOutputReverse(getReversePower(config, "elevatorMotorDownPower", -0.5), 100);
            
            // PID tunings for elevator
			talonSRX.config_kP(0, config.getDouble(type + "_PID_P", 25.0), 100);
	    	talonSRX.config_kI(0, config.getDouble(type + "_PID_I", 0.001), 100);
	    	talonSRX.config_kD(0, config.getDouble(type + "_PID_D", 30.0), 100);
            talonSRX.config_kF(0, config.getDouble(type + "_PID_F", 0.0), 100);
            
            // Not sure what values to use here
//    		talonSRX.configPeakCurrentLimit(35, 100); /* 35 A */
//    		talonSRX.configPeakCurrentDuration(200, 100); /* 200ms */
//    		talonSRX.configContinuousCurrentLimit(30, 100); /* 30A */
//    		talonSRX.enableCurrentLimit(true); /* turn it on */    	
    	}
    	
    	if (type.equalsIgnoreCase("frontWinch")) {
			// Choose to ensure sensor is positive when output is positive
			talonSRX.setSensorPhase(config.getBoolean("frontWinchSensorPhase", true));

			// Choose based on what direction you want forward/positive to be.
			// This does not affect sensor phase. 
            talonSRX.setInverted(config.getBoolean("frontWinchInverted", false));

    		talonSRX.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, 
    				LimitSwitchNormal.NormallyOpen, 0);
    		talonSRX.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, 
    				LimitSwitchNormal.NormallyOpen, 0);
    		// If front winch moves too fast, limit it here
			talonSRX.configPeakOutputForward(config.getDouble("frontWinchUpPower", 0.5), 100);
            talonSRX.configPeakOutputReverse(config.getDouble("frontWinchDownPower", -0.5), 100);
//            talonSRX.configPeakOutputReverse(getReversePower(config, "frontWinchDownPower", -0.5), 100);
            
            // PID Tunings for climbing winch
			talonSRX.config_kP(0, config.getDouble(type + "_PID_P", 25.0), 100);
	    	talonSRX.config_kI(0, config.getDouble(type + "_PID_I", 0.0), 100);
	    	talonSRX.config_kD(0, config.getDouble(type + "_PID_D", 30.0), 100);
            talonSRX.config_kF(0, config.getDouble(type + "_PID_F", 0.0), 100);

    		// Not sure what values to use here
//    		talonSRX.configPeakCurrentLimit(35, 100); /* 35 A */
//    		talonSRX.configPeakCurrentDuration(200, 100); /* 200ms */
//    		talonSRX.configContinuousCurrentLimit(30, 100); /* 30A */
//    		talonSRX.enableCurrentLimit(true); /* turn it on */    	
     	}
    	
         if (type.equalsIgnoreCase("rearWinch")) {
			// Choose to ensure sensor is positive when output is positive
			talonSRX.setSensorPhase(config.getBoolean("rearWinchSensorPhase", true));

			// Choose based on what direction you want forward/positive to be.
			// This does not affect sensor phase. 
            talonSRX.setInverted(config.getBoolean("rearWinchInverted", false));
            
    		talonSRX.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, 
    				LimitSwitchNormal.NormallyOpen, 0);
    		talonSRX.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, 
    				LimitSwitchNormal.NormallyOpen, 0);
    		// If rear winch moves too fast, limit it here
			talonSRX.configPeakOutputForward(config.getDouble("rearWinchUpPower", 0.5), 100);
			talonSRX.configPeakOutputReverse(config.getDouble("rearWinchDownPower", -0.5), 100);
//			talonSRX.configPeakOutputReverse(getReversePower(config, "rearWinchDownPower", -0.5), 100);

            // PID Tunings for climbing winch
			talonSRX.config_kP(0, config.getDouble(type + "_PID_P", 25.0), 100);
	    	talonSRX.config_kI(0, config.getDouble(type + "_PID_I", 0.0), 100);
	    	talonSRX.config_kD(0, config.getDouble(type + "_PID_D", 30.0), 100);
            talonSRX.config_kF(0, config.getDouble(type + "_PID_F", 0.0), 100);

            // Not sure what values to use here
//    		talonSRX.configPeakCurrentLimit(35, 100); /* 35 A */
//    		talonSRX.configPeakCurrentDuration(200, 100); /* 200ms */
//    		talonSRX.configContinuousCurrentLimit(30, 100); /* 30A */
//    		talonSRX.enableCurrentLimit(true); /* turn it on */    	
     	}	
    }
    
    public static void enableVoltageCompensation(TalonSRX talonSRX, double voltage) {
    	
		// Set max voltage out to drive motors to to compensate for battery level
    	if (voltage > 14.0 || voltage < 9.0)
    		voltage = 11.0;
		talonSRX.configVoltageCompSaturation(voltage, 100);
		talonSRX.enableVoltageCompensation(true);
		talonSRX.configVoltageMeasurementFilter(32, 100);
    }
    
    public static void disableVoltageCompensation(TalonSRX talonSRX) {
    	
		// Disable voltage compensation
		talonSRX.enableVoltageCompensation(false);
	}
	
	public static boolean isFwdLimitSwitchClosed(TalonSRX talonSRX) {

		SensorCollection sensorCollection = talonSRX.getSensorCollection();
		return sensorCollection.isFwdLimitSwitchClosed();
	}

	public static boolean isRevLimitSwitchClosed(TalonSRX talonSRX) {

		SensorCollection sensorCollection = talonSRX.getSensorCollection();
		return sensorCollection.isRevLimitSwitchClosed();
    }
    
    private static  double getReversePower(Config config, String revPowerSetting, double defaultValue) {
        // Make sure value read from config file is negative
        double value;
        value = config.getDouble(revPowerSetting, defaultValue);
        if (value > 0.0)
            value = value * -1.0;
        return value;
    }
}
