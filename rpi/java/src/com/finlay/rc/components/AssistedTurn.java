package com.finlay.rc.components;

public class AssistedTurn {

	public static void turnRight(int speed, int duration) {
		PiconZero.getInstance().forward(speed);
		int left = speed;   //-speed
		
		while(left != -speed) {
			PiconZero.getInstance().setMotorSpeed(0, left);
			left -= 10;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		while(left != speed) {
			PiconZero.getInstance().setMotorSpeed(0, left);
			left += 10;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void turnLeft(int speed, int duration) {
		
	}
	
}
