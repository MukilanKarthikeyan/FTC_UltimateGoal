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
    double leftPow, rightPow; //using the tilerunner, so only two sides are distingused in powering
    double drive;
    double turn;
    DcMotor conveyM;
    DcMotor flyWheel;

    DcMotor rightFront, rightBack, leftFront, leftBack;
    boolean change;


    public void runOpMode() {
        conveyM = hardwareMap.get(DcMotor.class, "cm");
        flyWheel = hardwareMap.get(DcMotor.class, "fw");

        rightFront = hardwareMap.get(DcMotor.class, "rf");
        rightBack = hardwareMap.get(DcMotor.class, "rb");
        leftFront = hardwareMap.get(DcMotor.class, "lf");
        leftBack = hardwareMap.get(DcMotor.class, "lb");


        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        conveyM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        conveyM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        flyWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftPow = 0;
        rightPow = 0;
        drive = 0;
        turn = 0;

        change = false;

        while(!isStopRequested()){
            drive = gamepad1.left_stick_y + gamepad2.left_stick_y;
            turn = gamepad1.right_stick_x + gamepad2.right_stick_x;
            leftPow = drive + turn;
            rightPow = drive - turn;

            conPow = -gamepad1.left_stick_y;
            conveyM.setPower(conPow);

            //flywheel toggler: pressing "a" turns on the flywheel, pressing "a" again turns it off
            // NOTE: tried creating a separate method but does not work as intended

            //shooting method: pressing a begins to slowly turn the conveyor and boots up the flywheel
            if(gamepad1.a && !change && !gamepad1.start){
                if(flyWheel.getPower() == 0 && conPow == 0){
                    flyWheel.setPower(0.7);
                    conveyM.setPower(0.5);
                }
                else{flyWheel.setPower(0);}
                change = true;
            }
            else if(!gamepad1.a){change = false;}

            telemetry.addData("1", "encoder:" + flyWheel.getCurrentPosition());
            telemetry.update();
        }
    }
}
