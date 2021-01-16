
package frc.robot;

public class Target {

	private double attackAngle;
	private double distance;
	private double frameCount;
	private double width;
	private double xCoordinate;
	
	Target (){
		attackAngle = 0.0;
		distance = 0.0;
		frameCount = 0.0;
		width = 0.0;
		xCoordinate = 0.0;
	}

	public boolean isHaveTarget() {
		return width > 0.0 ;
	}
	
	public double getxCoordinate() {
		return xCoordinate;
	}
	
	public double getTargetX() {
		return ((xCoordinate - 640) / 640.0);
	}

	public void setxCoordinate(double xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getFrameCount() {
		return frameCount;
	}

	public void setFrameCount(double frameCount) {
		this.frameCount = frameCount;
	}
	
	public double getDistance(){
		if (isHaveTarget())
			//distance = (.005*(width*width)) + (-1.15*width) + 74.31;
            //distance = (0.000834 * (width*width*width) + -.094*(width*width)) + (2.501*width) + 33.66;
            distance = 1026.058536 * Math.pow(width, -0.9322344448);
		else
			distance = 0.0;
		return distance;
	}

	public double getAttackAngle(){
		if (isHaveTarget()) {
			double translation;
			distance = getDistance();
			translation = (xCoordinate - 320.0)/(width/2.0);
			attackAngle = Math.atan((translation/distance));
		} else 
			attackAngle = 0.0;
		return attackAngle;
	}
}
