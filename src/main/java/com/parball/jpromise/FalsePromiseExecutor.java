package com.parball.jpromise;

class FalsePromiseExecutor extends PromiseExecutorBase {

	FalsePromiseExecutor() {
		super(new FalsePromiseTask());
	}

	FalsePromiseExecutor(PromiseTask promiseTask) {
		super(promiseTask);
	}

	@Override
	void initExecutor() {
		try {
			executeTask();
		} catch (Exception e) {
			done(e);
		}
	}

}
