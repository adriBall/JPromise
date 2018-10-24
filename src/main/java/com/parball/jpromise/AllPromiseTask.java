package com.parball.jpromise;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class AllPromiseTask<E> extends PromiseTask implements RejectHandler {
	private final AtomicBoolean rejected = new AtomicBoolean();
	private final AtomicInteger count = new AtomicInteger(1);
	private final ContainerIterable<E> results = new ContainerIterable<E>();
	private Iterable<Promise<E>> promises;

	AllPromiseTask(Iterable<Promise<E>> promises) {
		this.promises = promises;
	}

	@Override
	public void execute() throws Exception {
		for (Promise<E> promise : promises)
			promise.then(getFulfillHandler(), this);
		if (count.decrementAndGet() == 0)
			resolve(results);
	}

	@Override
	public void onRejected(Exception e) {
		if (!rejected.getAndSet(true))
			reject(e);
	}

	private FulfillHandler<E> getFulfillHandler() {
		count.getAndIncrement();
		final ContainerIterable<E>.Container container = results.allocate();
		return new FulfillHandler<E>() {
			@Override
			public void onFulfilled(E res) {
				container.content = res;
				if (count.decrementAndGet() == 0)
					resolve(results);
			}
		};
	}

}
