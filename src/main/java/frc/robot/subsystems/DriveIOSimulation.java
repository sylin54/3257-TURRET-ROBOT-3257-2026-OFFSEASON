package frc.robot.subsystems;

 import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.swerve.SwerveDrivetrainConstants;
import com.ctre.phoenix6.swerve.SwerveModuleConstants;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.LinearAcceleration;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;

public class DriveIOSimulation extends CommandSwerveDrivetrain implements DriveIO {

    private String currentSwerveRequest;

    private StatusSignal<Angle> yaw;
    private StatusSignal<AngularVelocity> angularYawVelocity;
    private StatusSignal<LinearAcceleration> accelerationX;
    private StatusSignal<LinearAcceleration> accelerationY;

    private double m_lastSimTime = 0;
    private Notifier m_simNotifier;
    
    //we cache the state so we don't have to update it constantly
    private AtomicReference<SwerveDriveState> telemetryCache = new AtomicReference<>();

    Consumer<SwerveDriveState> telemetryConsumer =
        swerveDriveState -> {
            telemetryCache.set(swerveDriveState.clone());
        };

    public DriveIOSimulation(SwerveDrivetrainConstants swerveDrivetrainConstants, SwerveModuleConstants<?, ?, ?>... modules) {
        super(swerveDrivetrainConstants, modules);

        registerTelemetry(telemetryConsumer);

        Pigeon2 pigeon2 = getPigeon2();

        yaw = pigeon2.getYaw();
        angularYawVelocity = pigeon2.getAngularVelocityZWorld();

        accelerationX = pigeon2.getAccelerationX();
        accelerationY = pigeon2.getAccelerationY();

        initializeSim();
    }

    public void initializeSim() {
        m_lastSimTime = Utils.getCurrentTimeSeconds();

        /* Run simulation at a faster rate so PID gains behave more reasonably */
        this.m_simNotifier =
            new Notifier(
                () -> {
                final double currentTime = Utils.getCurrentTimeSeconds();
                double deltaTime = currentTime - m_lastSimTime;
                m_lastSimTime = currentTime;

                /* Use the measured time delta, get battery voltage from WPILib */
                updateSimState(deltaTime, RobotController.getBatteryVoltage());
                });
        m_simNotifier.startPeriodic(0.004);
    }

    @Override
    public void updateInputs(DriveIOInputs drivetrainIOInputs) {

        if(telemetryCache == null) return;

        drivetrainIOInputs.swerveRequest = currentSwerveRequest;
        drivetrainIOInputs.updateFromSwerveDriveState(telemetryCache.get());

        BaseStatusSignal.refreshAll(yaw, angularYawVelocity, accelerationX, accelerationY);

        drivetrainIOInputs.angle = yaw.getValueAsDouble();
        drivetrainIOInputs.angularVelocity = angularYawVelocity.getValueAsDouble();
        drivetrainIOInputs.accelerationX = accelerationX.getValueAsDouble();
        drivetrainIOInputs.accelerationY = accelerationY.getValueAsDouble();
    }

    @Override
    public void resetOdometry(Pose2d pose) {
       resetPose(pose);
    }

    @Override
    public void setControl(SwerveRequest request) {
        currentSwerveRequest = request.toString();
        super.setControl(request);
    }


    
}
