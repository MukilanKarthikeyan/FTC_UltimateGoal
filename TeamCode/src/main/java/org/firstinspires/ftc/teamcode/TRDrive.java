package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class TRDrive {
    LinearOpMode opmode;

    DcMotor right_FrontM = null;
    DcMotor right_BackM = null;
    DcMotor left_FrontM = null;
    DcMotor left_BackM = null;

    double right_power;
    double left_power;

    /*both right motors move are liked and both left motors are linked so each side only have one encoder
    * NOTE: three wheel turns are not possible with the TileRunner*/
    double right_encoder_counts;
    double left_encoder_counts;

    BNO055IMU imu;
    Orientation angles;

    int target_counts;

    //wheel radius is in inches
    double wheel_radius = 4;
    double wheel_circumfrence = 2*Math.PI*wheel_radius;
    private final int TICKS_PER_MOTOR_REV = 1120;
    private final int TICKS_PER_WHEEL_REV = (int)(TICKS_PER_MOTOR_REV*wheel_circumfrence);
    private final int motor_to_wheel_ratio = 1;

    /**
     * constructor method: initializes all motors on robot
     * sets motor behavior and run modes
     * @param mode
     */
    public TRDrive (LinearOpMode mode){
        opmode = mode;

        right_FrontM = opmode.hardwareMap.get(DcMotor.class, "rf");
        right_BackM = opmode.hardwareMap.get(DcMotor.class, "rb");
        left_FrontM = opmode.hardwareMap.get(DcMotor.class, "lf");
        left_BackM = opmode.hardwareMap.get(DcMotor.class, "lb");

        right_FrontM.setDirection(DcMotor.Direction.FORWARD);
        right_BackM.setDirection(DcMotor.Direction.FORWARD);
        left_FrontM.setDirection(DcMotor.Direction.REVERSE);
        left_BackM.setDirection(DcMotor.Direction.REVERSE);

        right_FrontM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_BackM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left_FrontM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left_BackM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        right_FrontM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        right_BackM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        left_FrontM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        left_BackM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        right_FrontM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right_BackM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left_FrontM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left_BackM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //each encoder is shared between two motors
        right_BackM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_BackM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        setUpIMU();

    }

    /**
     * this method uses
     * @param dist the distance in inches (positive distance moves forward, negative distance moves backward.)
     * @param power the power that is to be applied to the motors
     */
    public void move(double dist, double power){
        int tics = (int)((dist/wheel_circumfrence)*(motor_to_wheel_ratio)*TICKS_PER_WHEEL_REV);

        int right_target = Math.abs(right_BackM.getCurrentPosition()) + tics;
        int left_target = Math.abs(left_BackM.getCurrentPosition()) + tics;

        while((Math.abs(right_BackM.getCurrentPosition())-Math.abs(right_target) > 10) &&
                (Math.abs(left_BackM.getCurrentPosition())-Math.abs(left_target) > 10) &&
                    !opmode.isStopRequested()){

            right_FrontM.setPower(power);
            right_BackM.setPower(power);
            left_FrontM.setPower(power);
            left_BackM.setPower(power);
        }
    }
    public void turn_relative_robot(double turn_angle, double power){
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double current_angle = angles.firstAngle;

        //adds the turning degrees to the robot angle so that the turn starts form where the robot is facing
        double target_angle = current_angle + turn_angle;


        boolean clockwise = true;
        if(turn_angle > 0){
            right_power = power;
            left_power = -power;
            clockwise = true;
        }
        else{
            right_power = -power;
            left_power = power;
            clockwise = false;
        }

        while(Math.abs(target_angle) - Math.abs(current_angle) > 5 && !opmode.isStopRequested()){
            right_FrontM.setPower(right_power);
            right_BackM.setPower(right_power);
            left_FrontM.setPower(left_power);
            left_BackM.setPower(left_power);
        }


    }
    public void turn_relative_field(double target_angle, double power){
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double current_angle = angles.firstAngle % 360;
        boolean clockwise = true;
        //if clockwise else counter clockwise
        if(current_angle > target_angle){
            right_power = power;
            left_power = -power;
            clockwise = true;
        }
        else{
            right_power = -power;
            left_power = power;
            clockwise = false;
        }

        while(Math.abs(Math.abs(current_angle) - Math.abs(target_angle)) > 5 && !opmode.isStopRequested()){
            right_FrontM.setPower(right_power);
            right_BackM.setPower(right_power);
            left_FrontM.setPower(left_power);
            left_BackM.setPower(left_power);
        }

    }


    public void setUpIMU(){
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // initializes the IMU
        imu = opmode.hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
    }
}
