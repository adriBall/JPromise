package com.parball.jpromise;

import java.util.concurrent.atomic.AtomicBoolean;

class RacePromiseTask<E> extends PromiseTask implements FulfillHandler<E>, RejectHandler {
	private final AtomicBoolean done = new AtomicBoolean();
	private Iterable<Promise<E>> promises;

	RacePromiseTask(Iterable<Promise<E>> promises) {
		this.promises = promises;
	}

	@Override
	public void execute() throws Exception {
		for (Promise<E> promise : promises)
			promise.then(this, this);
	}

	@Override
	public void onFulfilled(E res) {
		if (!done.getAndSet(true))
			resolve(res);
	}

	@Override
	public void onRejected(Exception e) {
		if (!done.getAndSet(true))
			reject(e);
	}

}
