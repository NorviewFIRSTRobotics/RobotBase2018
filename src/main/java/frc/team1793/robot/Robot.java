package frc.team1793.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1793.robot.commands.SolenoidExtendCommand;
import frc.team1793.robot.commands.SolenoidRetractCommand;
import frc.team1793.robot.components.Arm;
import frc.team1793.robot.components.GamepadRumble;
import frc.team1793.robot.components.GyroSet;
import frc.team1793.robot.components.SolenoidSet;
import frc.team1793.robot.no.DriverCamera;
import frc.team1793.robot.util.EnumAuto;
import frc.team1793.robot.util.SwitchToggle;
import org.strongback.Strongback;
import org.strongback.SwitchReactor;
import org.strongback.command.CommandGroup;
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
    private SolenoidSet scissorLift;
    private DriverCamera camera;
    private Arm arm;

    private final int SHOULDER_ANGLE = 150;
    private final int WRIST_ANGLE = 90;

    @Override
    public void robotInit() {

        //drive
        Motor left = Motor.compose(Hardware.Motors.talon(0), Hardware.Motors.talon(1));
        Motor right = Motor.compose(Hardware.Motors.talon(2), Hardware.Motors.talon(3));
        SpeedController shoulder = Hardware.Motors.spark(4);
        SpeedController wrist = Hardware.Motors.spark(5);
        drive = new TankDrive(left, right);

        //pneumatics
        grabber = Hardware.Solenoids.doubleSolenoid(0, 1, Solenoid.Direction.STOPPED);
        Solenoid solenoid1 = Hardware.Solenoids.doubleSolenoid(2, 3, Solenoid.Direction.STOPPED);
        Solenoid solenoid2 = Hardware.Solenoids.doubleSolenoid(4, 5, Solenoid.Direction.STOPPED);
        scissorLift = new SolenoidSet(solenoid1, solenoid2);

        //analog
        gyro = new GyroSet(Hardware.AngleSensors.gyroscope(0), Hardware.AngleSensors.gyroscope(1));
        gyro.zero();
        arm = new Arm(grabber, Hardware.AngleSensors.potentiometer(2, SHOULDER_ANGLE), Hardware.AngleSensors.potentiometer(3, WRIST_ANGLE), shoulder, wrist);

        //TODO initialize with dashboard
        startPos = EnumAuto.LEFT;

        initControls();
    }


    @Override
    public void robotPeriodic() {
        SmartDashboard.putNumber("angle", gyro.getAngle());
        arm.periodic();
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
    }

    @Override
    public void teleopPeriodic() {
        drive.arcade(driveSpeed.read(), turnSpeed.read());
        //arm.setSpeed(armSpeed.read());
    }

    @Override
    public void disabledInit() {
        // Tell Strongback that the robot is disabled so it can flush and kill commands.
        Strongback.disable();
    }

    private void initControls() {
//        Gamepad controller = Hardware.HumanInterfaceDevices.logitechDualAction(0);
//        driveSpeed = controller.getLeftY();
//        turnSpeed = controller.getLeftX();

        FlightStick driveController = Hardware.HumanInterfaceDevices.microsoftSideWinder(0);
        GamepadRumble armController = GamepadRumble.xbox360(1);
        SwitchReactor switchReactor = Strongback.switchReactor();

        driveSpeed = driveController.getPitch();
        turnSpeed = driveController.getYaw();

        shoulderSpeed = armController.getLeftY();
        wristSpeed = armController.getRightY();
        switchReactor.onTriggered(armController.getRightBumper(), new SwitchToggle(new SolenoidExtendCommand(grabber), new SolenoidRetractCommand(grabber))::execute);
        //TODO actually make the right button
        switchReactor.onTriggered(armController.getLeftBumper(), new SwitchToggle(new SolenoidExtendCommand(scissorLift), new SolenoidRetractCommand(scissorLift))::execute);
        switchReactor.onTriggered(armController.getA(), () -> {armController.setRumble(GenericHID.RumbleType.kLeftRumble,1);});
    }


}