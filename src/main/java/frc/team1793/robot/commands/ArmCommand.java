package frc.team1793.robot.commands;

import frc.team1793.robot.components.Arm;
import org.strongback.command.Command;
import org.strongback.components.ui.ContinuousRange;

public class ArmCommand extends Command {

    public static final int TIMEOUT_IN_SECONDS = 3;
    private Arm arm;
    private double wristAngle;
    private double shoulderAngle;
    private ContinuousRange shoulderSpeed;
    private ContinuousRange wristSpeed;

    public ArmCommand(Arm arm, double shoulderAngle, double wristAngle, ContinuousRange shoulderSpeed, ContinuousRange wristSpeed) {
        super(TIMEOUT_IN_SECONDS);
        this.arm = arm;
        this.wristAngle = wristAngle;
        this.shoulderAngle = shoulderAngle;
        this.shoulderSpeed = shoulderSpeed;
        this.wristSpeed = wristSpeed;
    }

    @Override
    public boolean execute() {
        arm.runShoulder(shoulderSpeed, shoulderAngle);
        arm.runWrist(wristSpeed, wristAngle);
        return true;
    }
}
