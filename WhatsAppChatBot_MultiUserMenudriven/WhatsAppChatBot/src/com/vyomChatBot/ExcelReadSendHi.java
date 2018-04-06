package com.vyomChatBot;



import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ExcelReadSendHi {
	public static WebDriver driver = whatsAppMain.driver;
	public static Actions actions = new Actions(driver);
	WebDriverWait driverWait;
	public static boolean init = false;
	public static WebDriverWait wait;

	public void readExcel() throws AWTException, InterruptedException {
		wait = new WebDriverWait(driver, 60);
		 long startTime = System.currentTimeMillis();
		if (init == false) /***** Initialize Web.Whatsapp.com *****/
		{
			driver.get("https://web.whatsapp.com/");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("side")));
			init = true;
		}
		/***** switch to first frame in the frameset. *****/
		driver.switchTo().defaultContent();
		Robot robot = new Robot();  // Robot class throws AWT Exception
		 String[] contactName = { "Pooja Pawar", "Darshan","Saleem","Geetanjali","Keul Shaha","Puja Kumari","Sunil Kumar","Sushil Chauhan","Deepak","Monika","Pradnya Pujari","Darshan Shirsath","Geetanjali khyale","Saleem Shaikh","Kishor Lalvani","Pooja Pawar"};
	        // stored contact name in list
	        ArrayList<String> list = new ArrayList<>();
	        for (int i = 0; i < contactName.length; i++) {
	        	 list.add(contactName[i]);
			}
	        
	        Iterator< String> itr=list.iterator();
	        while (itr.hasNext()) {
				String str = (String) itr.next();
				//clear the search box
				driver.findElement(By.xpath(".//*[@id='input-chatlist-search']")).clear();
				//enter the name in search box
				driver.findElement(By.xpath(".//*[@id='input-chatlist-search']")).sendKeys(str);
				// press enter key of keyboard 
				 robot.keyPress(KeyEvent.VK_ENTER);	
			       Thread.sleep(500);
			       //get the name of profile
				String getName=driver.findElement(By.xpath(".//*[@id='main']/header/div[2]/div/div/span")).getText();
				Thread.sleep(500);
				System.out.println(getName);
				//match enter name and profile name
				if (str.equals(getName)) {
					 Thread.sleep(500);
				//send Hi message
					 driver.findElement(By.xpath(".//*[@id='main']/footer/div[1]/div[2]/div/div[2]")).sendKeys("Hi");	
					 driver.findElement(By.xpath(".//*[@id='main']/footer/div[1]/button")).click();
					
				       Thread.sleep(500);
				}
			    Thread.sleep(1000);
			}
	       
	      //code
	      long endTime = System.currentTimeMillis();
	      long time=endTime - startTime;
	      System.out.println("Took "+(endTime - startTime) + " ns"); 
	      long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
	      System.out.println(minutes);
	}
}
