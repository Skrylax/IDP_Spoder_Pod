package org.nhl.spoderpod.hexapod.utils;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.nhl.spoderpod.hexapod.interfaces.I_Threaded;


/**
 * Server for routing objects.
 * @author achmed
 *
 */
public final class RouterServer implements I_Threaded {
	private final Thread thread;
	private final ServerSocket serverSocket;
	private final HashMap<String, Socket> connectedClients;

	public RouterServer(int port) {
		this.thread = new Thread(this);
		this.serverSocket = Utils.CreateServerSocket(port);
		this.connectedClients = new HashMap<String, Socket>();
	}

	/**
	 * Close the connections.
	 * @param socket
	 * @return
	 */
	private boolean close(Socket socket) {
		try {
			socket.close();
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * Add the client to the connected client list. If a service name already exists it closes that connection.
	 * @param client
	 */
	private void handleNewConnection(Socket client) {
		String serviceName = "";
		if (this.connectedClients.containsKey(serviceName)) {
			close(this.connectedClients.get(serviceName));
		}
		this.connectedClients.put(serviceName, client);
	}

	public void start() {
		this.thread.start();
	}

	public void stop() {
		this.thread.interrupt();
	}

	public void run() {
		if (this.serverSocket != null && this.thread != Thread.currentThread()) {
			return;
		}
		while (!this.thread.isInterrupted()) {
			try {
				handleNewConnection(this.serverSocket.accept());
			} catch (Exception e) {
			}
		}
	}
}
