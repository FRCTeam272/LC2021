package frc.robot;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax.IdleMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import  com.revrobotics.ControlType;
import com.revrobotics.CANPIDController;

public class Shooter {

    public  TalonSRX        shooterPostionMotor; //This Talon needs to be public because the colorSwitch Class needs to be able toacess it too.
    private TalonSRX        shooterFeederMotor;
    private TalonSRX        shooterHoodMotor;
    private CANSparkMax     shooterPowerMotor;
    private CANEncoder      shooterSpeedEncoder;
    private CANPIDController shooterPIDcontroller;


    private double          shooterSensorOffset = 52.0;
    private double          flyWheelSpeed = 0.5;
    private double          shooterFeederSpeed = 1.0;
    private double          flyWheelRPM = 0.0;
    private double          flyWheelTargetRPM = 4000.0;
    private double          llHorizontialOffset = 0.0;
    private double          llSkewRotation = 0.0;
    private double          llDistance = 0.0;
    private double          shooterSensorPosition = 0.0;
    private double          RPMDistanceFactor = 0.0;
    private double          degreesToPotFactor = .05;
    private double          llMountAngle = 28;
    private double          llMountHeight = 18.75;
    private double          shooterHoodSensorPosition = 200.0;
    private double          shooterHoodTopPosition = 5100.0;
    private double          shooterHoodBottomPosition = 200.0;
    private double          flyWheelTarget2RPM = 0.0;
    public double           kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;
    private double          realShooterHoodPostion;
    

    private double          shooterFarLeftPosition = 52.0;
    private double          shooterFarRightPosition = 30.0;


    


    public Shooter(Config config){
        loadConfig(config);
        shooterPowerMotor    = new CANSparkMax(config.getInt("shooterPowerMotor", 9), MotorType.kBrushless);
        shooterPIDcontroller = shooterPowerMotor.getPIDController();
        shooterSpeedEncoder  = shooterPowerMotor.getEncoder();

        shooterPostionMotor  = new TalonSRX(config.getInt("shooterPostionMotorInput", 5));
        shooterFeederMotor   = new TalonSRX(config.getInt("shooterPowerMotorInput", 4));
        shooterHoodMotor     = new TalonSRX(config.getInt("shooterHoodMotorInput", 8));
        
        LCTalonSRX.configureTalonSRX(this.shooterPostionMotor, config, ControlMode.Position, "ShooterPosition");
        LCTalonSRX.configureTalonSRX(this.shooterHoodMotor, config, ControlMode.Position, "ShooterHood");
        shooterHoodMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);

        this.shooterPostionMotor.set(ControlMode.Position, shooterSensorOffset + shooterSensorPosition );
        
        
        //shooterPowerMotor.setIdleMode(IdleMode.kCoast);
        //shooterPowerMotor.set(flyWheelSpeed);

    }

    public void update(Config config, Sensors sensors, ControlVars controlVars, LimeLight limelight, Inputs input){

        if(controlVars.isshooterTarget()){
            if(limelight.getValidTargetFound()){

                llHorizontialOffset = limelight.getHorizontialOffset();
                /*llSkewRotation = limelight.getSkewRotation();
                if(llSkewRotation >= -45){
                    llSkewRotation = Math.abs(llSkewRotation);
                }
                else if(llSkewRotation < -45 ){
                    llSkewRotation = -(90 + llSkewRotation);
                }*/
                // if(llHorizontialOffset> .5){
                //     moveShooterRight();
                // } else if(llHorizontialOffset <-.5){
                //     moveShooterLeft();
                // }

                if(input.getOpZAxisValue() > .5){
                    moveShooterRight();
                } else if(input.getOpZAxisValue() <-.5){
                    moveShooterLeft();
                }
                

                this.llDistance = limelight.getDistance(this.llMountHeight, this.llMountAngle);
                
                
                //can play with changing RPM's based off 
                //flyWheelTargetRPM = lldistance * RPMDistanceFactor;
                SmartDashboard.putNumber("shooterLLHO", this.llHorizontialOffset);
                SmartDashboard.putNumber("shooterLLSR", this.llSkewRotation);
                SmartDashboard.putNumber("shooterllD", this.llDistance);

            }
        } else {
            this.flyWheelTargetRPM = config.getDouble("flyWheelTargetRPM", 2300.0);
            //if (!controlVars.colorSwitchButton) -- Do this to allow the color switch to control this mortor if it needs too -- this button has not been defined yet
            //this.shooterPostionMotor.set(ControlMode.Position, this.shooterSensorOffset);
        }

        //Manual control of shooter Postion;
        if (controlVars.isShooterLeft()) {
           moveShooterLeft();
        }
        
        if (controlVars.isShooterRight()) {
           moveShooterRight();
        }

        //Keeping the speed of the fly wheel speed between Target RPM and (Target RPM + 1000RPM)


        flyWheelRPM = shooterSpeedEncoder.getVelocity();
        flyWheelRPM = 0;
        System.out.println(flyWheelRPM);
        flyWheelTarget2RPM = (flyWheelTargetRPM * ((input.getOpThrottleValue()+1)/2)) + 500;
        SmartDashboard.putNumber("flyWheelTargetRPM", flyWheelTargetRPM);
        SmartDashboard.putNumber("flyWheelTarget2RPM", flyWheelTarget2RPM);
        SmartDashboard.putNumber("flyWheelRPM", flyWheelRPM);

        kP = 6e-5; 
        kI = 0;
        kD = 0; 
        kIz = 0; 
        kFF = 0.0002; 
        kMaxOutput = 1; 
        kMinOutput = -1;
        maxRPM = 5700;
        
        // set PID coefficients
        shooterPIDcontroller.setP(kP);
        shooterPIDcontroller.setI(kI);
        shooterPIDcontroller.setD(kD);
        shooterPIDcontroller.setIZone(kIz);
        shooterPIDcontroller.setFF(kFF);
        shooterPIDcontroller.setOutputRange(kMinOutput, kMaxOutput);

        shooterPIDcontroller.setReference(flyWheelTarget2RPM, ControlType.kVelocity);
        
        

        //Finding a target and adjusting angle of shooterPostionMotor, else set shooter postion to far right 
        this.shooterPostionMotor.set(ControlMode.Position, this.shooterSensorOffset + this.shooterSensorPosition);

        //Starts feeder motor and sends balls up to the fly wheel
        if(controlVars.isshooterShoot()){
            shooterFeederMotor.set(ControlMode.PercentOutput, shooterFeederSpeed);
            //System.out.println("shooting");
        } else {
            shooterFeederMotor.set(ControlMode.PercentOutput, 0.0);
        }


        this.realShooterHoodPostion = shooterHoodMotor.getSelectedSensorPosition();

        if(controlVars.isShooterHoodAngleUp()){
            
            this.shooterHoodSensorPosition += 25;
            if (this.shooterHoodSensorPosition > this.shooterHoodTopPosition)
               this.shooterHoodSensorPosition = this.shooterHoodTopPosition;
            
            //this.shooterHoodMotor.set(ControlMode.PercentOutput, 0.75);
        }

        if(controlVars.isShooterHoodAngleDown()){
            
            this.shooterHoodSensorPosition -= 25;
            if (this.shooterHoodSensorPosition < this.shooterHoodBottomPosition)
                this.shooterHoodSensorPosition = this.shooterHoodBottomPosition;
            
            //this.shooterHoodMotor.set(ControlMode.PercentOutput, -0.5);
        }

        if(controlVars.isShooterHoodBottom()){
            this.shooterHoodSensorPosition = 100.0;
        }

        if( controlVars.isShooterHoodMiddle()){
            this.shooterHoodSensorPosition = 2000.0;
        }

        if( controlVars.isShooterHoodTop()){
            this.shooterHoodSensorPosition = 5000.0;
        }

        this.realShooterHoodPostion = this.shooterHoodMotor.getSelectedSensorPosition();
        System.out.println("ENTERER");
        if((this.realShooterHoodPostion  > (this.shooterHoodSensorPosition - 50)) && (this.realShooterHoodPostion  < (this.shooterHoodSensorPosition + 50)) ){
            this.shooterHoodMotor.set(ControlMode.PercentOutput, 0.0);
            System.out.println("ENTERED INTO IF");
        }
        else if (this.realShooterHoodPostion > this.shooterHoodSensorPosition){
            this.shooterHoodMotor.set(ControlMode.PercentOutput, 1);
        } 
        else if (this.realShooterHoodPostion < this.shooterHoodSensorPosition){
            this.shooterHoodMotor.set(ControlMode.PercentOutput, -1);
        } else{
            //this.shooterHoodSensorPosition = this.shooterHoodMotor.getSelectedSensorPosition();
        }
        System.out.println("EXIT");

        /*
        if(!controlVars.isShooterHoodAngleDown() && !controlVars.isShooterHoodAngleUp()){
            this.shooterHoodMotor.set(ControlMode.PercentOutput, 0.0);
        }*/
        SmartDashboard.putNumber("ShooterHoodPostion", this.shooterHoodSensorPosition);
        SmartDashboard.putNumber("ShooterHoodRealPostion", this.shooterHoodMotor.getSelectedSensorPosition());
        SmartDashboard.putNumber("SHSensorPosition", this.shooterHoodMotor.getSelectedSensorPosition());
        SmartDashboard.putNumber("SHClosedLoopError", this.shooterHoodMotor.getClosedLoopError());
        SmartDashboard.putNumber("SHClosedLoopTarget", this.shooterHoodMotor.getClosedLoopTarget());
        SmartDashboard.putNumber("SHQuadPosition", this.shooterHoodMotor.getSensorCollection().getQuadraturePosition());
        
        //this.shooterHoodMotor.set(ControlMode.Position, this.shooterHoodSensorPosition);
        
    }

    public void moveShooterLeft(){
        this.shooterSensorPosition -= 2;
        if (this.shooterSensorPosition < this.shooterFarLeftPosition)
           this.shooterSensorPosition = this.shooterFarLeftPosition;
    }
    public void moveShooterRight(){
        this.shooterSensorPosition += 2;
        if (this.shooterSensorPosition > this.shooterFarRightPosition)
            this.shooterSensorPosition = this.shooterFarRightPosition;
    }

    public void loadConfig(Config config){
        this.shooterSensorOffset        = config.getDouble("shooterSensorOffset", 108.0);
        this.flyWheelSpeed              = config.getDouble("flyWheelSpeed", 1.0);
        this.flyWheelTargetRPM          = config.getDouble("flyWheelTargetRPM", 2300.0);
        this.RPMDistanceFactor          = config.getDouble("RPMDistanceFactor", 200.0);
        this.shooterFeederSpeed         = config.getDouble("shooterFeederSpeed", 1.0);
        this.degreesToPotFactor         = config.getDouble("degreesToPotFactor", 20);
        this.llMountAngle               = config.getDouble("llMountAngle", 28);
        this.llMountHeight              = config.getDouble("llMountHeight", 18.75);
        this.shooterFarLeftPosition     = config.getDouble("shooterFarLeftPosition", 0.0);
        this.shooterFarRightPosition    = config.getDouble("shooterFarRightPosition", 150.0);
    }
}
