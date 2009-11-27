package example;

// $Id: GZCat.java,v 1.1 2009/11/13 10:22:33 tim Exp $
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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.bolet.jgz.GZipInputStream;

/**
 * <p>This simple command-line application uncompresses one or several
 * files. If one or several file names are provided, then each file
 * is decompressed (independantly of each other). If no file name is
 * provided, then compressed data is read from standard input. Either
 * way, all uncompressed data is written on standard output.</p>
 *
 * @version   $Revision: 1.1 $
 * @author    Thomas Pornin
 */

public class GZCat {

	public static void main(String[] args)
		throws IOException
	{
		if (args.length == 0) {
			uncompress(System.in);
		} else {
			for (int i = 0; i < args.length; i ++) {
				FileInputStream in =
					new FileInputStream(args[i]);
				uncompress(in);
				in.close();
			}
		}
		System.out.close();
	}

	private static void uncompress(InputStream in)
		throws IOException
	{
		GZipInputStream gzin = new GZipInputStream(
			new BufferedInputStream(in));
		byte[] buf = new byte[8192];
		for (;;) {
			int rlen = gzin.read(buf);
			if (rlen < 0)
				break;
			System.out.write(buf, 0, rlen);
		}
	}
}
