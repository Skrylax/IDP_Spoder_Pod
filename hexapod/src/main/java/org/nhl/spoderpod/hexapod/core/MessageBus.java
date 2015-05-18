package org.nhl.spoderpod.hexapod.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.nhl.spoderpod.hexapod.interfaces.IMessage;

/**
 * The MessageBus is a mailbox for all messages send to all components.
 * @author achmed
 *
 */
public final class MessageBus {
	private final Map<ComponentRef, Queue<IMessage>> messages;

	public MessageBus() {
		this.messages = new HashMap<ComponentRef, Queue<IMessage>>();
	}

	/**
	 * Make a mailbox for this component.
	 * @param recipient Reference to the component.
	 */
	public void addComponent(ComponentRef recipient) {
		this.messages.put(recipient, new ConcurrentLinkedQueue<IMessage>());
	}

	/**
	 * Send a message to a component. Component reference to be used are retrieved from the message.
	 * @param message
	 * @return
	 */
	public boolean send(IMessage message) {
		if (this.messages.containsKey(message.getRecipient())) {
			this.messages.get(message.getRecipient()).add(message);
			return true;
		}
		return false;
	}

	/**
	 * Get and remove a message from the mailbox.
	 * @param recipient Reference to the owner of the mailbox.
	 * @return
	 */
	public IMessage getMessage(ComponentRef recipient) {
		return this.messages.get(recipient).poll();
	}

	/**
	 * Check if the component already has a mailbox.
	 * @param recipient Reference to the component to check
	 * @return
	 */
	public boolean hasMessage(ComponentRef recipient) {
		return this.messages.containsKey(recipient)
				&& !this.messages.get(recipient).isEmpty();
	}
}
