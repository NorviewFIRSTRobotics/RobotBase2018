package frc.team1793.robot.commands;

import org.strongback.command.Command;
import org.strongback.components.SpeedController;
import org.strongback.components.ui.ContinuousRange;

public class RunRampCommand extends Command {

    private SpeedController ramp;
    private ContinuousRange speed;

    public RunRampCommand(SpeedController ramp, double duration, ContinuousRange speed) {
        super(duration);
        this.ramp = ramp;
        this.speed = speed;
    }

    @Override
    public boolean execute() {
        ramp.setSpeed(speed.read());
        return true;
    }
}
