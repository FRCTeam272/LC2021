package frc.robot;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class LCTalonFX {


    public static void configureTalonFX(TalonFX talonFX, Config config, ControlMode controlMode, String type) {

    	talonFX.selectProfileSlot(0, 0);

    	if (ControlMode.Position == controlMode) {
	    	//Timeout configured for 100ms to wait for configFeedbackSensor. if timeout error
	    	ErrorCode error = talonFX.configSelectedFeedbackSensor(FeedbackDevice.Analog, 0, 100);
	    	if(error.value!=0) {
	    		System.out.println("!!!!!!!! Error updating Config for Feedback Senor while configuring CAN Talon" + error.toString());
	    	}
	    		
	    	// Don't accumulate position information e.g. position is absolute
	    	talonFX.configSetParameter(ParamEnum.eFeedbackNotContinuous, 1, 0x00, 0x00, 0x00);
	    	
			// Choose to ensure sensor is positive when output is positive
			talonFX.setSensorPhase(false);

			// Choose based on what direction you want forward/positive to be.
			// This does not affect sensor phase. 
			talonFX.setInverted(false);

			// Set the peak and nominal outputs
			talonFX.configNominalOutputForward(0, 100);
			talonFX.configNominalOutputReverse(0, 100);
			talonFX.configPeakOutputForward(1, 100);
			talonFX.configPeakOutputReverse(-1, 100);
			
			talonFX.setNeutralMode(NeutralMode.Brake);

			talonFX.configAllowableClosedloopError(0,config.getInt(type + "AllowableClosedLoopErr", 0), 100);

			// Set the PID loop values
			talonFX.config_kP(0, config.getDouble(type + "_PID_P", 10.0), 100);
	    	talonFX.config_kI(0, config.getDouble(type + "_PID_I", 0.0), 100);
	    	talonFX.config_kD(0, config.getDouble(type + "_PID_D", 100.0), 100);
	    	talonFX.config_kF(0, config.getDouble(type + "_PID_F", 0.2), 100);

	    	talonFX.set(ControlMode.Position, 0.0);
    	} else if (ControlMode.PercentOutput == controlMode) {
			// Set the peak and nominal outputs
			talonFX.configNominalOutputForward(0, 100);
			talonFX.configNominalOutputReverse(0, 100);
			talonFX.configPeakOutputForward(1, 100);
			talonFX.configPeakOutputReverse(-1, 100);
    		//talonSRX.configOpenloopRamp(0.3, 100);
			talonFX.setNeutralMode(NeutralMode.Brake);

    		talonFX.set(ControlMode.PercentOutput, 0.0);    		
    	}
 
//    	if (type.equalsIgnoreCase("drive")) {
//    		// Set max voltage out to drive motors to 10 V. to compensate for battery level
//    		talonSRX.configVoltageCompSaturation(10.0, 100);
//    		talonSRX.enableVoltageCompensation(true);
//    		talonSRX.configVoltageMeasurementFilter(32, 100);
//    	}
    	
    	
    	
    	
    }
    
    public static void enableVoltageCompensation(TalonFX talonFX, double voltage) {
    	
		// Set max voltage out to drive motors to to compensate for battery level
    	if (voltage > 14.0 || voltage < 9.0)
    		voltage = 11.0;
		talonFX.configVoltageCompSaturation(voltage, 100);
		talonFX.enableVoltageCompensation(true);
		talonFX.configVoltageMeasurementFilter(32, 100);
    }
    
    public static void disableVoltageCompensation(TalonFX talonFX) {
    	
		// Disable voltage compensation
		talonFX.enableVoltageCompensation(false);
	}
	
	// TODO : Recode if needed
	/*
	public static boolean isFwdLimitSwitchClosed(TalonFX talonFX) {

		SensorCollection sensorCollection = talonFX.getSensorCollection();
		return sensorCollection.isFwdLimitSwitchClosed();
	}

	public static boolean isRevLimitSwitchClosed(TalonFX talonFX) {

		SensorCollection sensorCollection = talonSRX.getSensorCollection();
		return sensorCollection.isRevLimitSwitchClosed();
	}
    may need to be re implimented
    private static  double getReversePower(Config config, String revPowerSetting, double defaultValue) {
        // Make sure value read from config file is negative
        double value;
        value = config.getDouble(revPowerSetting, defaultValue);
        if (value > 0.0)
            value = value * -1.0;
        return value;
	}
	*/
}
