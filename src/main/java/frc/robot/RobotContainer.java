// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.FollowPathCommand;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.DriveIO;
import frc.robot.subsystems.DriveIOSimulation;
import frc.robot.subsystems.DriveIOTalonFX;

public class RobotContainer {

    private final CommandXboxController driverController = new CommandXboxController(0);

    /* Path follower */
    private final SendableChooser<Command> autoChooser;

    private final Drive drive;


    public RobotContainer() {

        switch(Constants.CURR_MODE) {
            case REAL:

                drive =
                    new Drive(
                        new DriveIOTalonFX(
                            TunerConstants.DrivetrainConstants,
                            TunerConstants.FrontLeft,
                            TunerConstants.FrontRight,
                            TunerConstants.BackLeft,
                            TunerConstants.BackRight));

                break;

            case SIM:

                drive =
                    new Drive(
                        new DriveIOSimulation(
                            TunerConstants.DrivetrainConstants,
                            TunerConstants.FrontLeft,
                            TunerConstants.FrontRight,
                            TunerConstants.BackLeft,
                            TunerConstants.BackRight));

                break;

            default:

                drive = new Drive(new DriveIO() {});

                break;

        }

        autoChooser = AutoBuilder.buildAutoChooser("Tests");
        SmartDashboard.putData("Auto Mode", autoChooser);

        configureBindings();

        // Warmup PathPlanner to avoid Java pauses
        CommandScheduler.getInstance().schedule(FollowPathCommand.warmupCommand());
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drive.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drive.joystickDrive(driverController)
        );
    }

    public Command getAutonomousCommand() {
        /* Run the path selected from the auto chooser */
        return autoChooser.getSelected();
    }
}
