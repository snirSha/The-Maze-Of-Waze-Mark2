package dataStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.Point3D;
import utils.StdDraw;


/*
 * The implementation of graph interface.
 * Contains HashMap data structure for all the nodes in the graph.
 * The key is the data of the node.
 * The value is the node.
 * 
 * @authors Snir and Omer
*/
public class DGraph implements graph, Serializable {

/*
 * @param nodes = The HashMap of the graph
 * @param counterEdges = Keep track of the amount of edges
 * @param changes = Keep track of the amount of changes (add or remove of node and edge)
 */
	private HashMap<Integer, node_data> nodes;//1,a    2,b    3,c   4,d    |    data,Node
	private int counterEdges;
	private int changes;//every change in the graph the counter goes up by one
	
	/*
	 * Default constructor
	 */
	public DGraph() {
		nodes = new HashMap<>();
		counterEdges = 0;
		changes = 0;
	}

	/*
	 * Copy constructor (deep copy) 
	 */
	public DGraph(DGraph other) {
		nodes = new HashMap<>();
		for(node_data n: other.nodes.values()) {
			this.nodes.put(n.getKey(), new Node((Node) n));
		}
		this.counterEdges = other.counterEdges;
		this.changes = other.changes;
	}

	/*
	 * Return the vertex by it's data/key/number (if it exist)
	 */
	public node_data getNode(int key) {
		return nodes.get(key);
	}

	/*
	 * Return the edge by the source key and destination key (if they exist)
	 * @param s = casting from node_data to Node
	 */
	public edge_data getEdge(int src, int dest) {
		if(nodes.containsKey(src) && nodes.containsKey(dest)) {
			Node s=(Node) nodes.get(src);
			if(s.getEdgesOf().get(dest)!=null)
				return s.getEdgesOf().get(dest);
		}
		//System.out.println("src ot dst doesnt exist");
		return null;
	}

	/*
	 * Add a vertex to the HashMap (if it not exist)
	 * @param no = casting from node_data to Node11
	 */
	public void addNode(node_data n) {
		Node no = (Node)n;
		if(!nodes.containsKey(no.getKey())) {

			changes++;//adding a node
			nodes.put(n.getKey(), n);
		}
		else {
			System.out.println("Node already in the graph");
		}
	}

	/*
	 * Add an edge between the source vertex and the destination vertex (if they exist)
	 * @param n = casting from node_data to Node
	 */
	public void connect(int src, int dest, double w) {

		if(nodes.containsKey(src) && nodes.containsKey(dest) && (src != dest)) {
			Node n = (Node)nodes.get(src);
			if(!n.getEdgesOf().containsKey(dest)) {
				Node s=(Node) nodes.get(src);
				Edge e=new Edge(src,dest,w);
				s.addEdge(e);
				changes++;//adding an edge
				counterEdges++;
			}
		}
		else {
			System.out.println("src or dst doesnt exist OR src equals to dest");
		}
	}
	
	/*
	 * Return all the nodes in the HashMap 
	 */
	public Collection<node_data> getV() {
		return nodes.values();
	}

	/*
	 * Return all the edges that come out from this vertex (using the HashMap of this vertex). 
	 * @param list = save all the edges that come out of the parameter's node
	 * @param n = casting from node_data to Node
	 */
	public Collection<edge_data> getE(int node_id) {
		Collection<edge_data> list=new ArrayList<edge_data>();
		if(nodes.containsKey(node_id)) {
			Node n=(Node) nodes.get(node_id);
			list.addAll(n.getEdgesOf().values());
		}
		return list;
	}

	/*
	 * To remove a node we need to find all edges that connect to it.
	 * After deleting every edge, we remove the node and update the counters.
	 * @param n = The Node we want to remove
	 * @param count = A counter the count all the edges we are going to delete and subtract from counterEdges
	 * @param tmp = casting from node_data to Node 
	 */
	@Override
	public node_data removeNode(int key) {
		if(nodes.containsKey(key)) {
			Node n=(Node) nodes.get(key);//find all the edges that connected to node(key)
			int count=n.getEdgesOf().size();//every edge that we delete the counter goes down by one
			counterEdges-=count;
			n.getEdgesOf().clear();

			for(node_data a:nodes.values()) {//remove all 
				Node tmp=(Node)a;
				if(tmp.getEdgesOf().containsKey(key)) {
					tmp.getEdgesOf().remove(key);
					counterEdges--;
				}
			}
			changes++;//removing node

			return nodes.remove(key);

		}else {
			System.out.println("key doesnt exist");
			return null;
		}
	}
	
	/*
	 * To remove an edge we need to find the source node and check if contains the destination in it's HashMap of edges
	 * @param n = The node of the source
	 * @param nSrc = The node of the source
	 */
	@Override
	public edge_data removeEdge(int src, int dest) {

		if(nodes.containsKey(src) && nodes.containsKey(dest)) {
			Node n = (Node) nodes.get(src);
			if(n.getEdgesOf().containsKey(dest)) {
				Node nSrc=(Node) nodes.get(src);
				counterEdges--;
				changes++;
				return nSrc.getEdgesOf().remove(dest);

			}
			else {
				System.out.println("Edge doesnt exist");
			}
		}else {
			System.out.println("src or dest doesnt exist");
		}
		return null;
	}
	
	/*
	 * Return the HashMap size (HashMap of all the nodes in the graph) 
	 */
	@Override
	public int nodeSize() {
		return nodes.size();
	}

	/*
	 * Return the amount of edges in the graph using the globally counter
	 */
	@Override
	public int edgeSize() {
		return counterEdges;
	}

	/*
	 * Return the amount of changes in the graph using the globally counter
	 */
	@Override
	public int getMC() {
		return changes;
	}

	/*
	 * This function reverse all the edges in the graph
	 * @param saveTheChanges = save the changes counter number because this function is one change 
	 * @param nod = A collection of all the nodes in the graph 
	 * @param edges = A collection of all the edges that come out of the parameter's vertex 
	 * @param ed = cast the edge_data parameter to Edge
	 */
	public void reversedGraph() {
		int saveTheChanges = changes;
		zeroEdgeTag();
		Collection<node_data> nod=getV();
		for(node_data a: nod) {
			Collection<edge_data> edges=getE(a.getKey());
			for(edge_data e: edges) {
				if(!isBidirectional(e.getSrc(),e.getDest())){
					Edge ed = (Edge)e;
					if(ed.getTag() == 0) {
						connect(ed.getDest(), ed.getSrc(), ed.getWeight());
						Edge ed1 = (Edge)getEdge(ed.getDest(), ed.getSrc());
						ed1.setTag(1);
						removeEdge(ed.getSrc(), ed.getDest());
					}
				}
				else {//if the edge is bidirectional -> change only the weight between the edges
					Edge e1=(Edge)e;
					Node nDst=(Node)getNode(e.getDest());
					Edge e2=nDst.getEdgesOf().get(e.getSrc());
					if(e1.getTag()==0 && e2.getTag()==0) {
						double tmp=e1.getWeight();
						e1.setWeight(e2.getWeight());
						e2.setWeight(tmp);
						e1.setTag(1);
					}
				}
			}
		}
		changes = saveTheChanges + 1;
	}

	/*
	 * A helper function to the reversGraph function, if the edge is bidirectional we need to change only the weight
	 * @param nDst = the destination node
	 */
	private boolean isBidirectional(int src,int dst) {
		Node nDst = (Node)getNode(dst);
		if(nDst.getEdgesOf().containsKey(src))
			return true;
		else
			return false;
	}

	/*
	 * A helper function to the reverseGraph function, set the tags of all the edges to 0 
	 * @param nod = A collection of all the nodes in the graph 
	 */
	private void zeroEdgeTag() {
		Collection<node_data> nod = nodes.values();
		for(node_data a:nod) {
			Node n = (Node)a;
			Collection<Edge> e = n.getEdgesOf().values();
			for(edge_data ed: e) {
				ed.setTag(0);
			}
		}
	}

	public void init(final String jsonSTR) {
        try {
            final JSONObject graph = new JSONObject(jsonSTR);
            final JSONArray nodes = graph.getJSONArray("Nodes");
            final JSONArray edges = graph.getJSONArray("Edges");
            for (int i = 0; i < nodes.length(); ++i) {
                final int id = nodes.getJSONObject(i).getInt("id");
                final String pos = nodes.getJSONObject(i).getString("pos");
                final Point3D p = new Point3D(pos);
                this.addNode(new Node(id, p));
            }
            for (int i = 0; i < edges.length(); ++i) {
                final int s = edges.getJSONObject(i).getInt("src");
                final int d = edges.getJSONObject(i).getInt("dest");
                final double w = edges.getJSONObject(i).getDouble("w");
                this.connect(s, d, w);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}