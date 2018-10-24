package com.parball.jpromise;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class ContainerIterable<E> implements Iterable<E> {
	class Container {
		public volatile E content;
	}

	private List<Container> containerList = new LinkedList<Container>();

	Container allocate() {
		Container container = new Container();
		containerList.add(container);
		return container;
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			private Iterator<Container> containerIterator = containerList.iterator();

			@Override
			public boolean hasNext() {
				return containerIterator.hasNext();
			}

			@Override
			public E next() {
				return containerIterator.next().content;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}
}
