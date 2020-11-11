package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class PrototypingClamp extends LinearOpMode {

    double up;
    double down;

    Servo ClampRotate = null;
    Servo Clamp = null;
    DcMotor ClampArm = null;

    @Override
    public void runOpMode() throws InterruptedException{

        ClampRotate = hardwareMap.servo.get("ClampRotate");
        Clamp = hardwareMap.servo.get("Clamp");
        ClampArm = hardwareMap.dcMotor.get("ClampArm");

        ClampArm.setDirection(DcMotorSimple.Direction.FORWARD);
        //ClampArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //ClampArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //boolean rotate = false;
        //double motor = 0.0;
        //double clamping = 0.0;

        waitForStart();

        while (!isStopRequested()) {

            up = gamepad1.right_trigger;
            down = gamepad1.left_trigger;

            if(gamepad1.right_trigger > 0.1) {
                ClampArm.setPower(up);
            }
            else if(gamepad1.left_trigger > 0.1) {
                ClampArm.setPower(-down);
            }
            else {
                ClampArm.setPower(0);
            }

            if(gamepad1.a) {
                //rotate = true;
                ClampRotate.setPosition(0.25);
            }

            if(gamepad1.x) {
                //rotate = false;
                ClampRotate.setPosition(0.0);
            }

            if(gamepad1.dpad_up) {
                Clamp.setPosition(0.4);

            }
            else if(gamepad1.dpad_down) {
                Clamp.setPosition(0.0);
            }

            //telemetry.addData("Arm encoder counts:", ClampArm.getCurrentPosition());
            //telemetry.update();

        }

    }
}

