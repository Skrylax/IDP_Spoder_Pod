package org.nhl.spoderpod.hexapod.libraries;

import java.util.ArrayList;
import java.util.List;

public class L_Decoder {
	private static byte type;
	private static byte id;
	private static short[] data;
	private static byte checkSum;
	private static byte destination;
	private static List<Byte> dataMsg = new ArrayList<Byte>();

	public static void recieveMsg(List<Byte> msg) {
		dataMsg = msg;
		killZero();
		DecodeCOBS(dataMsg);
		System.out.println(dataMsg);
		boolean check = getCheckSum(msg);
		if (check == true)
			readMessage(msg);
	}

	private static void killZero() {
		dataMsg.remove(dataMsg.size() - 1);
	}

	/***
	 * sum all bytes, overload is checksum.
	 * 
	 * @return
	 */
	private static boolean getCheckSum(List<Byte> msg) {
		byte x = 0;
		for (byte b : msg) {
			x += b;
		}
		checkSum = x;
		if (x != msg.get(0)) {
			return false;
		}
		return true;
	}

	private void findDestination(List<Byte> msg) {
		destination = msg.get(1);
	}

	private static void DecodeCOBS(List<Byte> msg) {
		int n;
		int lastZeroByte = msg.size() - 1;
		while (lastZeroByte > -1) {
			n = lastZeroByte;
			lastZeroByte -= msg.get(lastZeroByte);

			msg.set(n, (byte) 0);
		}
		dataMsg = msg;
	}

	private static void readMessage(List<Byte> msg) {
		for (int i = 2; i < msg.size(); i += 4) {
			if (msg.size() - 2 == i) {
				break;
			}
			type = msg.get(i + 1);
			id = msg.get(i + 2);
			data[0] = msg.get(i + 4);
			data[1] = msg.get(i + 3);
		}
	}

	public static void getData() {
		System.out.println(checkSum);
		System.out.println(destination);
		System.out.println(type);
		System.out.println(id);
		System.out.println(data);
	}

}