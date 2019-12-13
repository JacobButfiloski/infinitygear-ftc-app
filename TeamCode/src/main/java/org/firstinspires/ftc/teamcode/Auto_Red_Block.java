package org.firstinspires.ftc.teamcode;

import android.util.Half;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Red Block", group="Iterative Opmode")
public class Auto_Red_Block extends LinearOpMode {

    private DcMotor rightMotorFront;
    private DcMotor rightMotorBack;
    private DcMotor leftMotorFront;
    private DcMotor leftMotorBack;
    private Servo armServo;

    private void Right_Strafe() {
        rightMotorFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotorBack.setDirection(DcMotorSimple.Direction.FORWARD);
        leftMotorFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotorBack.setDirection(DcMotorSimple.Direction.FORWARD);
        Half_Power();
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

    private void Forward() {
        rightMotorFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotorBack.setDirection(DcMotorSimple.Direction.FORWARD);
        leftMotorFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotorBack.setDirection(DcMotorSimple.Direction.REVERSE);
    }

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

    private void Left_Strafe() {
        rightMotorFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotorBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotorFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftMotorBack.setDirection(DcMotorSimple.Direction.REVERSE);
        Half_Power();
    }

    @Override
    public void runOpMode() {
        rightMotorFront = hardwareMap.dcMotor.get("rightMotorFront");
        rightMotorBack = hardwareMap.dcMotor.get("rightMotorBack");
        leftMotorFront = hardwareMap.dcMotor.get("leftMotorFront");
        leftMotorBack = hardwareMap.dcMotor.get("leftMotorBack");
        armServo = hardwareMap.servo.get("armServo");

        // Put initialization blocks here.
        Forward();
        Reset();
        waitForStart();
        if (opModeIsActive()) {
            // Put run blocks here.
            while (opModeIsActive()) {
                // Put loop blocks here.
                while (!(leftMotorBack.getCurrentPosition() >= 2.4 * 1120 || isStopRequested())) {
                    Half_Power();
                    Backwards();
                }
                Reset();
                armServo.setPosition(0.99);
                Zero_Power();
                sleep(2500);
                while (!(leftMotorBack.getCurrentPosition() >= 1.25 * 1120 || isStopRequested())) {
                    Half_Power();
                    Forward();
                }
                armServo.setPosition(0);
                Reset();
                Zero_Power();
                sleep(500);
                while (!(leftMotorBack.getCurrentPosition() >= .15 * 1120 || isStopRequested())) {
                    Half_Power();
                    Forward();
                }
                Zero_Power();
                sleep(500);
                Reset();
                while (!(leftMotorBack.getCurrentPosition() >= 1.5 * 1120 || isStopRequested())) {
                    Left_Strafe();
                }
                Reset();
                Zero_Power();
                sleep(500);
                while (!(leftMotorBack.getCurrentPosition() >= .9 * 1120 || isStopRequested())) {
                    Half_Power();
                    Backwards();
                }
                Zero_Power();
                sleep(500);
                Reset();
                while (!(leftMotorBack.getCurrentPosition() >= 1.45 * 1120 || isStopRequested())) {
                    Right_Strafe();
                }
                sleep(2500);
                Reset();
                while (!isStopRequested()) {
                    Zero_Power();
                }
            }
        }
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
}