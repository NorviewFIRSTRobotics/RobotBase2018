package frc.team1793.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1793.robot.commands.*;
import frc.team1793.robot.components.*;
import frc.team1793.robot.no.DriverCamera;
import frc.team1793.robot.util.EnumAuto;
import frc.team1793.robot.util.SwitchToggle;
import org.strongback.Strongback;
import org.strongback.SwitchReactor;
import org.strongback.command.CommandGroup;
import org.strongback.components.*;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.components.ui.FlightStick;
import org.strongback.components.ui.Gamepad;
import org.strongback.drive.TankDrive;
import org.strongback.hardware.Hardware;
import org.strongback.util.Values;

public class Robot extends IterativeRobot {

    public static TankDrive drive;
    private ContinuousRange driveSpeed, turnSpeed, shoulderSpeed, wristSpeed;

    private EnumAuto startPos;
    private GyroSet gyro;
    public static Solenoid grabber;
//    private Solenoid scissorLift;
    private DriverCamera camera;
    private Arm arm;
    private Gamepad armController;
    private AngleSensor shoulderSensor;
    private AngleSensor wristSensor;
    private Gamepad driveController;
    private static double wristGoal;
    private static boolean shoulderMoving;

    public static final double SHOULDER_DOWN = 20;
    public static final double SHOULDER_UP = 53;
    public static final double WRIST_STRAIGHT = 30;
    public static final double WRIST_DOWN = 12;
    public static final double WRIST_STORED = 110;


    @Override
    public void robotInit() {

        //drive
        Motor left = Motor.compose(Hardware.Motors.talon(0), Hardware.Motors.talon(1)).invert();
        Motor right = Motor.compose(Hardware.Motors.talon(2), Hardware.Motors.talon(3));
        SpeedController shoulder = Hardware.Motors.spark(5);
        SpeedController wrist = Hardware.Motors.spark(4);
        drive = new TankDrive(left, right);

        //pneumatics
        grabber = Hardware.Solenoids.doubleSolenoid(4, 5, Solenoid.Direction.STOPPED);
//        scissorLift = Hardware.Solenoids.doubleSolenoid(5, 4, Solenoid.Direction.STOPPED);

        //analog
//        gyro = new GyroSet(Hardware.AngleSensors.gyroscope(8), Hardware.AngleSensors.gyroscope(1));
//        gyro.zero();
        shoulderSensor = Hardware.AngleSensors.potentiometer(2,135);
        wristSensor = Hardware.AngleSensors.potentiometer(0,220);
        arm = new Arm(grabber, shoulderSensor, wristSensor, shoulder, wrist);

        initControls();
    }


    @Override
    public void robotPeriodic() {
        pushToDashboard();
        RumbleTime.getInstance().periodic(armController);
        startPos = EnumAuto.valueOf(SmartDashboard.getString("autonomous", "SWITCH").toUpperCase());
    }

    @Override
    public void autonomousInit() {
        Strongback.disable();
        Strongback.start();
        Strongback.submit(new SolenoidExtendCommand(grabber));
        Strongback.submit(startPos.getCommand());
    }

    @Override
    public void autonomousPeriodic() {
        arm.runWrist(() -> 0.3);
    }

    @Override
    public void teleopInit() {
        // Kill anything if it is ...
        Strongback.disable();
        // Start Strongback functions ...
        Strongback.start();
    }

    @Override
    public void teleopPeriodic() {
        drive.arcade(driveSpeed.read(), turnSpeed.read());
//        arm.runShoulder(shoulderSpeed);
//        arm.runWrist(wristSpeed);
        if(!shoulderMoving){
            arm.runWrist(wristSpeed);
        }
//        if(shoulderSensor.getAngle() > 50){
//            arm.runShoulder(() -> 0.3);
//        }
    }

    @Override
    public void disabledInit() {
        // Tell Strongback that the robot is disabled so it can flush and kill commands.
        Strongback.disable();
    }

    private void initControls() {
        driveController = Hardware.HumanInterfaceDevices.xbox360(0);
        armController = Hardware.HumanInterfaceDevices.xbox360(1);
        SwitchReactor switchReactor = Strongback.switchReactor();
        driveSpeed = driveController.getLeftY().scale(0.7);
        turnSpeed = driveController.getLeftX();

//        shoulderSpeed = armController.getRightY().scale(0.75);
        wristSpeed = armController.getLeftY().scale(0.8);/*.map(a -> {
            if(Values.fuzzyCompare(a,0,0.25) == 0){
                return 0.6;
            }
            else {
                return a;
            }
        });*/
        double w = 0.65, su = 0.5, sd = 0.25;
        //TODO get potentiometer angles!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! please
        switchReactor.onTriggered(armController.getRightBumper(), () -> Strongback.submit(new ShoulderDownCommand(arm, shoulderSensor::getAngle, wristSensor::getAngle, () -> -sd, () -> w)));
        switchReactor.onTriggered(armController.getRightStick(), () -> Strongback.submit(new ShoulderUpCommand(arm, shoulderSensor::getAngle, wristSensor::getAngle, () -> su, () -> w)));

//        switchReactor.onTriggered(armController.getLeftStick(), new SwitchToggle(new SolenoidExtendCommand(scissorLift), new SolenoidRetractCommand(scissorLift))::execute);
        switchReactor.onTriggered(armController.getLeftBumper(), new SwitchToggle(new SolenoidExtendCommand(grabber), new SolenoidRetractCommand(grabber))::execute);
    }
    private static Switch toSwitch(ContinuousRange range) {
        return () -> range.read() > 0.5;
    }

    public static void setWristGoal(double newGoal) {
        wristGoal = newGoal;
    }

    public static void setArmMovement(boolean armMoving){
        shoulderMoving = armMoving;
    }

    private void pushToDashboard() {
//        SmartDashboard.putNumber("angle", gyro.getAngle());
        SmartDashboard.putString("grabberDirection", grabber.getDirection().name());
//        SmartDashboard.putString("scissorLiftDirection", scissorLift.getDirection().name());
        SmartDashboard.putNumber("shoulderAngle", shoulderSensor.getAngle());
        SmartDashboard.putNumber("wristAngle", wristSensor.getAngle());
        SmartDashboard.putNumber("driveSpeed",driveSpeed.read());
        SmartDashboard.putNumber("turnSpeed",turnSpeed.read());
    }

}