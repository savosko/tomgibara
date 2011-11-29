/*
 * Copyright 2011 Tom Gibara
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.tomgibara.crinch.bits;


public abstract class ByteBasedBitReader extends AbstractBitReader {

	// fields
	
	private int buffer = 0;
	private long position = 0;
	
	// methods for overriding
	
	// returns -1 for end of stream
	protected abstract int readByte() throws BitStreamException;
	
	// permitted to skip fewer - possibly zero
	protected abstract long skipBytes(long count) throws BitStreamException;

	// returns -1 if seek not supported
	protected abstract long seekByte(long index) throws BitStreamException;

	// public methods

	public void setPosition(long position) {
		if (position < 0) throw new IllegalArgumentException("negative position");
		if (position == this.position) return;
		long index = seekByte(position >> 3);
		if (index < 0L) {
			long count = position - this.position;
			if (count < 0L) throw new IllegalArgumentException("Backward seek not supported");
			skipBits(count);
		} else {
			this.position = index << 3;
			skipBits(position - this.position);
		}
	}
	
	// bit reader methods
	
	@Override
	public int readBit() {
		int count = (int)position & 7;
		if (count == 0) { // need new bits
			buffer = readByte();
			if (buffer == -1) throw new EndOfBitStreamException();
		}
		position++;
		return (buffer >> (7 - count)) & 1;
	}
	
	@Override
	public int read(int count) {
    	if (count < 0) throw new IllegalArgumentException("negative count");
    	if (count > 32) throw new IllegalArgumentException("count too great");
    	if (count == 0) return 0;
    	
    	int value;
		int remainder = (8 - (int)position) & 7;
		if (remainder == 0) {
			value = 0;
		} else if (count > remainder) {
			value = buffer & ((1 << remainder) - 1);
			count -= remainder;
			position += remainder;
		} else {
			position += count;
			return (buffer >> (remainder - count)) & ((1 << count) - 1);
		}
		
		while (true) {
			buffer = readByte();
			if (buffer == -1) throw new EndOfBitStreamException();
			if (count >= 8) {
				value = (value << 8) | buffer;
				count -= 8;
				position += 8;
				if (count == 0) return value;
			} else {
				value = (value << count) | (buffer >> (8 - count));
				position += count;
				return value;
			}
		}
	}
	
	@Override
	public long skipBits(long count) {
		if (count < 0L) throw new IllegalArgumentException("negative count");
		int boundary = bitsToBoundary(BitBoundary.BYTE);
		if (count <= boundary) {
			position += count;
			return count;
		}
		
		position += boundary;
		long bytes = (count - boundary) >> 3;
		long skipped = skipFully(bytes);
		long bits = skipped << 3;
		if (skipped < bytes) return boundary + bits;

		for (int remainder = (int)(count - boundary - bits); remainder > 0; remainder--) {
			try {
				readBit();
			} catch (EndOfBitStreamException e) {
				return count - remainder;
			}
		}
		return count;
	}
	
	@Override
	public long getPosition() {
		return position;
	}

	// private utility methods
	
	private long skipFully(long count) {
		long total = 0L;
		while (total < count) {
			long skipped = skipBytes(count);
			if (skipped == 0L) {
				if (readByte() < 0) {
					break;
				} else {
					skipped = 1L;
				}
			}
			total += skipped;
			position += skipped << 3;
		}
		return total;
	}

}