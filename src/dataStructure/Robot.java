package dataStructure;

import java.util.List;
import org.json.JSONObject;

import Server.game_service;
import utils.Point3D;

public class Robot {
	int id;
	node_data node;
	edge_data edge;
	Point3D location;
	int value;
	graph g;
	int speed;
	List <node_data> Track;
	
	public Robot() {	
		this.id = -1;
		this.node = null;
		this.edge = null;
		this.location = null;
		this.value = -1;
		this.g = null;
		this.speed = -1;
		Track = null;
	}

	public Robot(int id, node_data node, edge_data edge, Point3D location, int value, graph g, int speed,
			List<node_data> track) {
		
		this.id = id;
		if(node != null) {
			this.node = new Node((Node)node);
		}
		if(edge != null) {
			this.edge = new Edge((Edge)edge);
		}
		
		this.location = location;
		this.value = value;
		this.g = g;
		this.speed = speed;
		Track = track;	
	}

	/**
	 * initializing robot from json string
	 * @param robot_json the json string
	 */
	public void initJson (String robot_json) {
		try {
			JSONObject line = new JSONObject(robot_json);
			JSONObject ttt = line.getJSONObject("Robot");
			int rid = ttt.getInt("id");
			this.id = rid;
			int speed = ttt.getInt("speed");
			this.speed = speed;
			int value = ttt.getInt("value");
			this.value = value;
			String pos = ttt.getString("pos");
			this.location = new Point3D(pos);
			
			if(g != null) {
				int src = ttt.getInt("src");
				this.node = g.getNode(src);
			}
			
		}catch (Exception e) {
			
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public node_data getNode() {
		return node;
	}
	public int getNodeKey() {
		return node.getKey();
	}

	public void setNode(node_data node) {
		this.node = node;
	}

	public edge_data getEdge() {
		return edge;
	}

	public void setEdge(edge_data edge) {
		this.edge = edge;
	}

	public Point3D getLocation() {
		return location;
	}

	public void setLocation(Point3D location) {
		this.location = location;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public graph getG() {
		return g;
	}

	public void setG(graph g) {
		this.g = g;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public List<node_data> getTrack() {
		return Track;
	}

	public void setTrack(List<node_data> track) {
		Track = track;
	}	
}
