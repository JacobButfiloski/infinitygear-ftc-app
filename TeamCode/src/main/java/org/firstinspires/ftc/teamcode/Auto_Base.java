package org.firstinspires.ftc.teamcode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

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
    static final String JSON = "{\"command\":[\"MOVE\",\"WAIT\",\"TOGGLEARM\"],\"arg\":[\"20\",\"300\",\"\",\"\",\"\"],\"arg2\":[\"20\",\"300\",\"\",\"\",\"\"]}\"";
    static final boolean useJSON = false;
    static private AutoCommands commands = new AutoCommands();
    public static final String TAG = "Vuforia VuMark Sample";

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    //Autonomous Vars
    private ElapsedTime runtime = new ElapsedTime();

    //Hardware
    private DcMotor leftMotorFront = null;
    private DcMotor rightMotorFront = null;
    private DcMotor leftMotorBack = null;
    private DcMotor rightMotorBack = null;
    private DcMotor leftIntake = null;
    private DcMotor rightIntake = null;

    private Servo armServo = null;
    private Servo leftArmServo = null;
    private Servo rightArmServo = null;

    //External
    Gson gson = new GsonBuilder().create();

    private DcMotor[] motors = {};

    private static final String VUFORIA_KEY = "AV5J3VH/////AAABmUzrZLjpXUnZhQkg7Cftp+ocILx7axVLhz/mYK9YOh+/wkUzBIN5ItpeP7EECqQ/doezbGxGOZ3jdbqdSVQhNyd4lYs6HEgOhENyUUxd44Zsa7Y8TWMep9sb4jBUH2rxowGjJfEwIHFD5FFgeHvGFo8Cc2F0gBTs9sN8p5QTo/f3a7mD0rGqJiCozJvY9xnJRYDz3uiR+Mzk9dc/4YWD/m5NYzUTIHAuieSloUYWLatwMrREoz04312bEWIALaKMCmHdmvrWxJTZeohHLUdeyBj2E0L2MQD/xbUyM9HBMdFGY6wel/dko7X6kuTV1mTdU89qj9d4T0jZVH3+l2sSpR2uApcZZ0QbJTXMdQFL1Tfb";

    private VuforiaLocalizer vuforia;

    private TFObjectDetector tfod;
    //Initialize and assign hardware
    public void deserializeJSON(String s)
    {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        AutoCommands c = gson.fromJson(s, AutoCommands.class); //Deserializes JSON string into obj
        commands = c;

    }
    @Override
    public void runOpMode() {
        leftMotorFront = hardwareMap.get(DcMotor.class, "leftMotorFront");
        leftMotorFront = hardwareMap.get(DcMotor.class, "leftMotorFront");
        rightMotorFront = hardwareMap.get(DcMotor.class, "rightMotorFront");
        leftMotorBack = hardwareMap.get(DcMotor.class, "leftMotorBack");
        rightMotorBack = hardwareMap.get(DcMotor.class, "rightMotorBack");

        leftIntake = hardwareMap.get(DcMotor.class, "leftMotorLift");
        rightIntake = hardwareMap.get(DcMotor.class, "rightMotorLift");
        armServo = hardwareMap.get(Servo.class, "armServo");
        leftArmServo = hardwareMap.get(Servo.class, "leftArm");
        rightArmServo = hardwareMap.get(Servo.class, "rightArm");

        armServo.setPosition(0);
        //leftArmServo.setPosition(0);
        //rightArmServo.setPosition(0);

        rightMotorBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotorFront.setDirection(DcMotorSimple.Direction.REVERSE);

        //Telemetry to show motor values
        telemetry.addData("Path0", "Starting at %7d :%7d",
                leftMotorFront.getCurrentPosition(),
                rightMotorFront.getCurrentPosition(),
                leftMotorBack.getCurrentPosition(),
                rightMotorBack.getCurrentPosition());
        telemetry.update();

        //initVuforia();

        /*if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }*/

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        /*if (tfod != null) {
            tfod.activate();
        }*/

        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        waitForStart();

        /*if (useJSON) {
            for (int i = 0; i < commands.command.length; i++) {
                switch (commands.command[i]) {
                    case "MOVE":
                        encoderDrive(1, Double.parseDouble(commands.arg[i]), Double.parseDouble(commands.arg2[i]), 1);
                        break;
                    case "WAIT":
                        sleep(Long.parseLong(commands.arg[i]));
                        break;
                    case "TOGGLEARM":
                        ToggleArm();
                        break;
                }

                sleep(50);
            }
        }*/
        float left = 0;
        float right = 0;
        float avg;
        boolean skystone = false;
            /*if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());

                    // step through the list of recognitions and display boundary info.
                    int i = 0;
                    for (Recognition recognition : updatedRecognitions) {
                        telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                        telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                recognition.getLeft(), recognition.getTop());
                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                recognition.getRight(), recognition.getBottom());
                        if(recognition.getLabel() == "Skystone")
                        {
                            left = recognition.getLeft();
                            right = recognition.getRight();
                            avg = left - right;

                            if(avg > 0)
                            {
                                while(avg > 0) {
                                    //move left
                                    encoderDrive(2, -2, 2, .2);
                                }
                            } else if(avg < 0)
                            {
                                while(avg < 0)
                                {
                                    encoderDrive(2, -2, 2, .2);
                                }
                            }
                        }
                    }
                    telemetry.update();
                    if(skystone)
                    {
                        avg = left-right;
                    }

                }
            }*/
            encoderDrive(10, -100, 100, 0);
            encoderDrive(10, -100, 100, 0);
            encoderDrive(10, -100, 100, 0);
            encoderDrive(10, -100, 100, 0);
            if(!leftMotorBack.isBusy() && !leftMotorFront.isBusy() && !rightMotorBack.isBusy() && !rightMotorFront.isBusy()){
            stop();}

    }

    private String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }

    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }
    public boolean intakeOn = false;
    public void ToggleIntake()
    {
        if(!intakeOn)
        {
            intakeOn = true;
            leftIntake.setPower(-1);
            rightIntake.setPower(-1);
        } else {
            intakeOn = false;
            leftIntake.setPower(0);
            rightIntake.setPower(0);
        }
    }

    public boolean armToggled = false;
    public void ToggleArm()
    {
        if(!armToggled) { armServo.setPosition(1); } else { armServo.setPosition(0); }
        armToggled = !armToggled;
    }

    public void encoderDrive(double speed, double leftInches, double rightInches, double timeoutS)
    {
        leftMotorFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotorFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotorBack.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotorBack.setDirection(DcMotorSimple.Direction.REVERSE);
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

class AutoCommands {
    @SerializedName("command")
    public String[] command;

    @SerializedName("arg")
    public String[] arg;

    @SerializedName("arg2")
    public String[] arg2;
}
