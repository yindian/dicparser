jgz-0.2
=======

This is the jgz library, which is an implementation in pure Java of the
DEFLATE compression algorithm and the zlib and gzip file formats (see
RFC 1950, 1951 and 1952).

The release version is 0.2. It is considered of beta quality; there is
no known bugs but some extensive testing is still needed.

In this archive, the following directories are present:

  src/        library source code (under the org.bolet.jgz package)
  test/       unit tests source code (for JUnit)
  examples/   example application code using jgz
  api/        the Javadoc-generated API documentation

The LICENSE.txt file specifies the license for the whole package.
In plain words, it means: feel free to use all this as you wish. But
remember that there is no guarantee that this software is not flawed
or is not utterly useless; and I am not to be blamed for _anthing_.
In other words, this is open-source software.

Please refer to the main jgz web site for details:

   http://www.bolet.org/jgz/

You may also contact me by e-mail; my address is:

   Thomas Pornin <pornin@bolet.org>


Changes from version 0.1:
* Implementation of flush modes has been fixed.
* State machine API for deflation has been added.
* Deflater code has been tweaked: runs faster, compressed better, uses
  less memory.
* Some internal code reorganization has been applied.
