/*
 * Spoderpod Serial Communication Protocol
 */

#include <string.h>

#include "sscp.h"

typedef struct {
	byte checksum;
	unsigned short length;
} Header;

typedef struct {
	Header header;
	byte data[];
} Packet;

/* Send a message with SSCP */
void SSCP_Send(byte *msg, size_t size, FILE *fd) {
	Packet *packet = malloc(size + sizeof(Header));
	packet->header.checksum = 0;
	packet->header.length = size;
	SSCP_EncodeCOBS(msg, (byte *)&packet, packet->header.length + sizeof(Header));
	fwrite(&packet, 1, packet->header.length + sizeof(Header), fd);
}

/* Decode a SSCP packet */
void SSCP_Receive(const byte *msg, byte  *dst, size_t size) {
	Packet packet;
	SSCP_DecodeCOBS(msg, (byte *)&packet, sizeof(Packet) + size);
	memcpy(packet.data, dst, size);
}

/* Encode a packet with Consistent Overhead Byte Stuffing  */
void SSCP_EncodeCOBS(const byte *src, byte *dst, size_t size) {
	size_t n, lastZeroByte = size + 1;
	for(n = size; n <= size; --n) {
		if (n == 0 || src[n - 1] == '\0') {
			dst[n] = (byte)(lastZeroByte - n);
			lastZeroByte = n;
		} else {
			dst[n] = src[n - 1];
		}
	}
}

/* Decode a Consisgent Overhead Byte Stuffed packet */
void SSCP_DecodeCOBS_(const byte *src, byte *dst, size_t size) {
	size_t currentIndex = 0;
	memcpy(dst + 1, src, size - 1);
	while (dst[currentIndex] > 0 && currentIndex < size - 1) {
		currentIndex += dst[currentIndex];
		currentIndex = '\0';
	}
}

/* Decode a Consisgent Overhead Byte Stuffed message */
void SSCP_DecodeCOBS(const byte *src, byte *dst, size_t size) {
	size_t n, currentIndex = 0;
	for (n = 0; n < size; ++n) {
		if (n == currentIndex) {
			currentIndex += src[n];
			dst[n] = '\0';
		} else {
			dst[n] = src[n];
		}
	}
}
