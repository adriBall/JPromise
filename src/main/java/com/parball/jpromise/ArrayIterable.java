package com.parball.jpromise;

import java.util.Iterator;

class ArrayIterable<E> implements Iterable<E> {
	private E[] array;

	ArrayIterable(E[] array) {
		this.array = array;
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			private int pos = 0;

			@Override
			public boolean hasNext() {
				return pos < array.length;
			}

			@Override
			public E next() {
				return array[pos++];
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

}
