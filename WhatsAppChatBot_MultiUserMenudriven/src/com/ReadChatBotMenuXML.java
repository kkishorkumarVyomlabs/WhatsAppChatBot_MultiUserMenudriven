package com;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.xmlbeans.XmlException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import noNamespace.AEChatBotMenuDocument;
import noNamespace.AEChatBotMenuDocument.AEChatBotMenu.MenuNode;
import noNamespace.AEChatBotMenuDocument.AEChatBotMenu.MenuNode.ContainerDisplayNode;


public class ReadChatBotMenuXML {
	public static  Document doc ;
	public static  Map<String,String> nextCallMap = new HashMap<String,String>();
	public static String position = "",displayText = "", childId = "";
	public static String menuLevel = "1_1";
	public static String parentId = null;
	public static Scanner reader = new Scanner(System.in); 
	public static NodeList menuList;
	public static AEChatBotMenuDocument aeChatBotMenuDocument;
	public static MenuNode[] menunodeArray = null;
	public static Scanner scanner = new Scanner(System.in); 
	public static String userInput = "";
//	public static HashMap<String, Map<String,String> > map_UserNo_nextCallMap = new HashMap<String, Map<String,String> >();
	
	public ReadChatBotMenuXML() {
		try {
			File xmlFile = new File(".\\Menu.xml");
	    	aeChatBotMenuDocument = AEChatBotMenuDocument.Factory.parse(xmlFile);
	    	menunodeArray = aeChatBotMenuDocument.getAEChatBotMenu().getMenuNodeArray();
    		
		}catch (Exception e) {
			e.printStackTrace();			
		}
	}
	public static void main(String argv[]) throws Exception
	{
		ReadChatBotMenuXML readChatBotMenuXML = new ReadChatBotMenuXML(); 
    	do {
	    		String output = readChatBotMenuXML.MenuOptions("SampleUserName" , userInput);
//	    		System.out.println("nextCallMap : " + map_UserNo_nextCallMap.get("SampleUserName"));
	    		userInput = scanner.nextLine();
//	    		output = readChatBotMenuXML.MenuOptions("SampleUserName2" , userInput);
//	    		System.out.println("nextCallMap : " + map_UserNo_nextCallMap.get("SampleUserName2"));
//	    		userInput = scanner.nextLine();
		} while (!userInput.equals("0"));
	   	System.out.println("Thanks for using Chat Bot");
	}
	
	public static void XMLRead(String fileName) throws FileNotFoundException, IOException, XmlException  {
			File xmlFile = new File(fileName);
	    	AEChatBotMenuDocument aeChatBotMenuDocument = AEChatBotMenuDocument.Factory.parse(xmlFile);
	    	System.out.println(aeChatBotMenuDocument.toString());
	    	System.out.println(aeChatBotMenuDocument.getAEChatBotMenu().getMenuID());
	 }
	public String MenuOptions( String userNo, String userInput) throws Exception{
		
		String msg="";
		if(userInput == null)
			return "Invalid input please restart session with Hi";
		if(userInput.equals(""))
				this.menuLevel = "1_1";
		else
			if(userInput.equals("0")) {
				nextCallMap.clear();
				return "Thanks for using chat services.";
			}
			else { 
//				nextCallMap = map_UserNo_nextCallMap.get(userNo);
				System.out.println("nextCallMap in else : "+ nextCallMap);
				this.menuLevel = nextCallMap.get(userInput);
				if(this.menuLevel == null)return "Invalid Input";
			}
		
		System.out.println("this.menuLevel : " + this.menuLevel);
		if(!this.menuLevel.equals(null) && this.menuLevel.length() > 0)
			nextCallMap.clear();
		else {
			System.out.println("This should be Action node..");
			return "This should be Action node..";
		} 
		
		for(int i=0;i<menunodeArray.length;i++) {
			 MenuNode menunode = menunodeArray[i];
			 if(this.menuLevel.equals(menunode.getMenuLevel())) {
				 this.parentId = menunode.getParentLevel();
				 ContainerDisplayNode[] containerDisplayNodeArray = menunode.getContainerDisplayNodeArray();
				 for (int j = 0; j < containerDisplayNodeArray.length; j++) {
					 ContainerDisplayNode containerDisplayNode = containerDisplayNodeArray[j];
					 this.position = containerDisplayNode.getPosition();
					 this.displayText=containerDisplayNode.getDisplayText();
					 this.childId=containerDisplayNode.getChildId();
					 msg = msg + "\n" + this.position + ". " + this.displayText;
					 nextCallMap.put(this.position, this.childId);
				}
				 nextCallMap.put("*",this.parentId );	
				 msg = msg + "\n" +"*. Go to previous menu";
				 nextCallMap.put("#","1_1");
				 msg = msg + "\n" +"#. Go to Main menu";
				 nextCallMap.put("0","0");
				 msg = msg + "\n" +"0. To Exit";
				 msg = msg + "\n\nEnter a sequence number which you want to select : ";
				 System.out.println(msg);
//				 map_UserNo_nextCallMap.put(userNo, nextCallMap);
				 return msg;
			 } 
		} 
		
		
		return "Invalid option, Please try again..";
	}
}