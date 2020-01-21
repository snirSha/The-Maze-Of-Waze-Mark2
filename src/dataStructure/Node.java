package dataStructure;
import java.io.Serializable;
import java.util.HashMap;

import dataStructure.node_data;
import utils.Point3D;

/*
 * The implementation of node_data interface.
 * Contains HashMap data structure for all the edges that start at this node.
 * The key is the data of the node of the destination.
 * The value is the edge between this node and the destination. 
 * 
 * @authors Snir and Omer
 */
public class Node implements node_data, Serializable{

	/*
	 * @param _edges = The HashMap of the vertex
	 * @param _data = The key number of this vertex
	 * @param  _p = The point where the vertex stands
	 * @param _weight = The weight of the vertex, we will use it in Graph_Algo
	 * @param _info = The information of the vertex, we will use it in Graph_Algo
	 * @param _tag = The tag of the vertex, we will use it in Graph_Algo
	 */
	private HashMap<Integer, Edge> _edges = new HashMap<>(); //b.key,a->b     c.key,b->c     d.key,c->d     a.key,d->a  |  Edge.dest.key,Edge
	private int _data;
	private Point3D _p;
	private double _weight;
	private String _info;
	private int _tag;
	
	private static int count = 0;
	
	/*
	 * Default constructor 
	 */
	public Node() {
		this._data=count;
		this._p = new Point3D(0, 0);
		this._weight=0;
		this._tag=0;
		this._info="";
		count++;
	}
	
	/*
	 * Constructor with all the parameters
	 */
	public Node(int d,Point3D p, double w, String s, int t) {
		this._data = d;
		this._p = new Point3D(p);
		this._weight=w;
		this._info=s;
		this._tag=t;
	}
	
	/*
	 *Copy constructor (deep copy) 
	 */
	public Node (Node other) {
		this._edges = new HashMap<>();
		for(Edge e: other.getEdgesOf().values()) {
			this._edges.put(e.getDest(), new Edge(e));
		}
		_data = other._data;
		_p = other._p;
		_weight = other._weight;
		_info = other._info;
		_tag = other._tag;
	}
	
	public Node(int key, Point3D point3d) {
		this._data = key;
		this._p = new Point3D(point3d.x(), point3d.y());
		_weight = 0;
		_info = "";
		_tag = 0;
		
	}

	/*
	 * This function add a Edge to the HashMap
	 */
	public void addEdge(Edge e) {
		if(this.getKey() == e.getSrc()) {
			_edges.put(e.getDest(), e);
		}
		else
			System.out.println("Wrong edge!!!");
	}
	
	/*
	 * Return the data of the vertex
	 */
	@Override
	public int getKey() {
		return _data;
	}

	/*
	 * Return the point of the vertex (x,y)
	 */
	@Override
	public Point3D getLocation() {
		return _p;
	}

	/*
	 * Change this point to the parameter's point
	 */
	@Override
	public void setLocation(Point3D p) {
		if(p!=null)
			this._p=new Point3D(p);
		else
			System.out.println("There is no location!!!");
	}

	/*
	 * Return the weight of the vertex
	 */
	@Override
	public double getWeight() {
		return _weight;
	}

	/*
	 * Change this weight to the parameter's weight
	 */
	@Override
	public void setWeight(double w) {
		this._weight=w;
	}

	/*
	 * Return the information of the vertex
	 */
	@Override
	public String getInfo() {
		return _info;
	}

	/*
	 * Change the information to the parameter's information
	 */
	@Override
	public void setInfo(String s) {
		this._info=s;
	}

	/*
	 * Return the tag of the vertex
	 */
	@Override
	public int getTag() {
		return _tag;
	}

	/*
	 * Change the tag to the parameter's tag
	 */
	@Override
	public void setTag(int t) {
		this._tag=t;
	}
	
	/*
	 * Return the HashMap (all the edges that this vertex is their source) 
	 */
	public HashMap<Integer,Edge> getEdgesOf(){
		return _edges;
	}
}