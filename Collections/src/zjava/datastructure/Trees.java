package zjava.datastructure;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Stack;

/**
 * This class contains useful methods and classes to work with trees (data structures) and tree-nodes
 * 
 * @author Ivan Zaitsau
 */
final public class Trees {
	
	/**
	 * NodeFilter defines conditions for ignoring nodes during tree traversal<br>
	 * 
	 * <p>Can be used to track and ignore already visited nodes as well as 
	 * ignore nodes on certain conditions.
	 * 
	 * @author Ivan Zaitsau
	 */
	public interface NodeFilter<T extends Node<T>> {
		@SuppressWarnings("rawtypes")
		NodeFilter NONE = new NodeFilter() {
			public boolean isIgnored(Node node) {
				return false;
			}
		};
		
		/**
		 * Returns true for every node that needs to be completely ignored.<br>
		 * (In other words, this node and every reference from this node to other
		 * (child) nodes will be ignored during tree traversal) <br>
		 * 
		 * <p>Called exactly once for each available path for every node.
		 * 
		 * @param node - node for which conditions needs to be checked
		 * @return true if node needs to be ignored
		 */
		boolean isIgnored(T node);
	}
	
	/**
	 * <tt>Iterator</tt> which navigates root node and it's successors
	 * in depth-first search order.<br>
	 * Does not support remove operation.
	 */
	private static class DfsIterator<T extends Node<T>> implements Iterator<T>, Iterable<T> {

		private Stack<Iterator<T>> iteratorsStack;
		private T currentNode;		
		private final NodeFilter<T> filter;
		private boolean filterApplied;
		private boolean iterationDone;

		private DfsIterator(T root, NodeFilter<T> filter) {
			iteratorsStack = new Stack<>();
			currentNode = root;
			this.filter = filter;
		}
		
		private void moveToNext() {
			while (!iteratorsStack.empty() && !iteratorsStack.peek().hasNext())
				iteratorsStack.pop();
			if (iteratorsStack.isEmpty()) {
				iterationDone = true;
			}
			else {
				currentNode = iteratorsStack.peek().next();
				filterApplied = false;
			}
		}
		
		public boolean hasNext() {
			while (true) {
				if (iterationDone)
					return false;
				if (filterApplied)
					return true;
				if (!filter.isIgnored(currentNode)) {
					filterApplied = true;
					return true;
				}
				moveToNext();
			}
		}

		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();
			
			T result = currentNode;
			iteratorsStack.push(currentNode.getChildren().iterator());
			moveToNext();
			return result;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		public Iterator<T> iterator() {
			return this;
		}
	}
	
	/**
	 * Returns <tt>Iterable</tt> which navigates given node and it's successors
	 * in depth-first search order.
	 * 
	 * @param root - starting node
	 * 
	 * @return <tt>Iterable</tt> which navigates root node and it's successors
	 * in depth-first search order.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Node<T>> Iterable<T> depthFirstSearch(T root) {
		return new DfsIterator<T>(root, NodeFilter.NONE);
	}
	
	/**
	 * Returns <tt>Iterable</tt> which navigates given node and it's successors
	 * in depth-first search order.
	 * 
	 * @param root - starting node
	 * @param filter - reports if node needs to be filtered away
	 * 
	 * @return <tt>Iterable</tt> which navigates root node and it's successors
	 * in depth-first search order.
	 */	
	public static <T extends Node<T>> Iterable<T> depthFirstSearch(T root, NodeFilter<T> filter) {
		return new DfsIterator<T>(root, filter);
	}
	
	/**
	 * <tt>Iterator</tt> which navigates root node and it's successors
	 * in breadth-first search order.<br>
	 * Does not support remove operation.
	 */
	private static class BfsIterator<T extends Node<T>> implements Iterator<T>, Iterable<T> {

		private Iterator<T> currentIterator;
		private final Queue<T> nodesQueue = new LinkedList<>();
		private T currentNode;		
		private final NodeFilter<T> filter;
		private boolean filterApplied;
		private boolean iterationDone;

		private BfsIterator(T root, NodeFilter<T> filter) {
			currentIterator = Collections.emptyIterator();
			currentNode = root;
			this.filter = filter;
		}
		
		private void moveToNext() {
			while (!currentIterator.hasNext() && !nodesQueue.isEmpty())
				currentIterator = nodesQueue.poll().getChildren().iterator();
			if (!currentIterator.hasNext()) {
				iterationDone = true;
			}
			else {
				currentNode = currentIterator.next();
				filterApplied = false;
			}
		}
		
		public boolean hasNext() {
			while (true) {
				if (iterationDone)
					return false;
				if (filterApplied)
					return true;
				if (!filter.isIgnored(currentNode)) {
					filterApplied = true;
					return true;
				}
				moveToNext();
			}
		}

		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();
			T result = currentNode;
			nodesQueue.add(currentNode);
			moveToNext();
			return result;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		public Iterator<T> iterator() {
			return this;
		}
	}
	
	/**
	 * Returns <tt>Iterable</tt> which navigates root node and it's successors
	 * in breadth-first search order.
	 * 
	 * @return <tt>Iterable</tt> which navigates root node and it's successors
	 * in breadth-first search order.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Node<T>> Iterable<T> breadthFirstSearch(T root) {
		return new BfsIterator<T>(root, NodeFilter.NONE);
	}
	
	/**
	 * Returns <tt>Iterable</tt> which navigates given node and it's successors
	 * in breadth-first search order.
	 * 
	 * @param root - starting node
	 * @param filter - reports if node needs to be filtered away
	 * 
	 * @return <tt>Iterable</tt> which navigates root node and it's successors
	 * in breadth-first search order.
	 */
	public static <T extends Node<T>> Iterable<T> breadthFirstSearch(T root, NodeFilter<T> filter) {
		return new BfsIterator<T>(root, filter);
	}
	
	private Trees() {};
}
