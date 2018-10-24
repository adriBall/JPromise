package com.parball.jpromise;

public abstract class PromiseTask {
	private PromiseTaskListener listener;
	private boolean finished;

	public final <E> Promise<E> promise() {
		return new Promise<E>(this);
	}

	void setListener(PromiseTaskListener listener) {
		this.listener = listener;
	}

	protected final void resolve(Object res) {
		if(!finished)
			listener.done(res);
		finished = true;
	}

	protected final void reject(Exception e) {
		if(!finished)
			listener.done(e);
		finished = true;
	}

	public abstract void execute() throws Exception;
}
