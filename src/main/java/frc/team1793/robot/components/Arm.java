package frc.team1793.robot.components;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.strongback.components.AngleSensor;
import org.strongback.components.Solenoid;
import org.strongback.components.SpeedController;
import org.strongback.components.ui.ContinuousRange;

public class Arm {
    private Solenoid grabber;
    private AngleSensor shoulderAngle;
    private LimitSwitch wristSwitch;
    private SpeedController shoulder;
    private SpeedController wrist;
    private static final double DELTA_ERROR = 20;

    public Arm(Solenoid grabber, AngleSensor shoulderAngle, LimitSwitch wristSwitch, SpeedController shoulder, SpeedController wrist) {
        this.grabber = grabber;
        this.shoulderAngle = shoulderAngle;
        this.wristSwitch = wristSwitch;
        this.shoulder = shoulder;
        this.wrist = wrist;

    }

    public void runShoulder(ContinuousRange speed) {
        shoulder.setSpeed(speed.read());
    }

    public void runWrist(ContinuousRange speed) {
        wrist.setSpeed(speed.read());
        SmartDashboard.putNumber("wristSpeed", speed.read());
    }
}
