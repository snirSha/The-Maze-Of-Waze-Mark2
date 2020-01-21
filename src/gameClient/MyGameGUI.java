package gameClient;

import dataStructure.Node;
import dataStructure.Robot;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;


import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.json.JSONException;
import org.json.JSONObject;
import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.Fruit;
import utils.Point3D;
import utils.StdDraw;
/*
 * This class draw graphs using stdDraw
 *
 * @authors Snir and Omer 
 */ 

public class MyGameGUI {
	
	final double xMax = 35.216;
	final double xMin = 35.1835;
	final double yMax = 32.11;
	final double yMin = 32.1;
	final static double robotIconSize = .0015;
	final static double ourEPS = 0.0002;
	final static double minEPS = 0.00001;
	static int rid = -1;
	private int scenario;

	public Graph_Algo ga;
	public HashMap<Integer, Robot> robots;
	public HashMap<Point3D, Fruit> fruits;
	public game_service game;

	/*
	 * Default constructor
	 */
	public MyGameGUI() {
		ga = new Graph_Algo();
		this.robots = new HashMap<>();
		this.fruits = new HashMap<>();
	}

	/*
	 * Copy constructor using the init function from Graph_Algo class
	 */
	public MyGameGUI(graph g) {

		this.ga = new Graph_Algo();
		ga.init(g);
	}

	/*
	 *If the user ask for the manual game this message will appear (instruction for the game)
	 *@param Instructions - the JPanel message 
	 */
	public void InstructionForManual() {
		JPanel Instructions = new JPanel();
		JLabel info = new JLabel("<html>Hello player, in this manual game you can choose from 24 maps (0-23).<br>"
				+ "Some maps have one robot and some have more than one.<br>"
				+ "The purpose of the robot, is to eat as many fruits as possible.<br><br>" 
				+ "Banana - Bananas can be taken from high node to low node (number).<br>"
				+ "Apple - Apples can be taken from low node to high node(number).<br><br>"
				+ "Manual control of a single robot will be performed by left-clicking on the<br>"
				+ "neighboring node of the robot."
				+ "In the game where there is more than one robot, control is done by clicking the<br>"
				+ "key number of the robot on the keyboard, and than click on the neighboring nodes"
				+ "you wish to go.<br><br>"
				+ "Good luck!");

		JOptionPane.showInternalConfirmDialog(Instructions, 
				info, null, JOptionPane.DEFAULT_OPTION);
	}

	/**
	 * start the procces of the manual scenario given
	 * @param s
	 * @return
	 */
	public game_service gameManualScenario(int s) {
		scenario = s;
		game_service game = Game_Server.getServer(s); // you have [0,23] games
		String g = game.getGraph();
		this.ga.dg.init(g);
		drawDGraph();
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("robots");
			int src_node = 0;  

			String[] nodes = new String[ga.dg.nodeSize()];
			for (int i = 0; i < ga.dg.nodeSize(); i++) {
				nodes[i] = "" + i;
			}
			for(int a = 0; a < rs ; a++) {

				String string = (String) JOptionPane.showInputDialog(
						null, "Pick node for robot " + a + "\n",				                   
						"Pick starting noeds",
						JOptionPane.PLAIN_MESSAGE,
						null, nodes,
						"ham");
				int node = 0;
				try {
					node = Integer.parseInt(string);
					src_node = node;
				}catch (Exception e) {
					e.getMessage();
				}
				game.addRobot(src_node);
				src_node = 0;
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return game;
	}

	/**
	 * by given game, run the game manualy
	 * @param game
	 */
	public void runManualScenario(game_service game) {
		
		Long tmpTime = game.timeToEnd();
		KML_Logger kml = new KML_Logger();
		game.startGame();
		while(game.isRunning()) {
			StdDraw.enableDoubleBuffering();

			refreshDraw();
			drawFruits(game);
			drawRobots(game);
			
			if (tmpTime - game.timeToEnd() > 200L) {
				kml.addRobotsFruits(robots, fruits);
				tmpTime = game.timeToEnd();
			}
			
			moveRobotsManual(game);
			refreshElements(game);
			printScore(game);

			StdDraw.show();
		}
		displayFinalScore(game);
		askToSaveKml(kml, scenario);
	}

	/**
	 * update the fruits tnd robots in the list (by location and value)
	 * @param game
	 */
	public void refreshElements(game_service game) {
		initFruits(game);
		initRobots(game);
	}

	/**
	 * move the robots using graph algorithms to get the most fruits 
	 * @param game
	 */
	public void moveRobotsManual(game_service game) {
		List<String> log = game.move();
		if(log != null) {
			long t = game.timeToEnd();
			for(int i = 0; i < log.size(); i++) {
				String robot_json = log.get(i);
				try {
					JSONObject line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");

					if(dest == -1) {
						/* snir's shit */
						if(game.getRobots().size() > 1) {
							rid = chooseRid();
							if(rid == -1)
								return;
							Robot r = robots.get(rid);
							if(r.getNode() != null) {
								src = robots.get(rid).getNode().getKey();
							}
						}
						dest = nextNodeManual(ga.dg,src);
						////////////////////

						game.chooseNextEdge(rid, dest);
						Robot r = robots.get(rid);
						r.setNode(ga.dg.getNode(dest));
						System.out.println("Turn to node: " + dest + "  time to end:" + (t / 1000));
						System.out.println(ttt);
					}
				} 
				catch (JSONException e) {e.printStackTrace();}
			}
		}
	}
	/**
	 * by clicking the node the player want to go to, moves the chosen robot to him
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNodeManual(graph g, int src) {//The manual moves
		int ans = -1;
		if(StdDraw.pointOfMouse != null) {
			Point3D mouseClick = new Point3D(StdDraw.pointOfMouse);
			for (edge_data e: g.getE(src)) {
				if(mouseClick.distance2D(g.getNode(e.getDest()).getLocation()) < ourEPS)
					ans = e.getDest();
			}
		}
		return ans;
	}
	/**
	 * by clicking on numbers 0 - 4 the player can choose robot to move
	 * @return
	 */
	private int chooseRid() {
		char p = StdDraw.keyPress;
		int rid = -1;
		switch (p) {
		case '0':
			rid = 0;
			break;
		case '1':
			rid = 1;
			break;
		case '2':
			rid = 2;
			break;
		case '3':
			rid = 3;
			break;
		case '4':
			rid = 4;
			break;

		default:
		}

		return rid;
	}

	/*
	 * Add a node to the drawing using addNode function from DGraph
	 */
	public void addNode(Node a) {
		ga.dg.addNode(a);
	}


	/*
	 * Draw the nodes
	 * @param x = the x of the node location (point) 
	 * @param y = the y of the node location (point)
	 * @param abs = the number of the node
	 */
	public void drawNodes() {
		try {
			Collection<node_data> n=ga.dg.getV();
			if(n != null && n.size() > 0) {
				for (node_data a:n) {
					double x=a.getLocation().x();
					double y=a.getLocation().y();
					StdDraw.setPenRadius(0.03);
					StdDraw.setPenColor(StdDraw.BLUE);//nodes in blue
					StdDraw.point(x,y);
					StdDraw.setPenColor(StdDraw.BLACK);
					String abs = a.getKey()+"";
					StdDraw.text(x,y,abs);
				}
			}

		}catch(Exception e) {
			System.out.println("No nodes to draw");
		}
	}

	/*
	 * Draw the edges
	 * @param allNodes = A collection of all the nodes
	 * @param allEdgesOfNode = A collection of all the edges that came out of the parameter's node
	 * @param Sx = the x of the source location
	 * @param Sy = the y of the source location
	 * @param Dx = the x of the destination location
	 * @param Dy = the y of the destination location
	 * @param arrowX = the x of the "arrow" point location
	 * @param arrowY = the y of the "arrow" point location
	 * @param te = the string of the weight number
	 */
	public void drawEdges() {
		try {
			Collection<node_data> allNodes = ga.dg.getV();
			if(allNodes != null && allNodes.size() > 0) {
				for(node_data n:allNodes) {
					Collection<edge_data> allEdgesOfNode = ga.dg.getE(n.getKey());
					if(allEdgesOfNode != null && allEdgesOfNode.size() > 0) {
						for(edge_data edges:allEdgesOfNode) {
							double Sx = ga.dg.getNode(edges.getSrc()).getLocation().x();
							double Sy = ga.dg.getNode(edges.getSrc()).getLocation().y();
							double Dx = ga.dg.getNode(edges.getDest()).getLocation().x();
							double Dy = ga.dg.getNode(edges.getDest()).getLocation().y();

							StdDraw.setPenRadius(0.005);
							StdDraw.setPenColor(StdDraw.ORANGE);//paint the line between the nodes in orange
							StdDraw.line(Sx,Sy,Dx,Dy);

							StdDraw.setPenRadius(0.01);
							StdDraw.setPenColor(StdDraw.RED);

							double arrowX= (Dx*8+Sx)/9;
							double arrowY= (Dy*8+Sy)/9;
							StdDraw.point(arrowX,arrowY);

							double tmp = edges.getWeight();
							String dou = String.format("%.4g%n", tmp);

							String te = dou+"";

							StdDraw.setPenRadius(0.1);
							StdDraw.setPenColor(Color.BLACK);

							double newX= (Dx*4+Sx)/5;
							double newY= (Dy*4+Sy)/5;

							StdDraw.text(newX, newY, te);
						}
					}
				}
			}

		}catch(Exception e) {
			System.out.println("No edges to Draw");
		}
	}


	/*
	 * Remove node from the drawing using removeNode from DGraph class
	 */
	public void removeNode(int x) {
		ga.dg.removeNode(x);
	}

	/*
	 * Remove edge from the drawing using removeEdge from DGraph class 
	 */
	public void removeEdge(int x,int y) {
		ga.dg.removeEdge(x,y);
	}

	/*
	 * Reverse the graph using reverseGraph in DGraph class
	 */
	public void reversedGraph() {
		ga.dg.reversedGraph();
	}

	/*
	 * This function open a window and calls to drawNode and drawEdge
	 */
	public void drawDGraph() {
		try {
			if(ga.dg.getV() != null) {
				StdDraw.setGui(this);
				setPageSize();
				drawEdges();
				drawNodes();
			}
		}catch(Exception e){
			System.out.println("Nothing to draw");
		}
	}

	/**
	 * set the page size
	 */
	private void setPageSize() {
		final double fixScale = 0.0015;
		StdDraw.setCanvasSize(1200 , 600 );
		StdDraw.setXscale(xMin - fixScale, xMax + fixScale);
		StdDraw.setYscale(yMin - fixScale, yMax + fixScale);
	}

	/**
	 * refresh a already drawed graph
	 */
	public void refreshDraw() {
		StdDraw.clear();
		drawEdges();
		drawNodes();
	}

	/*
	 * Delete the graph in the drawing
	 */
	public void deleteGraph() {
		StdDraw.clear();
		ga.dg = new DGraph();
	}

	/**
	 * draw the fruits in the HashMap
	 * @param game
	 */
	public void drawFruits(game_service game) {

		Collection<Fruit> f_list = fruits.values();
		for (Fruit fruit : f_list) {
			int type = fruit.getType();
			Point3D pos = new Point3D(fruit.getP());
			if(type == -1) {
				StdDraw.picture(pos.x(), pos.y(), "banana.png", 0.0007, 0.0007);
			}else if(type == 1) {
				StdDraw.picture(pos.x(), pos.y(), "apple.png", 0.0007, 0.0007);
			}
		}
	}

	/**
	 * draw the robots in the HashMap
	 * @param game
	 */
	public void drawRobots(game_service game) {

		Collection<Robot> r_list = robots.values();
		for (Robot robot : r_list) {
			int rid = robot.getId();
			Point3D pos = new Point3D(robot.getLocation());
			String file = "";
			if(rid == 0) {
				file = "robot0.png";
			}
			else if(rid == 1) {
				file = "robot1.png";
			}
			else if(rid == 2) {
				file = "robot2.png";
			}
			else if(rid == 3) {
				file = "robot3.png";
			}
			else file = "robot4.png";
			try {
				StdDraw.picture(pos.x(), pos.y(), file, robotIconSize, robotIconSize);
			}catch (Exception e) {
				StdDraw.circle(pos.x(), pos.y(), robotIconSize * 0.3);
			}

		}
	}


	/**
	 * when game over, prints the final score
	 * @param game
	 */
	public void displayFinalScore(game_service game){

		int scoreInt = 0;
		int movesInt = 0;
		try {
			String results = game.toString();
			System.out.println("Game Over: " + results);

			JSONObject score = new JSONObject(results);
			JSONObject ttt = score.getJSONObject("GameServer");
			scoreInt = ttt.getInt("grade");
			movesInt = ttt.getInt("moves");
			String endGame = "Youre score is: " + scoreInt + "\n"
					+ "Amount of moves:   " + movesInt	;

			JOptionPane.showMessageDialog(null, endGame);
		}
		catch (Exception e) {
			e.getMessage();
		}
	}
	
	
	/**
	 * ask the player if he wants to save the kml log of the game
	 * @param kml
	 * @param scenario
	 */
	public void askToSaveKml(KML_Logger kml, int scenario) {

		Object[] options = {"Yes",
		"No"};
		int n = JOptionPane.showOptionDialog(null,
				"Would you like to save KML log of the game?",
				"Save KML",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,     //do not use a custom Icon
				options,  //the titles of buttons
				options[0]); //default button title

		if (n == 0) {
			JFrame frame = new JFrame();
			String message = "Enter file name for scenario " + scenario + ": ";
			String filename = JOptionPane.showInputDialog(frame, message);
			if (filename == null) {
				
				return;
				
			}else if(filename.isEmpty()) {
				filename = "" + scenario;
			}
			try {
				kml.saveToFile(filename);
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		}

	}

	/**
	 * while the game is running, it shows the current score on the game window
	 * @param game
	 */
	public void printScore(game_service game) {
		String results = game.toString();
		long t = game.timeToEnd();
		try {
			int scoreInt=0;
			JSONObject score = new JSONObject(results);
			JSONObject ttt = score.getJSONObject("GameServer");
			scoreInt = ttt.getInt("grade");

			String countDown = "Time: " + t / 1000 + "." + t % 1000;
			String scoreStr = "Score: " + scoreInt;
			double tmp1 = xMax - xMin;
			double tmp2 = yMax - yMin;

			StdDraw.setPenRadius(0.05);
			StdDraw.setPenColor(Color.BLACK);
			StdDraw.text(xMin+tmp1 / 1.05 , yMin + tmp2 / 0.95, countDown);
			StdDraw.text(xMin+tmp1 / 1.05 , yMin + tmp2, scoreStr);

		}catch (Exception e) {
			System.out.println("Failed to print score");
		}
	}

	/**
	 * initialize the fruits of the game
	 * @param game
	 */
	public void initFruits(game_service game) {
		List<String> f_list = game.getFruits();
		if(fruits != null)
		{
			fruits.clear();
		}
		else fruits = new HashMap<>();

		for (String string : f_list) {
			String json = string;
			Fruit f = new Fruit(ga.dg);
			f.initJson(json);
			edge_data e = f.getEdgeFruit();
			if(e != null) {
				f.setEdge(f.getEdgeFruit());
				fruits.put(f.getP(),f);
			}
			else {
				System.out.println("could not find edge to fruit");
			}

		}
	}

	/**
	 * initialize the robots of the game
	 * @param game
	 */
	public void initRobots(game_service game) {
		List<String> r_list = game.getRobots();
		if(robots != null)
		{
			robots.clear();
		}
		else robots = new HashMap<>();
		try {
			for (String json : r_list) {
				Robot r = new Robot();
				r.setG(ga.dg);
				r.initJson(json);
				robots.put(r.getId(),r);
			}
		}catch (Exception e) {

		}

	}
}