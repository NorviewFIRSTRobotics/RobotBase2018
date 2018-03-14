package frc.team1793.robot.commands;

import frc.team1793.robot.Robot;
import frc.team1793.robot.components.Arm;
import org.strongback.command.Command;
import org.strongback.components.ui.ContinuousRange;

public class WristMoveCommand extends Command {
    private Arm arm;
    private ContinuousRange wristSpeed;

    public WristMoveCommand(Arm arm, double duration, ContinuousRange wristSpeed, boolean override) {
        super(duration);
        this.arm = arm;
        this.wristSpeed = wristSpeed;
        Robot.setWristMovement(override);
    }

    public WristMoveCommand(Arm arm, double duration, ContinuousRange wristSpeed) {
        this(arm, duration, wristSpeed, false);
    }

    @Override
    public boolean execute() {
        arm.runWrist(wristSpeed);
        return true;
    }
}
