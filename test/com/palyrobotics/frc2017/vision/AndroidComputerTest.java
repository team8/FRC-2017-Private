package com.palyrobotics.frc2017.vision;

import com.palyrobotics.frc2017.config.Constants;

public class AndroidComputerTest {

	public static void main(String[] args) throws InterruptedException {

		VisionManager.getInstance().start(Constants.kAndroidConnectionUpdateRate,true);

//		while (true) {
//
//			if (VisionManager.getInstance().isAppStarted()) {
//
////			System.out.println(String.format("X: %b, Y: %b", VisionData.getXData().exists(), VisionData.getZData().exists()));
////
//				final String x = Double.toString(VisionData.getXDataValue()), y = Double.toString(VisionData.getZDataValue());
//
//				System.out.println(String.format("X: %s, Y: %s", x, y));
//			}
//			Thread.sleep(500);
//		}
	}
}
