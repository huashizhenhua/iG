package com.highgic.ig.device;

import java.io.DataOutputStream;

import android.util.Log;

public class RootCommand
{
	private static final String TAG = "RootCommand";
	
	public static boolean command(String command)
	{
		Process process = null;
		DataOutputStream os = null;
		try
		{
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			Log.d(TAG, "command = " + command);
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			Thread.sleep(300);
			os.flush();
			process.waitFor();
		}
		catch (Exception e)
		{
			Log.d(TAG, "ROOT REE fail!" + e.getMessage());
			return false;
		}
		finally
		{
			try
			{
				if (os != null)
				{
					os.close();
				}
				process.destroy();
			}
			catch (Exception e)
			{
			}
		}
		Log.d(TAG, "Root SUC success!");
		return true;
	}
}
