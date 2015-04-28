package org.nhl.spoderpod.hexapod.core;

import java.util.Arrays;
import java.util.List;

import org.nhl.spoderpod.hexapod.interfaces.IComponent;
import org.nhl.spoderpod.hexapod.interfaces.IThreaded;

public final class Service implements IThreaded {
	private final ComponentRef self;
	private final Thread thread;
	private final MessageBus messageBus;
	private final List<IComponent> components;
	private volatile boolean running;

	public Service(String name, IComponent[] components) {
		this.self = new ComponentRef(name);
		this.thread = new Thread(this);
		this.messageBus = new MessageBus();
		this.components = Arrays.asList(components);
	}

	public void start() {
		if (!this.running) {
			this.thread.start();
		}
	}

	public void stop() {
		this.running = false;
	}

	private void init() {
		for (IComponent component : this.components) {
			this.messageBus.addComponent(component.getSelf());
			component.init(this.messageBus);
		}
	}

	private void close() {
		for (IComponent component : this.components) {
			component.close(this.messageBus);
		}
	}

	private void tick() {
		for (IComponent component : this.components) {
			component.update(this.messageBus);
		}
	}

	public void run() {
		if (this.running || Thread.currentThread() != this.thread) {
			return;
		}
		init();
		this.running = true;
		while (this.running) {
			tick();
		}
		close();
	}
}
