package frc.robot;

public class Auton_GSC {
    public AutonSteps[] steps; 
    
    // TODO @Someone find how far each rotation can take up plug in the claue
    private final double rotation = 2; // value should be feet | meters
    
    public Auton_GSC(Alliance path){
        switch(path){
            case RED_A:
                // TODO @Jake double check that this works
                // starts at 
                // B1
                // x = 2'6" | 30.00
                // y = 7'6" | 90.00   
                steps = new AutonSteps[]{
                    new AutonSteps( point_to_rotation(2), 90.0),
                    new AutonSteps( point_to_rotation(2), -90.0),
                    new AutonSteps( point_to_rotation(2), -90.0),
                    new AutonSteps( point_to_rotation(3), 90.0),
                    new AutonSteps( point_to_rotation(9))
                };
            case BLUE_A:
                // starts at 
                // D1
                // x = 2'6" | 30.00
                // y = 5'0" | 60.00  
                steps = new AutonSteps[]{
                    new AutonSteps( point_to_rotation(1), 90.0),
                    new AutonSteps( point_to_rotation(1), -90.0),
                    new AutonSteps( point_to_rotation(5), -90.0),
                    new AutonSteps( point_to_rotation(4)),
                    new AutonSteps( point_to_rotation(-2), 90), // TODO Test this
                    new AutonSteps (point_to_rotation(5))
                };
                
            case RED_B:
                // starts at 
                // B1
                steps = new AutonSteps[]{
                    new AutonSteps( point_to_rotation(2), 45.0),
                    new AutonSteps( point_to_rotation(7.071067811865475), -90.0),
                    new AutonSteps( point_to_rotation(7.071067811865475 + 2), 45.0),
                    new AutonSteps ( point_to_rotation(7))
                };
            case BLUE_B:
                // starts at
                // D1
                steps = new AutonSteps[]{
                    new AutonSteps( point_to_rotation(2), 45.0),
                    new AutonSteps( point_to_rotation(7.071067811865475), -90.0),
                    new AutonSteps( point_to_rotation(7.071067811865475), 90.0),
                    new AutonSteps( point_to_rotation(7.071067811865475), -90.0),
                    new AutonSteps( point_to_rotation(5), 45.0),
                };
        }
    }

    private double point_to_rotation(double point){
        return (point * rotation);
    }
}
