package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.Test;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;

class DGraph_Test {
	private DGraph getDGraph() {
		DGraph tmp=new DGraph();
		Node a=new Node(0,new Point3D(-50,-50),1.5,"din",0);
		Node b=new Node(1,new Point3D(0,75),1.5,"din",0);
		Node c=new Node(2,new Point3D(50,50),1.5,"din",0);
		Node d=new Node(3,new Point3D(50,-50),1.5,"din",0);
		Node e=new Node(4,new Point3D(0,-75),1.5,"din",0);
		Node f=new Node(5,new Point3D(-50,50),1.5,"din",0);
		tmp.addNode(a);
		tmp.addNode(b);
		tmp.addNode(c);
		tmp.addNode(d);
		tmp.addNode(e);
		tmp.addNode(f);

		for(int i=0;i<5;i++) {
			tmp.connect(i, i+1, 2.5);
		}
		return tmp;
	}

	private DGraph getDGraph2() {
		DGraph tmp=new DGraph();
		tmp.addNode(new Node(0,new Point3D(0,0),6,"",0)); 
		tmp.addNode(new Node(1,new Point3D(-10,-10),3,"",0)); 
		tmp.addNode(new Node(2,new Point3D(0,20),1,"",0)); 
		tmp.addNode(new Node(3,new Point3D(10,10),2,"",0)); 
		tmp.addNode(new Node(4,new Point3D(20,0),4,"",0)); 
		tmp.addNode(new Node(5,new Point3D(10,-10),8,"",0)); 
		tmp.addNode(new Node(6,new Point3D(0,-20),10,"",0)); 
		tmp.addNode(new Node(7,new Point3D(-10,10),2.5,"",0)); 
		tmp.addNode(new Node(8,new Point3D(-20,0),7,"",0)); 
		for(int i=1;i<9;i++) {
			tmp.connect(0, i, 3);
		}
		return tmp;
	}

	@Test
	void getNodeGetEdgeTest() {
		DGraph dg=getDGraph();

		for(int j=0;j<5;j++) {
			assertEquals(dg.getNode(j).getKey(),j);
			assertEquals(dg.getEdge(j, j+1).getDest(),j+1);
			assertEquals(dg.getEdge(j, j+1).getSrc(),j);	
		}
	}

	@Test
	void getVgetEtest() {
		DGraph dg=getDGraph();
		Collection<node_data> nod=dg.getV();
		for(node_data a:nod) {
			Collection<edge_data> edg=dg.getE(a.getKey());
			for(edge_data e:edg) {
				assertEquals(e.getSrc(),a.getKey());
				assertEquals(e.getDest(),a.getKey()+1);
			}	
		}
		assertEquals(dg.nodeSize(),6);
		assertEquals(dg.edgeSize(),5);
	}

	@Test
	void removeNodeAndEdgeTest() {
		DGraph dg2=getDGraph2();
		assertEquals(dg2.nodeSize(),9);
		assertEquals(dg2.edgeSize(),8);

		dg2.removeEdge(0, 8);
		assertEquals(dg2.edgeSize(),7);
		dg2.removeEdge(0, 8);
		assertEquals(dg2.edgeSize(),7);	
		dg2.removeNode(0);
		assertEquals(dg2.edgeSize(),0);
		assertEquals(dg2.nodeSize(),8);

		dg2.removeNode(0);
		assertEquals(dg2.nodeSize(),8);

		for(int x=1;x<9;x++) {
			dg2.removeNode(x);
		}
		assertEquals(dg2.nodeSize(),0);
	}

	@Test
	void getMCtest() {
		DGraph dg3=getDGraph2();
		assertEquals(dg3.getMC(),17);//9 nodes + 8 edges = 17 changes
		dg3.removeNode(0);
		assertEquals(dg3.getMC(),18);//remove node = 1 change
		dg3.removeEdge(0, 7);
		assertEquals(dg3.getMC(),18);//nothing changed

		for(int y=1;y<8;y++) {
			dg3.connect(y, y+1, y*2.3);
			dg3.connect(y+1, y, y*2.3);
		}
		dg3.connect(1, 8, 5);
		dg3.connect(8, 1, 6.1);
		assertEquals(dg3.getMC(),34);

		dg3.removeNode(5);
		assertEquals(dg3.getMC(),35);
		dg3.removeEdge(5, 6);
		assertEquals(dg3.getMC(),35);//nothing changed
	}

	@Test
	void reverseGraphTest() {
		DGraph dg4=getDGraph();
		dg4.reversedGraph();
		for(int k=5;k>0;k--) {
			assertEquals(dg4.getEdge(k,k-1).getDest(),k-1);
			assertEquals(dg4.getEdge(k-1,k),null);
		}

		DGraph dg5 =getDGraph2();
		dg5.reversedGraph();
		for(int k=8;k>0;k--) {
			assertEquals(dg5.getEdge(k,0).getDest(),0);
			assertEquals(dg5.getEdge(0,k),null);
		}
	}
	
	@Test
	void generalTest() {
		DGraph gg=theHugeGraph();
		assertEquals(gg.getMC(),43);//22 nodes + 21 edges = 43 changes
		assertEquals(gg.nodeSize(),22);
		assertEquals(gg.edgeSize(),21);
		assertEquals(gg.getEdge(1, 3).getDest(),3);

		gg.removeNode(3);
		assertEquals(gg.getEdge(1, 3),null);
		assertEquals(gg.getMC(),44);
		assertEquals(gg.nodeSize(),21);
		assertEquals(gg.edgeSize(),15);
		
		Collection<node_data> nod=gg.getV();
		for(node_data aa:nod) {
			gg.connect(aa.getKey(), 0, 99);
		}
		assertEquals(gg.edgeSize(),34);//there is a edge between 1 to 0 and we can't connect 0 to 0 , thats why we added 19 new edges and not 21
		assertEquals(gg.getMC(),63);

		for(node_data aa:nod) {
			Collection<edge_data> edges=gg.getE(aa.getKey());
			for(edge_data e: edges) {
				gg.removeEdge(aa.getKey(), e.getDest());
			}
		}
		assertEquals(gg.getMC(),97);//63 changes + remove 34 edges
		assertEquals(gg.nodeSize(),21);
		assertEquals(gg.edgeSize(),0);		
		
		
	}
	
	private DGraph theHugeGraph() {
		DGraph tmp=new DGraph();
		tmp.addNode(new Node(0,new Point3D(-4,4),1,"",0));
		tmp.addNode(new Node(1,new Point3D(0,0),1,"",0));
		tmp.addNode(new Node(2,new Point3D(4,4),1,"",0));
		tmp.addNode(new Node(3,new Point3D(20,-4),1,"",0));
		tmp.addNode(new Node(4,new Point3D(17,-2),1,"",0));
		tmp.addNode(new Node(5,new Point3D(23,-2),1,"",0));
		tmp.addNode(new Node(6,new Point3D(30,-4),1,"",0));
		tmp.addNode(new Node(7,new Point3D(23,-9),1,"",0));
		tmp.addNode(new Node(8,new Point3D(17,-9),1,"",0));
		tmp.addNode(new Node(9,new Point3D(0,-20),1,"",0));
		tmp.addNode(new Node(11,new Point3D(2,-25),1,"",0));
		tmp.addNode(new Node(12,new Point3D(-5,-18),1,"",0));
		tmp.addNode(new Node(25,new Point3D(5,-19),1,"",0));
		tmp.addNode(new Node(20,new Point3D(-5,-24),1,"",0));
		tmp.addNode(new Node(13,new Point3D(0,30),1,"",0));
		tmp.addNode(new Node(14,new Point3D(-5,35),1,"",0));
		tmp.addNode(new Node(50,new Point3D(-5,28),1,"",0));
		tmp.addNode(new Node(45,new Point3D(5,29),1,"",0));
		tmp.addNode(new Node(22,new Point3D(-20,-2),1,"",0));
		tmp.addNode(new Node(23,new Point3D(-25,5),1,"",0));
		tmp.addNode(new Node(24,new Point3D(-19,-10),1,"",0));
		tmp.addNode(new Node(26,new Point3D(-24,-15),1,"",0));
		
		tmp.connect(1, 0, 3);
		tmp.connect(1, 2, 3);
		tmp.connect(1, 3, 3);
		tmp.connect(1, 22, 3);
		tmp.connect(1, 13, 3);
		tmp.connect(1, 9, 3);
		tmp.connect(3, 4, 3);
		tmp.connect(3, 5, 3);
		tmp.connect(3, 6, 3);
		tmp.connect(3, 7, 3);
		tmp.connect(3, 8, 3);
		tmp.connect(9, 11, 3);
		tmp.connect(9, 12, 3);
		tmp.connect(9, 25, 3);
		tmp.connect(9, 20, 3);
		tmp.connect(13, 14, 3);
		tmp.connect(13, 45, 3);
		tmp.connect(13, 50, 3);
		tmp.connect(22, 23, 3);
		tmp.connect(22, 24, 3);
		tmp.connect(22, 26, 3);
		
		return tmp;
	}
	
	@Test
	void initTest() {
		game_service game = Game_Server.getServer(2); 
		String g = game.getGraph();
		DGraph gg = new DGraph();
		gg.init(g);
		assertEquals(gg.edgeSize(),22);
		assertEquals(gg.nodeSize(),11);
	}

	
}
