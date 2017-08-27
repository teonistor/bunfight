package io.github.teonistor.suhc;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An iterator implementation which generates elements of the destination type from a source type collection, according to a given function, randomly and forever, without repeating the last n/2 elements (n being the size of the source collection)
 * @author Teodor Gherasim Nistor
 *
 * @param <S> Source type
 * @param <D> Destination type
 * 
 * This sotware --
 */
public class CacheIterator<S,D> implements Iterator<D> {

	private List<S> source;
	private Function<S, D> srcToDest;
	
	private Queue<Integer> previous;
	private D next;
	private Random random;
	
	/**
	 * Create a CacheIterator from the given collection and generator function
	 * @param source Collection of elements of source type
	 * @param srcToDest Function describing how an element of the source collection becomes an object of destination type. This must only return null in case of an error, and when it does, operation will be retried (perhaps on a different, randomly picked element of the source collection)
	 */
	public CacheIterator (Collection<S> source, Function <S,D> srcToDest) {
		this.source = new ArrayList<>(source);
		this.srcToDest = srcToDest;
		
		previous = new LinkedList<>();
		next = null;
		random = new Random();
		cacheNext();
	}

	/**
	 * Create a CacheIterator from the given array and generator function
	 * @param source Array of elements of source type
	 * @param srcToDest Function describing how an element of the source collection becomes an object of destination type. This must only return null in case of an error, and when it does, operation will be retried (perhaps on a different, randomly picked element of the source collection)
	 */
	public CacheIterator (S[] source, Function <S,D> srcToDest) {
		this(asList(source), srcToDest);
	}

	/**
	 * Create a CacheIterator from the given supplier and generator function
	 * @param source Supplier providing a collection of elements of source type
	 * @param srcToDest Function describing how an element of the source collection becomes an object of destination type. This must only return null in case of an error, and when it does, operation will be retried (perhaps on a different, randomly picked element of the source collection)
	 */
	public CacheIterator (Supplier<Collection<S>> source, Function <S,D> srcToDest) {
		this (source.get(), srcToDest);
	}
	
	/**
	 * @return true if and only if the cached element is not null, in other words if the generator function provided at construction time succeeded at least once
	 */
	public boolean hasNext() {
		return next != null;
	}

	/**
	 * @return the next cached item. This has been generated using the generator function provided at construction time from a randomly picked element of the source collection.
	 */
	public D next() {
		D next = this.next;
		cacheNext();
		return next;
	}
	
	/* Trim the record of previously used indexes to at most half the total collection size. Then
	 * generate random indexes until one that does not belong to this record occurs. Apply the
	 * generator function on the element at this index and store the result as next element to be
	 * returned by the iterator; also remember its index. If the generator function fails, repeat
	 * the procedure until it succeeds.
	 */
	private void cacheNext() {
		do {
			while (previous.size() > source.size() / 2)
				previous.remove();
			Integer index;
			do {
				index = random.nextInt(source.size());
			} while (previous.contains(index));
			next = srcToDest.apply(source.get(index));
			previous.add(index);
		} while (next == null);
	}
}
