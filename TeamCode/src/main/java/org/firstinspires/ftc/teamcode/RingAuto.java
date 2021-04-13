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

        rightFront = hardwareMap.get(DcMotor.class, "rf"); // control motor port 0
        rightBack = hardwareMap.get(DcMotor.class, "rb"); // control motor port 1
        leftFront = hardwareMap.get(DcMotor.class, "lf");// control motor port 2
        leftBack = hardwareMap.get(DcMotor.class, "lb");// control motor port 3

        moveToShoot = hardwareMap.get(Servo.class, "ms"); // servo port 0
        transfer1 = hardwareMap.get(Servo.class, "t1");  //servo port 1
        transfer2 = hardwareMap.get(Servo.class, "t2"); // servo port 2
        storageArm = hardwareMap.get(Servo.class, "sa"); // servo port 3
        wobbleGrab = hardwareMap.get(Servo.class, "wg");// servo port 5



        //reverse the direction of the motors neeeded so positive is the same dirction for all
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        //flyWheel.setDirection(DcMotor.Direction.REVERSE);


        //establish 0 power behavior for all motors
        //we want the motors to stop running when the input power is 0
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        wobbleLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        //establish that the power sent will be in the form of a float or a decimal
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        wobbleLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);


        //currently not using it but helps to set and maintain the speed
        flyWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot_drive.move(12, 0.75);
        moveToShoot.setPosition(0.05);
        storageArm.setPosition(0.8);



        flyWheel.setPower(0.8);

        transfer1.setPosition(0.8);
        transfer2.setPosition(0.2);



        for(int i = 0; i<3; i++){
            storageArm.setPosition(1.0);
            moveToShoot.setPosition(1.0);

            moveToShoot.setPosition(0.05);
            storageArm.setPosition(0.8);
        }


    }
}
