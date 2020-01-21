package gameClient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import Server.game_service;
import dataStructure.Fruit;
import dataStructure.Robot;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;


public class KML_Logger {

	StringBuilder SBans;

	public KML_Logger() {
		SBans = new StringBuilder();
		SBans.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<kml xmlns=\"http://earth.google.com/kml/2.2\">\n" +
				"  <Document>\n" +
				"    <name>Points with TimeStamps</name>\n" +
				"    <Style id=\"paddle-a\">\n" +
				"      <IconStyle>\n" +
				"        <Icon>\n" +
				"          <href>http://maps.google.com/mapfiles/kml/paddle/A.png</href>\n" +
				"        </Icon>\n" +
				"        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\n" +
				"      </IconStyle>\n" +
				"    </Style>\n" +
				"    <Style id=\"paddle-b\">\n" +
				"      <IconStyle>\n" +
				"        <Icon>\n" +
				"          <href>http://maps.google.com/mapfiles/kml/paddle/B.png</href>\n" +
				"        </Icon>\n" +
				"        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\n" +
				"      </IconStyle>\n" +
				"    </Style>\n" +
				"    <Style id=\"hiker-icon\">\n" +
				"      <IconStyle>\n" +
				"        <Icon>\n" +
				"          <href>http://maps.google.com/mapfiles/ms/icons/hiker.png</href>\n" +
				"        </Icon>\n" +
				"        <hotSpot x=\"0\" y=\".5\" xunits=\"fraction\" yunits=\"fraction\"/>\n" +
				"      </IconStyle>\n" +
				"    </Style>\n" +
				"    <Style id=\"check-hide-children\">\n" +
				"      <ListStyle>\n" +
				"        <listItemType>checkHideChildren</listItemType>\n" +
				"      </ListStyle>\n" +
				"    </Style>\n" +
				" ");
	}
	
	
	/**
	 * add the location of the nodes to the kml file
	 * @param g the graph that held the nodes
	 */
	void addNodes(graph g) {
		for (node_data node_data : g.getV()) {
			SBans.append("<Placemark>\n" + "    <description>" + "place num:").append(node_data.getKey()).append("</description>\n").append("    <Point>\n").append("      <coordinates>").append(node_data.getLocation().x()).append(",").append(node_data.getLocation().y()).append(",0</coordinates>\n").append("    </Point>\n").append("  </Placemark>\n");
		}
	}
	/**
	 * add the location of the fruits and robots to the kml file
	 * @param robots
	 * @param fruits
	 */
	public void addRobotsFruits(HashMap<Integer, Robot> robots,
			HashMap<Point3D, Fruit> fruits) {
		Date date = new Date(0);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
		String timeStr = df.format(date);
		String timeStr2 = df2.format(date);
		String finalDate = timeStr+"T"+timeStr2+"Z";

		for (Robot robot : robots.values()) {		
			SBans.append("<Placemark>\n" + "      <TimeStamp>\n" + "        <when>").append(finalDate).append("</when>\n").append("      </TimeStamp>\n").append("      <styleUrl>#hiker-icon</styleUrl>\n").append("      <Point>\n").append("        <coordinates>").append(robot.getLocation().x()).append(",").append(robot.getLocation().y()).append(",0</coordinates>\n").append("      </Point>\n").append("    </Placemark>");
		}
		for (Fruit fruit : fruits.values()) {
			String typer = "#paddle-a";
			if (fruit.getType() == -1){
				typer = "#paddle-b";
			}
			SBans.append("<Placemark>\n" + "      <TimeStamp>\n" + "        <when>").append(finalDate).append("</when>\n").append("      </TimeStamp>\n").append("      <styleUrl>").append(typer).append("</styleUrl>\n").append("      <Point>\n").append("        <coordinates>").append(fruit.getP().x()).append(",").append(fruit.getP().y()).append(",0</coordinates>\n").append("      </Point>\n").append("    </Placemark>");
		}
		

	}
	/**
	 * save the kml file, add the finished format to the file
	 * @param file_name
	 */
	public void saveToFile(String file_name){
		
		SBans.append("  </Document>\n" +
				"</kml>");
		
		try {
			Path path = FileSystems.getDefault().getPath(".");
			File file = new File(path.toString() +"//data//" +  file_name + ".kml");
			FileWriter writer = new FileWriter(file);
			writer.write(String.valueOf(SBans));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return the string of the kml file
	 */
	public String getLogOfGame() {
        return SBans.toString();
    }
}

