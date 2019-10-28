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

    @Override
    public void init() {
        //Motor Initialization
        leftMotorFront = hardwareMap.get(DcMotor.class, "leftMotorFront");
        leftMotorFront = hardwareMap.get(DcMotor.class, "leftMotorFront");
        rightMotorFront = hardwareMap.get(DcMotor.class, "rightMotorFront");
        leftMotorBack = hardwareMap.get(DcMotor.class, "leftMotorBack");
        rightMotorBack = hardwareMap.get(DcMotor.class, "rightMotorBack");
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
        rightMotorFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotorBack.setDirection(DcMotorSimple.Direction.FORWARD);
        leftMotorFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotorBack.setDirection(DcMotorSimple.Direction.REVERSE);

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

        //Speed Setting
        leftMotorFront.setPower(LFspeed);
        leftMotorBack.setPower(LBspeed);
        rightMotorBack.setPower(RBspeed);
        rightMotorFront.setPower(RFspeed);
    }
}