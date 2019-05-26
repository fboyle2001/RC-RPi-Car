package com.finlay.rc.components.motion;

import com.finlay.rc.components.PiconZero;

public class AssistedTurn {

	private static boolean override = false;
	
	public static void overrideHalt() {
		override = true;
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		override = false;
	}
	
	public static void performUTurn(int speed, int duration) {
		new Thread(() -> {
			PiconZero.getInstance().forward(speed);
			int left = speed;   //-speed
			
			while(left != -speed) {
				if(override) {
					return;
				}
				
				PiconZero.getInstance().setMotorSpeed(0, left);
				left -= 10;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	
			while(left != speed) {
				if(override) {
					return;
				}
				
				PiconZero.getInstance().setMotorSpeed(0, left);
				left += 10;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "AssistedUTurn").start();
	}
	
	public static void turnRight(int speed, int duration) {
		
	}

	public static void turnLeft(int speed, int duration) {
		
	}
	
}
