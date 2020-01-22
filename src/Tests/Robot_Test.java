package Tests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Collection;
import java.util.List;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import Server.Game_Server;
import Server.game_service;
import dataStructure.Edge;
import dataStructure.Node;
import dataStructure.Robot;
import gameClient.MyGameGUI;
import utils.Point3D;

class Robot_Test {
	game_service game;
	MyGameGUI mgg;
	
	private List<String> initRobots(int Scenario) {
		game_service game = Game_Server.getServer(16); // you have [0,23] games
		String g = game.getGraph();
		mgg=new MyGameGUI();
		mgg.ga.dg.init(g);
	
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("robots");

			String[] nodes = new String[mgg.ga.dg.nodeSize()];
			for (int i = 0; i < mgg.ga.dg.nodeSize(); i++) {
				nodes[i] = "" + i;
			}
			for(int a = 0; a < rs ; a++) {
				game.addRobot(a);
			}
			System.out.println(game.getRobots());

		}catch(Exception e) {e.getMessage();}
		
		return game.getRobots();
	}
	
	@Test
	void initJsonGetSetIDTest() {
		Collection<String> rob=initRobots(16);
		Robot r = new Robot();
		int a = 0;
		for(String strR : rob) {
			r.initJson(strR);
			r.setId(0);
			assertEquals(r.getId(),a);
		}	
	}
	
	@Test
	void getSetIDTest() {
		Collection<String> rob=initRobots(16);
		Robot r = new Robot();
		for(String strR : rob) {
			r.initJson(strR);
			r.setId(8);
			assertEquals(r.getId(),8);
		}
	}
	
	@Test
	void getSetEdgeTest() {
		Collection<String> rob=initRobots(5);
		Robot r = new Robot();
		for(String strR : rob) {
			r.initJson(strR);
			Edge e = new Edge(1,1,5);
			r.setEdge(e);
			assertEquals(r.getEdge().getSrc(),1);
			assertEquals(r.getEdge().getDest(),1);
		}
	}
	
	@Test
	void getSetPositionTest() {
		Collection<String> rob=initRobots(0);
		Robot r = new Robot();
		Point3D p = new Point3D(5,5);
		for(String strR : rob) {
			r.initJson(strR);
			r.setLocation(p);
			assertEquals(r.getLocation(),new Point3D(5,5));
		}
	}
	
	@Test
	void getSetNodeTest() {
		Collection<String> rob=initRobots(2);
		Robot r = new Robot();
		Node n = new Node(2,new Point3D(0,0));
		for(String strR : rob) {
			r.initJson(strR);
			r.setNode(n);
			assertEquals(r.getNode().getLocation(),new Point3D(0,0));
			assertEquals(r.getNode().getKey(),2);
		}
	}
	
	
	@Test
	void getSetValueTest() {
		Collection<String> rob=initRobots(0);
		Robot r = new Robot();
		for(String strR : rob) {
			r.initJson(strR);
			r.setValue(100);
			assertEquals(r.getValue(),100);
		}
	}
	
	
	@Test
	void getSetSpeedTest() {
		Collection<String> rob=initRobots(0);
		Robot r = new Robot();
		for(String strR : rob) {
			r.initJson(strR);
			r.setSpeed(14);
			assertEquals(r.getSpeed(),14);
		}
	}
	
}
