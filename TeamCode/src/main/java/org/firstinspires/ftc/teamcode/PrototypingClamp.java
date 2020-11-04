package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
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

        //boolean rotate = false;
        //double motor = 0.0;
        //double clamping = 0.0;

        waitForStart();

        while (!isStopRequested()) {

            up = gamepad1.right_trigger + gamepad2.right_trigger;
            down = gamepad1.left_trigger + gamepad2.left_trigger;

            if(gamepad1.right_trigger > 0.1 || gamepad2.right_trigger > 0.1) {
                ClampArm.setPower(up);
            }
            else if(gamepad1.left_trigger > 0.1 || gamepad2.left_trigger > 0.1) {
                ClampArm.setPower(-down);
            }
            else {
                ClampArm.setPower(0);
            }

            if(gamepad1.a || gamepad2.a) {
                //rotate = true;
                ClampRotate.setPosition(0.25);
            }

            if(gamepad1.x || gamepad2.x) {
                //rotate = false;
                ClampRotate.setPosition(0.0);
            }

            if(gamepad1.dpad_up || gamepad2.dpad_up) {
                Clamp.setPosition(0.4);

            }
            else if(gamepad1.dpad_down || gamepad2.dpad_down) {
                Clamp.setPosition(0.0);
            }

            telemetry.addData("Arm encoder counts:", ClampArm.getCurrentPosition());
            telemetry.update();

        }

    }
}

