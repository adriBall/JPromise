package com.parball.jpromise;

abstract class ChainedPromiseBundle<E> implements PromiseTaskListener {
	protected PromiseExecutorBase chainedPromiseExecutor;

	ChainedPromiseBundle() {
		this.chainedPromiseExecutor = new FalsePromiseExecutor();
	}

	Promise<E> getPromise() {
		return new Promise<E>(chainedPromiseExecutor);
	}

	@Override
	public void done(Object result) {
		chainedPromiseExecutor.done(result);
	}
}
