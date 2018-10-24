package com.parball.jpromise;

class ChainedPromiseFulfillBundle<E> extends ChainedPromiseBundle<E> {
	private FulfillHandler<E> handler;

	ChainedPromiseFulfillBundle(FulfillHandler<E> handler) {
		this.handler = handler;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void done(Object result) {
		// Due to optimization
		// This sentence must be first
		super.done(result);
		
		if (result instanceof Exception)
			return;

		try {
			handler.onFulfilled((E) result);
		} catch (Exception e) {
			new IllegalStateException("Can not handle result: " + e.getMessage()).printStackTrace();
		}
	}

}
