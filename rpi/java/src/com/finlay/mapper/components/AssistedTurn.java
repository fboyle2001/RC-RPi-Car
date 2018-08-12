package com.finlay.mapper.components;

public class AssistedTurn {

	public static void turnRight(int speed, int duration) {
		PiconZero.getInstance().right(speed);
		long start = System.nanoTime();
		double half = duration / 2;
		
		int current = speed;
		
		while(System.nanoTime() - start < half) {
			current -= 10;
			
			if(current < 0) {
				current = 0;
			}
			
			PiconZero.getInstance().setMotorSpeed(1, current);
		}
		
		start = System.nanoTime();
		
		while(System.nanoTime() - start < half) {
			current += 10;
			
			if(current > speed) {
				current = speed;
			}
			
			PiconZero.getInstance().setMotorSpeed(1, current);
		}
		
		PiconZero.getInstance().forward(speed);
	}

	public static void turnLeft(int speed, int duration) {
		
	}
	
}
