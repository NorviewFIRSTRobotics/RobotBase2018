package frc.team1793.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1793.robot.commands.*;
import frc.team1793.robot.components.Arm;
import frc.team1793.robot.components.LimitSwitch;
import frc.team1793.robot.components.RumbleTime;
import frc.team1793.robot.util.EnumAuto;
import frc.team1793.robot.util.SwitchToggle;
import org.strongback.Strongback;
import org.strongback.SwitchReactor;
import org.strongback.components.*;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.components.ui.FlightStick;
import org.strongback.components.ui.Gamepad;
import org.strongback.drive.TankDrive;
import org.strongback.hardware.Hardware;

public class Robot extends IterativeRobot {

    public static Arm arm;
    private Gamepad armController;
    public static TankDrive drive;
    private FlightStick driveController;
    private ContinuousRange driveSpeed;
    public static Solenoid grabber;
    private SpeedController ramp;
    private static boolean shoulderMoving;
    private AngleSensor shoulderSensor;
    private ContinuousRange shoulderSpeed;
    private EnumAuto startPos;
    private ContinuousRange turnSpeed;
    private static boolean wristMoving;
    private ContinuousRange wristSpeed;
    private LimitSwitch wristSwitch;

    public static final double SHOULDER_DOWN = 20;
    public static final double SHOULDER_UP = 53;
    public static final double WRIST_DOWN = 12;
    public static final double WRIST_STRAIGHT = 30;
    public static final double WRIST_STORED = 110;


    @Override
    public void robotInit() {

        //drive
        Motor left = Motor.compose(Hardware.Motors.talon(0), Hardware.Motors.talon(1)).invert();
        Motor right = Motor.compose(Hardware.Motors.talon(2), Hardware.Motors.talon(3));
        SpeedController shoulder = Hardware.Motors.spark(5);
        SpeedController wrist = Hardware.Motors.spark(4);
        drive = new TankDrive(left, right);
        ramp = Hardware.Motors.spark(6);

        //pneumatics
        grabber = Hardware.Solenoids.doubleSolenoid(4, 5, Solenoid.Direction.STOPPED);

        //sensors
        shoulderSensor = Hardware.AngleSensors.potentiometer(2, 135);
        wristSwitch = new LimitSwitch(0);
        arm = new Arm(grabber, shoulderSensor, wristSwitch, shoulder, wrist);

        CameraServer.getInstance().startAutomaticCapture();

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
        if (!wristMoving) {
            arm.runWrist(() -> 0.3);
        }
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
        if (!shoulderMoving) {
            arm.runWrist(wristSpeed);
        }
    }

    @Override
    public void disabledInit() {
        // Tell Strongback that the robot is disabled so it can flush and kill commands.
        Strongback.disable();
    }

    private void initControls() {
        driveController = Hardware.HumanInterfaceDevices.logitechAttack3D(0);
        armController = Hardware.HumanInterfaceDevices.xbox360(1);
        SwitchReactor switchReactor = Strongback.switchReactor();

        driveSpeed = driveController.getPitch().scale(0.7);
        turnSpeed = driveController.getYaw();
        wristSpeed = armController.getLeftY().scale(0.8);

        double w = 0.65, su = 0.5, sd = 0.25;
        //TODO get potentiometer angles!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! please
        switchReactor.onTriggered(armController.getRightBumper(), () -> Strongback.submit(new ShoulderDownCommand(arm, shoulderSensor::getAngle, wristSwitch::isTriggered, () -> -sd, () -> w)));
        switchReactor.onTriggered(armController.getRightStick(), () -> Strongback.submit(new ShoulderUpCommand(arm, shoulderSensor::getAngle, wristSwitch::isTriggered, () -> su, () -> w)));
        switchReactor.onTriggered(armController.getStart(), () -> Strongback.submit(new RunRampCommand(ramp, 0.5, () -> 0.5)));
        switchReactor.onTriggered(armController.getLeftBumper(), new SwitchToggle(new SolenoidExtendCommand(grabber), new SolenoidRetractCommand(grabber))::execute);
    }

    private static Switch toSwitch(ContinuousRange range) {
        return () -> range.read() > 0.5;
    }

    public static void setShoulderMovement(boolean armMoving) {
        shoulderMoving = armMoving;
    }

    public static void setWristMovement(boolean armMoving) {
        wristMoving = armMoving;
    }

    private void pushToDashboard() {

        SmartDashboard.putString("grabberDirection", grabber.getDirection().name());
        SmartDashboard.putNumber("shoulderAngle", shoulderSensor.getAngle());
        SmartDashboard.putString("wristStored", Boolean.toString(wristSwitch.isTriggered()));
        SmartDashboard.putNumber("driveSpeed", driveSpeed.read());
        SmartDashboard.putNumber("turnSpeed", turnSpeed.read());
    }

}