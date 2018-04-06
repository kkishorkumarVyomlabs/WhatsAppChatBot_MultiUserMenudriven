package com.vyomChatBot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WhatsApp {
	
	public static WebDriver driver = whatsAppMain.driver;
	public static Actions actions = new Actions(driver);
	WebDriverWait driverWait;
	public static boolean init = false;
	public static HashMap<String, String> map_UserNo_LastMsg = new HashMap<String, String>();
	
	public static WebDriverWait wait; 
	
	public static HashMap<String, Integer> map_UserState = new HashMap<>();
	
	public void userList() throws Exception 
	{		
		wait = new WebDriverWait(driver, 60);
		
	/*	if (init == false) 				//***** Initialize Web.Whatsapp.com *****//*
		{
			driver.get("https://web.whatsapp.com/");			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("side")));
			init = true;
		}*/
															/***** switch to first frame in the frameset. *****/
		driver.switchTo().defaultContent();
																
		WebElement sidePanle = (WebElement) driver.findElement(By.id("side"));
		WebElement chatContactPanle = (WebElement) sidePanle.findElement(By.id("pane-side"));
		
		List<WebElement> listOfChatingUsers = chatContactPanle.findElements(By.className("_2wP_Y"));	
		
		System.out.println("Number of users in Queue : "+listOfChatingUsers.size());
		
		boolean hasNewMsg = false;
		int i = 0;
		for (WebElement currentUserNo : listOfChatingUsers) 
		{	
			i++;
			try 
			{		
				
				hasNewMsg = driver.findElements(By.xpath("//*[@id='pane-side']/div/div/div/div["+ i +"]/div/div/div[2]/div[2]/div[2]/span[1]/div/span")).size() > 0;
				String userNo = currentUserNo.findElement(By.className("_25Ooe")).findElement(By.tagName("span")).getAttribute("title");
				System.out.println("Is " + userNo + " has new msg : " +hasNewMsg);
				if(!hasNewMsg)
					continue;
				
				currentUserNo.click();
				actions.moveToElement(currentUserNo).click().perform();					
				
				System.out.println("chat Title (Customer No/Name): " + userNo);									//chat Title (Customer No/Name)			
				
				actions.moveToElement(currentUserNo).click().perform();
				String customerLastMsg = null;
				
				customerLastMsg = currentUserNo.findElement(By.className("_1AwDx")).findElement(By.className("_itDl")).findElement(By.className("_2_LEW")).getAttribute("title");
				customerLastMsg = removeSpecialCharacter(customerLastMsg);
				System.out.println("customerLastMsg try: " + customerLastMsg);
				
				/****
					 * if map_UserNo_LastMsg not having current user then add
				 ****/				
				if ( ! map_UserNo_LastMsg.containsKey(userNo))
				{
					map_UserNo_LastMsg.put(userNo,customerLastMsg);
				}
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("_1AwDx")));
				if (processMessage(userNo))
				{
					if (customerLastMsg.equals("â€ªMessages you text to this chat and calls are secured with end-to-end encryption.")) 									
						continue;
					messageReply(userNo, customerLastMsg);
				} 	
			} 
			
			
			catch (Exception e) 
			{	
				e.printStackTrace();				
				break;
			}				
			System.out.println("-----------------------------------------------");
		}
		
		//Thread.sleep(2000);
		userList();
	}
	public String messageReply(String userNo, String userReplay) throws Exception {
		String reply = "";
		userNo = userNo.replace("+91", "").replace(" ", "");			
		Pattern intermediateIdPattern = Pattern.compile("(\\d{12})");
		intermediateIdPattern.matcher(userReplay);
		Pattern.compile("(([A-D]{1}|[a-d]{1})[1-4]{1})|(([A-D]{1}|[a-d]{1})[1-4]{1}[$]{1}[0-9]{1,7})|(([A-D]{1}|[a-d]{1})[1-4]{1}[@]{1}[0-9]{1,5})|(([A-D]{1}|[a-d]{1})[1-4]{1}[$]{1}[0-9]{1,7}[@]{1}[0-9]{1,5})|(([A-D]{1}|[a-d]{1})[1-4]{1}[@]{1}[0-9]{1,5}[$]{1}[0-9]{1,7})");
		userNo.split(" ");
		
		if(!map_UserState.containsKey(userNo))		//check userNo is already register in userState map or not
			map_UserState.put(userNo, 1);			//if not then set userState 1 (fresh chat) in userState map
		
		ReadChatBotMenuXML readChatBotMenuXML = new ReadChatBotMenuXML();
		int userState = map_UserState.get(userNo);
					
		switch(userState)
		{
			case 1 :
						if (userReplay.equalsIgnoreCase("Hi")||userReplay.equalsIgnoreCase("Hello")||userReplay.equalsIgnoreCase("Hey")) 
						{
							reply=" Hi " + userNo + ",\n\nWelcome to Vyomlabs ChatBot. \n\nGreetings for the day..! ";
							reply = reply+""+readChatBotMenuXML.MenuOptions(userNo,"");			//get menu list from XML file
							sendMessage(userNo, reply);								
							map_UserState.put(userNo, 2);		//Set UserState 2 for farther communication								
						}
						else
						{
							reply="If you want to start communication then please start with Hi or Hello";
							sendMessage(userNo, reply);
						}							
						break;
			case 2 : 	reply = reply+""+readChatBotMenuXML.MenuOptions(userNo,userReplay);
						sendMessage(userNo, reply);
						if(userReplay.equals("0"))
						{
							map_UserState.remove(userNo);
							map_UserNo_LastMsg.remove(userNo);							
						}
						break;
			default :	reply="We are working to serve you better service and become more intelligent.Till then,please follow the Q and A format.\nIf you want try again please start with Hi";
						sendMessage(userNo, reply);
						map_UserState.remove(userNo);
						map_UserNo_LastMsg.remove(userNo);
						break;
						
		}
		lastReply.put(userNo, new Date());
		archiveContact();
		Thread.sleep(2000);
		return removeSpecialCharacter(reply);
	}


	public void sendMessage(String contactNo, String reply)
	{
																			/***** switch to first frame in the frameset. *****/
		driver.switchTo().defaultContent();										 	
																			/***** wait until switch frame to chatPanel  *****/
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("main")));	
																			/***** get element of chatPanel  *****/
		WebElement chatPanel = (WebElement) driver.findElement(By.id("main"));		
																			/***** get element of inputPanel  *****/
		WebElement inputPanel = (WebElement) chatPanel.findElement(By.className("_3oju3"));		
																			/***** get element of inputTextBox  *****/
		WebElement inputTextBox = (WebElement) inputPanel.findElement(By.className("_2bXVy"));	
																			/*****  wait until locate pointer  *****/
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main\"]/footer/div[1]/div[2]/div/div[2]")));	
																			/*****  get element of pointer  *****/
		WebElement inputText = (WebElement) inputTextBox.findElement(By.xpath("//*[@id=\"main\"]/footer/div[1]/div[2]/div/div[2]"));
																			/***** Click in textBox  *****/
		inputText.click();
																			/***** Clear textBox  *****/
		inputText.clear();																		
																			/***** Write message in textBox  *****/
		inputText.sendKeys(reply.replaceAll("\n", Keys.chord(Keys.SHIFT, Keys.ENTER)));			
																			/***** get element of send button  *****/
		WebElement sendButton = (WebElement) inputPanel.findElements(By.tagName("button")).get(1);
																			/***** Click on send button  *****/
		sendButton.click();																		
		map_UserNo_LastMsg.put(contactNo, reply);							/***** update last sent message in map  *****/
	}
	
	public static HashMap<String, Date> lastReply = new HashMap<>();	

	public String removeSpecialCharacter(String temp) {
		try {
			return temp.replaceAll("\\P{Print}", "").trim();
		} catch (Exception e) {
			return temp;
		}
	}
	
	public void archiveContact()
	{
		WebElement archi = driver.findElement(By.xpath("//*[@id='pane-side']/div/div/div/div/div/div/div/div[2]"));
		
		actions.moveToElement(archi).click().perform();
		archi.click();
		actions.contextClick(archi).build().perform();
		wait.until(ExpectedConditions.visibilityOfElementLocated((By.className("_3s1D4"))));
		WebElement arch = driver.findElement(By.className("_3s1D4"));
		List<WebElement> archive = arch.findElements(By.tagName("li"));
		archive.get(0).click();
	}
	
	public boolean processMessage(String key) {
		Date d1 = lastReply.get(key);
		if (d1 == null)
			return true;
		Date d2 = new Date();
		long seconds = (d2.getTime() - d1.getTime()) / 1000;
		if (seconds > 1) {
			return true;
		}
		return false;
	}

}
