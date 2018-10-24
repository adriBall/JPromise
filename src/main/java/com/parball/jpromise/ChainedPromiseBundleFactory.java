package com.parball.jpromise;

class ChainedPromiseBundleFactory {
	@SuppressWarnings("unchecked")
	static <E, F> ChainedPromiseBundle<F> create(Handler handler) {
		if (handler instanceof FulfillHandler)
			return new ChainedPromiseFulfillBundle<F>((FulfillHandler<F>) handler);

		if (handler instanceof TransformHandler)
			return new ChainedPromiseTransformBundle<E, F>((TransformHandler<E, F>) handler);

		if (handler instanceof RejectHandler)
			return new ChainedPromiseRejectBundle<F>((RejectHandler) handler);

		throw new IllegalArgumentException("Unsupported handler of type " + handler.getClass().getName());
	}
}
