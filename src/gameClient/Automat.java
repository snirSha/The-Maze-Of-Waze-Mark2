package gameClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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


	public Automat(int s, game_service game, MyGameGUI mgg) {

		int id = 204040687;
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

		while(game.isRunning()) {
			StdDraw.enableDoubleBuffering();
			mgg.refreshDraw();
			mgg.drawFruits(game);
			mgg.drawRobots(game);
			if (tmpTime - game.timeToEnd() > 300L) {
				kml.addRobotsFruits(mgg.robots, mgg.fruits);
				tmpTime = game.timeToEnd();
			}


			moveRobotsAuto(game);
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}



			mgg.refreshElements(game);
			mgg.printScore(game);

			StdDraw.show();
		}

		String remark = kml.getLogOfGame();
		System.out.println(remark);
		game.sendKML(remark);
		mgg.displayFinalScore(game);
		//mgg.askToSaveKml(kml, scenario);

	}

	

	/**
	 * this func moves the robots by graph algorithms to get much fruits as possible
	 * @param game
	 */
	public void moveRobotsAuto(game_service game) {

			List<String> log = game.move();


			if(log != null)
			{
				ArrayList<Robot> botsToMove = new ArrayList<Robot>();
				ArrayList<Fruit> fruitsWithoutBots = new ArrayList<Fruit>();
				Set<Integer> botS = mgg.robots.keySet();
				for (Integer integer : botS) {
					Robot b = mgg.robots.get(integer);
					if(b.getTrack() == null)
					{
						botsToMove.add(b);
					}
				}
				Set<Point3D> fruitSet = mgg.fruits.keySet();
				for (Point3D point3d : fruitSet) {
					Fruit currF = mgg.fruits.get(point3d);
					if(!currF.isTaken());
					{
						fruitsWithoutBots.add(currF);
					}
				}

				while(!botsToMove.isEmpty() && !fruitsWithoutBots.isEmpty())
				{
					int srcIndex = 0;
					Robot SrcFrom = null;
					Fruit DestTo = null;
					int destIndex = 0;
					double dist = Integer.MAX_VALUE;
					for(int i = 0; i < botsToMove.size(); i++)
					{
						//double distTemp = Integer.MAX_VALUE;
						for(int j = 0; j < fruitsWithoutBots.size(); j++)
						{
							double tmp = ga.shortestPathDist(botsToMove.get(i).getNodeKey(), fruitsWithoutBots.get(j).getEdge().getSrc()) + fruitsWithoutBots.get(j).getEdge().getWeight();
							if(tmp < dist)
							{
								srcIndex = i;
								destIndex = j;
								SrcFrom = botsToMove.get(i);
								DestTo = fruitsWithoutBots.get(j);
								dist = tmp;
							}
						}
					}
					List<node_data> path = ga.shortestPath(SrcFrom.getNodeKey(), DestTo.getEdge().getSrc());
					path.add(ga.dg.getNode(DestTo.getEdge().getDest()));
					path.remove(0);
					SrcFrom.setTrack(path);
					botsToMove.remove(srcIndex);
					DestTo.setTaken(true);
					fruitsWithoutBots.remove(destIndex);
				}

				for (Integer integer : botS) {
					Robot b = mgg.robots.get(integer);
					if(b.getTrack() != null)
					{
						if(b.getNode().getLocation().distance2D(b.getLocation())<= 0.00001)
						{

							List<node_data> path = b.getTrack();
							game.chooseNextEdge(b.getId(), path.get(0).getKey());
							b.setNode(path.get(0));
							path.remove(0);
							if(path.size() == 0)
							{
								b.setTrack(null);
							}
						}
					}
				}
				mgg.initFruits(game);
				mgg.initRobots(game);
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
		return game;
	}


		
	
}