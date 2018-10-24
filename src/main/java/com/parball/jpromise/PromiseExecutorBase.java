package com.parball.jpromise;

abstract class PromiseExecutorBase implements PromiseTaskListener {
	private final PromiseResultProxy resultListeners = new PromiseResultProxy();
	private PromiseTask promiseTask;

	abstract void initExecutor();

	PromiseExecutorBase(PromiseTask promiseTask) {
		this.promiseTask = promiseTask;
		promiseTask.setListener(this);
	}

	<E, F> Promise<F> chain(Handler handler) {
		ChainedPromiseBundle<F> bunlde = ChainedPromiseBundleFactory.<E, F>create(handler);
		resultListeners.add(bunlde);
		return bunlde.getPromise();
	}

	protected void executeTask() throws Exception {
		promiseTask.execute();
	}

	@Override
	public void done(Object result) {
		if (result instanceof Promise)
			unpackPromise((Promise<?>) result);
		else
			resultListeners.giveResult(result);
	}

	private void unpackPromise(Promise<?> promise) {
		promise.easyThen(new FulfillHandler<Object>() {
			@Override
			public void onFulfilled(Object res) {
				done(res);
			}
		});

		promise.unless(new RejectHandler() {
			@Override
			public void onRejected(Exception e) {
				done(e);
			}
		});
	}

}
