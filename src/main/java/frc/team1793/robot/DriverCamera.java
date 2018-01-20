package frc.team1793.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

/**
 * Created by melvin on 1/26/2017.
 * <p>
 * Controller for Camera
 */
public class DriverCamera {

    private static final int IMG_HEIGHT = 120, IMG_WIDTH = 160;

    public DriverCamera() {
        UsbCamera camera0 = CameraServer.getInstance().startAutomaticCapture();
        camera0.setResolution(IMG_WIDTH, IMG_HEIGHT);
        camera0.setFPS(30);
    }

}

