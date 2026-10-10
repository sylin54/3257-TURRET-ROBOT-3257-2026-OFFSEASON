package frc.robot.subsystems.intake;

import java.lang.Thread.State;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.VortechsUtil;

public class Intake extends SubsystemBase {

    private IntakeIOInputsAutoLogged inputs = new IntakeIOInputsAutoLogged();

    private IntakeState intakeState = IntakeState.IDLE;
    
    private IntakeIO intakeIO;

    public Intake(IntakeIO intakeIO) {
        this.intakeIO = intakeIO;
    }

    public void periodic() {
        intakeIO.updateInputs(inputs);
        Logger.processInputs("intake", inputs);

        handleStateBehavior();
        Logger.recordOutput("Intake/state", intakeState);
    }

    public void handleStateBehavior() {
        switch(intakeState) {
            case IDLE:
                intakeIO.stop();
            break;

            case DEPLOYING_INTAKING:
                intakeIO.setPositionControl(IntakeConstants.INTAKE_DOWN_POSITION);
                intakeIO.setRollerVoltage(0);
                
                if(VortechsUtil.isOnTarget(intakeIO.getPosition(), IntakeConstants.INTAKE_DOWN_POSITION, IntakeConstants.POSITION_TOLERANCE)) {
                    intakeState = IntakeState.INTAKING;
                }
            break;

            case INTAKING:
                intakeIO.setPositionControl(IntakeConstants.INTAKE_DOWN_POSITION);
                intakeIO.setRollerVoltage(IntakeConstants.INTAKE_VOLTS);
                
                if(!VortechsUtil.isOnTarget(intakeIO.getPosition(), IntakeConstants.INTAKE_DOWN_POSITION, IntakeConstants.POSITION_TOLERANCE)) {
                    intakeState = IntakeState.DEPLOYING_INTAKING;
                }

            break;

            case STOWING:
                intakeIO.setPositionControl(IntakeConstants.INTAKE_SHOOTING_DISTURBANCE_UP);
                intakeIO.setRollerVoltage(0);
            break;

            case DEPLOYING:
                intakeIO.setPositionControl(IntakeConstants.INTAKE_DOWN_POSITION);
                intakeIO.setRollerVoltage(0);
            break;
        }
    }

    public IntakeState getState() {
        return intakeState;
    }

    public void startIntaking() {
        intakeState = IntakeState.DEPLOYING_INTAKING;
    }

    public void startDeploying() {
        intakeState = IntakeState.DEPLOYING;
    }

    public void startStowing() {
        intakeState = IntakeState.STOWING;
    }

    public Command startIntakingCommand() {
        return new InstantCommand(() -> startIntaking());
    }

    public Command startDeployingCommand() {
        return new InstantCommand(() -> startDeploying());
    }

    public Command startStowingCommand() {
        return new InstantCommand(() -> startStowing());
    }
}
