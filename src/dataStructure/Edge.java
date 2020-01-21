package dataStructure;
import java.io.Serializable;

import dataStructure.edge_data;

/*
 * The implementation of edge_data interface.
 * A regular class with constructors, getters and setters (no data structure).
 *  
 * @authors Snir and Omer
 */
public class Edge implements edge_data, Serializable{
	/*
	 * @_src = The vertex that the edge came out of.
	 * @_dst = The vertex that the edge came in to. 
	 * @param _weight = The weight of the Edge, we will use it in Graph_Algo
	 * @param _info = The information of the Edge, we will use it in Graph_Algo
	 * @param _tag = The tag of the Edge, we will use it in Graph_Algo 
	 */
	private int _src;
	private int _dst;
	private double _weight;
	private String _info;
	private int _tag;
	
	/*
	 * Default constructor
	 */
	public Edge() {
		this._src=0;
		this._dst=0;
		this._weight=0;
		this._info="";
		this._tag=0;
	}

	/*
	 * Constructor with some parameters (not all)
	 */
	public Edge(int s,int d,double w) {
		this._src=s;  
		this._dst=d;
		this._weight=w;
		this._info="";
		this._tag=0;
	}
	
	/*
	 * Constructor with all parameters
	 */
	public Edge(int s,int d,double w,String str,int t) {
		this._src=s;  
		this._dst=d;
		this._weight=w;
		this._info=str;
		this._tag=t;
	}
	
	/*
	 * Copy constructor
	 */
	public Edge(Edge other) {
		this(other._src,other._dst,other._weight,other._info,other._tag);
	}
	
	/*
	 * Return the source vertex
	 */
	@Override
	public int getSrc() {
		return this._src;
	}

	/*
	 * Return the destination vertex
	 */
	@Override
	public int getDest() {
		return this._dst;
	}

	/*
	 * Return this Edge's weight
	 */
	@Override
	public double getWeight() {
		return this._weight;
	}

	/*
	 * Return this Edge's information
	 */
	@Override
	public String getInfo() {
		return this._info;
	}

	/*
	 * Change the information to the parameter's information
	 */
	@Override
	public void setInfo(String s) {
		this._info=s;
	}

	/*
	 * Return the Edge's tag
	 */
	@Override
	public int getTag() {
		return this._tag;
	}

	/*
	 * Change the tag to the parameter's tag
	 */
	@Override
	public void setTag(int t) {
		this._tag=t;		
	}
	
	/*
	 * Change the weight to the parameter's weight
	 */
	public void setWeight(double w) {
		this._weight=w;
	}
	

}
