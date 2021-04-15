package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class AutoTest extends LinearOpMode {

    DcMotor flyWheel, intake, wobbleLift;
    DcMotor rightFront, rightBack, leftFront, leftBack;

    Servo transfer1;
    Servo transfer2;
    Servo wobbleGrab;
    Servo moveToShoot;
    Servo storageArm;

    @Override
    public void runOpMode() throws InterruptedException{

        intake = hardwareMap.get(DcMotor.class, "in"); // expansion motor port 0
        flyWheel = hardwareMap.get(DcMotor.class, "fw"); // expansion motor port 2
        wobbleLift = hardwareMap.get(DcMotor.class, "wl"); // expansion motor port 3

        rightFront = hardwareMap.get(DcMotor.class, "rf"); // control motor port 0
        rightBack = hardwareMap.get(DcMotor.class, "rb"); // control motor port 1
        leftFront = hardwareMap.get(DcMotor.class, "lf");// control motor port 2
        leftBack = hardwareMap.get(DcMotor.class, "lb");// control motor port 3

        moveToShoot = hardwareMap.get(Servo.class, "ms"); // servo port 0
        transfer1 = hardwareMap.get(Servo.class, "t1");  //servo port 1
        transfer2 = hardwareMap.get(Servo.class, "t2"); // servo port 2
        storageArm = hardwareMap.get(Servo.class, "sa"); // servo port 3
        wobbleGrab = hardwareMap.get(Servo.class, "wg");// servo port 5

        TRDrive robot_drive = new TRDrive(this);
        waitForStart();

        //robot_drive.setUpIMU();
        //sleep(100);

        //robot_drive.move(12, 0.75);

        //sleep (500);
        //robot_drive.move(-24, 0.75);

        //robot_drive.turn_relative_robot(90, 1);
        //robot_drive.turn_relative_robot(90, 0.5);
        //robot_drive.turn_relative_robot(-90, 0.5);

        //robot_drive.move_using_tics(1120, 0.5);
        //        //robot_drive.right_FrontM.setPower(0.75);
        //sleep(1000);

        wobbleGrab.setPosition(0.0);

        waitForStart();

        wobbleLift.setPower(0.6);

        /*
        flyWheel.setPower(0.7);
        sleep(2000);
        transfer2.setPosition(0.63);
        sleep(4000);
        //ring 1 shot
        flyWheel.setPower(0.0);
        transfer2.setPosition(0.0);
        sleep(1000);
        flyWheel.setPower(0.7);
        sleep(1000);
        transfer1.setPosition(0.2);
        sleep(1000);
        transfer2.setPosition(0.63);
         */

        while (opModeIsActive()) {

        }
    }
}
