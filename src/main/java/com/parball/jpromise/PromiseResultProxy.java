package com.parball.jpromise;

import java.util.LinkedList;
import java.util.Queue;

class PromiseResultProxy {
	private final Queue<PromiseTaskListener> waitingForResult = new LinkedList<PromiseTaskListener>();
	private Object result;

	void add(PromiseTaskListener resultListener) {
		if (result != null)
			concurrentDone(resultListener, result);
		else
			queueListener(resultListener);
	}

	private synchronized void queueListener(PromiseTaskListener resultListener) {
		if (result != null)
			concurrentDone(resultListener, result);
		else
			waitingForResult.offer(resultListener);
	}

	synchronized void giveResult(Object result) {
		this.result = result;
		while (!waitingForResult.isEmpty()) {
			if(waitingForResult.size() == 1)
				waitingForResult.poll().done(result);
			else
				concurrentDone(waitingForResult.poll(), result);
		}
	}
	
	private static void concurrentDone(final PromiseTaskListener resultListener, final Object result) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				resultListener.done(result);
			}
		});
		thread.start();
	}
}
