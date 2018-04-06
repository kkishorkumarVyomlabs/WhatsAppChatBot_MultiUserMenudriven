package com.vyomChatBot;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BroadcastHi 

{
	public static WebDriver driver = whatsAppMain.driver;
	public static Actions actions = new Actions(driver);
	WebDriverWait driverWait;
	public static boolean init = false;
	public static WebDriverWait wait; 
	
	public void userList() throws Exception 
	{		
		wait = new WebDriverWait(driver, 60);
		
		if (init == false) 				/***** Initialize Web.Whatsapp.com *****/
		{
			driver.get("https://web.whatsapp.com/");		//for loading url			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("side")));  //wait until QR code is  scanned
			init = true;
		}
		
				//getting Contacts Name from Excel file to inputstream
				FileInputStream contactsList = new FileInputStream("D:/ContactList.xls");
				
				//adding contacts from inputstream to Excel Workbook Object
				Workbook contactsWorkbook= new HSSFWorkbook(contactsList);
				
				//Adding contacts to excel sheet1 in workbook
				HSSFSheet contactsListSheet= (HSSFSheet)contactsWorkbook.getSheet("Sheet1");
				
				//Creating arraylist of contact names  from excel sheet
				ArrayList<String> contactsNameList= new ArrayList<String>();
				
				//getting excel data(contacts name) into array list
				for(int r1=1;r1<=contactsListSheet.getLastRowNum();r1++)
				{
					Row row=contactsListSheet.getRow(r1);
					Cell name=row.getCell(0);
					String contactNumber=name.toString();
					contactsNameList.add(contactNumber);
				}
				
				System.out.println("Contactnames List :" +contactsNameList);
				
				//Creating contacts array of string
				String[] array=new String[contactsNameList.size()];
				
				int num=0;
				
				//store arraylist data into array
				for(String ContactNo:contactsNameList)
				{
					array[num]=ContactNo;
					num++;
				}
				
				
				driver.findElement(By.xpath("//*[@title='New chat' and @role='button']")).click(); //clicking on new chat button
				
				//wait
				Thread.sleep(700);
				
				//input-chatlist-search

				for(int i=0;i<array.length;i++)
				{
					
					//clear the search textbox
					WebElement contatcNameElement=null;
				
					//for clearing search textbox
					//.//*[@id='side']/div[2]/div/label/input
					driver.findElement(By.xpath(".//*[@id='side']/div[2]/div/label/input")).clear(); 
					//driver.findElement(By.xpath("//*[@id='input-chatlist-search']")).clear(); 
					Thread.sleep(500);
					//put contact number/name into search textbox
					driver.findElement(By.xpath(".//*[@id='side']/div[2]/div/label/input")).sendKeys(array[i].trim());
					//driver.findElement(By.xpath("//*[@id='input-chatlist-search']")).sendKeys(array[i].trim());
					
					Thread.sleep(500);
					
					try
					{
						//for searching contacts from whole contact list of whatsapp and click on it
						driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/div[1]/div[1]/span/div/span/div/div[2]/div/div/div/div/div/div/div[2]")).click();
					}
					
					catch(Exception e)
					{
						//for searching contacts from chatlist/frequently contacted chats and click on it
						driver.findElement(By.xpath("//*[@id=\"pane-side\"]/div/div/div/div[1]")).click();
						//e.printStackTrace();
					}
					
					//Thread.sleep(1000);
					
					//If same name contacts are present in whatsapp then get xpath of list
					int size=driver.findElements(By.xpath("//*[@class='_1wjpf']")).size();
				
					//Thread.sleep(500);
					
					/*for(int k=0;k<size;k++)
					{
						ArrayList<WebElement> names=(ArrayList<WebElement>) driver.findElements(By.xpath("//*[@class='_1wjpf']"));
						//System.out.println(names.get(k).getText());
						if(names.get(k).getText().equalsIgnoreCase(array[i]))
						{
							//System.out.println(names.get(k).getText()+"  =  "+array[i]);
							contatcNameElement=names.get(k);
							break;
						}
					}*/
					//if(contatcNameElement!=null)
					//{
						//contatcNameElement.click();
					
						//robot.keyPress(java.awt.event.KeyEvent.VK_ENTER);
						//get chat name 
						//String name=driver.findElement(By.xpath("//*[@class='_2zCDG']")).getText();
						//checking contact name and chat name
						/*if(array[i].equals(name))
						{*/
							//put massage
							Thread.sleep(500);
							
							//if contact number is present then put 'Hi' into textbox
							driver.findElement(By.xpath("//*[@class='_2bXVy']")).sendKeys("Hi");
							
							
							
							
							//Thread.sleep(700);
							
							//click on send button 
							driver.findElement(By.xpath("//*[@class='_2lkdt']")).click();
							//Thread.sleep(500);
						//}
					//}
							//code for printing time
							/*String time=null;
							time=driver.findElement(By.className("_3T2VG")).getText();
							 Map<String,String> map=new HashMap<String,String>();  
							 map.put(array[i], time);
							 
							 for(Map.Entry m:map.entrySet())
							 {  
								   System.out.println(m.getKey()+" "+m.getValue());  
							 }  */
					

				}
				
				//userList();
				
			//click on menu
				/*String timeOfMsg=driver.findElement(By.className("_3T2VG")).getText();
				System.out.println("msg time"+timeOfMsg);*/
				
				/*driver.findElement(By.xpath("//*[@role='button' and @title='Menu']")).click();
				
				//wait
				Thread.sleep(500);
				
				//click on logout button
				driver.findElement(By.xpath("//*[@class='_3lSL5 _2dGjP' and @role='button' and @title='Log out']")).click();
				
				//wait
				Thread.sleep(500);
				
				//close browser
				driver.close();*/
		
	}
	
}
