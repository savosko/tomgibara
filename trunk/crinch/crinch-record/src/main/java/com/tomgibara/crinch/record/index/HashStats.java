package com.tomgibara.crinch.record.index;

import com.tomgibara.crinch.coding.CodedReader;
import com.tomgibara.crinch.coding.CodedStreams;
import com.tomgibara.crinch.coding.CodedWriter;
import com.tomgibara.crinch.record.process.ProcessContext;

class HashStats extends IndexStats {

	boolean ordinal;
	int tableSize;
	int[] hashSeeds;
	int valueBits;
	
	HashStats(ProcessContext context) {
		super("hash", context);
	}

	@Override
	public void writeTo(CodedWriter writer) {
		writer.getWriter().writeBoolean(ordinal);
		writer.writePositiveInt(tableSize + 1);
		CodedStreams.writePrimitiveArray(writer, hashSeeds);
		writer.writePositiveInt(valueBits);
	}

	@Override
	public void readFrom(CodedReader reader) {
		ordinal = reader.getReader().readBoolean();
		tableSize = reader.readPositiveInt() - 1;
		hashSeeds = CodedStreams.readIntArray(reader);
		valueBits = reader.readPositiveInt();
	}
	

	
}