package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class Drive extends SubsystemBase{

    private SwerveRequest.FieldCentric fieldCentric = new SwerveRequest.FieldCentric();

    private DriveIO driveIO;
    private DriveIOInputsAutoLogged inputs = new DriveIOInputsAutoLogged();

    public Drive(DriveIO driveIO) {
        this.driveIO = driveIO;
    }

    @Override
    public void periodic() {
        driveIO.updateInputs(inputs);
        Logger.processInputs("Drive", inputs);
    }

    public Command joystickDrive(CommandXboxController controller) {
        return joystickDrive(() -> -controller.getLeftY(), () -> -controller.getLeftX(), () -> -controller.getRightX());
    }

    public Command joystickDrive(DoubleSupplier joystickX, DoubleSupplier joystickY, DoubleSupplier joystickOmega) {
        return Commands.run(
        () -> {

              double xSpeed =
              joystickX.getAsDouble() * DriveConstants.MAX_LINEAR_SPEED;
          double ySpeed =
              joystickY.getAsDouble() * DriveConstants.MAX_LINEAR_SPEED;

          double omegaSpeed =
              Math.copySign(
                  joystickOmega.getAsDouble() * joystickOmega.getAsDouble(),
                  joystickOmega.getAsDouble());

          omegaSpeed *= DriveConstants.MAX_ANGULAR_SPEED;

          driveIO.setControl(fieldCentric.withVelocityX(xSpeed).withVelocityY(ySpeed).withRotationalRate(omegaSpeed));
          
        },
        this);
    }
}
