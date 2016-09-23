package Util;

import static org.testng.AssertJUnit.assertEquals;

import java.io.File;

import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

//import com.google.inject.Key;
import BaseMethod.Page;

public class PageElement {

	private Page _page = null;
	public PageElement(Page page) {
		_page = page;
	}

	
	/**
	 * 去除文本框readonly属性
	 */
	public void enableReadOnlyText(WebElement element) {
		String key = null;
		if (element.getAttribute("id") != null) {
			key = element.getAttribute("id");
			_page.executeJavaScript("var oText=document.getElementById('" + key
					+ "');oText.removeAttribute('readonly');");
		} else if (element.getAttribute("class") != null) {
			key = element.getAttribute("class");
			_page.executeJavaScript("var oText=document.getElementsByClassName('"
					+ key + "');oText.removeAttribute('readonly');");
		} else {
			// 待定
		}
	}

	
	/**
	 * 点击textbox
	 */
	public void clickTextbox(WebElement element) {
		String key = null;
		if (element.getAttribute("id") != null) {
			key = element.getAttribute("id");
			_page.executeJavaScript("var oText=document.getElementById('" + key
					+ "');oText.click()");
		} else if (element.getAttribute("class") != null) {
			key = element.getAttribute("class");
			_page.executeJavaScript("var oText=document.getElementById('" + key
					+ "');oText.click()");
		} else {
			// 待定
		}
	}
	
	
	/**
	 * 用于处理如百度搜索框类似通过ajax获取数据的文本框选值
	 */
	public void fillDropText(WebElement element,String value){
		_page.sendKeys(element, value);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		element.sendKeys(Keys.ARROW_DOWN);
		element.sendKeys(Keys.ENTER);
	}
	
//	/*
//	 * 上次文件
//	*/
//	public void uploadFile(WebElement element,String value) {
//		String FilePath=System.getProperty("user.dir")+File.separator+"File"+File.separator+"Contract"+File.separator;
//		System.out.println("*************找到模板路径");
//		try {
//			_page.sendKeysWithoutclean(_page.getElement("excel"), FilePath+value);
//			System.out.println("*************导入模板成功");
//		} catch (Exception e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
//
//	}
	
	
	/**
	 * 获取元素文本
	 */
	public String getElementText(String key) throws Exception{
		return _page.getElement(key).getText();
	}
	
	
	/**
	 * 获取text文本
	 */
	public String getText(String key) throws Exception{
		return _page.getElement(key).getAttribute("value");
	}
	
	
	/**
	 * 验证alert文本
	 */
	public boolean verifyAlertText(String text) throws InterruptedException{
		  Alert alert = _page.driver.switchTo().alert();  
	      String str = alert.getText();  
	      Thread.sleep(1000);
	      assertEquals(text,str);
	      return str.equals(text);	     
	}
	
	
	
	public void login(){
		//窗口最大化  待定
		_page.driver.manage().window().maximize();
		try {
			_page.sendKeys(_page.getElement("loginName"), "qingxin.zheng");
			_page.sendKeys(_page.getElement("loginPassword"), "ldap@VIP.com");
			_page.sendKeys(_page.getElement("ValidateText"), "test1");
			_page.click(_page.getElement("loginButton"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(_page.getTitle());
		assertEquals("首页 - VIP - 供应商管理平台", _page.getTitle());
	}
	public void closeBrowser(){
		_page.quit();
	}
	
	//打开URL
	public void redirectURL(String url){
		_page.driver.get(url);
	}
}
