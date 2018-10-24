package com.parball.jpromise;

class ChainedPromiseRejectBundle<E> extends ChainedPromiseBundle<E> {
	private RejectHandler handler;

	ChainedPromiseRejectBundle(RejectHandler handler) {
		this.handler = handler;
	}

	@Override
	public void done(Object result) {
		if (!(result instanceof Exception))
			return;

		// Due to optimization
		// The order of the following two sentences matters
		super.done(result);
		handler.onRejected((Exception) result);
	}

}
