package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LCIntake {
    private TalonSRX positionControlMotor;
    private TalonSRX intakeBeltMotor;

    //TODO: Adjust speeds and the direction
    final double GRAB_SPEED = 0.7;
    final double RELEASE_SPEED = -1.0;
    final double INTAKE_PARK_SPEED = 0.7;
    final double INTAKE_DEPLOY_SPEED = -0.7;
    int intakeCounter = 0;

	public LCIntake(Config config) {

        this.positionControlMotor = new TalonSRX(config.getInt("intakeMotorInput", 3));
        LCTalonSRX.configureTalonSRX(this.positionControlMotor, config, ControlMode.PercentOutput, "intake");

        this.intakeBeltMotor = new TalonSRX(config.getInt("intakeBeltMotor", 2));
        LCTalonSRX.configureTalonSRX(this.intakeBeltMotor, config, ControlMode.PercentOutput, "intakeBelt");
    }    
    
	public void grab(Sensors sensor) {
        this.intakeBeltMotor.set(ControlMode.PercentOutput, GRAB_SPEED);
        if (sensor.isIntakePhotoEye())
            intakeCounter++;
    }
    
    public void release(Sensors sensor) {
        this.intakeBeltMotor.set(ControlMode.PercentOutput, RELEASE_SPEED);
    }   

	public void park(Sensors sensor) {
        this.positionControlMotor.set(ControlMode.PercentOutput,INTAKE_PARK_SPEED);
        intakeCounter = 0;
    }    

	public void deploy(Sensors sensor) {
        this.positionControlMotor.set(ControlMode.PercentOutput,INTAKE_DEPLOY_SPEED);
    }     
    

    public void Update(ControlVars controlVars, Sensors sensor){

        if (controlVars.isIntakeUp()) {
            park(sensor);
        }
        if (controlVars.isIntakeDown()) {
            deploy(sensor);
        }
        if(!controlVars.isIntakeUp() && !controlVars.isIntakeDown()){
            this.positionControlMotor.set(ControlMode.PercentOutput,0.0);
        }
        if (controlVars.isIntakeIn()) {
            grab(sensor);
        } 
        if (controlVars.isIntakeOut()) {
            System.out.println("release");
            release(sensor);
        } if(!controlVars.isIntakeIn() && !controlVars.isIntakeOut()){
            this.intakeBeltMotor.set(ControlMode.PercentOutput, 0.0);
        }
 
    }    
    public void loadConfig(Config config) {

    }
}

