package TestCases;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import BaseMethod.Page;

@Test
public class QQmail {
	public void QQmailTest() throws Exception
	{
		Page page = new Page("mail","chrome");
		page.driver.get("https://mail.qq.com");
	
		
		//page.driver.switchTo().frame(page.driver.findElement(By.id("login_frame")));
		page.switchToiframe(page.getElement("LoginFrame"));
		
		//page.driver.findElement(By.id("switcher_plogin")).click();
		page.getElement("LoginNode").click();
		
		//page.sendKeys(page.driver.findElement(By.id("u")),"274135122");
		page.sendKeys(page.getElement("UserName"), "274135122");
		//page.sendKeys(page.driver.findElement(By.id("p")),"guoyuchen");
		page.sendKeys(page.getElement("Password"), "guoyuchen");
		
		//page.driver.findElement(By.id("login_button")).click();
		
		page.getElement("Button").click();
		Thread.sleep(7000);
		page.quit();
		
	}

}
