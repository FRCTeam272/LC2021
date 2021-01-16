package frc.robot;

public class InputCurve {

	private double curve[] = new double[20];
	private int points = 0;
	
	public InputCurve(String ics) {
		
		String values[];
		int len;
		
		values = ics.split(",");
		len = values.length;
		if (len > 0)
			this.points = len - 1;
		else
			this.points = 0;
		
		for (int i = 0; i < len; i++) {
			curve[i] = Double.parseDouble(values[i]);
		}			
	}
	
	public double getCurveValue(double input) {
		
		double result = 0.0;
		
		if (input > 1.0)
			input = 1.0;
		if (input < 0.0)
			input = 0.0;
		
		Double temp = input * this.points;
		int index = temp.intValue();
		if (index == this.points)
			return curve[index];
		else {
			double diff = Math.abs(curve[index] - curve[index + 1]);
			double base = curve[index];
			result = base + diff * ((input * 10.0) % 1.0);
		}			
		return result;
	}
	
	public int getPoints() {
		return this.points;
	}
}
