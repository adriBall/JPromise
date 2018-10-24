package com.parball.jpromise;

class ChainedPromiseTransformBundle<E, F> extends ChainedPromiseBundle<F> {
	private TransformHandler<E, F> handler;

	ChainedPromiseTransformBundle(TransformHandler<E, F> handler) {
		this.handler = handler;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void done(Object result) {
		if (result instanceof Exception) {
			super.done(result);
			return;
		}

		F transformedResult;
		try {
			transformedResult = handler.transform((E) result);
		} catch (Exception e) {
			super.done(e);
			return;
		}
		super.done(transformedResult);
	}

}
