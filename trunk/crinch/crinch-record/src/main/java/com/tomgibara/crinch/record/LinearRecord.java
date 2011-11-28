package com.tomgibara.crinch.record;

public interface LinearRecord extends Record {

	boolean hasNext();
	
	String nextString();

	char nextChar();

	boolean nextBoolean();
	
	byte nextByte();

	short nextShort();
	
	int nextInt();

	long nextLong();

	float nextFloat();
	
	double nextDouble();
	
	void skipNext();
	
	boolean wasNull();
	
	boolean wasInvalid();

	void mark();
	
	void reset();
	
	void exhaust();
}