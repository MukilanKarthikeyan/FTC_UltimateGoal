package org.firstinspires.ftc.teamcode;
import com.qualcomm.ftccommon.FtcLynxFirmwareUpdateActivity;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.util.*;

@TeleOp
public class DriverControl extends LinearOpMode {
    double conPow;
    double leftPow, rightPow; //using the tilerunner, so only two sides are distinguished in powering
    double drive;
    double turn;
    DcMotor flyWheel;
    DcMotor intake;
    DcMotor wobbleLift;

    Servo transfer1;
    Servo transfer2;
    Servo wobbleGrab;


    DcMotor rightFront, rightBack, leftFront, leftBack;
    boolean shootToggle, grabToggle, wobbleOpen, cycling, transferToggle, transitDown;
    boolean pad1DriveToggle, pad1DualStickDrive;
    boolean pad2DriveToggle, pad2DualStickDrive;
    double transPosUp, transPosDown;


    public void runOpMode() {
        flyWheel = hardwareMap.get(DcMotor.class, "fw");
        intake = hardwareMap.get(DcMotor.class, "in");
        wobbleLift = hardwareMap.get(DcMotor.class, "wl");

        rightFront = hardwareMap.get(DcMotor.class, "rf");
        rightBack = hardwareMap.get(DcMotor.class, "rb");
        leftFront = hardwareMap.get(DcMotor.class, "lf");
        leftBack = hardwareMap.get(DcMotor.class, "lb");

        transfer1 = hardwareMap.get(Servo.class, "t1");
        transfer2 = hardwareMap.get(Servo.class, "t2");
        wobbleGrab = hardwareMap.get(Servo.class, "wg");

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        flyWheel.setDirection(DcMotor.Direction.REVERSE);


        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        wobbleLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);


        flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        wobbleLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        flyWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftPow = 0;
        rightPow = 0;
        drive = 0;
        turn = 0;

        shootToggle = false;
        grabToggle = true;
        cycling = false;
        wobbleOpen = true;
        transferToggle = false;
        transitDown = true;
        transPosUp = 0.0;
        transPosDown = 0.5;

        pad1DriveToggle = false;
        pad2DriveToggle = false;
        pad1DualStickDrive = true;
        pad2DualStickDrive = true;
        transfer1.setPosition(0.5);
        transfer2.setPosition(0.5);
        wobbleGrab.setPosition(1.0);
        while(!isStopRequested()){

            //flywheel toggler: pressing "a" turns on the flywheel, pressing "a" again turns it off
            // NOTE: tried creating a separate method but does not work as intended

            //shooting method: pressing a begins to slowly turn the conveyor and boots up the flywheel
            if(gamepad1.a && !shootToggle && !gamepad1.start){
                if(cycling){
                    flyWheel.setPower(0.0);
                    intake.setPower(0.0);

                    transfer1.setPosition(0.6);
                    transfer2.setPosition(0.4);
                    cycling = false;
                }
                else{
                    flyWheel.setPower(-0.8);
                    intake.setPower(0.8);
                    transfer1.setPosition(0.5);
                    transfer2.setPosition(0.5);
                    cycling = true;
                }
                /*
                if(flyWheel.getPower() == 0 && conPow == 0){
                    flyWheel.setPower(0.7);
                    conveyM.setPower(0.5);
                    intake.setPower(0.25);
                }
                else{flyWheel.setPower(0.0); conveyM.setPower(0.0); intake.setPower(0.0);}
                */

                shootToggle = true;
            }
            else if(!gamepad1.a){shootToggle = false;}


            if(gamepad1.x && grabToggle ){
                if(wobbleOpen){
                    wobbleGrab.setPosition(0.5);
                    wobbleOpen = false;
                }
                else{
                    wobbleGrab.setPosition(1.0);

                    wobbleOpen = true;
                }
                grabToggle = false;
            }
            else if(!gamepad1.x){grabToggle = true;}


            if(gamepad2.x){wobbleGrab.setPosition(0.5);}
            if(gamepad2.y){wobbleGrab.setPosition(1.0);}

            if(gamepad1.right_bumper){intake.setPower(0.6);}
            else if((gamepad1.right_trigger > 0.0)){intake.setPower(gamepad1.right_trigger);}
            else{intake.setPower(0.0);}



            if(gamepad1.dpad_down){wobbleLift.setPower(0.3);}
            else if(gamepad1.left_bumper && gamepad1.dpad_down){wobbleLift.setPower(0.5);}
            else if(gamepad1.dpad_up){wobbleLift.setPower(-0.3);}
            else if(gamepad1.left_bumper && gamepad1.dpad_up){wobbleLift.setPower(-0.5);}
            else if(gamepad1.dpad_right){wobbleLift.setPower(-0.7);}
            else{wobbleLift.setPower(0.0);}


            if(gamepad1.left_stick_button && pad1DriveToggle ){
                if(pad1DualStickDrive){ pad1DualStickDrive = false; }
                else{ pad1DualStickDrive = true; }
                pad1DriveToggle = false;
            }
            else if(!gamepad1.left_stick_button){pad1DriveToggle = true;}


            if( gamepad2.left_stick_button && pad2DriveToggle ){
                if(pad2DualStickDrive){ pad2DualStickDrive = false; }
                else{ pad2DualStickDrive = true; }
                pad2DriveToggle = false;
            }
            else if( !gamepad2.left_stick_button){pad2DriveToggle = true;}


            drive = Range.clip(gamepad1.left_stick_y + gamepad2.left_stick_y, -0.7,0.7);

            if(pad1DualStickDrive){ turn = gamepad1.right_stick_x; }
            else{ turn = gamepad1.left_stick_x; }
            if(pad2DualStickDrive){ turn += gamepad2.right_stick_x; }
            else{ turn += gamepad2.left_stick_x; }

            leftPow = drive - turn;
            rightPow = drive + turn;

            rightFront.setPower(rightPow);
            rightBack.setPower(rightPow);
            leftFront.setPower(leftPow);
            leftBack.setPower(leftPow);

            telemetry.addData("1", "cycling:" + cycling);
            telemetry.addData("2", "gamepad1 single stick drive:" + pad1DualStickDrive);
            telemetry.addData("3", "gamepad2 single stick drive:" + pad2DualStickDrive);
            telemetry.update();

            if(gamepad1.y && !transferToggle){
                if(transitDown){
                    transfer1.setPosition(0.65);
                    transfer2.setPosition(0.45);
                    transitDown = false;
                }
                else {
                    transfer1.setPosition(0.5);
                    transfer2.setPosition(0.5);
                    transitDown = true;

                }
                transferToggle = true;
            }
            else if(!gamepad1.y){transferToggle = false;}
        }
    }
}
