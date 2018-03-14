package frc.team1793.robot.util;

import frc.team1793.robot.Robot;
import frc.team1793.robot.commands.SolenoidRetractCommand;
import frc.team1793.robot.commands.TimedDriveCommand;
import frc.team1793.robot.commands.WristMoveCommand;
import openrio.powerup.MatchData;
import org.strongback.command.Command;
import org.strongback.command.CommandGroup;
import org.strongback.drive.TankDrive;

import java.util.function.Supplier;

import static openrio.powerup.MatchData.OwnedSide.LEFT;

public class SwitchAuto {


    public static final double DURATION = 1.25;
    public static final double SPEED = 0.7;
    public static final double TURN_SPEED = 0.7;

    public static Command getTurnCommand(MatchData.OwnedSide ownedSide, TankDrive drive) {
        return CommandGroup.runSequentially(
                new TimedDriveCommand(drive, DURATION, SPEED, ownedSide == LEFT ? -TURN_SPEED : TURN_SPEED),
                new TimedDriveCommand(drive, 0.25, 0.3, 0),
                new TimedDriveCommand(drive, DURATION - 0.15, SPEED, ownedSide == LEFT ? TURN_SPEED : -TURN_SPEED)
        );
    }

    public static Supplier<Command> doSwitch() {
        return () -> {
            MatchData.OwnedSide side = LEFT; //MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);
            return CommandGroup.runSequentially(
                    getTurnCommand(side, Robot.drive),
                    new TimedDriveCommand(Robot.drive, 2.0, 0.3, 0),
                    new WristMoveCommand(Robot.arm, 0.3, () -> -0.3, true),
                    new SolenoidRetractCommand(Robot.grabber),
                    new WristMoveCommand(Robot.arm, 0.3, () -> 0.3)
            );
        };
    }
}
