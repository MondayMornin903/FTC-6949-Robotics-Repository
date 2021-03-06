/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Main Code", group="Iterative Opmode")
//@Disabled
public class MainCode extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    DcMotor LeftFront;
    DcMotor LeftBack;
    DcMotor RightFront;
    DcMotor RightBack;
    DcMotor racklift;
    DcMotor car;
    DcMotor linearlift;
    //Servos
    Servo l;
    Servo r;
    Servo capclaw;
    Servo pivot;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        LeftBack = hardwareMap.dcMotor.get("lb");
        LeftFront = hardwareMap.dcMotor.get("lf");
        RightBack = hardwareMap.dcMotor.get("rb");
        RightFront = hardwareMap.dcMotor.get("rf");
        racklift = hardwareMap.dcMotor.get("racklift");
        linearlift = hardwareMap.dcMotor.get("linearlift");
        car = hardwareMap.dcMotor.get("car");

        l = hardwareMap.servo.get("l");
        capclaw = hardwareMap.servo.get("capclaw");
        r = hardwareMap.servo.get("r");
        pivot = hardwareMap.servo.get("pivot");

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // Setup a variable for each drive wheel to save power level for telemetry
        float gamepad1LeftY = -gamepad1.left_stick_y;
        float gamepad1LeftX = gamepad1.left_stick_x;
        float gamepad1RightX = gamepad1.right_stick_x;

        //Mecanum Formula

        float FrontLeft = -gamepad1LeftY  -gamepad1LeftX  -gamepad1RightX;
        float FrontRight = gamepad1LeftY  -gamepad1LeftX  -gamepad1RightX;
        float BackLeft = -gamepad1LeftY  +gamepad1LeftX  -gamepad1RightX;
        float BackRight = gamepad1LeftY  +gamepad1LeftX  -gamepad1RightX;

        FrontLeft = Range.clip(FrontLeft,-1,1);
        FrontRight = Range.clip(FrontRight,-1,1);
        BackLeft = Range.clip(BackLeft,-1,1);
        BackRight = Range.clip(BackRight,-1,1);

        LeftFront.setPower(FrontLeft);
        LeftBack.setPower(BackLeft);
        RightFront.setPower(FrontRight);
        RightBack.setPower(BackRight);
        racklift.setPower(gamepad2.left_stick_y);
        linearlift.setPower(gamepad2.right_stick_y);
        //
        if(gamepad1.a) {
            l.setPosition(0.1);
            r.setPosition(0.5);
        } else if (gamepad1.b) {
            l.setPosition(0.6);
            r.setPosition(0);
        }
        //
        if(gamepad1.x) {
            capclaw.setPosition(0);
        } else if (gamepad1.y) {
            capclaw.setPosition(.6);
        }
        //
        if(gamepad2.dpad_left) {
            car.setPower(.8);
        } else if(gamepad2.dpad_right) {
            car.setPower(-.8);
        } else {
            car.setPower(0);
        }
        //
        if(gamepad2.dpad_up) {
            pivot.setPosition(1);
        } else if (gamepad2.dpad_down) {
            pivot.setPosition(0);
        }


        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
