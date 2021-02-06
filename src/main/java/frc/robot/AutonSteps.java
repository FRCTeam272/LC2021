package frc.robot;

public class AutonSteps {
    public double rotations = 0;
    public double adjust_angle = 0;

    public AutonSteps(){

    }

    public AutonSteps(double rotations, double adjust_angle){
        this.rotations = rotations;
        this.adjust_angle = adjust_angle;
    }

    public AutonSteps(double rotations){
        this.rotations = rotations;
    }
}
