// Copyright (c) 2021-2026 Littleton Robotics
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by a BSD
// license that can be found in the LICENSE file
// at the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.CANBus;
import edu.wpi.first.wpilibj.RobotBase;

/**
 * This class defines the runtime mode used by AdvantageKit. The mode is always "real" when running
 * on a roboRIO. Change the value of "simMode" to switch between "sim" (physics sim) and "replay"
 * (log replay from a file).
 */
public final class Constants {

  public static final CANBus MECHANISM_CANBUS = new CANBus("Mech - Canivore");

  public static final Mode SIM_MODE = Mode.SIM;
  public static final Mode CURR_MODE = RobotBase.isReal() ? Mode.REAL : SIM_MODE;

  public static final double FREQUENCY_HZ = 50;

  public static final double HIGH_PRIORITY_FREQUENCY_HZ = 50;
  public static final double MEDIUM_PRIORITY_FREQUENCY_HZ = 25;
  public static final double LOW_PRIORITY_FREQUENCY_HZ = 10;
  public static final double VERY_LOW_PRIORITY_FREQUENCY_HZ = 4;

  public static enum Mode {
    /** Running on a real robot. */
    REAL,

    /** Running a physics simulator. */
    SIM,

    /** Replaying from a log file. */
    REPLAY
  }


}
