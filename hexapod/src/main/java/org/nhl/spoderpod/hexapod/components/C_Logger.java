package org.nhl.spoderpod.hexapod.components;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.nhl.spoderpod.hexapod.core.Message;
import org.nhl.spoderpod.hexapod.core.MessageBus;
import org.nhl.spoderpod.hexapod.interfaces.I_Message;

/**
 * Simple logger that logs data to memory. It send data to a component that does
 * a get request.
 * 
 * @author achmed
 */
public final class C_Logger extends BaseComponent {
	private final StringBuilder log;
	private File file;
	private FileWriter fw;
	private BufferedWriter bw;

	/**
	 * @param name
	 *            Name of the component.
	 */
	public C_Logger(String name) {
		super(name);
		this.log = new StringBuilder();
	}

	public void init(MessageBus messageBus) {
	}

	@Override
	protected boolean composeMessage(MessageBus messageBus) {
		return true;
	}

	public void receiveMessage(MessageBus messageBus, I_Message message) {
		if (message instanceof Message) {
			Message m = (Message) message;
			if ("Get".equals(m.getData())) {
				m.getSender().tell(messageBus, getSelf(), this.log.toString());
			} else {
				this.log.append(String.format("From: %s, Data: [%s]\n",
						m.getSender(), m.getData()));
			}
			toFile(m);
		}
	}

	private void toFile(I_Message message) {
		try {
			file = new File("C:\\test.dat");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			if (message instanceof Message) {
				Message m = (Message) message;

				bw = new BufferedWriter(new FileWriter(file, true));
				bw.write(m.getData());
				bw.newLine();
				bw.flush();
				bw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close(MessageBus messageBus) {
		System.out.println(this.log.toString());
	}
}
