package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
public class AutoTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException{
        TRDrive robot_drive = new TRDrive(this);
        waitForStart();

        robot_drive.move(12, 0.5);
        robot_drive.move(-12, 0.5);
        //robot_drive.turn_relative_robot(90, 0.5);
        //robot_drive.turn_relative_robot(-90, 0.5);

        //robot_drive.move_using_tics(1120, 0.5);
        //robot_drive.right_FrontM.setPower(0.75);
        //sleep(1000);

    }
}
