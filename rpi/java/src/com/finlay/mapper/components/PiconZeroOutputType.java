package com.finlay.mapper.components;

public enum PiconZeroOutputType {

	DIGITAL(0, (value) -> {
		return value == 0 || value == 1;
	}),
	
	PWM(1, (value) -> {
		return value >= 0 && value <= 100;
	}),
	
	SERVO(2, (value) -> {
		return value >= -100 && value <= 100;
	});
	
	private int value;
	private PiconZeroOutputValidation validation;
	
	private PiconZeroOutputType(int value, PiconZeroOutputValidation validation) {
		this.value = value;
		this.validation = validation;
	}
	
	public byte getValue() {
		return (byte) value;
	}
	
	public boolean isInputValid(int value) {
		return validation.isInputValid(value);
	}
	
	@FunctionalInterface
	interface PiconZeroOutputValidation {
		
		boolean isInputValid(int value);
		
	}
	
}
