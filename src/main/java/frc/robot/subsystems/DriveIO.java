package frc.robot.subsystems;

import org.littletonrobotics.junction.AutoLog;

import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveDrivetrain.SwerveDriveState;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public interface DriveIO {
    @AutoLog
    public class DriveIOInputs {

        String swerveRequest;

        double angle;
        double angularVelocity;
        double accelerationX;
        double accelerationY;

        //swerve module state stuff
        int failedDaqs;
        SwerveModuleState[] moduleStates;
        SwerveModuleState[] moduleTargets;
        double odometryPeriod;
        Pose2d pose;
        Rotation2d rawHeading;
        ChassisSpeeds robotCentricSpeeds;
        int successfulDaqs;
        double captureTimestamp;

        public DriveIOInputs() {
            updateFromSwerveDriveState(new SwerveDriveState());
            swerveRequest = "NONE-SELECTED";
        }

        public void updateFromSwerveDriveState(SwerveDriveState stateIn) {
            this.pose = stateIn.Pose;
            this.successfulDaqs = stateIn.SuccessfulDaqs;
            this.failedDaqs = stateIn.FailedDaqs;
            this.moduleStates = stateIn.ModuleStates;
            this.moduleTargets = stateIn.ModuleTargets;
            this.robotCentricSpeeds = stateIn.Speeds;
            this.odometryPeriod = stateIn.OdometryPeriod;

            this.moduleStates = stateIn.ModuleStates;
            this.moduleTargets = stateIn.ModuleTargets;
        }
    }

    void updateInputs(DriveIOInputs drivetrainIOInputs);

    void resetOdometry(Pose2d pose);

    void setControl(SwerveRequest swerveRequest);
}
