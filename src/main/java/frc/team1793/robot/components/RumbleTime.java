package frc.team1793.robot.components;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import org.strongback.components.ui.Gamepad;

public class RumbleTime {
    private static final double MAX_TIME = 150;
    private Gamepad gamepad;

    public RumbleTime(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    public void periodic() {
        double progress = (DriverStation.getInstance().getMatchTime() / MAX_TIME);
        if(progress > 0.5) {
            gamepad.setRumble(GenericHID.RumbleType.kRightRumble, progress);
        } else {
            gamepad.setRumble(GenericHID.RumbleType.kLeftRumble, progress);
        }
    }
}
