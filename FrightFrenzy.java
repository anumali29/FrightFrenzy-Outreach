package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

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
    private Servo intakeLift;
    private Servo pixelScore;

    double slideLeftPosition;
    double slideRightPosition;
    double liftPosition;
    double scorePosition;

    ElapsedTime clawTimer = new ElapsedTime();

    @Override
    public void runOpMode(){
        fL = hardwareMap.get(DcMotor.class, "frontLeft");
        fR = hardwareMap.get(DcMotor.class, "frontRight");
        bL = hardwareMap.get(DcMotor.class, "backLeft");
        bR = hardwareMap.get(DcMotor.class, "backRight");
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        imu = hardwareMap.get(Gyroscope.class, "imu");
        slideLeft = hardwareMap.get(Servo.class, "slideLeft");
        slideRight = hardwareMap.get(Servo.class, "slideRight");
        intakeLift = hardwareMap.get(Servo.class, "intakeLift");
        pixelScore = hardwareMap.get(Servo.class, "pixelScore");

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

            fL.setPower(0.6 * (forward + strafe + rotate));
            fR.setPower(0.6 * (forward - strafe - rotate));
            bL.setPower(0.6 * (forward - strafe + rotate));
            bR.setPower(0.6 * (forward + strafe - rotate));

            telemetry.addData("Forward", forward);
            telemetry.addData("Strafe", strafe);
            telemetry.addData("Rotate", rotate);
            telemetry.update();

            if (gamepad1.right_bumper) {
                slideLeftPosition = 1;
                slideRightPosition = -1;
            } else if (gamepad1.left_bumper) {
                slideLeftPosition = -1;
                slideRightPosition = 1;
            }
            if (gamepad1.right_trigger > 0.5){
                scorePosition = 0.41;
                liftPosition = -1;
            }

            if (gamepad1.y){
                scorePosition = 0.71;
            }

            if (gamepad1.left_trigger > 0.5){
                scorePosition -= 0.08;
                liftPosition = 1;
            }

            if (gamepad1.a){
                scorePosition = 0.1;
            }

            intakeMotor.setPower(-1 * (gamepad1.right_stick_y));

            slideLeftPosition = Range.clip(slideLeftPosition, -1, 1);
            slideRightPosition = Range.clip(slideRightPosition, -1, 1);
            liftPosition = Range.clip(liftPosition, 0, 1);
            scorePosition = Range.clip(scorePosition, 0, 1);
            slideLeft.setPosition(slideLeftPosition);
            slideRight.setPosition(slideRightPosition);
            intakeLift.setPosition(liftPosition);
            pixelScore.setPosition(scorePosition);

            telemetry.addData("viperLeft position", slideLeftPosition);
            telemetry.addData("viperRight position", slideRightPosition);
            telemetry.addData("intakeLift position", liftPosition);
            telemetry.addData("pixelScore position", scorePosition);

            telemetry.update();

        }

    }
}
