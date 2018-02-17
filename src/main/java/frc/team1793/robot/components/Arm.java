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

    public void runShoulder(ContinuousRange speed){
        shoulder.setSpeed(speed.read());
    }

    public void runWrist(ContinuousRange speed){
        wrist.setSpeed(speed.read());
    }
}
