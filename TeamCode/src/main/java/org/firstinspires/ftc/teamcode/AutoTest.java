package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


import java.util.*;
@Autonomous
public class AutoTest extends LinearOpMode {

        DcMotor flyWheel;
        DcMotor intake;
        Servo transfer2;
        Servo transfer1;
        Servo bucket;

        public void runOpMode(){

            flyWheel = hardwareMap.get(DcMotor.class, "fw");
            transfer2 = hardwareMap.get(Servo.class, "t2");
            transfer1 = hardwareMap.get(Servo.class, "t1");
            bucket = hardwareMap.get(Servo.class, "bk");

            transfer2.setPosition(0.0);
            transfer1.setPosition(0.35);
            bucket.setPosition(0.75);

            waitForStart();

            //flyWheel.setPower(0.7);
            transfer1.setPosition(0.75);
            sleep(2000);
            transfer2.setPosition(0.63);
            sleep(4000);

            flyWheel.setPower(0.3);

            while (opModeIsActive()){
                //flyWheel.setPower(0.8);

            }

    }
}
