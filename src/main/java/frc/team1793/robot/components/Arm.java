package frc.team1793.robot.components;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.strongback.components.AngleSensor;
import org.strongback.components.Solenoid;
import org.strongback.components.SpeedController;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.hardware.Hardware;

public class Arm {
    private Solenoid grabber;
    private AngleSensor shoulderAngle;
    private AngleSensor wristAngle;
    private SpeedController shoulder;
    private SpeedController wrist;
    private static final double DELTA_ERROR = 20;

    public Arm(Solenoid grabber, AngleSensor shoulderAngle, AngleSensor wristAngle, SpeedController shoulder, SpeedController wrist) {
        this.grabber = grabber;
        this.shoulderAngle = shoulderAngle;
        this.wristAngle = wristAngle;
        this.shoulder = shoulder;
        this.wrist = wrist;

    }

    public void periodic(){
        SmartDashboard.putNumber("shoulder angle", shoulderAngle.getAngle());
        SmartDashboard.putNumber("wrist angle", wristAngle.getAngle());
    }

    public void runShoulder(ContinuousRange speed, double angle){
        int range = isOutOfRange(shoulderAngle.getAngle(), angle);
        while(range != 0){
            shoulder.setSpeed(speed.scale(range).read());
        }
    }

    public void runWrist(ContinuousRange speed, double angle){
        int range = isOutOfRange(wristAngle.getAngle(), angle);
        while(range != 0){
            wrist.setSpeed(speed.scale(range).read());
        }
    }

    public int isOutOfRange(double angle1, double angle2){
         if(angle1 > angle2 + DELTA_ERROR || angle1 > angle2 + DELTA_ERROR){
            return -1;
         } else if (angle1 < angle2 + DELTA_ERROR || angle1 < angle2 + DELTA_ERROR){
             return 1;
         } else {
             return 0;
         }
    }
}
