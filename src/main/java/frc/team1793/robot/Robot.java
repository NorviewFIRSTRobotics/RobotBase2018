package frc.team1793.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1793.robot.commands.SolenoidExtendCommand;
import frc.team1793.robot.commands.SolenoidRetractCommand;
import frc.team1793.robot.components.GyroSet;
import frc.team1793.robot.components.SolenoidSet;
import frc.team1793.robot.no.DriverCamera;
import frc.team1793.robot.util.EnumAuto;
import frc.team1793.robot.util.SwitchToggle;
import org.strongback.Strongback;
import org.strongback.SwitchReactor;
import org.strongback.components.Motor;
import org.strongback.components.Solenoid;
import org.strongback.components.SpeedController;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.components.ui.Gamepad;
import org.strongback.drive.TankDrive;
import org.strongback.hardware.Hardware;

public class Robot extends IterativeRobot {

    public static TankDrive drive;
    private ContinuousRange driveSpeed, turnSpeed, armSpeed;

    private EnumAuto startPos;
    private GyroSet gyro;
    private SolenoidSet solenoids;
    private DriverCamera camera;

    private SpeedController arm;

    @Override
    public void robotInit() {
        Motor left = Motor.compose(Hardware.Motors.talon(0), Hardware.Motors.talon(1));
        Motor right = Motor.compose(Hardware.Motors.talon(2), Hardware.Motors.talon(3));
        drive = new TankDrive(left, right);
        gyro = new GyroSet(Hardware.AngleSensors.gyroscope(0),Hardware.AngleSensors.gyroscope(1));
        gyro.zero();
        //camera = new DriverCamera();
        //CameraServer.getInstance().startAutomaticCapture();
        Solenoid solenoid2 = Hardware.Solenoids.doubleSolenoid(2,3, Solenoid.Direction.STOPPED);
        Solenoid solenoid1 = Hardware.Solenoids.doubleSolenoid(0,1, Solenoid.Direction.STOPPED);
        Solenoid solenoid3 = Hardware.Solenoids.doubleSolenoid(4,5,Solenoid.Direction.STOPPED);

        solenoids = new SolenoidSet(solenoid1, solenoid2, solenoid3);

        arm = Hardware.Motors.spark(4);

        //TODO initialize with dashboard
        startPos = EnumAuto.LEFT;

        initControls();
    }


    @Override
    public void robotPeriodic() {
        SmartDashboard.putNumber("angle", gyro.getAngle());
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
        Gamepad controller = Hardware.HumanInterfaceDevices.logitechDualAction(0);
        driveSpeed = controller.getLeftY();
        turnSpeed = controller.getLeftX();
        armSpeed = controller.getRightY();

        SwitchReactor switchReactor = Strongback.switchReactor();

        switchReactor.onTriggered(controller.getA(), new SwitchToggle(new SolenoidExtendCommand(solenoids), new SolenoidRetractCommand(solenoids))::execute);
    }
}