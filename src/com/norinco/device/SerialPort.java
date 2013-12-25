package com.norinco.device;

import java.io.File;
import java.io.IOException;
import android.util.Log;

public class SerialPort {

	private static final String TAG = "SerialPort";

	/*
	 * Do not remove or rename the field mFd: it is used by native method close();
	 */
	private int mFd;

	public SerialPort(File device, int baudrate) throws SecurityException, IOException {

		/* Check access permission */
		if (!device.canRead() || !device.canWrite()) {
			try {
				/* Missing read/write permission, trying to chmod the file */
				Process su;
				su = Runtime.getRuntime().exec("/system/xbin/su");
				String cmd = "chmod 777 " + device.getAbsolutePath() + "\n"	+ "exit\n";
				su.getOutputStream().write(cmd.getBytes());
				if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
					throw new SecurityException();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new SecurityException();
			}
		}
		mFd = open(device.getAbsolutePath(), baudrate);
		if (mFd == -1) {
			Log.e(TAG, "native open returns null");
			throw new IOException();
		}
		else
			Log.d(TAG, "open success");		
	}
	
	public int getmFd()
	{
		return mFd;
	}

	// JNI
	private native static int open(String path, int baudrate);
	public native void close(int mFd);
	static {
		System.loadLibrary("serial_port");
	}
}
