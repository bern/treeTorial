package creator.end.test;

import creator.end.api.KnowledgeNode;

public class ExampleKnowledgeNode {
	public static KnowledgeNode generateNode() {
		KnowledgeNode test = new KnowledgeNode();
		
		test.setName("Dijkstra's Algorithm");
		test.setBody("Dijkstra's algorithm, conceived by computer scientist Edsger Dijkstra in 1956 and published in 1959,[1][2] is a graph search algorithm that solves the single-source shortest path problem for a graph with non-negative edge path costs, producing a shortest path tree. This algorithm is often used in routing and as a subroutine in other graph algorithms. For a given source vertex (node) in the graph, the algorithm finds the path with lowest cost (i.e. the shortest path) between that vertex and every other vertex. It can also be used for finding costs of shortest paths from a single vertex to a single destination vertex by stopping the algorithm once the shortest path to the destination vertex has been determined. For example, if the vertices of the graph represent cities and edge path costs represent driving distances between pairs of cities connected by a direct road, Dijkstra's algorithm can be used to find the shortest route between one city and all other cities. As a result, the shortest path first is widely used in network routing protocols, most notably IS-IS and OSPF (Open Shortest Path First). Dijkstra's original algorithm does not use a min-priority queue and runs in O(|V|^2) (where |V| is the number of vertices). The idea of this algorithm is also given in (Leyzorek et al. 1957). The implementation based on a min-priority queue implemented by a Fibonacci heap and running in O(|E|+|V|\\log|V|) (where |E| is the number of edges) is due to (Fredman & Tarjan 1984). This is asymptotically the fastest known single-source shortest-path algorithm for arbitrary directed graphs with unbounded non-negative weights.");
		test.setId(0);
		
		KnowledgeNode test2 = new KnowledgeNode();
		
		test2.setName("Weighted Directed Graphs");
		test2.setBody("In computer science, a graph is an abstract data type that is meant to implement the graph and hypergraph concepts from mathematics. A graph data structure consists of a finite (and possibly mutable) set of ordered pairs, called edges or arcs, of certain entities called nodes or vertices. As in mathematics, an edge (x,y) is said to point or go from x to y. The nodes may be part of the graph structure, or may be external entities represented by integer indices or references. A graph data structure may also associate to each edge some edge value, such as a symbolic label or a numeric attribute (cost, capacity, length, etc.).");
		test2.setId(1);
		
		KnowledgeNode test3 = new KnowledgeNode();
		
		test3.setName("Path Optimization");
		test3.setBody("In computer science, a graph is an abstract data type that is meant to implement the graph and hypergraph concepts from mathematics. A graph data structure consists of a finite (and possibly mutable) set of ordered pairs, called edges or arcs, of certain entities called nodes or vertices. As in mathematics, an edge (x,y) is said to point or go from x to y. The nodes may be part of the graph structure, or may be external entities represented by integer indices or references. A graph data structure may also associate to each edge some edge value, such as a symbolic label or a numeric attribute (cost, capacity, length, etc.).");
		test3.setId(2);
		
		test.addDependency(test2);
		test.addDependency(test3);
		
		return test;
	}
}
