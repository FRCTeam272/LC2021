/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class LimeLight {

    private NetworkTable m_table;

    public LimeLight(){
        m_table = NetworkTableInstance.getDefault().getTable("limelight");
    }

    //tv - Whether the limelight has any valid targets (0 or 1)
    public boolean getValidTargetFound(){
        NetworkTableEntry tv = m_table.getEntry("tv");
        double validTarget = tv.getDouble(0);
        if (validTarget == 0.0f){
            return false;
        }else {
            return true;
        }
    }

     //tx - Horizontal Offset From Crosshair To Target (-29.8 to 29.8 degrees)
    public double getHorizontialOffset() {
        NetworkTableEntry tx = m_table.getEntry("tx");
        double horizontialOffset = tx.getDouble(0.0);
        SmartDashboard.putNumber("Limelight horizontial angle", horizontialOffset);
        return horizontialOffset;
    }

    //ty - Vertical Offset From Crosshair To Target (-24.85 to 24.85 degrees)
    public double getdegVerticalOffset() {
        NetworkTableEntry ty = m_table.getEntry("ty");
        double verticleOffset = ty.getDouble(0.0);
        return verticleOffset;
    }

    //ta - Target Area (0% of image to 100% of image)
    public double getTargetArea() {
        NetworkTableEntry ta = m_table.getEntry("ta");
        double targetArea = ta.getDouble(0.0);
        return targetArea;
    }

    //ts - Skew or rotation (-90 degrees to 0 degrees)
    public double getSkewRotation() {
        
        NetworkTableEntry ts = m_table.getEntry("ts");
        double skewRotation = ts.getDouble(0.0);
        SmartDashboard.putNumber("Limelight skew", skewRotation);
        return skewRotation;
    }
    
    //tl - The pipeline’s latency contribution (ms) Add at least 11ms for image capture latency.
    public double getPipelineLatency() {

        NetworkTableEntry tl = m_table.getEntry("tl");
        double latency = tl.getDouble(0.0);
        return latency;
    }

    // private void resetPilelineLatency(){
    //     m_table.getEntry("tl").setValue(0.0);
    // }

     /**
        0	use the LED Mode set in the current pipeline
        1	force off
        2	force blink
        3	force on 
    */
     public void setLEDMode(Integer ledMode) {
        m_table.getEntry("ledMode").setValue(ledMode);
    }

    public double getLEDMode() {
        NetworkTableEntry ledMode = m_table.getEntry("ledMode");
        double mode = ledMode.getDouble(0.0);
        return mode;
    }
    
    /** 
        0	Vision processor
        1	Driver Camera (Increases exposure, disables vision processing)
    */
    public void setCamMode(Integer camMode) {
        m_table.getEntry("camMode").setValue(camMode);
    }

    
    public double getCamMode() {
        NetworkTableEntry camMode = m_table.getEntry("camMode");
        double cam = camMode.getDouble(0.0);
        return cam;
    }

    /** 
        0 - 9	Select pipeline 0.9
    */
    public void setPipeline(Integer pipeline) {
        if(pipeline<0){
            pipeline = 0;
            throw new IllegalArgumentException("Pipeline can not be less than zero");
        }else if(pipeline>9){
            pipeline = 9;
            throw new IllegalArgumentException("Pipeline can not be greater than nine");
        }
        m_table.getEntry("pipeline").setValue(pipeline);
    }


    public double getPipeline(){
        NetworkTableEntry pipeline = m_table.getEntry("pipeline");
        double pipe = pipeline.getDouble(0.0);
        return pipe;
    }

    
    public Integer getPipelineInt(){
        NetworkTableEntry pipeline = m_table.getEntry("pipeline");
        Integer pipe = (int) pipeline.getDouble(0.0);
        return pipe;
    }

    /**
     stream	Sets limelight’s streaming mode
    0	Standard - Side-by-side streams if a webcam is attached to Limelight
    1	PiP Main - The secondary camera stream is placed in the lower-right corner of the primary camera stream
    2	PiP Secondary - The primary camera stream is placed in the lower-right corner of the secondary camera stream
     */ 
    public void setStream(Integer stream) {
        m_table.getEntry("stream").setValue(stream);
    }

    public double getStream() {
        NetworkTableEntry stream = m_table.getEntry("stream");
        double streamType = stream.getDouble(0.0);
        return streamType;
    }

    /**
    snapshot	Allows users to take snapshots during a match
    0	Stop taking snapshots
    1	Take two snapshots per second
    */

    public void setSnapshot(Integer snapshot) {
        m_table.getEntry("snapshot").setValue(snapshot);
    }

    public double getSnapshot() {
        NetworkTableEntry snapshot = m_table.getEntry("snapshot");
        double snshot = snapshot.getDouble(0.0);     
        return snshot;
    }

    public double getDistance( double mountHeight, double mountAngle) {
        //d = (h2-h1) / tan(a1+a2) - assuming the mount is at a fixed angle 
        // mountHeight is in inches and mountAngle is in degrees

        NetworkTableEntry ty = m_table.getEntry("ty");
        double verticleOffset = ty.getDouble(0.0);

        double distance = (98.5 - mountHeight) / Math.tan(Math.toRadians(verticleOffset + mountAngle));
        SmartDashboard.putNumber("Limelight Distance", distance);
        SmartDashboard.putNumber("Limelight verticle angle", verticleOffset);
        return distance;
    }

}
