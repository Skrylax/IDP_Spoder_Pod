package org.nhl.spoder.hexapod.movementservice;

import java.util.Arrays;

import org.nhl.spoderpod.hexapod.libraries.L_Encoder;

public final class SpoderLeg {
	private final int legId;
	private final Servo[] servoArray;
	private final int forceMultiplier;

	public SpoderLeg(int id, Servo[] servoArray, int forceMultiplier) {
		this.legId = id;
		this.servoArray = servoArray;
		this.forceMultiplier = forceMultiplier;
	}

	public int getLegId() {
		return this.legId;
	}

	public void updateServos(int x, int y, int z) {
		servoArray[0].setServo(servoArray[0].getOffset()
				+ Calculations.InsideServo(x, y, z) * this.forceMultiplier);
		servoArray[1].setServo(servoArray[1].getOffset()
				+ Calculations.MiddleServo(x, y, z) * this.forceMultiplier);
		servoArray[2].setServo(servoArray[2].getOffset()
				- Calculations.OutsideServo(x, y, z) * this.forceMultiplier);
		
		//System.out.format("LegId: %d: %s\n", legId, toString());
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("%s", Arrays.toString(servoArray));
	}

	public void sendPacket() {
		for (int n = 0; n < servoArray.length; n++) {
			L_Encoder.addData((byte) 1, (byte) (legId + n + 1),
					(short) servoArray[n].getPosition());
		}
	}
}
