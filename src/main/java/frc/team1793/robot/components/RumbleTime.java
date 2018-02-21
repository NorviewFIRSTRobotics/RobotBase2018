package frc.team1793.robot.components;

import edu.wpi.first.wpilibj.DriverStation;
import org.strongback.components.ui.Gamepad;

public class RumbleTime {
    private static RumbleTime INSTANCE;

    public static RumbleTime getInstance() {
        if(INSTANCE == null)
            INSTANCE = new RumbleTime();
        return INSTANCE;
    }
    private static final double MAX_TIME = 150;

    public void periodic(Gamepad gamepad) {
        double progress = (DriverStation.getInstance().getMatchTime() / MAX_TIME);
        gamepad.setRumble(progress <= MAX_TIME / 2 ? progress : 0, progress > MAX_TIME / 2 ? progress : 0);
    }
}
