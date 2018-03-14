package frc.team1793.robot.commands;

import frc.team1793.robot.components.Arm;
import org.strongback.command.Command;
import org.strongback.components.ui.ContinuousRange;

public class WristMoveCommand extends Command {
    private Arm arm;
    private double wristAngle;
    private ContinuousRange wristSpeed;
    private double desiredAngle;

    public WristMoveCommand(Arm arm, double wristAngle, ContinuousRange wristSpeed, double desiredAngle) {
        this.arm = arm;
        this.wristAngle = wristAngle;
        this.wristSpeed = wristSpeed;
        this.desiredAngle = desiredAngle;
    }

    @Override
    public boolean execute(){
        while(difference() != 0){
            arm.runWrist(wristSpeed.scale(difference()));
        }
        return true;
    }

    private int difference(){
        if(wristAngle > desiredAngle){
            return -1;
        } else if(wristAngle < desiredAngle){
            return 1;
        } else {
            return 0;
        }
    }
}
