package algorithms;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
/*
 * The implementation of graph_algorithms interface.
 * Algorithms:
 * init from graph = initial new graph from other graph
 * init from file = initial new graph from text file 
 * save to file = save graph in text file
 * isConnected = check if the graph is strongly connected
 * shortestPathDist = calculate the shortest path between source node and destination node by the weights of the edges
 * shortestPath = return the shortest route through the nodes (source and destination) 
 * TSP = return a route between a list of nodes (don't have to be the shortest route)
 * copy = deep copy a graph
 * 
 * @authors Snir and Omer 
 */
public class Graph_Algo implements graph_algorithms{
	public DGraph dg;

	/*
	 * Default constructor
	 */
	public Graph_Algo() {
		this.dg = new DGraph();
	}

	public Graph_Algo(graph _graph) {
		this.dg = new DGraph();
		init(_graph);
	}

	/*
	 * Set this graph to the parameter's graph
	 */
	public void init(graph g) {
		this.dg = (DGraph) g;		
	}

	/*
	 * Set the graph from a file (using Yael's algorithm)
	 */
	public void init(String file_name) {
		try{    
			FileInputStream file = new FileInputStream(file_name); 
			ObjectInputStream in = new ObjectInputStream(file);
			this.dg = (DGraph)in.readObject();
			in.close(); 
			file.close(); 
			System.out.println("Object has been deserialized");
		} 

		catch(IOException | ClassNotFoundException ex) 
		{ 
			System.out.println("IOException is caught"); 
		} 
	}

	/*
	 * Save a graph in a text file (using Yael's algorithm)
	 */
	public void save(String file_name) {	
		try{
			FileOutputStream file = new FileOutputStream(file_name); 
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(this.dg);
			out.close(); 
			file.close(); 

			System.out.println("Object has been serialized"); 
		}   
		catch(IOException ex){
			System.out.println(ex.getMessage()); 
		}
	}

	/**
	 * sets tag to 0 and weight MAX_VALUE on all nodes
	 */
	private void zeroTagsMaxWeight(){
		Collection<node_data> nodes = dg.getV();
		for (node_data node_data : nodes) {
			node_data.setTag(0);
			node_data.setWeight(Integer.MAX_VALUE);
			node_data.setInfo("");
		}
	}
	
	/**
	 * Diakstra algorithm
	 * @param src
	 */
	private void diaksta(int src){
		zeroTagsMaxWeight();
		ArrayList<node_data> nodes = new ArrayList<node_data>();
		nodes.add(dg.getNode(src));
		nodes.get(0).setWeight(0);
		while(!nodes.isEmpty()){
			node_data currNode = nodes.get(0);
			if(currNode.getTag() == 0){
				currNode.setTag(1);
				nodes.remove(0);
				Collection<edge_data> edges = dg.getE(currNode.getKey());
				for (edge_data edge_data : edges) {
					node_data destNode = dg.getNode(edge_data.getDest());
					double dstNodeW = destNode.getWeight();
					double edge_dataW = edge_data.getWeight();
					if(dstNodeW > currNode.getWeight() + edge_dataW){
						destNode.setWeight(currNode.getWeight()+edge_data.getWeight());
						destNode.setInfo(currNode.getKey() + "");
						if(destNode.getTag() == 0){
							nodes.add(getIndex(nodes, destNode.getWeight()),destNode);
						}
					}
				}
			}
			else{
				nodes.remove(0);
			}
		}
	}
	/**
	 * get the insert position in sorted array
	 * @param nodes
	 * @param dstN
	 * @return
	 */
	private int getIndex(ArrayList<node_data> nodes,double dstN){
		int minIn = 0;
		int maxIn = nodes.size() - 1;
		int mid = minIn + (maxIn - minIn) / 2;

		while(minIn <= maxIn){
			if(nodes.get(mid).getWeight() == dstN){
				return mid;
			}
			if(dstN > nodes.get(mid).getWeight()){
				minIn = mid + 1;
			}
			if(dstN < nodes.get(mid).getWeight()){
				maxIn = mid - 1;
			}
			mid = minIn + (maxIn - minIn) / 2 ;
		}
		return mid;
	}

	public double shortestPathDist(int src, int dest) {
		diaksta(src);
		node_data ans = dg.getNode(dest);
		return ans.getWeight();
	}
	
	public List<node_data> shortestPath(int src, int dest) {
		diaksta(src);
		node_data node = dg.getNode(dest);
		if(node.getWeight() >= Integer.MAX_VALUE) {
			return null;
		}
		List<node_data> ans = new ArrayList<node_data>();
		while(!node.getInfo().isEmpty()){
			ans.add(0, node);
			node = dg.getNode(Integer.parseInt(node.getInfo()));
		}
		ans.add(0, node);
		return ans;
	}
}