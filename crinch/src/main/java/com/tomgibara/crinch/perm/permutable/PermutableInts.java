package com.tomgibara.crinch.perm.permutable;

import java.util.Arrays;

import com.tomgibara.crinch.perm.Permutable;

public class PermutableInts implements Permutable {

	private final int[] values;
	
	public PermutableInts(int[] values) {
		if (values == null) throw new IllegalArgumentException("null values");
		this.values = values;
	}
	
	public int[] getValues() {
		return values;
	}
	
	@Override
	public int getPermutableSize() {
		return values.length;
	}
	
	@Override
	public PermutableInts transpose(int i, int j) {
		int v = values[i];
		values[i] = values[j];
		values[j] = v;
		return this;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(values);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof PermutableInts)) return false;
		PermutableInts that = (PermutableInts) obj;
		return Arrays.equals(this.values, that.values);
	}
	
	@Override
	public String toString() {
		return Arrays.toString(values);
	}
	
}