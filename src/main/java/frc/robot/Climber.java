/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;


public class Climber {
    private TalonSRX climberMotor;

    public Climber(Config config){
        climberMotor  = new TalonSRX(config.getInt("climberMotorInput", 7));
        LCTalonSRX.configureTalonSRX(this.climberMotor, config, ControlMode.PercentOutput, "ShooterHood");
    }

    public void update(ControlVars controlVars, Config config){
        if(controlVars.isClimberUp()){
            this.climberMotor.set(ControlMode.PercentOutput, 1);
        } else if (controlVars.isClimberDown()) {
            
            this.climberMotor.set(ControlMode.PercentOutput, -1);
        }else{
            this.climberMotor.set(ControlMode.PercentOutput, 0);
        }
    }
}
