package com.parball.jpromise;

class ThreadPromiseExecutor extends PromiseExecutorBase {

	ThreadPromiseExecutor(PromiseTask promiseTask) {
		super(promiseTask);
	}

	@Override
	void initExecutor() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					executeTask();
				} catch (Exception e) {
					done(e);
				}
			}
		});
		thread.start();
	}

}
