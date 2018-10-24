package com.parball.jpromise;

public class Promise<E> {
	private PromiseExecutorBase executor;

	Promise(PromiseExecutorBase executor) {
		this.executor = executor;
		executor.initExecutor();
	}

	void easyThen(final FulfillHandler<Object> handler) {
		executor.<E, E>chain(new FulfillHandler<E>() {
			@Override
			public void onFulfilled(E res) throws Exception {
				handler.onFulfilled(res);
			}
		});
	}

	public Promise(PromiseTask promiseTask) {
		this.executor = new ThreadPromiseExecutor(promiseTask);
		this.executor.initExecutor();
	}

	public Promise<E> then(FulfillHandler<E> fulfillHandler) {
		return executor.<E, E>chain(fulfillHandler);
	}

	public Promise<E> then(FulfillHandler<E> fulfillHandler, RejectHandler rejectHandler) {
		return executor.<E, E>chain(fulfillHandler).unless(rejectHandler);
	}

	public <F> Promise<F> then(TransformHandler<E, ?> fulfillHandler) {
		return executor.<E, F>chain(fulfillHandler);
	}

	public <F> Promise<F> then(TransformHandler<E, ?> fulfillHandler, RejectHandler rejectHandler) {
		return executor.<E, F>chain(fulfillHandler).unless(rejectHandler);
	}

	public Promise<E> unless(RejectHandler rejectHandler) {
		return executor.<E, E>chain(rejectHandler);
	}

	public static <E> Promise<Iterable<E>> all(Iterable<Promise<E>> promises) {
		return new AllPromiseTask<E>(promises).promise();
	}

	public static <E> Promise<E> race(Iterable<Promise<E>> promises) {
		return new RacePromiseTask<E>(promises).promise();
	}

	public static <E> Promise<Iterable<E>> all(@SuppressWarnings("unchecked") Promise<E>... promises) {
		return new AllPromiseTask<E>(new ArrayIterable<Promise<E>>(promises)).promise();
	}

	public static <E> Promise<E> race(@SuppressWarnings("unchecked") Promise<E>... promises) {
		return new RacePromiseTask<E>(new ArrayIterable<Promise<E>>(promises)).promise();
	}

	public static <E> Promise<E> resolve(final Object res) {
		return new Promise<E>(new FalsePromiseExecutor(new PromiseTask() {
			@Override
			public void execute() throws Exception {
				resolve(res);
			}
		}));
	}

	public static <E> Promise<E> reject(final Exception e) {
		return new Promise<E>(new FalsePromiseExecutor(new PromiseTask() {
			@Override
			public void execute() throws Exception {
				reject(e);
			}
		}));
	}

}
