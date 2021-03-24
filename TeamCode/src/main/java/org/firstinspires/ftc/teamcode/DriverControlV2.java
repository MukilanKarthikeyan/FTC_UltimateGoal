package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class DriverControlV2 extends LinearOpMode {
    double conPow;
    double leftPow, rightPow; //using the tilerunner, so only two sides are distinguished in powering
    double drive, turn;

    DcMotor flyWheel, intake, wobbleLift;
    DcMotor rightFront, rightBack, leftFront, leftBack;

    Servo transfer1;
    Servo transfer2;
    Servo wobbleGrab;
    CRServo moveToShoot1;
    CRServo moveToShoot2;

    boolean shootToggle, grabToggle, transferToggle; //one button two actions -> a monostable circuit
    boolean transitDown, wobbleOpen, cycling;
    boolean pad1DriveToggle, pad1DualStickDrive; //monostable circuit for switching between dual stick drive and right stick drive
    boolean pad2DriveToggle, pad2DualStickDrive;//same as above but for pad 2
    double transPosUp, transPosDown; //transfer mechanism servos

    public void runOpMode() {

        //initialize all the declared hardware
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


        //reverse the direction of the motors neeeded so positive is the same dirction for all
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        flyWheel.setDirection(DcMotor.Direction.REVERSE);


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


        //these are the variables used for driving
        //the left and right pow is calculated based on the drive and turn
        leftPow = 0;
        rightPow = 0;
        drive = 0;
        turn = 0;


        //initializing all the toggles to be set to their respective start position
        shootToggle = false;
        grabToggle = true;
        transferToggle = false;
        pad1DriveToggle = false;
        pad2DriveToggle = false;

        //the variables that describe the state of the system within the monostable circuit
        cycling = false;
        wobbleOpen = true;
        transitDown = true;
        pad1DualStickDrive = true;
        pad2DualStickDrive = true;

        transPosUp = 0.0;
        transPosDown = 0.5;

        transfer1.setPosition(0.5);
        transfer2.setPosition(0.5);
        wobbleGrab.setPosition(0.5);

        //the code that runs in loop during the driver control period
        while(!isStopRequested()){

            //flywheel toggler: pressing "a" turns on the flywheel, pressing "a" again turns it off
            // NOTE: tried creating a separate method but does not work as intended

            if(gamepad1.a && !shootToggle && !gamepad1.start){
                if(cycling){
                    flyWheel.setPower(0.0);
                    intake.setPower(0.0);

                    transfer1.setPosition(0.6);
                    transfer2.setPosition(0.4);
                    cycling = false;
                }
                else{
                    flyWheel.setPower(0.8);
                    intake.setPower(0.8);
                    transfer1.setPosition(0.5);
                    transfer2.setPosition(0.5);
                    cycling = true;
                }
                shootToggle = true;
            }
            else if(!gamepad1.a){shootToggle = false;}

            //toggle for grabbing and relesing the wobble goal
            if(gamepad1.x && grabToggle ){
                if(wobbleOpen){
                    wobbleGrab.setPosition(0.7);
                    wobbleOpen = false;
                }
                else{
                    wobbleGrab.setPosition(0.5);
                    wobbleOpen = true;
                }
                grabToggle = false;
            }
            else if(!gamepad1.x){grabToggle = true;}

            // toggle for the transfer mechanism changing between the rings form ramp to shooter
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

            //toogle for dual stick and right stick drive in game pad 1
            //NOTE: add one for right stick drive
            if(gamepad1.left_stick_button && pad1DriveToggle ){
                if(pad1DualStickDrive){ pad1DualStickDrive = false; }
                else{ pad1DualStickDrive = true; }
                pad1DriveToggle = false;
            }
            else if(!gamepad1.left_stick_button){pad1DriveToggle = true;}

            //same as the drive toggle for game pad 1 driving but implemented for game pad 2
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

        }
    }
}
