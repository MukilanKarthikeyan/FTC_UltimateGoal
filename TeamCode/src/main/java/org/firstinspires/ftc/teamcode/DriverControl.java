package org.firstinspires.ftc.teamcode;
import com.qualcomm.ftccommon.FtcLynxFirmwareUpdateActivity;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import java.util.*;

@TeleOp
public class DriverControl extends LinearOpMode {
    double conPow;
    double leftPow, rightPow; //using the tilerunner, so only two sides are distinguished in powering
    double drive;
    double turn;
    DcMotor conveyM;
    DcMotor flyWheel;
    DcMotor intake;

    DcMotor rightFront, rightBack, leftFront, leftBack;
    boolean shootToggle, cycling;
    boolean pad1DriveToggle, pad1DualStickDrive;
    boolean pad2DriveToggle, pad2DualStickDrive;

    public void runOpMode() {
        conveyM = hardwareMap.get(DcMotor.class, "cm");
        flyWheel = hardwareMap.get(DcMotor.class, "fw");
        intake = hardwareMap.get(DcMotor.class, "in");

        rightFront = hardwareMap.get(DcMotor.class, "rf");
        rightBack = hardwareMap.get(DcMotor.class, "rb");
        leftFront = hardwareMap.get(DcMotor.class, "lf");
        leftBack = hardwareMap.get(DcMotor.class, "lb");

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);


        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        conveyM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        conveyM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        flyWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftPow = 0;
        rightPow = 0;
        drive = 0;
        turn = 0;

        shootToggle = false;
        cycling = false;

        pad1DriveToggle = false;
        pad2DriveToggle = false;
        pad1DualStickDrive = true;
        pad2DualStickDrive = true;

        while(!isStopRequested()){

            conPow = -gamepad1.left_stick_y;
            conveyM.setPower(conPow);

            //flywheel toggler: pressing "a" turns on the flywheel, pressing "a" again turns it off
            // NOTE: tried creating a separate method but does not work as intended

            //shooting method: pressing a begins to slowly turn the conveyor and boots up the flywheel
            if(gamepad1.a && !shootToggle && !gamepad1.start){
                if(cycling){
                    flyWheel.setPower(0.0);
                    conveyM.setPower(0.0);
                    intake.setPower(0.0);
                    cycling = false;
                }
                else{
                    flyWheel.setPower(0.7);
                    conveyM.setPower(0.5);
                    intake.setPower(0.7); // 0.5 or lower gets stuck in the system

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


            drive = gamepad1.left_stick_y + gamepad2.left_stick_y;

            if(pad1DualStickDrive){ turn = gamepad1.right_stick_x; }
            else{ turn = gamepad1.left_stick_x; }
            if(pad2DualStickDrive){ turn += gamepad2.right_stick_x; }
            else{ turn += gamepad2.left_stick_x; }

            leftPow = drive + turn;
            rightPow = drive - turn;

            rightFront.setPower(rightPow);
            rightBack.setPower(rightPow);
            leftFront.setPower(leftPow);
            leftBack.setPower(leftPow);

            telemetry.addData("1", "cycling:" + cycling);
            telemetry.addData("2", "gamepad1 single stick drive:" + pad1DualStickDrive);
            telemetry.addData("3", "gamepad2 single stick drive:" + pad2DualStickDrive);
            telemetry.update();
        }
    }
}
