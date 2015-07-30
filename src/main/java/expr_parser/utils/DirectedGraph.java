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
	public final Map<T, Set<T>> graph = new HashMap<T, Set<T>>();

	public boolean addNode(T node) {
		if (graph.containsKey(node)) {
			return false;
		}

		graph.put(node, new HashSet<T>());
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
		graph.get(src).add(dest);
	}

	public void removeEdge(T src, T dest) {
		validateSourceAndDestinationNodes(src, dest);

		graph.get(src).remove(dest);
	}

	public boolean edgeExists(T src, T dest) {
		validateSourceAndDestinationNodes(src, dest);

		return graph.get(src).contains(dest);
	}

	public Set<T> edgesFrom(T node) {
		// Check that the node exists.
		Set<T> edges = graph.get(node);
		if (edges == null)
			throw new NoSuchElementException("Source node does not exist.");

		return Collections.unmodifiableSet(edges);
	}

	public Iterator<T> iterator() {
		return graph.keySet().iterator();
	}

	public int size() {
		return graph.size();
	}

	public boolean isEmpty() {
		return graph.isEmpty();
	}

	private void validateSourceAndDestinationNodes(T src, T dest) {
		// Confirm both endpoints exist
		if (!graph.containsKey(src) || !graph.containsKey(dest))
			throw new NoSuchElementException("Both nodes must be in the graph.");
	}

}