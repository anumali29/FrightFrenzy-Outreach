package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class Auto extends LinearOpMode {

    @Override
    public void runOpMode(){
        Drivetrain dt = new Drivetrain(this, hardwareMap);

        ElapsedTime runtime = new ElapsedTime();
        runtime.startTime();

        waitForStart();

        while (opModeIsActive()) {
            dt.wait(300);

            break;
        }
    }
}
