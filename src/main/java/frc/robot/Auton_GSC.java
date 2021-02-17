package frc.robot;

public class Auton_GSC {
    public AutonSteps[] steps; 
    
    // TODO @Logan find how far each rotation can take up plug in the claue
    private final double rotation = 2; // value should be feet | meters
    
    public Auton_GSC(Alliance path){

        switch(path){
            case RED_A:
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
                // 2, 90 
        }


    }

    private double point_to_rotation(int point){
        return point * rotation;
    }
}
