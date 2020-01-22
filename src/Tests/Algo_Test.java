package Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.Edge;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;

class Algo_Test {
	

	@Test
	void initTest() {
		Graph_Algo ga = new Graph_Algo();
		DGraph dg = new DGraph();
		ga.init(dg);
		boolean ans = ga.dg == dg;
		assertEquals(true, ans);
	}

	/**
	 * Init a graph from file
	 * @param file_name
	 */
	@Test
	void initAndSaveFromFileTest() {
		Graph_Algo ga = new Graph_Algo();
		ga.dg.addNode(new Node(4,new Point3D(15, 15), 3.5, "cz", 0));
		ga.dg.addNode(new Node(5,new Point3D(5, 5), 2, "asd", 0));
		ga.dg.connect(4, 5, 2);
		ga.save("JunitTest");
		Graph_Algo ga2 = new Graph_Algo();
		ga2.init("JunitTest");
		Collection<edge_data> cl = ga2.dg.getE(0);
		for(edge_data e: cl) {
			Edge ed = (Edge)e;
			System.out.println(ed.getDest());
			assertEquals(true, ed.getSrc() == 4 && ed.getDest() == 5);
		}
	}

	
	/**
	 * returns the length of the shortest path between src to dest
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return
	 */
	@Test
	void shortestPathDistTest() {
		Graph_Algo ga = new Graph_Algo();
		ga.dg.addNode(new Node(0,new Point3D(-50, -50), 0, "snir", 0));
		ga.dg.addNode(new Node(1,new Point3D(50, 50), 0, "rotze", 0));
		ga.dg.addNode(new Node(2,new Point3D(50, -50), 0, "shetishvy", 0));
		ga.dg.addNode(new Node(3,new Point3D(-50, 50), 0, "lo", 0));
		ga.dg.addNode(new Node(4,new Point3D(0, -75), 0, "hal", 0));
		ga.dg.addNode(new Node(5,new Point3D(0, 75), 0, "hapanim", 0));
		ga.dg.addNode(new Node(6,new Point3D(0, 0), 0, "ya shramu&*^^%#@", 0));
		ga.dg.connect(0,3, 2);
		ga.dg.connect(3,5, 1);
		ga.dg.connect(5,1, 5);
		ga.dg.connect(1,2, 0);
		ga.dg.connect(2,4, 6);
		ga.dg.connect(4,0, 3);
		ga.dg.connect(6,4, 4);
		ga.dg.connect(6,5, 2.5);
		
		assertEquals(ga.shortestPathDist(0, 2),8);
		assertEquals(ga.shortestPathDist(6, 2),7.5);
		assertEquals(ga.shortestPathDist(6, 3),9);
	}
	/**
	 * returns the the shortest path between src to dest - as an ordered List of nodes:
	 * src--> n1-->n2-->...dest
	 * see: https://en.wikipedia.org/wiki/Shortest_path_problem
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return
	 */
	@Test
	void shortestPathTest() {
		Graph_Algo ga = new Graph_Algo();
		ga.dg.addNode(new Node(0,new Point3D(0, 0), 0, "ba", 0));
		ga.dg.addNode(new Node(1,new Point3D(-50,-50), 0, "li", 0));
		ga.dg.addNode(new Node(2,new Point3D(0,75), 0, "lamut", 0));
		ga.dg.addNode(new Node(3,new Point3D(50,50), 0, "mehazaeen", 0));
		ga.dg.addNode(new Node(4,new Point3D(50,-50), 0, "hakatan", 0));
		ga.dg.addNode(new Node(5,new Point3D(0,-75), 0, "haze", 0));
		ga.dg.addNode(new Node(6,new Point3D(-50,50), 0, "sheli", 0));
		ga.dg.connect(0,1, 1);
		ga.dg.connect(0,2, 2);
		ga.dg.connect(0,3, 3);
		ga.dg.connect(0,4, 4);
		ga.dg.connect(0,5, 5);
		ga.dg.connect(0,6, 6);
		ga.dg.connect(1,0, 6);
		ga.dg.connect(2,0, 5);
		ga.dg.connect(3,0, 4);
		ga.dg.connect(4,0, 3);
		ga.dg.connect(5,0, 2);
		ga.dg.connect(6,0, 1);
		ga.dg.connect(6,1, 0);
		ga.dg.connect(1,2, 0);
		ga.dg.connect(2,3, 0);
		ga.dg.connect(3,4, 0);
		ga.dg.connect(4,5, 0);
		ga.dg.connect(5,6, 0);

		List<node_data> ans=new ArrayList<>();
		ans.add(ga.dg.getNode(6));
		ans.add(ga.dg.getNode(1));
		ans.add(ga.dg.getNode(2));
		List<node_data> ans2=ga.shortestPath(6, 2);
		assertEquals(ans,ans2);
	
		ans.clear();
		List<node_data> ans3=ga.shortestPath(0, 4);	
		ans.add(ga.dg.getNode(0));
		ans.add(ga.dg.getNode(1));
		ans.add(ga.dg.getNode(2));
		ans.add(ga.dg.getNode(3));
		ans.add(ga.dg.getNode(4));
		assertEquals(ans,ans3);
	}




}
