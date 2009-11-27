package example;

// $Id: GZip.java,v 1.1 2009/11/13 10:22:33 tim Exp $
/*
 * Copyright (c) 2007  Thomas Pornin
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.bolet.jgz.GZipOutputStream;

/**
 * <p>This simple application compresses one or several files, using
 * the gzip file format. It is intended for demonstration purposes;
 * it is <strong>not</strong> a replacement for the standard utility
 * <code>gzip</code> (the command-line flags are different). Each
 * specified file is compressed into another file, whose name is
 * deduced by appending <code>".gz"</code> to the source file name;
 * if the source file does not exist, or the destination file already
 * exists, then the process is aborted. If the file is successfully
 * compressed, then the source file is deleted. If no file name is
 * specified, then the standard input is compressed, and the result
 * is written on the standard output.</p>
 *
 * @version   $Revision: 1.1 $
 * @author    Thomas Pornin
 */

public class GZip {

	private static void usage()
	{
		System.err.println("usage: GZip [ -# ] [ file... ]");
		System.err.println(
			"   # = compression level (1 to 4, default = 3)");
		System.exit(1);
	}

	public static void main(String[] args)
		throws IOException
	{
		int level = 0;
		boolean noFile = true;
		for (int i = 0; i < args.length; i ++) {
			String a = args[i];
			if (a.length() == 0 || a.charAt(0) != '-') {
				noFile = false;
				continue;
			}
			if (a.length() != 2)
				usage();
			switch (a.charAt(1)) {
			case '0':
				level = 0;
				break;
			case '1':
				level = 1;
				break;
			case '2':
				level = 2;
				break;
			case '3':
				level = 3;
				break;
			case '4':
				level = 4;
				break;
			default:
				usage();
				break;
			}
			args[i] = null;
		}

		if (noFile) {
			GZipOutputStream out = new GZipOutputStream(
				System.out, level);
			byte[] buf = new byte[8192];
			for (;;) {
				int rlen = System.in.read(buf);
				if (rlen < 0)
					break;
				out.write(buf, 0, rlen);
			}
			out.close();
			return;
		}

		for (int i = 0; i < args.length; i ++) {
			String a = args[i];
			if (a == null)
				continue;
			File f1 = new File(a);
			if (!f1.isFile()) {
				System.err.println("file \"" + a
					+ "\" does not exist");
				System.exit(1);
			}
			String b = a + ".gz";
			File f2 = new File(b);
			if (f2.exists()) {
				System.err.println("file \"" + b
					+ "\" already exists");
				System.exit(1);
			}

			try {
				InputStream in = new FileInputStream(a);
				GZipOutputStream out = new GZipOutputStream(
					new FileOutputStream(b), level);
				byte[] buf = new byte[8192];
				for (;;) {
					int rlen = in.read(buf);
					if (rlen < 0)
						break;
					out.write(buf, 0, rlen);
				}
				in.close();
				out.close();
				f1.delete();
				f2 = null;
			} finally {
				if (f2 != null)
					f2.delete();
			}
		}
	}
}
