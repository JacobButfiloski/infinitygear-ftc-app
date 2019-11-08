/*
 * TODOLIST:
 * ///TODO: move from test to master
 *
 *
 *
 *
 *
 * DONE: LIST
 * ///TODO: remove references to subassemblies that are no longer used
 * ///TODO: remove legacy imports
 * ///TODO: removed legacy functions
 * ///TODO: removed legacy comments
 */



package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Mecanum Teleop 2 ", group="Iterative Opmode")
public class INFINITYFIRSTteleop extends OpMode {
    private DcMotor leftMotorFront = null;
    private DcMotor rightMotorFront = null;
    private DcMotor leftMotorBack = null;
    private DcMotor rightMotorBack = null;
    private DcMotor leftIntake = null;
    private DcMotor rightIntake = null;

    private Servo armServo = null;
    private Servo beaconServo = null;
    @Override
    public void init() {
        //Motor Initialization
        leftMotorFront = hardwareMap.get(DcMotor.class, "leftMotorFront");
        leftMotorFront = hardwareMap.get(DcMotor.class, "leftMotorFront");
        rightMotorFront = hardwareMap.get(DcMotor.class, "rightMotorFront");
        leftMotorBack = hardwareMap.get(DcMotor.class, "leftMotorBack");
        rightMotorBack = hardwareMap.get(DcMotor.class, "rightMotorBack");

        leftIntake = hardwareMap.get(DcMotor.class, "leftMotorIntake");
        rightIntake = hardwareMap.get(DcMotor.class, "rightMotorIntake");
        armServo = hardwareMap.get(Servo.class, "armServo");
        beaconServo = hardwareMap.get(Servo.class, "beacon");
    }


    //!
    ///Do not delete, could have use at a later time
    /*public void ToggleServo(Servo servo) {
        final double min = 0.0;
        final double max = 1.0;

        if (servo.getPosition() == min) {
            servo.setPosition(max);
        } else if (servo.getPosition() == max) {
            servo.setPosition(min);
        } else {
            //Failsafe
            telemetry.addData("Servo Error", "Position is not min or max, setting to min.");
            servo.setPosition(min);
        }
    }*/

    @Override
    public void loop() {
        //Direction Setting
        rightMotorFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotorBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotorFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftMotorBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightIntake.setDirection(DcMotorSimple.Direction.REVERSE);

        //Value Definition
        float LFspeed = gamepad1.left_stick_y - gamepad1.left_stick_x;
        float LBspeed = gamepad1.left_stick_y + gamepad1.left_stick_x;
        float RFspeed = gamepad1.right_stick_y + gamepad1.left_stick_x;
        float RBspeed = gamepad1.right_stick_y - gamepad1.left_stick_x;

        //Clipping
        LFspeed = Range.clip(LFspeed, -1, 1);
        LBspeed = Range.clip(LBspeed, -1, 1);
        RFspeed = Range.clip(RFspeed, -1, 1);
        RBspeed = Range.clip(RBspeed, -1, 1);




        if(gamepad2.right_bumper)
        {
            leftIntake.setPower(1);
            rightIntake.setPower(1);
        } else if (gamepad2.left_bumper)
        {
            leftIntake.setPower(-1);
            rightIntake.setPower(-1);
        } else {
            leftIntake.setPower(0);
            rightIntake.setPower(0);
        }

        if(gamepad2.x)
        {
            armServo.setPosition(1);
        } else {
            armServo.setPosition(0);
        }

        if(gamepad2.y)
        {
            beaconServo.setPosition(1);
        } else {
            beaconServo.setPosition(0);
        }
        //Speed Setting
        leftMotorFront.setPower(LFspeed);
        leftMotorBack.setPower(LBspeed);
        rightMotorBack.setPower(RBspeed);
        rightMotorFront.setPower(RFspeed);
    }
}