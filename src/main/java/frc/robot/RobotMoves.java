package frc.robot;

import edu.wpi.first.wpilibj.Timer;

public class RobotMoves {

    // private double attackAngle = 0.0;
    // private double travelTime = 0.0;
    private boolean centered = false;
    private boolean rotateRight = false;
    private boolean rotateLeft = false;
    private boolean declineInit = false;
    private int rotIdx;
    private int rotSteps = 0;
    private Timer rmTimer;
    private int angle = 0;

    RobotMoves() {
        this.rmTimer = new Timer();
    }
    /*
     * public void dockJeVois (ControlVars controlVars, Sensors sensors, boolean
     * reset) {
     * 
     * Target target = null; double rightIRDist; double leftIRDist;
     * 
     * if (reset) { this.rmTimer.reset(); this.rmTimer.start(); // If we have a
     * target in view, calculate angle and distance and then // drive to that
     * location this.attackAngle = 0.0; this.travelTime = 0.0; target =
     * sensors.getTarget(); if (target != null && target.isHaveTarget()) {
     * this.attackAngle = target.getAttackAngle() * 57.295; //Compute dist we have
     * to move then divide by 2.4 ft per sec to get time this.travelTime =
     * target.getDistance() / (2.4 * 12); } }
     * controlVars.setRobotAngle(degreesToPlusMinusOne(this.attackAngle));
     * controlVars.setRobotSpeed(0.3); rightIRDist = sensors.getRightRocketDist();
     * leftIRDist = sensors.getLeftRocketDist(); if (rightIRDist > 2000.0 ||
     * leftIRDist > 2000.0) { // if (this.rmTimer.get() >= this.travelTime) {
     * controlVars.setRobotSpeed(0.0); } else if (rightIRDist - leftIRDist > 100) {
     * controlVars.setRobotRotation(0.2); } else if (leftIRDist - rightIRDist > 100)
     * { controlVars.setRobotRotation(-0.2); } }
     * 
     */

    public void dock(ControlVars controlVars, Sensors sensors, double cargoRotationDist, double rocketRotationDist,
            boolean isRocket, boolean reset) {

        double rightIRDist;
        double leftIRDist;
        double dist;
        double speed;
        double delta;
        double rotation;
        double rotationDistance = 600;

        if (reset) {
            this.rmTimer.reset();
            this.rmTimer.start();
            this.centered = false;
            this.rotateRight = false;
            this.rotateLeft = false;
        }

        if (isRocket) {
            rotationDistance = rocketRotationDist;
        } else {
            rotationDistance = cargoRotationDist;
        }
        rightIRDist = sensors.getRightRocketDist();
        leftIRDist = sensors.getLeftRocketDist();
        dist = Math.max(rightIRDist, leftIRDist);
        if (isRocket)
            speed = 0.5119572 - 0.0006569679 * dist + 0.0000002117939 * (dist * dist);
        else
            speed = -0.0005118962 * dist + 0.4834174;
        controlVars.setRobotSpeed(speed);
        if (!sensors.isGroundIR3()) {
            centered = true;
        } else if (!sensors.isGroundIR0() || !sensors.isGroundIR1()) {
            controlVars.setRobotAngle(0.22);
            controlVars.setRobotSpeed(0.0);
        } else if (!sensors.isGroundIR2()) {
            controlVars.setRobotAngle(0.17);
            controlVars.setRobotSpeed(0.0);
        } else if (!sensors.isGroundIR4()) {
            controlVars.setRobotAngle(-0.17);
            controlVars.setRobotSpeed(0.0);
        } else if (!sensors.isGroundIR5() || !sensors.isGroundIR6()) {
            controlVars.setRobotAngle(-0.22);
            controlVars.setRobotSpeed(0.0);
        }
        if (centered) {
            controlVars.setRobotSpeed(0.0);
            controlVars.setRobotAngle(0.0);
        }
        // Try to align robot to be parallel to cargo ship or rocket
        delta = Math.abs(rightIRDist - leftIRDist);
        if (delta > 35)
            delta = 35;
        rotation = delta * 0.01;
        // System.out.println("Delta " + delta + " Rotation " + rotation);

        if (leftIRDist > rotationDistance && rightIRDist > rotationDistance) {
            controlVars.setRobotSpeed(0.0);
            if (delta > 5) {
                if (rightIRDist > leftIRDist) {
                    this.rotateRight = true;
                    controlVars.setRobotRotation(rotation);
                } else {
                    this.rotateLeft = true;
                    controlVars.setRobotRotation(-rotation);
                }
            }
        }
        if (this.rotateRight && this.rotateLeft) {
            controlVars.setRobotRotation(0.0);
        }
    }

    public void sixInchShuffle(ControlVars controlVars, boolean reset, double angle, double power) {

        if (reset) {
            this.rmTimer.reset();
            this.rmTimer.start();
        }

        double ang = (Math.toRadians(angle) * -1.0) + Math.PI / 2;
        controlVars.setRobotSpeed(Math.sin(ang) * power);
        controlVars.setRobotAngle(Math.cos(ang) * power);
        controlVars.setRobotRotation(0);
        if (this.rmTimer.get() > 0.5) {
            controlVars.setRobotSpeed(0.0);
            controlVars.setRobotAngle(0.0);
        }
    }

    // private double degreesToPlusMinusOne(double angle) {

    //     double result = angle;

    //     // Validate angle
    //     if (angle > 90.0)
    //         result = 90.0;
    //     else if (angle < -90.0)
    //         result = -90.0;
    //     return result / 90.0;
    // }

    public boolean rotateByGyro(ControlVars controlVars, Sensors sensors, double angle, double rotation,
            boolean reset) {

        boolean done = false;
        double currBearing = 0.0;
        double diff;
        double r;

        if (reset) {
            sensors.zeroGyroBearing();
        }

        currBearing = (int) sensors.getNavxAngle() % 360;
        controlVars.setRobotAngle(0.0);
        controlVars.setRobotSpeed(0.0);
        r = rotation;
        diff = Math.abs(angle - currBearing);
        if (diff < (angle * 0.2)) {
            r = r * (diff / angle);
        }
        if (angle < 0.0) {
            r *= -1;
        }
        controlVars.setRobotRotation(r);
        // controlVars.setRobotRotation((angle >= 0.0) ? rotation : -rotation);
        System.out.println("angle: " + angle + "  currBearing: " + currBearing + "  rotation" + r);
        controlVars.setGyroDrive(false);
        if ((angle >= 0.0 && currBearing > angle) || (angle < 0.0 && currBearing < angle)) {
            controlVars.setRobotRotation(0.0);
            done = true;
        } else
            done = false;
        return done;
    }

    public boolean rotate180(ControlVars controlVars, Sensors sensors, boolean clockwise, boolean reset) {

        if (reset)
            sensors.zeroGyroBearing();
        return rotate(controlVars, sensors, (clockwise) ? 180 : -180, reset);
    }

    public boolean rotateHome(ControlVars controlVars, Sensors sensors, boolean reset) {

        int currBearing;

        if (reset) {
            currBearing = (int) sensors.getNavxAngle() % 360; // bearing in range of 0 to 359 or -0 to -359
            if (currBearing >= 0 && currBearing <= 180) {
                this.angle = currBearing * -1; // rotate left to get back home
            } else if (currBearing > 180) {
                this.angle = 360 - currBearing; // rotate right to get back home
            } else if (currBearing < 0.0 && currBearing >= -180) {
                this.angle = currBearing * -1; // rotate right to get back home
            } else if (currBearing < -180) {
                this.angle = (360 + currBearing) * -1; // rotate left to get back home
            }
        }
        return rotate(controlVars, sensors, this.angle, reset);
    }

    public boolean rotate(ControlVars controlVars, Sensors sensors, int angle, boolean reset) {

        boolean clockwise = true;
        boolean done = false;
        boolean pastEndPoint = false;
        boolean pastMidPoint = false;
        int currBearing;
        int endPointDelta;
        int midPointDelta;
        double r;
        double maxRot = 1.0;
        double rotIncrement = 0.005;

        if (reset) {
            if (angle < 0)
                clockwise = false;
            this.rotSteps = Math.abs(angle) / 2;
            this.rotIdx = 0;
            rotIncrement = maxRot / (this.rotSteps / 2);
            this.declineInit = false;
            this.rmTimer.reset();
            this.rmTimer.start();
        }

        controlVars.setRobotAngle(0.0);
        controlVars.setRobotSpeed(0.0);
        currBearing = (int) sensors.getNavxAngle() % 360;
        endPointDelta = Math.abs(angle) - Math.abs(currBearing);
        midPointDelta = Math.abs(angle / 2) - Math.abs(currBearing);
        pastEndPoint = endPointDelta < (Math.abs(angle) * 2) / 10;
        pastMidPoint = midPointDelta < (Math.abs(angle / 2) * 2) / 10;

        if (!pastMidPoint) {
            r = (this.rotIdx * rotIncrement);
            if (r < 0.2)
                r = 0.2;
            else if (r > 1.0)
                r = 1.0;
        } else {
            if (!declineInit) {
                this.rotIdx = 0;
                this.rmTimer.reset();
                this.rmTimer.start();
                declineInit = true;
            }
            r = 1.0 - (this.rotIdx * rotIncrement);
            if (pastEndPoint)
                r = -0.1;
        }
        if (!clockwise)
            r *= -1.0;
        controlVars.setRobotRotation(r);
        System.out.println("Idx: " + this.rotIdx + "  currBearing: " + currBearing + "  rotation " + r + " pmp "
                + pastMidPoint + " pep " + pastEndPoint);
        controlVars.setGyroDrive(false);
        this.rotIdx = (int) Math.round(this.rmTimer.get() / 0.022);

        if ((clockwise && currBearing > angle) || (!clockwise && currBearing < angle)) {
            controlVars.setRobotRotation(0.0);
            done = true;
        } else
            done = false;
        return done;
    }

    public boolean goForwardFromWall(ControlVars controlVars, Sensors sensors, double angle, double distance,
            double power) {

        boolean success = false;
       

        
        double ang = (Math.toRadians(angle) * -1.0) + Math.PI / 2;
        controlVars.setRobotAngle(Math.cos(ang) * controlVars.getRobotSpeed());
        // controlVars.setRobotAngle(degreesToPlusMinusOne(angle));
        controlVars.setRobotRotation(0.0);
        controlVars.setGyroDrive(true);
        return success;
    }

    public boolean goForwardToWall(ControlVars controlVars, Sensors sensors, double angle, double distance,
            double power) {

        boolean success = false;

        double ang = (Math.toRadians(angle) * -1.0) + Math.PI / 2;
        controlVars.setRobotSpeed(Math.sin(ang) * power);
        controlVars.setRobotAngle(Math.cos(ang) * power);
        // controlVars.setRobotSpeed(power);
        // controlVars.setRobotAngle(degreesToPlusMinusOne(angle));
        controlVars.setRobotRotation(0.0);
        controlVars.setGyroDrive(true);

        /*if (sensors.getFrontUltraDistance1() < distance) { // See if we're close enough
            success = true;
        }*/
        return success;
    }

}
