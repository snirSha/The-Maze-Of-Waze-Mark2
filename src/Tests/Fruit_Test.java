package Tests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.Edge;
import dataStructure.Fruit;
import gameClient.MyGameGUI;
import utils.Point3D;

class Fruit_Test {
	game_service game;
	MyGameGUI mgg;
	
	@Test
	void initJsonTest() {
		int scenario_num = 13;
		game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games
		String g = game.getGraph();
		DGraph gg = new DGraph();
		gg.init(g);
		JSONObject line;
		String info = game.toString();
		try {
			line = new JSONObject(info);
			Iterator<String> f_iter = game.getFruits().iterator();
			assertEquals(f_iter.next(),"{\"Fruit\":{\"value\":5.0,\"type\":-1,\"pos\":\"35.19149533609957,32.109186033266795,0.0\"}}");
			assertEquals(f_iter.next(),"{\"Fruit\":{\"value\":8.0,\"type\":-1,\"pos\":\"35.1975216180008,32.102581944657636,0.0\"}}");
		}
		catch (JSONException e) {e.printStackTrace();}
	}
	
	
	   @Test
	    void getSetType() {
	        game_service game = Game_Server.getServer(0); // you have [0,23] games
	        String g = game.getGraph();
	        DGraph gg = new DGraph();
	        gg.init(g);
	        Fruit f=new Fruit();
	        for(String s: game.getFruits()) {
	    	   f.initJson(s);
	    	   assertEquals(f.getType(),-1);
	    	   f.setType(1);
	    	   assertEquals(f.getType(),1);
	       }
	   }
	   
	   @Test
	    void getSetValue() {
	        game_service game = Game_Server.getServer(0); // you have [0,23] games
	        String g = game.getGraph();
	        DGraph gg = new DGraph();
	        gg.init(g);
	        Fruit f=new Fruit();
	        for(String s: game.getFruits()) {
	    	   f.initJson(s);
	    	   assertEquals(f.getValue(),5);
	    	   f.setValue(20);
	    	   assertEquals(f.getValue(),20);
	       }
	   }
	   
	   
	   @Test
	    void getSetEdge() {
	        game_service game = Game_Server.getServer(0); // you have [0,23] games
	        String g = game.getGraph();
	        DGraph gg = new DGraph();
	        gg.init(g);
	        Fruit f=new Fruit();
	        for(String s: game.getFruits()) {
	    	   f.initJson(s);
	    	   Edge e=new Edge(1,2,5);
	    	   f.setEdge(e);
	    	   assertEquals(f.getEdge().getDest(),2);
	    	   assertEquals(f.getEdge().getSrc(),1);
	    	   assertEquals(f.getEdge().getWeight(),5);
	       }
	   }
	   
	   
	   @Test
	    void getSetPosition() {
	        game_service game = Game_Server.getServer(0); // you have [0,23] games
	        String g = game.getGraph();
	        DGraph gg = new DGraph();
	        gg.init(g);
	        Fruit f=new Fruit();
	        for(String s: game.getFruits()) {
	        	Point3D p = new Point3D(0,0);
	        	f.setP(p);
	    	   assertEquals(f.getP().ix(),0);
	    	   assertEquals(f.getP().iy(),0);
	       }
	   }
	   
	   @Test
	    void isTakenTest() {
	        game_service game = Game_Server.getServer(0); // you have [0,23] games
	        String g = game.getGraph();
	        DGraph gg = new DGraph();
	        gg.init(g);
	        Fruit f=new Fruit();
	        for(String s: game.getFruits()) {
	        	assertEquals(f.isTaken(),false);
	       }
	   }
	   
	   
}
