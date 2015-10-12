import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {	
	public static void main(String args[]) throws FileNotFoundException{		    
        Scanner scanner = new Scanner(new File("input.txt"));
        String num = scanner.nextLine();
        int size = Integer.parseInt(num);
        
        HashMap<String, Integer> nodeToIndexMapping = new HashMap<String, Integer>();
        List<List<String>> graph = new ArrayList<List<String>>(); 
        
        for (int i=0; i<size; i++) {
            String line = scanner.nextLine();
            String[] text = line.split("->");

            List<String> list = new ArrayList<String>(); 
            for (int j=0; j<text.length; j++) {
            	list.add(j, text[j]);
            }
            
            nodeToIndexMapping.put(list.get(0), i);
            graph.add(list);
        }
        
        String startNode = scanner.nextLine();
        
        find_even_length_paths(graph, nodeToIndexMapping, startNode);
        scanner.close();	
	}
	
	public static void find_even_length_paths(List<List<String>> graph, HashMap<String, Integer> nodeToIndexMapping, String startNode) {
        Queue<String> queue = new LinkedList<String>();
        HashMap<String, Integer> distance = new HashMap<String, Integer>();
        distance.put(startNode, 0);
        queue.add(startNode);
        
        while (queue.size() != 0) {
        	String node = queue.poll();
        	int currentNodeDistance = distance.get(node);
        	List<String> childNodes = graph.get(nodeToIndexMapping.get(node));
        	
        	for (int i=1; i < childNodes.size(); i++) {        //Skip the first element since its the parent.
        		String childNode = childNodes.get(i);
        		if (distance.containsKey(childNode)) {
        			int childNodeDistance = distance.get(childNode);
        			if (childNodeDistance == 0) {              //child node is at even distance
        				switch(currentNodeDistance) {
	        				case 0:      					   //current node path was even
		            			distance.put(childNode, 2);    //child node path would be odd and odd
		            			queue.add(childNode);
		            			break;
		    				case 1:          				   //current node path was odd
		    					// Do nothing
		            			break;
		    				case 2:                            //current node path has both even and odd path
		            			distance.put(childNode, 2);    //child node path would be both even and odd
		            			queue.add(childNode);			
		            			break;        					
        				}
        			} else if (childNodeDistance == 1) {       //child node is at odd distance
        				switch(currentNodeDistance) {
	        				case 1:      					   //current node path was odd
		            			distance.put(childNode, 2);    //child node path would be odd and even
		            			queue.add(childNode);
		            			break;
		    				case 0:          				   //current node path was even
		    					// Do nothing
		            			break;
		    				case 2:                            //current node path has both even and odd path
		            			distance.put(childNode, 2);    //child node path would be both even and odd
		            			queue.add(childNode);			
		            			break;        					
        				}
        			} else if (childNodeDistance == 2) {
        				//Do nothing
        			}
        		} else {
        			switch (currentNodeDistance) {
        				case 0:          						//current node path was even
                			distance.put(childNode, 1);    		//child node path would be odd
                			break;
        				case 1:          						//current node path was odd
                			distance.put(childNode, 0);   		//child node path would be even
                			break;
        				case 2:         						//current node path has both even and odd path
                			distance.put(childNode, 2);    		//child node path would be both even and odd
                			break;        					
        			}
        			queue.add(childNode);
        		}
        	}
        }
        
        int count = 0;
        Map<String, Integer> map = new TreeMap<String, Integer>();
        String [] evenPathNodes = new String [distance.keySet().size()];
        for (String key : distance.keySet()) {
            Integer value = distance.get(key);
            if (value == 0 || value == 2) {       
            	evenPathNodes[nodeToIndexMapping.get(key)] = key;
            	map.put(key, value);
            	count++;
            }
        }
        
        List<String> list = new ArrayList<String>(Arrays.asList(evenPathNodes));
        list.removeAll(Arrays.asList("", null));
        
        PrintWriter writer;
		try {
			writer = new PrintWriter("output.txt", "UTF-8");
	        writer.println(count);
	        writer.println(list.toString().replace("[", "").replace("]", ""));
	        writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}        

//        System.out.println(count);
//        System.out.println(list.toString().replace("[", "").replace("]", ""));        
	}
}
