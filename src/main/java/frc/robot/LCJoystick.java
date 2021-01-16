package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

public class LCJoystick {

	final int k_LeftXAxis = 0;
	final int k_LeftYAxis = 1;
	final int k_LeftThrottle = 2;
	final int k_RightThrottle = 3;
	final int k_RightXAxis = 4;
	final int k_RightYAxis = 5;
	private Joystick joystick;
	private XboxController xboxController;
	private boolean isXbox;

	public LCJoystick(Config config, String user) {
		Joystick joyStickTester = new Joystick(config.getInt(user + "UsbConnector", 0));
		
		if(user.equalsIgnoreCase("driver")) {
			if(joyStickTester.getName().trim().equalsIgnoreCase("Controller (Gamepad F310)")) {
				this.xboxController = new XboxController(joyStickTester.getPort());
				
			}else if(joyStickTester.getName().trim().equalsIgnoreCase("Logitech Extreme 3D")) {
				this.xboxController = new XboxController(1);
			}
			this.isXbox = true;
			System.out.println(user + " Name: " + this.xboxController.getName() + " On Port: " + this.xboxController.getPort());
		}else {
			if(joyStickTester.getName().trim().equalsIgnoreCase("Controller (Gamepad F310)")) {
				this.joystick = new Joystick(0);
				
			}else if(joyStickTester.getName().trim().equalsIgnoreCase("Logitech Extreme 3D")){
				this.joystick = joyStickTester;
			}
			System.out.println(user + " Name: " + this.joystick.getName() + " On Port: " + this.joystick.getPort());
		}
		
	}

	double getX() {

		if (this.isXbox) {
			return this.xboxController.getX(Hand.kLeft);
		} else
			return this.joystick.getX();
	}

	double getRightX() {

		if (this.isXbox) {
			return this.xboxController.getX(Hand.kRight);
		} else {
			return this.joystick.getX();
		}
	}

	double getY() {

		if (this.isXbox) {
			return this.xboxController.getY(Hand.kLeft);
		} else {
			return this.joystick.getY();
		}
	}

	double getRightY() {

		if (this.isXbox) {
			return this.xboxController.getY(Hand.kRight);
		} else {
			return this.joystick.getY();
		}
	}

	double getZ() {

		if (this.isXbox) {
			return this.xboxController.getX(Hand.kRight);
		} else {
			return this.joystick.getZ();
		}
	}

	double getThrottle() {

		if (this.isXbox) {
			double leftThrottle = this.xboxController.getTriggerAxis(Hand.kLeft);
			if (leftThrottle > 0.0) {
				return -leftThrottle;
			} else {
				return this.xboxController.getTriggerAxis(Hand.kRight);
			}
		} else {
			return this.joystick.getThrottle();
		}
	}

	boolean getRawButton(int button) {

		boolean isPressed = false;

		if (this.isXbox && button >= 1 && button <= 10) {
			isPressed = this.xboxController.getRawButton(button);
		} else if (button >= 1 && button <= 12) {
			isPressed = this.joystick.getRawButton(button);
		}

		return isPressed;
	}

	int getPov() {

		if (this.isXbox)
			return this.xboxController.getPOV();
		else
			return this.joystick.getPOV();
	}

	public int getButtonCount() {
		
		if (this.isXbox)
			return 10;
		else
			return this.joystick.getButtonCount();
	}
	
	public void setRumble() {
		if(this.isXbox) {
			this.xboxController.setRumble(GenericHID.RumbleType.kLeftRumble , 1);
		}
	}
}
