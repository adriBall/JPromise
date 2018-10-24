package com.parball.jpromise;

public interface FulfillHandler<E> extends Handler {
	public void onFulfilled(E res) throws Exception;
}
