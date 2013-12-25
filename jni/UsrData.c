#include "rfid.h"

unsigned char decodeUSR(unsigned char *usr, int *y1, int *m1, int *d1, int *y2, int *m2, int *d2, int *y3, int *m3, int *d3)
{
	*y1 = (int)(usr[0] + 256*(usr[1]&0x0f));
	*m1 = (int)((usr[1]>>4)&0x0f);
	*d1 = (int)(usr[2]);
	*y2 = (int)(usr[3] + 256*(usr[4]&0x0f));
	*m2 = (int)((usr[4]>>4)&0x0f);
	*d2 = (int)(usr[5]);
	*y3 = (int)(usr[6] + 256*(usr[7]&0x0f));
	*m3 = (int)((usr[7]>>4)&0x0f);
	*d3 = (int)(usr[8]);
	unsigned char tm_char = (unsigned char)(usr[9]);
	return tm_char;
}

int encodeUSR(unsigned char *usr, int y1, int m1, int d1, int y2, int m2, int d2, int y3, int m3, int d3)
{
	usr[0] = (unsigned char)y1;
	usr[1] = (unsigned char)(16*m1+y1/256);
	usr[2] = (unsigned char)d1;

	usr[3] = (unsigned char)y2;
	usr[4] = (unsigned char)(16*m2+y2/256);
	usr[5] = (unsigned char)d2;

	usr[6] = (unsigned char)y3;
	usr[7] = (unsigned char)(16*m3+y3/256);
	usr[8] = (unsigned char)d3;

	usr[9] = 0xcf;
	return 1;
}
