package frc.robot;

public class Trajectory {
	private Double speed;
	private Double angle;
	private Double rotation;
	private boolean intakeIn;
	private boolean intakeUp;
	private boolean intakeDown;

	public Double getSpeed() {
		return speed;
	}



	public boolean isIntakeDown() {
		return intakeDown;
	}



	public void setIntakeDown(boolean intakeDown) {
		this.intakeDown = intakeDown;
	}



	public boolean isIntakeUp() {
		return intakeUp;
	}



	public void setIntakeUp(boolean intakeUp) {
		this.intakeUp = intakeUp;
	}



	public boolean isIntakeIn() {
		return intakeIn;
	}



	public void setIntakeIn(boolean intakeIn) {
		this.intakeIn = intakeIn;
	}



	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Double getAngle() {
		return angle;
	}

	public void setAngle(Double angle) {
		this.angle = angle;
	}

	public Double getRotation() {
		return rotation;
	}

	public void setRotation(Double rotation) {
		this.rotation = rotation;
	}

}
