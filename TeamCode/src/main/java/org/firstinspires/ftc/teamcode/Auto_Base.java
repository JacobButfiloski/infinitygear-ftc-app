package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous (name="Base Auto", group="Iterative Opmode")
public class Auto_Base extends LinearOpMode
{
    //Measurements
    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.6;
    static final double SLOW_SPEED = 0.3;
    static final double TURN_SPEED = 0.5;

    //Autonomous Vars
    private ElapsedTime runtime = new ElapsedTime();

    //Hardware
    private DcMotor leftMotorFront = null;
    private DcMotor rightMotorFront = null;
    private DcMotor leftMotorBack = null;
    private DcMotor rightMotorBack = null;

    private DcMotor[] motors = {};

    //Initialize and assign hardware
    @Override
    public void runOpMode()
    {
        leftMotorFront = hardwareMap.get(DcMotor.class, "leftMotorFront");
        rightMotorFront = hardwareMap.get(DcMotor.class, "rightMotorFront");
        leftMotorBack = hardwareMap.get(DcMotor.class, "leftMotorBack");
        rightMotorBack = hardwareMap.get(DcMotor.class, "rightMotorBack");

        rightMotorBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotorFront.setDirection(DcMotorSimple.Direction.REVERSE);

        //Telemetry to show motor values
        telemetry.addData("Path0", "Starting at %7d :%7d",
                leftMotorFront.getCurrentPosition(),
                rightMotorFront.getCurrentPosition(),
                leftMotorBack.getCurrentPosition(),
                rightMotorBack.getCurrentPosition());
        telemetry.update();

        encoderDrive(.5, 2.0, 2.0, .8);
        encoderDrive(.5,  3.0,  1.0, .8);
    }

    public void encoderDrive(double speed, double leftInches, double rightInches, double timeoutS)
    {
        int newLeftTarget;
        int newRightTarget;

        if(opModeIsActive())
        {
            newLeftTarget = leftMotorFront.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newRightTarget = rightMotorFront.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            leftMotorFront.setTargetPosition(newLeftTarget);
            rightMotorFront.setTargetPosition(newRightTarget);
            leftMotorBack.setTargetPosition(newLeftTarget);
            rightMotorBack.setTargetPosition(newRightTarget);

            //Run to position
            leftMotorFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotorFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftMotorBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotorBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //reset
            runtime.reset();
            leftMotorFront.setPower(Math.abs(speed));
            rightMotorFront.setPower(Math.abs(speed));
            leftMotorBack.setPower(Math.abs(speed));
            rightMotorBack.setPower(Math.abs(speed));

            while(opModeIsActive() && (runtime.seconds() < timeoutS) && (leftMotorFront.isBusy() && rightMotorFront.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        leftMotorFront.getCurrentPosition(),
                        rightMotorFront.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            rightMotorFront.setPower(0);
            leftMotorFront.setPower(0);
            rightMotorBack.setPower(0);
            leftMotorBack.setPower(0);


            // Turn off RUN_TO_POSITION
            leftMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }

}
