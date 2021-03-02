package frc.robot;

public abstract class DriveTrain {

	public double expo(double value, double exponent) {
		
		return Math.pow(value, exponent);

		// double temp;
		
		// temp = value;
		// for (int i = 1; i < exponent; i++)
		// 	temp *= temp;
		// if (value < 0.0 && temp > 0.0)  // preserve sign when multiplying by itself even number of times
		// 	return temp * -1.0;
		// else
		// 	return temp;
	}

	public abstract void calibrate(Inputs inputs);
	
	public abstract void mapInputsToControlVars(Inputs inputs, ControlVars controlVars);
	
	public abstract void update(ControlVars controlVars, Sensors sensors, GyroNavigate gyroNavigate);
	
	public abstract void outputToDashboard(Boolean minDisplay);
	
	public abstract void writeTelemetryValues(LCTelemetry telem);
	
}
