package frc.team1793.robot.util;

import frc.team1793.robot.Robot;
import frc.team1793.robot.commands.TimedDriveCommand;
import org.strongback.command.Command;
import org.strongback.command.CommandGroup;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 3/27/17
 */

@SuppressWarnings("unused")

public enum EnumAuto {

    //TODO implement arm stuff
    FORWARD(drive(3.5, Constants.SPEED, 0)),
    SWITCH(SwitchAuto.doSwitch());

    private Supplier<Command> command;
    private List<Supplier<Command>> commands;

    EnumAuto(Supplier<Command> command) {
        this.command = command;
    }

    @SafeVarargs
    EnumAuto(Supplier<Command>... commands) {
        this.commands = Arrays.asList(commands);
    }

    public Command getCommand() {
        if (commands != null) {
            Command[] array = commands.stream().map(Supplier::get).toArray(Command[]::new);
            for (Command command : array) {
                System.out.println(command);
            }
            return CommandGroup.runSequentially(array);
        } else {
            return command.get();
        }
    }

    public static EnumAuto fromString(String str) {
        return valueOf(str.toUpperCase());
    }

    public static Supplier<Command> drive(double time, double speed, double turn) {
        return () -> new TimedDriveCommand(Robot.drive, time, speed, turn);
    }

    private static class Constants {
        public static final double SPEED = .3;
    }
}
