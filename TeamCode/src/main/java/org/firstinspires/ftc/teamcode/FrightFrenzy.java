package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Config
@TeleOp

public class FrightFrenzy extends LinearOpMode {
    private DcMotor fL;
    private DcMotor fR;
    private DcMotor bL;
    private DcMotor bR;
    private DcMotor intakeMotor;
    private Gyroscope imu;
    private Servo slideLeft;
    private Servo slideRight;
    private Servo slide;
    private Servo cubeScore;
    private ColorSensor colorSensor;

    public static double elevatorPosition;
    public static double slidePosition;
    public static double scorePosition;

    ElapsedTime clawTimer = new ElapsedTime();

    @Override
    public void runOpMode(){
        fL = hardwareMap.get(DcMotor.class, "frontLeft");
        fR = hardwareMap.get(DcMotor.class, "frontRight");
        bL = hardwareMap.get(DcMotor.class, "backLeft");
        bR = hardwareMap.get(DcMotor.class, "backRight");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        imu = hardwareMap.get(Gyroscope.class, "imu");
        slideLeft = hardwareMap.get(Servo.class, "slideLeft");
        slideRight = hardwareMap.get(Servo.class, "slideRight");
        slide = hardwareMap.get(Servo.class, "bringBack");
        cubeScore = hardwareMap.get(Servo.class, "cubeScore");
        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");

        fL.setDirection(DcMotorSimple.Direction.REVERSE);
        fR.setDirection(DcMotorSimple.Direction.FORWARD);
        bL.setDirection(DcMotorSimple.Direction.REVERSE);
        bR.setDirection(DcMotorSimple.Direction.FORWARD);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        clawTimer.startTime();

        fL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        fL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        while(opModeIsActive()){
            telemetry.addData("Status", "Running");
//            telemetry.update();

            double forward = (-1 * (gamepad1.left_stick_y));
            double strafe = (gamepad1.left_stick_x);
            double rotate = (gamepad1.right_stick_x);

            fL.setPower(0.6 * (forward + strafe + rotate));
            fR.setPower(0.6 * (forward - strafe - rotate));
            bL.setPower(0.6 * (forward - strafe + rotate));
            bR.setPower(0.6 * (forward + strafe - rotate));

//            telemetry.addData("Forward", forward);
//            telemetry.addData("Strafe", strafe);
//            telemetry.addData("Rotate", rotate);
//            telemetry.update();

            if (gamepad1.right_bumper){ //moves elevator up
                elevatorPosition = 1;
            }
            else if (gamepad1.left_bumper){ //moves elevator down
                elevatorPosition = 0;
            }

            if(gamepad1.y){
                scorePosition = 0.4;
            }

            if(gamepad1.right_trigger > 0.5){ //scores
                slidePosition = 0.25;
                scorePosition = 0.75;
            }

            if(gamepad1.left_trigger > 0.5){ //unscores lol
                slidePosition = 0.6;
                scorePosition = 0.07;
            }

            intakeMotor.setPower(-1 * (gamepad1.right_stick_y));

            elevatorPosition = Range.clip(elevatorPosition, 0, 1);
            slidePosition = Range.clip(slidePosition, 0, 1);
            scorePosition = Range.clip(scorePosition, 0, 1);
            slideLeft.setPosition(elevatorPosition);
            slideRight.setPosition(1 - elevatorPosition);
            cubeScore.setPosition(scorePosition);
            slide.setPosition(slidePosition);

//            telemetry.addData("slideLeft position", elevatorPosition);
//            telemetry.addData("slideRight position", 1 - elevatorPosition);
            telemetry.addData("slide position", slidePosition);
//            telemetry.addData("cubeScore position", scorePosition);
            telemetry.addData("red", sampleDetection("red"));
            telemetry.addData("blue", sampleDetection("blue"));
            telemetry.addData("red v", colorVals("red"));
            telemetry.addData("blue v", colorVals("blue"));
            telemetry.addData("green v", colorVals("g"));

            telemetry.update();

        }

    }

    public double colorVals(String alliance) {
        double r = colorSensor.red();
        double g = colorSensor.green();
        double b = colorSensor.blue();

        if (alliance.equals("red")) {
            return r;
        } else if (alliance.equals("blue")) {
            return b;
        }
        return g;
    }

    public boolean sampleDetection(String alliance) {
        double r = colorSensor.red();
        double g = colorSensor.green();
        double b = colorSensor.blue();

        if (alliance.equals("red") && ((r > g && r > b && r > 300) || (g > r && g > b && g > 300))) {
            return true;
        }
        else if (alliance.equals("blue") && ((b > r && b > g && b > 300) || (g > r && g > b && g > 300))){
            return true;
        }
        return false;
//        boolean redTol = (r >= 180) && (r <= 255); //tolerances for yellow
//        boolean greenTol = (g >= 160) && (g <= 240);
//        boolean blueTol = (b >= 1) && (b <= 120);
//
//        if (alliance.equals("red")) {
//            if((r > 120) && (g < 128) && (b < 128)){ //red
//                return true;
//            }
//        }
//        if (alliance.equals("blue")) {
//            if((r < 128) && (g < 128) && (b > 180)){ //blue
//                return true;
//            }
//        }
//        else if (redTol && greenTol && blueTol){
//            return true;
//        }
//        return false;
    }
}
