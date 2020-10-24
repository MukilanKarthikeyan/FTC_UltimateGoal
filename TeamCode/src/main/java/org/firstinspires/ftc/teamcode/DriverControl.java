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

    DcMotor rightFront, rightBack, leftFront, leftBack;
    boolean shootToggle;
    boolean pad1DriveToggle;
    boolean pad2DriveToggle;
    boolean pad1DualStickDrive;
    boolean pad2DualStickDrive;

    public void runOpMode() {
        conveyM = hardwareMap.get(DcMotor.class, "cm");
        flyWheel = hardwareMap.get(DcMotor.class, "fw");

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

        shootToggle = true;

        pad1DriveToggle = false;
        pad2DriveToggle = false;
        pad1DualStickDrive = false;
        pad2DualStickDrive = false;

        while(!isStopRequested()){

            conPow = -gamepad1.left_stick_y;
            conveyM.setPower(conPow);

            //flywheel toggler: pressing "a" turns on the flywheel, pressing "a" again turns it off
            // NOTE: tried creating a separate method but does not work as intended

            //shooting method: pressing a begins to slowly turn the conveyor and boots up the flywheel
            if(gamepad1.a && shootToggle && !gamepad1.start){
                if(flyWheel.getPower() == 0 && conPow == 0){
                    flyWheel.setPower(0.7);
                    conveyM.setPower(0.5);
                }
                else{flyWheel.setPower(0);}
                shootToggle = false;
            }
            else if(!gamepad1.a){shootToggle = true;}

            if(gamepad1.right_stick_button && gamepad1.left_stick_button && !pad1DriveToggle ){
                pad1DualStickDrive = !pad1DualStickDrive;
                pad1DriveToggle = false;
            }
            else if(!gamepad1.a){pad1DriveToggle = true;}
            if(gamepad2.right_stick_button && gamepad2.left_stick_button && !pad2DriveToggle ){
                pad2DualStickDrive = !pad2DualStickDrive;
                pad2DriveToggle = false;
            }
            else if(!gamepad1.a){pad1DriveToggle = true;}

            drive = gamepad1.left_stick_y + gamepad2.left_stick_y;

            if(pad1DualStickDrive){ turn = gamepad1.right_stick_x; }
            else{ turn = gamepad1.left_stick_x; }
            if(pad2DualStickDrive){ turn += gamepad2.right_stick_x; }
            else{ turn += gamepad2.left_stick_x; }

            leftPow = drive + turn;
            rightPow = drive - turn;

            telemetry.addData("1", "encoder:" + flyWheel.getCurrentPosition());
            telemetry.addData("2", "gamepad1:" + pad1DualStickDrive);
            telemetry.addData("3", "gamepad2:" + pad2DualStickDrive);
            telemetry.update();
        }
    }
}
