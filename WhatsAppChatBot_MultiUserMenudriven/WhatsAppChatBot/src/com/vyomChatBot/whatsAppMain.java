package com.vyomChatBot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

	public class whatsAppMain
	{
		public static WebDriver driver = null;
		public static boolean init = false;
		public static int browserType;
		public static String browserLocation = "tools//chromedriver.exe";   //default browser location
		
	
		public static void main(String args[])
		{
			whatsAppMain whtAppMainObj = new whatsAppMain();
			whtAppMainObj.cleanBrowser();
			
			do
			{
				whtAppMainObj.init(browserLocation);
			}
			while(driver == null);
			
			 BroadcastHi whatsAppAccount = new  BroadcastHi();//object of class in which processing logic is written
			 
			 WhatsApp wht = new WhatsApp();
			 
			try 
			{
				whatsAppAccount.userList();
				Thread.sleep(10000);
				wht.userList();
				//method which consists main logic and chatbot flow
			}
			
			catch(Exception e)
			{
				e.printStackTrace();
			} 
		}
		
		public void cleanBrowser()
		{
			
			try 
			{ 
				Process p = Runtime.getRuntime().exec("Taskkill /f /im chromedriver.exe");
				p = Runtime.getRuntime().exec("Taskkill /f /im chrome.exe");
			}
			
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	
		public void init(String browserLocation) //used to initialize browser with its location and type
		{
			System.out.println("Startig INIT ");
			driver = DriverUtility.getDriver(browserLocation);		//Driver Type and Location			
		}
}
