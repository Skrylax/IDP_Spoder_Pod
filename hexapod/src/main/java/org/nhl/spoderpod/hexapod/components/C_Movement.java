package org.nhl.spoderpod.hexapod.components;

import org.nhl.spoderpod.hexapod.core.ComponentRef;
import org.nhl.spoderpod.hexapod.core.MessageBus;
import org.nhl.spoderpod.hexapod.interfaces.I_Message;
import org.nhl.spoderpod.hexapod.utils.U_MovementCsvReader;
import org.nhl.spoderpod.hexapod.utils.U_MovementServo;
import org.nhl.spoderpod.hexapod.utils.U_MovementServoMovement;
import org.nhl.spoderpod.hexapod.utils.U_MovementSpoderLeg;

public final class C_Movement extends BaseComponent {
	
	private final U_MovementServoMovement servoMovement;
	

	public C_Movement(String name){
		super(name);
		servoMovement = new U_MovementServoMovement();		
		
	}
	
	public void init(MessageBus messageBus) {
		servoMovement.start();
	}

	public void close(MessageBus messageBus) {
		servoMovement.stop();

	}

	@Override
	protected boolean composeMessage(MessageBus messageBus) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void receiveMessage(MessageBus messageBus, I_Message message) {
		
	}

}
