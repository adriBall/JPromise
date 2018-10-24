package com.parball.jpromise;

public interface TransformHandler<E, F> extends Handler {
	public F transform(E res) throws Exception;
}
