package com.coolerfall.lunarlite.utils;

import android.content.Context;
import android.support.annotation.RawRes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Some miscellaneous utils here.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 * @since Feb. 29, 2016
 */
public class Misc {
	/**
	 * Copy files in raw into destination file.
	 *
	 * @param context context
	 * @param resid   the resource id of file in raw
	 * @param file    the file to copy to
	 * @throws java.io.IOException
	 * @throws InterruptedException
	 */
	public static void copyRawFile(Context context, @RawRes int resid, File file)
		throws IOException {
		final InputStream is = context.getResources().openRawResource(resid);
		final FileOutputStream out = new FileOutputStream(file);
		byte buf[] = new byte[1024];
		int len;
		while ((len = is.read(buf)) > 0) {
			out.write(buf, 0, len);
		}

		out.close();
		is.close();
	}

	/**
	 * Copy database in raw into database directory.
	 *
	 * @param context  the context to use
	 * @param resid    resource id
	 * @param filename database filename
	 * @return true if copy successfully, otherwise return false
	 */
	public static boolean copyRawDatabase(Context context, @RawRes int resid, String filename) {
		File dbFile = context.getDatabasePath(filename);
		if (!dbFile.getParentFile().exists()) {
			dbFile.getParentFile().mkdirs();
		}

		try {
			copyRawFile(context, resid, dbFile);
		} catch (IOException e) {
			return false;
		}

		return true;
	}
}
