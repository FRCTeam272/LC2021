/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix. motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;

public class ColorSwitch{

    private final ColorMatch m_colorMatcher;
    private int numberOfRotation;
    String colorPath[];
    int numberOfSlice;
    String gameData;

    private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);
    TalonSRX colorSpinner;
    private Timer spinTimer;
    private boolean spinnerInitialized = false;
    private final double timeout = 20.0;


    public ColorSwitch(Config config){
        m_colorMatcher = new ColorMatch();
        m_colorMatcher.addColorMatch(kBlueTarget);
        m_colorMatcher.addColorMatch(kGreenTarget);
        m_colorMatcher.addColorMatch(kRedTarget);
        m_colorMatcher.addColorMatch(kYellowTarget);    
        this.colorSpinner=new TalonSRX(config.getInt("colorSpinner", 2));   //somenumber
        LCTalonSRX.configureTalonSRX(this.colorSpinner, config, ControlMode.PercentOutput, "colorSpinner");
        this.spinTimer = new Timer();
        colorPath = new String[4];
    }
    
    public void colorFinder(Sensors sensors){
      String colorposition[] = {"Yellow","Blue","Green","Red","Yellow","Blue","Green"};
      Color detectedColor = sensors.getDetectedColor();
      ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
      String colorString;
        if (match.color == kBlueTarget) {
          colorString = "Blue";
        } else if (match.color == kRedTarget) {
          colorString = "Red";
        } else if (match.color == kGreenTarget) {
          colorString = "Green";
        } else if (match.color == kYellowTarget) {
          colorString = "Yellow";
        } else {
          colorString = "Unknown";
        }
      if(spinnerInitialized ==false){
        spinTimer.reset();
        spinTimer.start();
        numberOfRotation = 0;
        numberOfSlice = 0;
        spinnerInitialized = true;
        //String colorposition[] = {"Yellow","Blue","Green","Red","Yellow","Blue","Green"};
        for(int i=0;i<colorposition.length;i++){
          if(colorposition[i] == colorString){
            colorPath[0] = colorString;
            colorPath[1] = colorposition[i+1];
            colorPath[2] = colorposition[i+2];
            colorPath[3] = colorposition[i+3];
            break;
            }
      }
      }
      if(spinnerInitialized){
        setColorSpinner(0.5);
        switch (colorString) {
          case "Red":
          if(colorPath[numberOfSlice] == "Red")
            numberOfSlice++;
            break;
          case "Yellow":
            if(colorPath[numberOfSlice] == "Yellow")
              numberOfSlice++;
              break;
          case "Blue":
            if(colorPath[numberOfSlice] == "Blue")
              numberOfSlice++;
              break;
          case "Green":
            if(colorPath[numberOfSlice] == "Green")
              numberOfSlice++;
              break;
        }
          if(numberOfSlice==4){
            numberOfRotation++;
            numberOfSlice=0;
          }
          if(numberOfRotation ==8){
            setColorSpinner(0);
            spinnerInitialized = false;
          }
          else if(spinTimer.get()>timeout){
            setColorSpinner(0);
            spinnerInitialized = false;
          }
      }
      if(spinnerInitialized){
      String requiredColor = "";
      gameData = DriverStation.getInstance().getGameSpecificMessage();
      switch(gameData.charAt(0)){
        case 'G':
          requiredColor = "Green";
          break;
        case 'B':
          requiredColor = "Blue";
          break;
        case 'Y':
          requiredColor = "Yellow";
          break;
        case 'R':
          requiredColor = "Red";
          break;
        default:
          System.out.print("No FMS Data");
      }
        for(int i =0;i<colorposition.length;i++){
          if(requiredColor == colorposition[i])
            requiredColor = colorposition[i+2];
        }
        setColorSpinner(0.5);
        if(requiredColor == colorString)
          setColorSpinner(0.0);
      }
    }
    
    public void inputSmartDashBoard(Sensors sensors) {
      /**
       * The method GetColor() returns a normalized color value from the sensor and can be
       * useful if outputting the color to an RGB LED or similar. To
       * read the raw color, use GetRawColor().
       * 
       * The color sensor works best when within a few inches from an object in
       * well lit conditions (the built in LED is a big help here!). The farther
       * an object is the more light from the surroundings will bleed into the 
       * measurements and make it difficult to accurately determine its color.
       */
      Color detectedColor = sensors.getDetectedColor();
  
      /**
       * Run the color match algorithm on our detected color
       * 
       */
      String colorString;
      ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
      String requiredColor = "";
      gameData = DriverStation.getInstance().getGameSpecificMessage();
      switch(gameData.charAt(0)){
        case 'G':
          requiredColor = "Green";
          break;
        case 'B':
          requiredColor = "Blue";
          break;
        case 'Y':
          requiredColor = "Yellow";
          break;
        case 'R':
          requiredColor = "Red";
          break;
        default:
          requiredColor = "No Color Yet!";
      }
  
      if (match.color == kBlueTarget) {
        colorString = "Blue";
      } else if (match.color == kRedTarget) {
        colorString = "Red";
      } else if (match.color == kGreenTarget) {
        colorString = "Green";
      } else if (match.color == kYellowTarget) {
        colorString = "Yellow";
      } else {
        colorString = "Unknown";
      }
  
      /**
       * Open Smart Dashboard or Shuffleboard to see the color detected by the 
       * sensor.
       */
      SmartDashboard.putNumber("Red", detectedColor.red);
      SmartDashboard.putNumber("Green", detectedColor.green);
      SmartDashboard.putNumber("Blue", detectedColor.blue);
      SmartDashboard.putNumber("Confidence", match.confidence);
      SmartDashboard.putString("Detected Color", colorString);
      SmartDashboard.putNumber("ColorSRXOutput", getColorSpinnerOutPut());
      SmartDashboard.putString("The requiredcolor for the match", requiredColor);
    }

  private double getColorSpinnerOutPut() {
    return colorSpinner.getMotorOutputPercent();
  }

  private void setColorSpinner(double input) {
    colorSpinner.set(ControlMode.PercentOutput,input);
  }
    
}

