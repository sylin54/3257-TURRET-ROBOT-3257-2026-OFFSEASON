package frc.robot.subsystems;

import static edu.wpi.first.units.Units.MetersPerSecond;

import frc.robot.generated.TunerConstants;

public class DriveConstants {
    public static final double MAX_LINEAR_SPEED = TunerConstants.kSpeedAt12Volts.abs(MetersPerSecond);

    public static final double DRIVE_BASE_RADIUS =
        Math.max(
            Math.max(
                Math.hypot(TunerConstants.FrontLeft.LocationX, TunerConstants.FrontLeft.LocationY),
                Math.hypot(
                    TunerConstants.FrontRight.LocationX, TunerConstants.FrontRight.LocationY)),
            Math.max(
                Math.hypot(TunerConstants.BackLeft.LocationX, TunerConstants.BackLeft.LocationY),
                Math.hypot(
                    TunerConstants.BackRight.LocationX, TunerConstants.BackRight.LocationY)));
    
    public static double MAX_ANGULAR_SPEED = MAX_LINEAR_SPEED / DRIVE_BASE_RADIUS;
}
