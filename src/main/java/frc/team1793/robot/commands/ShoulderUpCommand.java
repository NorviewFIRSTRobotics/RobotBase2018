package frc.team1793.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import frc.team1793.robot.Robot;
import frc.team1793.robot.components.Arm;
import org.strongback.command.Command;
import org.strongback.components.ui.ContinuousRange;

import java.util.function.Supplier;

public class ShoulderUpCommand extends Command {
    private Arm arm;
    private Supplier<Double> shoulderAngle;
    private Supplier<Boolean> wristSwitch;
    private ContinuousRange shoulderSpeed;
    private ContinuousRange wristSpeed;


    public ShoulderUpCommand(Arm arm, Supplier<Double> shoulderAngle, Supplier<Boolean> wristSwitch, ContinuousRange shoulderSpeed, ContinuousRange wristSpeed) {
        this.arm = arm;
        this.shoulderAngle = shoulderAngle;
        this.wristSwitch = wristSwitch;
        this.shoulderSpeed = shoulderSpeed;
        this.wristSpeed = wristSpeed;
    }

    @Override
    public boolean execute() {
        Robot.setShoulderMovement(true);
        while (shoulderAngle.get() < Robot.SHOULDER_UP) {
            if (DriverStation.getInstance().isDisabled()) {
                return true;
            }
            System.out.printf("Shoulder Angle %s\n", shoulderAngle);
            System.out.printf("Wrist Angle %s\n", wristSwitch);
            while (!wristSwitch.get()) {
                if (DriverStation.getInstance().isDisabled()) {
                    return true;
                }
                System.out.printf("Wrist Angle %s\n", wristSwitch);
                arm.runWrist(wristSpeed);
            }
            arm.runShoulder(shoulderSpeed);
        }
        Robot.setShoulderMovement(false);
        return true;
    }
}
