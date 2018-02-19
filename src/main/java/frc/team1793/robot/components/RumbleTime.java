package frc.team1793.robot.components;

import edu.wpi.first.wpilibj.DriverStation;
import org.strongback.components.ui.Gamepad;

public class RumbleTime {
    private static final double MAX_TIME = 150;
    private Gamepad gamepad;

    public RumbleTime(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    public void periodic() {
        double progress = (DriverStation.getInstance().getMatchTime() / MAX_TIME) * 2;
        gamepad.setRumble(progress <= MAX_TIME / 2 ? progress : 0, progress > MAX_TIME / 2 ? progress : 0);
    }
}
