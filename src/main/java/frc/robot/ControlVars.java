package frc.robot;

public class ControlVars {

	private double	robotSpeed;
	private double	robotAngle;
    private double 	robotRotation;
    
    private boolean auton;
    private boolean cargoElevatorLevel;
	private boolean deployHatchTarget;
	private boolean elevatorBottom;
	private boolean elevatorDown;
	private boolean elevatorGround;
	private boolean	elevatorMiddle;
	private boolean	elevatorTop;
	private boolean elevatorUp;
	private boolean extendorIn;
	private boolean extendorOut;
	private boolean fieldCentricDrive;
    private boolean flippaUp;
    private boolean flippaDown;
	private boolean grabBall;
	private boolean groundManipulatorUp;
	private boolean groundPickup;
	private boolean groundRelease;
	private boolean gyroDrive;
	private boolean hatchMode;
	private boolean loadingStationPickup;
	private boolean manipulatorOff;
	private boolean manipulatorOn;
	private boolean operatorDrive;
    private boolean releaseBall;
    private boolean intakeUp;
    private boolean intakeDown;
    private boolean intakeIn;
    private boolean intakeOut;
    private boolean shooterShoot;
    private boolean shooterTarget;
    private boolean shooterLeft;
    private boolean shooterRight;
    private boolean shooterHoodAngleUp;
    private boolean shooterHoodAngleDown;
    private boolean climberUp;
    private boolean climberDown;
    private boolean shooterHoodTop;
    private boolean shooterHoodMiddle;
    private boolean shooterHoodBottom;

	ControlVars() {
		zeroVars();
	}

    public void zeroVars() {
  
        robotAngle = 0.0;
        robotSpeed = 0.0;
        robotRotation = 0.0;

        auton = false;
        cargoElevatorLevel = false;
        deployHatchTarget = false;
        elevatorBottom = false;
        elevatorDown = false;
        elevatorGround = false;
        elevatorMiddle = false;
        elevatorTop = false;
        elevatorUp = false;
        extendorIn = false;
        extendorOut = false;
        fieldCentricDrive = false;
        flippaUp = false;
        flippaDown = false;
        grabBall = false;
        groundManipulatorUp = false;
        groundPickup = false;
        groundRelease = false;
        gyroDrive = false;
        hatchMode = false;
        loadingStationPickup = false;
        manipulatorOff = false;
        manipulatorOn = false;
        operatorDrive = false;
        releaseBall = false;
        intakeUp = false;
        intakeDown = false;
        intakeIn = false;
        intakeOut = false;
        shooterShoot = false;
        shooterTarget = false;
        shooterLeft = false;
        shooterRight = false;
        shooterHoodAngleUp = false;
        shooterHoodAngleDown = false;
        climberDown = false;
        climberUp = false;
        shooterHoodBottom = false;
        shooterHoodMiddle = false;
        shooterHoodTop = false;
        
    }

    /**
     * @return the robotSpeed
     */
    public double getRobotSpeed() {
        return robotSpeed;
    }

    /**
     * @param robotSpeed
     *                       the robotSpeed to set
     */
    public void setRobotSpeed(double robotSpeed) {
        this.robotSpeed = robotSpeed;
    }

    /**
     * @return the robotAngle
     */
    public double getRobotAngle() {
        return robotAngle;
    }

    /**
     * @param robotAngle
     *                       the robotAngle to set
     */
    public void setRobotAngle(double robotAngle) {
        this.robotAngle = robotAngle;
    }

    /**
     * @return the robotRotation
     */
    public double getRobotRotation() {
        return robotRotation;
    }

    /**
     * @param robotRotation
     *                          the robotRotation to set
     */
    public void setRobotRotation(double robotRotation) {
        this.robotRotation = robotRotation;
    }

    /**
     * @return the auton
     */
    public boolean isAuton() {
        return auton;
    }

    /**
     * @param auton
     *                  the auton to set
     */
    public void setAuton(boolean auton) {
        this.auton = auton;
    }

    /**
     * @return the cargoElevatorLevel
     */
    public boolean isCargoElevatorLevel() {
        return cargoElevatorLevel;
    }

    /**
     * @param cargoElevatorLevel
     *                               the cargoElevatorLevel to set
     */
    public void setCargoElevatorLevel(boolean cargoElevatorLevel) {
        this.cargoElevatorLevel = cargoElevatorLevel;
    }

    /**
     * @return the deployHatchTarget
     */
    public boolean isDeployHatchTarget() {
        return deployHatchTarget;
    }

    /**
     * @param deployHatchTarget
     *                              the deployHatchTarget to set
     */
    public void setDeployHatchTarget(boolean deployHatchTarget) {
        this.deployHatchTarget = deployHatchTarget;
    }

    /**
     * @return the elevatorBottom
     */
    public boolean isElevatorBottom() {
        return elevatorBottom;
    }

    /**
     * @param elevatorBottom
     *                           the elevatorBottom to set
     */
    public void setElevatorBottom(boolean elevatorBottom) {
        this.elevatorBottom = elevatorBottom;
    }

    /**
     * @return the elevatorDown
     */
    public boolean isElevatorDown() {
        return elevatorDown;
    }

    /**
     * @param elevatorDown
     *                         the elevatorDown to set
     */
    public void setElevatorDown(boolean elevatorDown) {
        this.elevatorDown = elevatorDown;
    }

    /**
     * @return the elevatorGround
     */
    public boolean isElevatorGround() {
        return elevatorGround;
    }

    /**
     * @param elevatorGround
     *                           the elevatorGround to set
     */
    public void setElevatorGround(boolean elevatorGround) {
        this.elevatorGround = elevatorGround;
    }

    /**
     * @return the elevatorMiddle
     */
    public boolean isElevatorMiddle() {
        return elevatorMiddle;
    }

    /**
     * @param elevatorMiddle
     *                           the elevatorMiddle to set
     */
    public void setElevatorMiddle(boolean elevatorMiddle) {
        this.elevatorMiddle = elevatorMiddle;
    }

    /**
     * @return the elevatorTop
     */
    public boolean isElevatorTop() {
        return elevatorTop;
    }

    /**
     * @param elevatorTop
     *                        the elevatorTop to set
     */
    public void setElevatorTop(boolean elevatorTop) {
        this.elevatorTop = elevatorTop;
    }

    /**
     * @return the elevatorUp
     */
    public boolean isElevatorUp() {
        return elevatorUp;
    }

    /**
     * @param elevatorUp
     *                       the elevatorUp to set
     */
    public void setElevatorUp(boolean elevatorUp) {
        this.elevatorUp = elevatorUp;
    }

    /**
     * @return the extendorIn
     */
    public boolean isExtendorIn() {
        return extendorIn;
    }

    /**
     * @param extendorIn
     *                       the extendorIn to set
     */
    public void setExtendorIn(boolean extendorIn) {
        this.extendorIn = extendorIn;
    }

    /**
     * @return the extendorOut
     */
    public boolean isExtendorOut() {
        return extendorOut;
    }

    /**
     * @param extendorOut
     *                        the extendorOut to set
     */
    public void setExtendorOut(boolean extendorOut) {
        this.extendorOut = extendorOut;
    }

    /**
     * @return the fieldCentricDrive
     */
    public boolean isFieldCentricDrive() {
        return fieldCentricDrive;
    }

    /**
     * @param fieldCentricDrive
     *                              the fieldCentricDrive to set
     */
    public void setFieldCentricDrive(boolean fieldCentricDrive) {
        this.fieldCentricDrive = fieldCentricDrive;
    }

    /**
     * @return the flippaUp
     */
    public boolean isFlippaUp() {
        return flippaUp;
    }

    /**
     * @param flippaUp
     *                     the flippaUp to set
     */
    public void setFlippaUp(boolean flippaUp) {
        this.flippaUp = flippaUp;
    }

    /**
     * @return the flippaDown
     */
    public boolean isFlippaDown() {
        return flippaDown;
    }

    /**
     * @param flippaDown
     *                       the flippaDown to set
     */
    public void setFlippaDown(boolean flippaDown) {
        this.flippaDown = flippaDown;
    }

    /**
     * @return the grabBall
     */
    public boolean isGrabBall() {
        return grabBall;
    }

    /**
     * @param grabBall
     *                     the grabBall to set
     */
    public void setGrabBall(boolean grabBall) {
        this.grabBall = grabBall;
    }

    /**
     * @return the groundManipulatorUp
     */
    public boolean isGroundManipulatorUp() {
        return groundManipulatorUp;
    }

    /**
     * @param groundManipulatorUp
     *                                the groundManipulatorUp to set
     */
    public void setGroundManipulatorUp(boolean groundManipulatorUp) {
        this.groundManipulatorUp = groundManipulatorUp;
    }

    /**
     * @return the groundPickup
     */
    public boolean isGroundPickup() {
        return groundPickup;
    }

    /**
     * @param groundPickup
     *                         the groundPickup to set
     */
    public void setGroundPickup(boolean groundPickup) {
        this.groundPickup = groundPickup;
    }

    /**
     * @return the groundRelease
     */
    public boolean isGroundRelease() {
        return groundRelease;
    }

    /**
     * @param groundRelease
     *                          the groundRelease to set
     */
    public void setGroundRelease(boolean groundRelease) {
        this.groundRelease = groundRelease;
    }

    /**
     * @return the gyroDrive
     */
    public boolean isGyroDrive() {
        return gyroDrive;
    }

    /**
     * @param gyroDrive
     *                      the gyroDrive to set
     */
    public void setGyroDrive(boolean gyroDrive) {
        this.gyroDrive = gyroDrive;
    }

    /**
     * @return the hatchMode
     */
    public boolean isHatchMode() {
        return hatchMode;
    }

    /**
     * @param hatchMode
     *                      the hatchMode to set
     */
    public void setHatchMode(boolean hatchMode) {
        this.hatchMode = hatchMode;
    }

    /**
     * @return the loadingStationPickup
     */
    public boolean isLoadingStationPickup() {
        return loadingStationPickup;
    }

    /**
     * @param loadingStationPickup
     *                                 the loadingStationPickup to set
     */
    public void setLoadingStationPickup(boolean loadingStationPickup) {
        this.loadingStationPickup = loadingStationPickup;
    }

    /**
     * @return the manipulatorOff
     */
    public boolean isManipulatorOff() {
        return manipulatorOff;
    }

    /**
     * @param manipulatorOff
     *                           the manipulatorOff to set
     */
    public void setManipulatorOff(boolean manipulatorOff) {
        this.manipulatorOff = manipulatorOff;
    }

    /**
     * @return the manipulatorOn
     */
    public boolean isManipulatorOn() {
        return manipulatorOn;
    }

    /**
     * @param manipulatorOn
     *                          the manipulatorOn to set
     */
    public void setManipulatorOn(boolean manipulatorOn) {
        this.manipulatorOn = manipulatorOn;
    }

    /**
     * @return the operatorDrive
     */
    public boolean isOperatorDrive() {
        return operatorDrive;
    }

    /**
     * @param operatorDrive
     *                          the operatorDrive to set
     */
    public void setOperatorDrive(boolean operatorDrive) {
        this.operatorDrive = operatorDrive;
    }

    /**
     * @return the releaseBall
     */
    public boolean isReleaseBall() {
        return releaseBall;
    }

    /**
     * @param releaseBall
     *                        the releaseBall to set
     */
    public void setReleaseBall(boolean releaseBall) {
        this.releaseBall = releaseBall;
    }

    /**
     * @return the intakeUp
     */
    public boolean isIntakeUp() {
        return intakeUp;
    }

    /**
     * @param intakeUp
     *                       the intakeUp to set
     */
    public void setIntakeUp(boolean intakeUp) {
        this.intakeUp = intakeUp;
    }    

        /**
     * @return the intakeDown
     */
    public boolean isIntakeDown() {
        return intakeDown;
    }

    /**
     * @param intakeDown
     *                       the intakeDown to set
     */
    public void setIntakeDown(boolean intakeDown) {
        this.intakeDown = intakeDown;
    }   


    /**
     * @return the intakeIn
     */
    public boolean isIntakeIn() {
        return intakeIn;
    }

    /**
     * @param intakeIn
     *                       the intakeIn to set
     */
    public void setIntakeIn(boolean intakeIn) {
        this.intakeIn = intakeIn;
    } 


     /**
     * @return the intakeOut
     */
    public boolean isIntakeOut() {
        return intakeOut;
    }

    /**
     * @param intakeOut
     *                       the intakeOut to set
     */
    public void setIntakeOut(boolean intakeOut) {
        this.intakeOut = intakeOut;
    } 

    /**
     * @return the shooterShoot
     */
    public boolean isshooterShoot() {
        return shooterShoot;
    }

    /**
     * @param shooterShoot
     *                       the shooterShoot to set
     */
    public void setshooterShoot(boolean shooterShoot) {
        this.shooterShoot = shooterShoot;
    } 

    /**
     * @return the shooterTarget
     */
    public boolean isshooterTarget() {
        return shooterTarget;
    }

    /**
     * @param shooterShoot
     *                       the shooterShoot to set
     */
    public void setshooterTarget(boolean shooterTarget) {
        this.shooterTarget = shooterTarget;
    }

    /**
     * @return the shooterLeft
     */
    public boolean isShooterLeft() {
        return shooterLeft;
    }

    /**
     * @param shooterLeft
     *                       the shooterLeft to set
     */
    public void setshooterLeft(boolean shooterLeft) {
        this.shooterLeft = shooterLeft;
    }

    /**
     * @return the shooterRight
     */
    public boolean isShooterRight() {
        return shooterRight;
    }

    /**
     * @param shooterRight
     *                       the shooterRight to set
     */
    public void setshooterRight(boolean shooterRight) {
        this.shooterRight = shooterRight;
    }

    /**
     * @return the shooterHoodAngle
     */
    public boolean isShooterHoodAngleUp() {
        return this.shooterHoodAngleUp;
    }

    /**
     * @param shooterHoodAngle
     *                       the shooterHoodAngle to set
     */
    public void setShooterHoodAngleUp(boolean shooterHoodAngleUp) {
        this.shooterHoodAngleUp = shooterHoodAngleUp;
    }

    /**
     * @return the shooterHoodAngle
     */
    public boolean isShooterHoodAngleDown() {
        return this.shooterHoodAngleDown;
    }

    /**
     * @param shooterHoodAngle
     *                       the shooterHoodAngle to set
     */
    public void setShooterHoodAngleDown(boolean shooterHoodAngleDown) {
        this.shooterHoodAngleDown = shooterHoodAngleDown;
    }

    /**
     * @return the ClimberDown
     */
    public boolean isClimberDown() {
        return this.climberDown;
    }

    /**
     * @param Climber
     *                       the Climber to set
     */
    public void setClimberDown(boolean climberDown) {
        this.climberDown = climberDown;
    }

    /**
     * @return the ClimberUp
     */
    public boolean isClimberUp() {
        return this.climberUp;
    }

    /**
     * @param Climber
     *                       the Climber to set
     */
    public void setClimberUp(boolean climberUp) {
        this.climberUp = climberUp;
    }

    public boolean isShooterHoodTop() {
        return shooterHoodTop;
    }

    public void setShooterHoodTop(boolean shooterHoodTop) {
        this.shooterHoodTop = shooterHoodTop;
    }

    public boolean isShooterHoodMiddle() {
        return shooterHoodMiddle;
    }

    public void setShooterHoodMiddle(boolean shooterHoodMiddle) {
        this.shooterHoodMiddle = shooterHoodMiddle;
    }

    public boolean isShooterHoodBottom() {
        return shooterHoodBottom;
    }

    public void setShooterHoodBottom(boolean shooterHoodBottom) {
        this.shooterHoodBottom = shooterHoodBottom;
    }

	public void moveForward(double rotations) {
        int encoder = 0;
        int speed = 1;
        if (rotations < 0){
            speed = -1;
        }
        
        while(encoder < rotations){
            setRobotSpeed(speed);
            encoder++; // TODO find the encoder value
        }
	}

}
