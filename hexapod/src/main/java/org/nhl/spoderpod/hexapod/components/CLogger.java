package org.nhl.spoderpod.hexapod.components;

import org.nhl.spoderpod.hexapod.core.Message;
import org.nhl.spoderpod.hexapod.core.MessageBus;
import org.nhl.spoderpod.hexapod.interfaces.IMessage;

public final class CLogger extends BaseComponent {
	private final StringBuilder log;

	public CLogger(String name) {
		super(name);
		this.log = new StringBuilder();
	}

	public void init(MessageBus messageBus) {
	}

	@Override
	protected boolean preReceive(MessageBus messageBus) {
		return true;
	}

	public void receive(MessageBus messageBus, IMessage message) {
		if (message instanceof Message) {
			Message m = (Message) message;
			if ("Get".equals(m.getData())) {
				m.getSender().tell(messageBus, getSelf(), this.log.toString());
			} else {
				this.log.append(String.format("From: %s, Data: %s\n",
						m.getSender(), m.getData()));
			}
		}
	}

	public void close(MessageBus messageBus) {
		System.out.println(this.log.toString());
	}
}
