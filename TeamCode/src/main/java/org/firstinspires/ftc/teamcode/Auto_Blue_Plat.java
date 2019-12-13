package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Blue Plat", group="Iterative Opmode")
public class Auto_Blue_Plat extends LinearOpMode {

    private DcMotor rightMotorFront;
    private DcMotor rightMotorBack;
    private DcMotor leftMotorFront;
    private DcMotor leftMotorBack;
    private Servo armServo;

    /**
     * Describe this function...
     */
    private void Reset() {
        rightMotorFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotorBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotorFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotorBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void Forward() {
        rightMotorFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotorBack.setDirection(DcMotorSimple.Direction.FORWARD);
        leftMotorFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotorBack.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    private void Backwards() {
        rightMotorFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotorBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotorFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftMotorBack.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    private void Half_Power() {
        leftMotorBack.setPower(0.5);
        leftMotorFront.setPower(0.5);
        rightMotorFront.setPower(0.5);
        rightMotorBack.setPower(0.5);
    }

    private void Left_Strafe() {
        rightMotorFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotorBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotorFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftMotorBack.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void runOpMode() {
        rightMotorFront = hardwareMap.dcMotor.get("rightMotorFront");
        rightMotorBack = hardwareMap.dcMotor.get("rightMotorBack");
        leftMotorFront = hardwareMap.dcMotor.get("leftMotorFront");
        leftMotorBack = hardwareMap.dcMotor.get("leftMotorBack");
        armServo = hardwareMap.servo.get("armServo");

        // Put initialization blocks here.
        //Forward();
        Reset();
        waitForStart();
        if (opModeIsActive()) {
            // Put run blocks here.
            while (opModeIsActive()) {
                // Put loop blocks here.
                Reset();
                while (!(leftMotorBack.getCurrentPosition() >= 2.2 * 1120 || isStopRequested())) {
                    Half_Power();
                    Backwards();
                }
                Reset();
                while (!(leftMotorBack.getCurrentPosition() >= .3 * 1120 || isStopRequested())) {
                    Left_Strafe();
                    Half_Power();
                }
                Reset();
                armServo.setPosition(1);
                Zero_Power();
                sleep(2500);
                while (!(leftMotorBack.getCurrentPosition() >= 2 * 1120 || isStopRequested())) {
                    Half_Power();
                    Forward();
                }
                Reset();
                armServo.setPosition(0);
                sleep(500);
                while (!(leftMotorBack.getCurrentPosition() >= 1.8 * 1120 || isStopRequested())) {
                    Full_Power();
                    Right_Strafe();
                }
                Reset();
                while (!(leftMotorBack.getCurrentPosition() >= 4.35 * 1120 || isStopRequested())) {
                    Half_Power();
                    Backwards();
                }
                Reset();
                while (!(leftMotorBack.getCurrentPosition() >= 2 * 1120 || isStopRequested())) {
                    Half_Power();
                    Left_Strafe();
                }
                Reset();
                while (!(leftMotorBack.getCurrentPosition() >= 3.2 * 1120 || isStopRequested())) {
                    Quater_Power();
                    Forward();
                }
                Reset();
                while (!(leftMotorBack.getCurrentPosition() >= 3.4 * 1120 || isStopRequested())) {
                    Full_Power();
                    Right_Strafe();
                }
                Reset();
                while (!isStopRequested()) {
                    Zero_Power();
                }
            }
        }
    }

    private void Right_Strafe() {
        rightMotorFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotorBack.setDirection(DcMotorSimple.Direction.FORWARD);
        leftMotorFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotorBack.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    private void Full_Power() {
        leftMotorFront.setPower(1);
        leftMotorBack.setPower(1);
        rightMotorFront.setPower(1);
        rightMotorBack.setPower(1);
    }

    private void Zero_Power() {
        leftMotorFront.setPower(0);
        leftMotorBack.setPower(0);
        rightMotorFront.setPower(0);
        rightMotorBack.setPower(0);
    }

    private void Quater_Power() {
        leftMotorFront.setPower(0.25);
        leftMotorBack.setPower(0.25);
        rightMotorFront.setPower(0.25);
        rightMotorBack.setPower(0.25);
    }
}