package dataStructure;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Point3D;
import utils.StdDraw;

public class Fruit {
	
	static final double EPS = Math.pow(10, -7);
	private graph g;
	private edge_data edge;
	private Point3D location;
	private int value;
	private int type;
	private boolean taken = false;
	
	
	/*contructors*/
	public Fruit() {
		this.g = null;
		this.edge = null;
		this.location = null;
		this.value = 0;
		this.type = 0;
	}
	
	public Fruit(graph g, edge_data edge, Point3D p, int value, int type, boolean taken){
		this.g = g;
		this.edge = edge;
		this.location = p;
		this.value = value;
		this.type = type;

	}
	
	public Fruit(graph g) {
		this();
		this.g = g;
	}
	
	/**
	 * initializing fruit from json string
	 * @param fruit_json - the json string
	 */
	public void initJson (String fruit_json) {
		try {
			JSONObject line = new JSONObject(fruit_json);
			JSONObject ttt = line.getJSONObject("Fruit");
			int value = ttt.getInt("value");
			int type = ttt.getInt("type");
			String pos = ttt.getString("pos");
			Point3D posP = new Point3D(pos);
			this.value = value;
			this.type = type;
			this.location = posP;
			
		} 
		catch (JSONException e) {e.printStackTrace();}
	}
	
	/**
	 * 
	 * @return the edge that the robot need to pass to eat the fruit
	 */
	public edge_data getEdgeFruit() {
		for(node_data ni: g.getV()) {
			for(node_data nj: g.getV()) {
				Point3D niP = ni.getLocation();
				Point3D njP = nj.getLocation();
				if(ni == nj) continue;
				double destFruitniP = this.location.distance2D(niP);
				double destFruitnjP = this.location.distance2D(njP);
				double destniPnjP = niP.distance2D(njP);
				if(Math.abs(destFruitniP + destFruitnjP - destniPnjP) <= EPS) {
					int niKey = ni.getKey();
					int njKey = nj.getKey();
					int nextMin = Math.min(niKey, njKey);
					int nextMax = Math.max(niKey, njKey);
					if(type == 1) {
						Edge e = (Edge) g.getEdge(nextMin, nextMax);
						if(e != null)return e;
					}
					else {
						edge_data e = g.getEdge(nextMax, nextMin);
						if(e != null)return e;
					}
				}
			}
		}
		return null;
	}
	
	public graph getG() {
		return g;
	}
	public void setG(graph g) {
		this.g = g;
	}
	public edge_data getEdge() {
		return edge;
	}
	public void setEdge(edge_data edge) {
		this.edge = edge;
	}
	public Point3D getP() {
		return location;
	}
	public void setP(Point3D p) {
		this.location = p;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public boolean isTaken() {
		return taken;
	}
	public void setTaken(boolean taken) {
		this.taken = taken;
	}
	public static double getEps() {
		return EPS;
	}
	
}
