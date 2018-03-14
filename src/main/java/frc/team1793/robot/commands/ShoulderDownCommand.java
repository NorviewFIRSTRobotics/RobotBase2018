package frc.team1793.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import frc.team1793.robot.Robot;
import frc.team1793.robot.components.Arm;
import org.strongback.command.Command;
import org.strongback.command.Requirable;
import org.strongback.components.ui.ContinuousRange;

import java.util.function.Supplier;

public class ShoulderDownCommand extends Command {
    private Arm arm;
    private Supplier<Double> shoulderAngle;
    private Supplier<Double> wristAngle;
    private ContinuousRange shoulderSpeed;
    private ContinuousRange wristSpeed;
    private double wristGoal;

    public ShoulderDownCommand(Arm arm, Supplier<Double> shoulderAngle, Supplier<Double> wristAngle, ContinuousRange shoulderSpeed, ContinuousRange wristSpeed) {
        this.arm = arm;
        this.shoulderAngle = shoulderAngle;
        this.wristAngle = wristAngle;
        this.shoulderSpeed = shoulderSpeed;
        this.wristSpeed = wristSpeed;
        this.wristGoal = wristGoal;
    }

    @Override
    public boolean execute(){
        Robot.setArmMovement(true);
        while(shoulderAngle.get() > Robot.SHOULDER_DOWN){
            if(DriverStation.getInstance().isDisabled()){
                return true;
            }
            System.out.printf("Shoulder Angle %s\n",shoulderAngle);
            while(wristAngle.get() < Robot.WRIST_STORED){
                if(DriverStation.getInstance().isDisabled()){
                    return true;
                }
                System.out.printf("Wrist Angle %s\n",wristAngle);
                arm.runWrist(wristSpeed);
            }
            arm.runShoulder(shoulderSpeed);
        }
//        Robot.setWristGoal(wristGoal);
        Robot.setArmMovement(false);
        return true;
    }
}
