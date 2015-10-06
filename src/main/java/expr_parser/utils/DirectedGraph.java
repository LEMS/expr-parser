package expr_parser.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class DirectedGraph<T> implements Iterable<T> {

	// key is a Node, value is a set of Nodes connected by outgoing edges from
	// the key
	private final Map<T, Set<T>> graph = new HashMap<T, Set<T>>();

	public boolean addNode(T node) {
		if (getGraph().containsKey(node)) {
			return false;
		}

		getGraph().put(node, new HashSet<T>());
		return true;
	}

	public void addNodes(Collection<T> nodes) {
		for (T node : nodes) {
			addNode(node);
		}
	}

	public void addEdge(T src, T dest) {
		validateSourceAndDestinationNodes(src, dest);

		// Add the edge by adding the dest node into the outgoing edges
		getGraph().get(src).add(dest);
	}

	public void removeEdge(T src, T dest) {
		validateSourceAndDestinationNodes(src, dest);

		getGraph().get(src).remove(dest);
	}

	public boolean edgeExists(T src, T dest) {
		validateSourceAndDestinationNodes(src, dest);

		return getGraph().get(src).contains(dest);
	}

	public Set<T> edgesFrom(T node) {
		// Check that the node exists.
		Set<T> edges = getGraph().get(node);
		if (edges == null)
			throw new NoSuchElementException("Source node does not exist.");

		return Collections.unmodifiableSet(edges);
	}

	public Iterator<T> iterator() {
		return getGraph().keySet().iterator();
	}

	public int size() {
		return getGraph().size();
	}

	public boolean isEmpty() {
		return getGraph().isEmpty();
	}

	private void validateSourceAndDestinationNodes(T src, T dest) {
		// Confirm both endpoints exist
		if (!getGraph().containsKey(src) || !getGraph().containsKey(dest))
			throw new NoSuchElementException("Both nodes must be in the graph.");
	}

	public Map<T, Set<T>> getGraph() {
		return graph;
	}

}