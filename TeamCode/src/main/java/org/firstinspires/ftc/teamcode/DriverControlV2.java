package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class DriverControlV2 extends LinearOpMode {
    double leftPow, rightPow; //using the tilerunner, so only two sides are distinguished in powering
    double drive, turn;

    DcMotor flyWheel, intake, wobbleLift;
    DcMotor rightFront, rightBack, leftFront, leftBack;

    Servo transfer1;
    Servo transfer2;
    Servo wobbleGrab;
    Servo moveToShoot;
    Servo storageArm;

    boolean shootToggle, grabToggle, transferToggle, moveToggle, storageToggle; //one button two actions -> a monostable circuit
    boolean transitDown, wobbleOpen, cycling, fedLaunch, ringPushed;
    boolean pad1DriveToggle, pad1DualStickDrive; //monostable circuit for switching between dual stick drive and right stick drive
    boolean pad2DriveToggle, pad2DualStickDrive;//same as above but for pad 2
    double transPosUp, transPosDown;           //transfer mechanism servos

    public void runOpMode() {

        //initialize all the declared hardware

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
        moveToggle = false;
        storageToggle = false;
        pad1DriveToggle = false;
        pad2DriveToggle = false;

        //the variables that describe the state of the system within the monostable circuit
        cycling = false;
        wobbleOpen = true;
        transitDown = true;
        fedLaunch = true;
        ringPushed = true;
        pad1DualStickDrive = true;
        pad2DualStickDrive = true;

        transPosUp = 0.0;
        transPosDown = 0.5;

        transfer1.setPosition(0.5);
        transfer2.setPosition(0.5);
        wobbleGrab.setPosition(0.5);
        moveToShoot.setPosition(1.0);
        storageArm.setPosition(1.0);

        //the code that runs in loop during the driver control period
        while(!isStopRequested()){

            //flywheel toggler: pressing "a" turns on the flywheel, pressing "a" again turns it off
            // NOTE: tried creating a separate method but does not work as intended

            if(gamepad1.a && !shootToggle && !gamepad1.start){
                if(cycling){
                    flyWheel.setPower(0.0);

                    //transfer1.setPosition(0.6);
                    //transfer2.setPosition(0.4);
                    cycling = false;
                }
                else{
                    flyWheel.setPower(0.8);
                    //transfer1.setPosition(0.5);
                    //transfer2.setPosition(0.5);
                    cycling = true;
                }
                shootToggle = true;
            }
            else if(!gamepad1.a){shootToggle = false;}

            if(gamepad1.b){intake.setPower(0.7);}else{intake.setPower(0.0);}

            // toggle for the transfer mechanism changing between the rings form ramp to shooter
            if(gamepad1.y && !transferToggle){
                if(transitDown){
                    transfer1.setPosition(0.8);
                    transfer2.setPosition(0.2);
                    transitDown = false;
                }
                else {
                    transfer1.setPosition(1.0);
                    transfer2.setPosition(0.0);
                    transitDown = true;

                }
                transferToggle = true;
            }
            else if(!gamepad1.y){transferToggle = false;}

            //storage arm
            if(gamepad1.x && !storageToggle){
                if(ringPushed){
                    moveToShoot.setPosition(0.05);
                    fedLaunch = false;
                    storageArm.setPosition(1.0);
                    ringPushed = false;
                }
                else {
                    storageArm.setPosition(0.8);
                    ringPushed = true;
                }
                storageToggle = true;
            }
            else if(!gamepad1.x){storageToggle = false;}

            if(gamepad1.right_bumper && !moveToggle){
                if(fedLaunch){
                    moveToShoot.setPosition(0.05);
                    fedLaunch = false;
                }
                else {
                    moveToShoot.setPosition(1.0);
                    fedLaunch = true;
                }
                moveToggle = true;
            }
            else if(!gamepad1.right_bumper){moveToggle = false;}

            //toggle for grabbing and relesing the wobble goal
            if(gamepad1.left_bumper && grabToggle ){
                if(wobbleOpen){
                    wobbleGrab.setPosition(0.5);
                    wobbleOpen = false;
                }
                else{
                    wobbleGrab.setPosition(0.0);
                    wobbleOpen = true;
                }
                grabToggle = false;
            }
            else if(!gamepad1.left_bumper){grabToggle = true;}

            if(gamepad1.dpad_down){wobbleLift.setPower(0.3);}
            else if(gamepad1.dpad_left){wobbleLift.setPower(0.5);}
            else if(gamepad1.dpad_up){wobbleLift.setPower(-0.3);}
            else if(gamepad1.dpad_right){wobbleLift.setPower(-0.7);}
            else{wobbleLift.setPower(0.0);}


            //toogle for dual stick and right stick drive in game pad 1
            //NOTE: add one for right stick drive
            if(gamepad1.left_stick_button && pad1DriveToggle ){
                if(pad1DualStickDrive){ pad1DualStickDrive = false; }
                else{ pad1DualStickDrive = true;}
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
