package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Config
@Disabled
@TeleOp

public class FrightFrenzyField extends LinearOpMode {
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
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        imu = hardwareMap.get(imu.getClass(), "imu");
        slideLeft = hardwareMap.get(Servo.class, "slideLeft");
        slideRight = hardwareMap.get(Servo.class, "slideRight");
        slide = hardwareMap.get(Servo.class, "intakeLift");
        cubeScore = hardwareMap.get(Servo.class, "pixelScore");

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
            telemetry.update();

            double forward = (-1 * (gamepad1.left_stick_y));
            double strafe = (gamepad1.left_stick_x);
            double rotate = (gamepad1.right_stick_x);

//            double heading = imu.getRoll(AngleUnit.RADIANS);
//            double heading = imu.getYaw
//            telemetry.addData("Heading", heading);
//            double angle = Math.atan2(forward, strafe);
//            double mag = Math.sqrt(forward*forward+strafe*strafe);
//            forward = Math.sin(angle-heading)*mag;
//            strafe = Math.cos(angle-heading)*mag;

            fL.setPower(0.6 * (forward + strafe + rotate));
            fR.setPower(0.6 * (forward - strafe - rotate));
            bL.setPower(0.6 * (forward - strafe + rotate));
            bR.setPower(0.6 * (forward + strafe - rotate));

            telemetry.addData("Forward", forward);
            telemetry.addData("Strafe", strafe);
            telemetry.addData("Rotate", rotate);
            telemetry.update();

            if (gamepad1.right_bumper) {
                elevatorPosition = 1;
            }
            else if (gamepad1.left_bumper) {
                elevatorPosition = 0;
            }

            if(gamepad1.y){
                scorePosition = 0.4;
            }

            if(gamepad1.right_trigger > 0.5){
                slidePosition = 0.25;
                scorePosition = 0.75;
            }

            if(gamepad1.left_trigger > 0.5){
                slidePosition = 0.6;
                scorePosition = 0.07;
            }

            intakeMotor.setPower(-1 * (gamepad1.right_stick_y));

            elevatorPosition = Range.clip(elevatorPosition, 0, 1);
            elevatorPosition = Range.clip(elevatorPosition, 0, 1);
            slidePosition = Range.clip(slidePosition, 0, 1);
            scorePosition = Range.clip(scorePosition, 0, 1);
            slideLeft.setPosition(elevatorPosition);
            slideRight.setPosition(1 - elevatorPosition);
            cubeScore.setPosition(scorePosition);
            slide.setPosition(slidePosition);

//            telemetry.addData("slideLeft position", elevatorPosition);
//            telemetry.addData("slideRight position", 1 - elevatorPosition);
//            telemetry.addData("slide position", slidePosition);
//            telemetry.addData("cubeScore position", scorePosition);

            telemetry.update();

        }

    }
}
