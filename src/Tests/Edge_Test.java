package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import dataStructure.Edge;
import dataStructure.Node;
import utils.Point3D;

class Edge_Test {
	Node n0=new Node(0,new Point3D(0,0));
	Node n1=new Node(1,new Point3D(1,1));
	Edge e=new Edge(0,1,2.5);
	
	@Test
	void testCopy() {
		Edge eCopy=new Edge(e);
		assertEquals(e.getDest(),eCopy.getDest());
		assertEquals(e.getSrc(),eCopy.getSrc());
		assertEquals(e.getWeight(),eCopy.getWeight());
	}
	
	@Test
	void testGetSrc() {
		Node[] nodes=new Node[10];
		Edge[] edges=new Edge[10];
		nodes[0]=n0;
		for (int i = 1; i < nodes.length; i++) {
			nodes[i]=new Node(i,new Point3D(i,i));
			edges[i-1]=new Edge(i-1,i,2.5);
		}
		for (int i = 0; i < edges.length-1; i++) {
			assertEquals(edges[i].getSrc(),i);
		}
	}
	
	@Test
	void testGetDest() {
		Node[] nodes=new Node[10];
		Edge[] edges=new Edge[10];
		nodes[0]=n0;
		for (int i = 1; i < nodes.length; i++) {
			nodes[i]=new Node(i,new Point3D(i,i));
			edges[i-1]=new Edge(i-1,i,2.5);
		}
		for (int i = 0; i < edges.length-1; i++) {
			assertEquals(edges[i].getDest(),i+1);
		}
	}
	
	@Test
	void testSetGetWeight() {
		Edge[] edges=new Edge[100];
		Node[] nodes=new Node[101];
		nodes[0]=n0;
		for (int i = 1; i < nodes.length; i++) {
			nodes[i]=new Node(i,new Point3D(i,i));
			edges[i-1]=new Edge(i-1,i,2.5);
			edges[i-1].setWeight(((i-1)+3)*5);
		}
		for (int i = 0; i < edges.length-1; i++) {
			assertEquals(edges[i].getWeight(),(i+3)*5);
			assertNotEquals(edges[i].getWeight(),2.5);
		}	
	}
	
	@Test
	void testSetGetInfo() {
		Edge[] edges=new Edge[100];
		Node[] nodes=new Node[101];
		nodes[0]=n0;
		for (int i = 1; i < nodes.length; i++) {
			nodes[i]=new Node(i,new Point3D(i,i));
			edges[i-1]=new Edge(i-1,i,2.5);
			edges[i-1].setInfo((i-1)+"");
		}
		for (int i = 0; i < edges.length-1; i++) {
			assertEquals(edges[i].getInfo(),i+"");
			assertNotEquals(edges[i].getInfo(),"");
		}			
	}
	
	@Test
	void testSetGetTag() {
		Edge[] edges=new Edge[100];
		Node[] nodes=new Node[101];
		nodes[0]=n0;
		for (int i = 1; i < nodes.length; i++) {
			nodes[i]=new Node(i,new Point3D(i,i));
			edges[i-1]=new Edge(i-1,i,2.5);
			edges[i-1].setTag((i-1)*5+1);
		}
		for (int i = 0; i < edges.length-1; i++) {
			assertEquals(edges[i].getTag(),i*5+1);
			assertNotEquals(edges[i].getTag(),0);
		}			
	}
}
