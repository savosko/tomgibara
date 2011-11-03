package com.tomgibara.crinch.bits;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigInteger;

public class ProfiledBitReader implements BitReader {

	private static final int POIS = 0;
	private static final int R = 1;
	private static final int RBI = 2;
	private static final int RB = 3;
	private static final int RBS = 4;
	private static final int RZ = 5;
	private static final int RL = 6;
	private static final int SB = 7;
	private static final int STB = 8;
	
	private final BitReader reader;
	private final long[] calls = new long[9];
	
	public ProfiledBitReader(BitReader reader) {
		if (reader == null) throw new IllegalArgumentException("null reader");
		this.reader = reader;
	}

	@Override
	public long getPositionInStream() {
		calls[POIS]++;
		return reader.getPositionInStream();
	}

	@Override
	public int read(int count) throws BitStreamException {
		calls[R]++;
		return reader.read(count);
	}

	@Override
	public BigInteger readBigInt(int count) throws BitStreamException {
		calls[RBI]++;
		return reader.readBigInt(count);
	}

	@Override
	public int readBit() throws BitStreamException {
		calls[RB]++;
		return reader.readBit();
	}

	@Override
	public void readBits(BitVector bits) throws BitStreamException {
		calls[RBS]++;
		reader.readBits(bits);
	}

	@Override
	public boolean readBoolean() throws BitStreamException {
		calls[RZ]++;
		return reader.readBoolean();
	}

	@Override
	public long readLong(int count) throws BitStreamException {
		calls[RL]++;
		return reader.readLong(count);
	}

	@Override
	public long skipBits(long count) {
		calls[SB]++;
		return reader.skipBits(count);
	}

	@Override
	public int skipToBoundary(BitBoundary boundary) {
		calls[STB]++;
		return reader.skipToBoundary(boundary);
	}

	public void dumpProfile(PrintStream out) {
		dump(out, "getPositionInStream", 0);
		dump(out, "read", 1);
		dump(out, "readBigInt", 2);
		dump(out, "readBit", 3);
		dump(out, "readBits", 4);
		dump(out, "readBoolean", 5);
		dump(out, "readLong", 6);
		dump(out, "skipBits", 7);
		dump(out, "skipToBoundary", 8);
	}
	
	private void dump(PrintStream out, String label, int i) {
		out.print(label);
		out.print(": ");
		out.print(calls[i]);
		out.print("\n");
	}
	
}
