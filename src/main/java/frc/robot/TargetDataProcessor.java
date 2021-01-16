package frc.robot;

public class TargetDataProcessor{
    private static TargetDataProcessor instance = new TargetDataProcessor();

    private Target returnTarget  = new Target();

    private TargetDataProcessor(){}

    public static TargetDataProcessor getInstance(){
        return instance;
    }

    public void setTargetData(Target target){
        this.returnTarget = target;
    }

    public Target getTargetData(){
        return returnTarget;
    }

}