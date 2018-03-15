package frc.team1793.robot.commands;

import org.strongback.command.Command;
import org.strongback.components.SpeedController;
import org.strongback.components.ui.ContinuousRange;

public class RunRampCommand extends Command {

    private SpeedController ramp;
    private ContinuousRange speed;

    public RunRampCommand(SpeedController ramp, ContinuousRange speed) {
        this.ramp = ramp;
        this.speed = speed;
    }

    @Override
    public boolean execute() {
        ramp.setSpeed(speed.read());
        return false;
    }
}
