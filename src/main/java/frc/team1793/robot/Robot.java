package frc.team1793.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1793.robot.commands.SolenoidExtendCommand;
import frc.team1793.robot.commands.SolenoidRetractCommand;
import frc.team1793.robot.components.Arm;
import frc.team1793.robot.components.GyroSet;
import frc.team1793.robot.components.RumbleTime;
import frc.team1793.robot.util.EnumAuto;
import frc.team1793.robot.util.SwitchToggle;
import org.strongback.Strongback;
import org.strongback.SwitchReactor;
import org.strongback.components.AngleSensor;
import org.strongback.components.Motor;
import org.strongback.components.Solenoid;
import org.strongback.components.SpeedController;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.components.ui.FlightStick;
import org.strongback.components.ui.Gamepad;
import org.strongback.drive.TankDrive;
import org.strongback.hardware.Hardware;

public class Robot extends IterativeRobot {

    public static TankDrive drive;
    private ContinuousRange driveSpeed, turnSpeed, shoulderSpeed, wristSpeed;

    private EnumAuto startPos;
    private GyroSet gyro;
    private Solenoid grabber;
    private Solenoid scissorLift;
    private Arm arm;
    private Gamepad armController;
    private AngleSensor shoulderSensor;
    private AngleSensor wristSensor;

    @Override
    public void robotInit() {
        //drive
        Motor left = Motor.compose(Hardware.Motors.talon(0), Hardware.Motors.talon(1)).invert();
        Motor right = Motor.compose(Hardware.Motors.talon(2), Hardware.Motors.talon(3));
        SpeedController shoulder = Hardware.Motors.spark(5);
        SpeedController wrist = Hardware.Motors.spark(4);
        drive = new TankDrive(left, right);

        //pneumatics
        grabber = Hardware.Solenoids.doubleSolenoid(0, 1, Solenoid.Direction.STOPPED);
        scissorLift = Hardware.Solenoids.doubleSolenoid(5, 4, Solenoid.Direction.STOPPED);

        //analog
        gyro = new GyroSet(Hardware.AngleSensors.gyroscope(0), Hardware.AngleSensors.gyroscope(1));
        gyro.zero();
        shoulderSensor = Hardware.AngleSensors.potentiometer(2, 1);
        wristSensor = Hardware.AngleSensors.potentiometer(3, 1);
        arm = new Arm(grabber, shoulderSensor, wristSensor, shoulder, wrist);

        //TODO initialize with dashboard
        startPos = EnumAuto.LEFT;

        initControls();
    }


    @Override
    public void robotPeriodic() {
        pushToDashboard();
        RumbleTime.getInstance().periodic(armController);
    }

    @Override
    public void autonomousInit() {
        Strongback.disable();
        Strongback.start();
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        // Kill anything if it is ...
        Strongback.disable();
        // Start Strongback functions ...
        Strongback.start();

        //guarantee retracted
        Strongback.submit(new SolenoidRetractCommand(scissorLift));
    }

    @Override
    public void teleopPeriodic() {
        drive.arcade(driveSpeed.read(), turnSpeed.read());
        arm.runShoulder(shoulderSpeed);
        arm.runWrist(wristSpeed);
    }

    @Override
    public void disabledInit() {
        // Tell Strongback that the robot is disabled so it can flush and kill commands.
        Strongback.disable();
    }

    private void initControls() {
        FlightStick driveController = Hardware.HumanInterfaceDevices.microsoftSideWinder(0);
        armController = Hardware.HumanInterfaceDevices.xbox360(1);
        SwitchReactor switchReactor = Strongback.switchReactor();

        driveSpeed = driveController.getPitch();
        turnSpeed = driveController.getYaw();

        shoulderSpeed = armController.getRightY().scale(0.75);
        wristSpeed = armController.getLeftY();

        switchReactor.onTriggered(armController.getLeftStick(), new SwitchToggle(new SolenoidExtendCommand(scissorLift), new SolenoidRetractCommand(scissorLift))::execute);
        switchReactor.onTriggered(armController.getLeftBumper(), new SwitchToggle(new SolenoidExtendCommand(grabber), new SolenoidRetractCommand(grabber))::execute);


        //TODO arm states on face buttons


    }

    private void pushToDashboard() {
        SmartDashboard.putNumber("angle", gyro.getAngle());
        SmartDashboard.putString("grabberDirection", grabber.getDirection().name());
        SmartDashboard.putString("scissorLiftDirection", scissorLift.getDirection().name());
        SmartDashboard.putNumber("shoulderAngle", shoulderSensor.getAngle());
        SmartDashboard.putNumber("wristAngle", wristSensor.getAngle());
        SmartDashboard.putNumber("driveSpeed", driveSpeed.read());
        SmartDashboard.putNumber("turnSpeed", turnSpeed.read());
    }

}