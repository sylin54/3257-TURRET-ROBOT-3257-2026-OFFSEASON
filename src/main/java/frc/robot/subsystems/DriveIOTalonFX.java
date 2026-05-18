package frc.robot.subsystems;

 import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.swerve.SwerveDrivetrainConstants;
import com.ctre.phoenix6.swerve.SwerveModuleConstants;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.LinearAcceleration;

//adapter/strategy pattern class to convert from the CommandSwerveDrivetrain to our DriveIO interface, which is what the rest of our code uses to interact with the drivetrain. This allows us to easily swap out the drivetrain implementation without affecting the rest of our code.
public class DriveIOTalonFX extends CommandSwerveDrivetrain implements DriveIO {

    private String currentSwerveRequest;

    private StatusSignal<Angle> yaw;
    private StatusSignal<AngularVelocity> angularYawVelocity;
    private StatusSignal<LinearAcceleration> accelerationX;
    private StatusSignal<LinearAcceleration> accelerationY;
    
    //we cache the state so we don't have to update it constantly
    private AtomicReference<SwerveDriveState> telemetryCache = new AtomicReference<>();

    Consumer<SwerveDriveState> telemetryConsumer =
        swerveDriveState -> {
            telemetryCache.set(swerveDriveState.clone());
        };

    public DriveIOTalonFX(SwerveDrivetrainConstants swerveDrivetrainConstants, SwerveModuleConstants<?, ?, ?>... modules) {
        super(swerveDrivetrainConstants, modules);

        registerTelemetry(telemetryConsumer);

        Pigeon2 pigeon2 = getPigeon2();

        yaw = pigeon2.getYaw();
        angularYawVelocity = pigeon2.getAngularVelocityZWorld();

        accelerationX = pigeon2.getAccelerationX();
        accelerationY = pigeon2.getAccelerationY();


    }

    @Override
    public void updateInputs(DriveIOInputs drivetrainIOInputs) {

        if(telemetryCache == null) return;

        BaseStatusSignal.refreshAll(yaw, angularYawVelocity, accelerationX, accelerationY);

        drivetrainIOInputs.swerveRequest = currentSwerveRequest;
        drivetrainIOInputs.updateFromSwerveDriveState(telemetryCache.get());

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
