package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
@Autonomous
public class RingAuto extends LinearOpMode{

    DcMotor flyWheel, intake, wobbleLift;
    DcMotor rightFront, rightBack, leftFront, leftBack;

    Servo transfer1;
    Servo transfer2;
    Servo wobbleGrab;
    Servo moveToShoot;
    Servo storageArm;

    @Override
    public void runOpMode() throws InterruptedException{
        TRDrive robot_drive = new TRDrive(this);
        waitForStart();

        robot_drive.setUpIMU();
        sleep(100);
        intake = hardwareMap.get(DcMotor.class, "in"); // expansion motor port 0
        flyWheel = hardwareMap.get(DcMotor.class, "fw"); // expansion motor port 2
        wobbleLift = hardwareMap.get(DcMotor.class, "wl"); // expansion motor port 3

        moveToShoot = hardwareMap.get(Servo.class, "ms"); // servo port 0
        transfer1 = hardwareMap.get(Servo.class, "t1");  //servo port 1
        transfer2 = hardwareMap.get(Servo.class, "t2"); // servo port 2
        storageArm = hardwareMap.get(Servo.class, "sa"); // servo port 3
        wobbleGrab = hardwareMap.get(Servo.class, "wg");// servo port 5

        flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        wobbleLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        wobbleLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        //currently not using it but helps to set and maintain the speed
        flyWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //robot_drive.move(12, 0.75);

        moveToShoot.setPosition(0.0);
        storageArm.setPosition(0.8);
        //flyWheel.setPower(0.8);

        transfer1.setPosition(0.6);
        transfer2.setPosition(0.4);

        for(int i = 0; i < 3; i++){
            storageArm.setPosition(0.8);
            sleep(700);
            //moveToShoot.setPosition(0.7);
            sleep(700);

            //moveToShoot.setPosition(0.0);
            //sleep(700);
            //storageArm.setPosition(0.0);
            //sleep(700);
        }


    }
}
