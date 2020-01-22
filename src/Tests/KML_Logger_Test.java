package Tests;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import dataStructure.Edge;
import dataStructure.Node;
import utils.Point3D;

import java.util.Collection;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.Fruit;
import dataStructure.edge_data;
import gameClient.Automat;
import gameClient.KML_Logger;
import gameClient.Management;
import gameClient.MyGameGUI;

class KML_Logger_Test {
	Management ma;
	Automat a;
	public Graph_Algo ga;
	
	/*need to wait 30 sec for the game to end*/

	@Test
	void test() {
		MyGameGUI mgg = new MyGameGUI();
		game_service game = mgg.game;
		ga=mgg.ga;
		int scenario = 0;
		game = Game_Server.getServer(scenario); // you have [0,23] games
		String g = game.getGraph();
	
		mgg.ga.dg.init(g);
		mgg.initFruits(game);
		mgg.initRobots(game);

		String info = game.toString();
		JSONObject line;
		int rs = 0;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			rs = ttt.getInt("robots");
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}

		int i = 0;
		Collection<Fruit> f = mgg.fruits.values();
		for (Fruit fruit : f) {
			if(i >= rs)break;
			if(!fruit.isTaken()) {
				edge_data e = fruit.getEdge();
				
				if(fruit.getType() == -1) {
					game.addRobot(e.getDest());
				}else {
					game.addRobot(e.getSrc());
				}
				fruit.setTaken(true);
				i++;
			}
		}
		
		mgg.refreshElements(game);

		game.startGame();

		Long tmpTime = game.timeToEnd();
		KML_Logger kml = new KML_Logger();
		while(game.isRunning()) {

			if (tmpTime - game.timeToEnd() > 300L) {
				kml.addRobotsFruits(mgg.robots, mgg.fruits);
				tmpTime = game.timeToEnd();
			}
			
			//a.moveRobotsAuto(game);
			mgg.refreshElements(game);
		}
		
		
		System.out.println(kml.getLogOfGame());
		assertNotNull(kml.getLogOfGame());
		
	}

}
