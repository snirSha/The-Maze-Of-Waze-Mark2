package gameClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.Fruit;
import dataStructure.Robot;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;
import utils.StdDraw;


public class Automat{

	game_service game;
	public Graph_Algo ga;
	MyGameGUI mgg;
	int scenario;
	private List<String> log;
	private Thread thread;
	private static long dt = 111;

	private static double EPS = 0.00001;


	public Automat(int s, game_service game, MyGameGUI mgg, int id) {

		
		Game_Server.login(id);
		this.game = game;
		this.mgg = mgg;
		ga = mgg.ga;
		game = gameAutoScenario(s);
		mgg.initFruits(game);
		mgg.initRobots(game);
		runAutoScenario(game);
	}

	/**
	 * starting the auto game
	 * @param game
	 */
	public void runAutoScenario(game_service game) {
		game.startGame();

		Long tmpTime = game.timeToEnd();
		KML_Logger kml = new KML_Logger();
		kml.addNodes(ga.dg);

		moveRobotsThread();

		while(game.isRunning()) {
			StdDraw.enableDoubleBuffering();
			mgg.refreshDraw();
			mgg.drawFruits(game);
			mgg.drawRobots(game);

			if (tmpTime - game.timeToEnd() > 103L) {
				kml.addRobotsFruits(mgg.robots, mgg.fruits);
				tmpTime = game.timeToEnd();
			}


			moveRobotsAuto(game);
			mgg.refreshElements(game);
			mgg.printScore(game);

			StdDraw.show();
		}

		String remark = kml.getLogOfGame();
		//System.out.println(remark);
		System.out.println("Send KML to DB: " + game.sendKML(remark));
		mgg.displayFinalScore(game);
		//kml.saveToFile("" + scenario);
		mgg.askToSaveKml(kml, scenario);

	}

	public synchronized void moveRobotsThread(){ 
		thread = new Thread(new Runnable() {

			@Override
			public synchronized void run() {
				while(game.isRunning()){
					if(game.isRunning()){
						log = game.move();
					}
					try {
						
						Thread.sleep(dt);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				thread.interrupt();
			}
		});
		thread.start();
	}

	/**
	 * this func moves the robots by graph algorithms to get much fruits as possible
	 * @param game
	 */
	public void moveRobotsAuto(game_service game) {

		if(log != null){
			ArrayList<Robot> botsToMove = new ArrayList<Robot>();
			ArrayList<Fruit> fruitsWithoutBots = new ArrayList<Fruit>();
			Collection<Integer> robots = mgg.robots.keySet();
			for (Integer integer : robots) {
				Robot b = mgg.robots.get(integer);
				if(b.getTrack() == null){
					botsToMove.add(b);
				}
			}
			Collection<Point3D> fruitSet = mgg.fruits.keySet();
			for (Point3D point3d : fruitSet) {
				Fruit currF = mgg.fruits.get(point3d);
				if(!currF.isTaken());{
					fruitsWithoutBots.add(currF);
				}
			}

			while(!botsToMove.isEmpty() && !fruitsWithoutBots.isEmpty()){
				int srcIndex = 0;
				Robot SrcFrom = null;
				Fruit DestTo = null;
				int destIndex = 0;
				double dist = Integer.MAX_VALUE;
				for(int i = 0; i < botsToMove.size(); i++){
					for(int j = 0; j < fruitsWithoutBots.size(); j++){
						double tmp = ga.shortestPathDist(botsToMove.get(i).getNodeKey(), fruitsWithoutBots.get(j).getEdge().getSrc()) + fruitsWithoutBots.get(j).getEdge().getWeight();
						if(tmp < dist){
							srcIndex = i;
							destIndex = j;
							SrcFrom = botsToMove.get(i);
							DestTo = fruitsWithoutBots.get(j);
							dist = tmp;
						}
					}
				}

				SrcFrom.setFruitPos(DestTo.getP());

				List<node_data> path = ga.shortestPath(SrcFrom.getNodeKey(), DestTo.getEdge().getSrc());
				path.add(ga.dg.getNode(DestTo.getEdge().getDest()));
				path.remove(0);
				SrcFrom.setTrack(path);
				botsToMove.remove(srcIndex);
				DestTo.setTaken(true);
				fruitsWithoutBots.remove(destIndex);
			}

			for (Integer integer : robots) {
				Robot b = mgg.robots.get(integer);
				if(b.getTrack() != null){
					if(b.getNode().getLocation().distance2D(b.getLocation()) <= EPS){// if the robot is on the node he  to be on

						List<node_data> path = b.getTrack();

						game.chooseNextEdge(b.getId(), path.get(0).getKey());

						b.setNode(path.get(0));
						path.remove(0);

						if(path.size() == 0){
							b.setTrack(null);
						}
					}
				}
			}
		}
	}

	/**
	 * returns the relevant game server to the game
	 * @param s is the scenario number
	 * @return
	 */
	public game_service gameAutoScenario(int s) {
		scenario = s;
		game = Game_Server.getServer(s); // you have [0,23] games
		String g = game.getGraph();
		this.ga.dg.init(g);
		mgg.initFruits(game);
		mgg.initRobots(game);
		mgg.drawDGraph();

		String info = game.toString();
		JSONObject line;
		int rs = 0;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			rs = ttt.getInt("robots");
		}catch (Exception e) {
			e.printStackTrace();
		}

		int i = 0;
		Collection<Fruit> f = mgg.fruits.values();
		

		
		/*locate the robots near the highest value fruits */	
//		Fruit biggestValue = null;
//		for (Fruit fruit0 : f) {
//			if (i >= rs)break;
//			if(fruit0.isTaken())continue;
//			biggestValue = null;
//			for (Fruit fruit1 : f) {
//				System.out.println(fruit1.getValue());
//				if(fruit1.isTaken())continue;
//				if (fruit0.getValue() > fruit1.getValue()) {
//					if(biggestValue == null || (fruit0.getValue() > biggestValue.getValue())) {
//						biggestValue = fruit0;
//					}
//				}else {
//					if(biggestValue == null || (fruit1.getValue() > biggestValue.getValue() && !fruit1.isTaken())) {
//						biggestValue = fruit1;
//					}
//				}
//			}
//			
//			int minNode = 0;
//			int maxNode = 0;
//			int nodeKey = 0;
//			
//			if(biggestValue != null) {
//				edge_data e = biggestValue.getEdge();
//				minNode = Math.min(e.getDest(), e.getSrc());
//				maxNode = Math.max(e.getDest(), e.getSrc());
//				
//				if(biggestValue.getType() == -1) {
//					nodeKey = maxNode;
//					
//				}else {
//					nodeKey = minNode;	
//				}
//				biggestValue.setTaken(true);
//			}
//			
//			game.addRobot(nodeKey);
//			System.out.println("robot: " + i + ",  starts at node: " + nodeKey + ", fruit val: " + biggestValue.getValue());
//			i++;
//		}
		
		/*locate the robots near the first fruits on the list (not very smart but helps in some stages) */	
		for (Fruit fruit : f) {
			if(i >= rs)break;
			if(!fruit.isTaken()) {
				edge_data e = fruit.getEdge();
				int minNode = Math.min(e.getDest(), e.getSrc());
				int maxNode = Math.max(e.getDest(), e.getSrc());

				if(fruit.getType() == -1) {
					game.addRobot(maxNode);
				}else {
					game.addRobot(minNode);
				}
				fruit.setTaken(true);
				i++;
			}
		}

		return game;
	}




}