package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.acmerobotics.dashboard.config.Config;

@Config
@TeleOp

public class TestingServos extends LinearOpMode {
    private Servo slideLeft;
    private Servo slideRight;
    private Servo slide;
    private Servo cubeScore;

    public static double elevatorPosition;
    public static double slidePosition;
    public static double scorePosition;

    @Override
    public void runOpMode(){
        slideLeft = hardwareMap.get(Servo.class, "slideLeft");
        slideRight = hardwareMap.get(Servo.class, "slideRight");
        slide = hardwareMap.get(Servo.class, "intakeLift");
        cubeScore = hardwareMap.get(Servo.class, "pixelScore");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()){

            telemetry.addData("Status", "Running");

            if (gamepad1.right_bumper) {
                elevatorPosition = 1;
            }
            else if (gamepad1.left_bumper) {
                elevatorPosition = 0;
            }

//            if(gamepad1.right_trigger > 0.5){
//                scorePosition = 0.4;
//            }

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

            elevatorPosition = Range.clip(elevatorPosition, 0, 1);
            elevatorPosition = Range.clip(elevatorPosition, 0, 1);
            slidePosition = Range.clip(slidePosition, 0, 1);
            scorePosition = Range.clip(scorePosition, 0, 1);
            slideLeft.setPosition(elevatorPosition);
            slideRight.setPosition(1 - elevatorPosition);
            cubeScore.setPosition(scorePosition);
            slide.setPosition(slidePosition);

//            telemetry.addData("slideLeft Position", elevatorPosition);
//            telemetry.addData("slideRight Position", elevatorPosition);

            //telemetry.addData("slide Position", slidePosition);
            telemetry.addData("cubeScore Position", scorePosition);

            telemetry.update();

        }

    }

}
