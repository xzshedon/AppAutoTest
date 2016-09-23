package BaseMethod;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class MouseAction {

	/**
	 * 本文档提供了如下方法： click 单击 addFile 上传附件-使用ctrl+v的方式 sengKeyFile
	 * 上传附件-使用sendkey方式 highlightElement 高亮显示，用与定位是否找到WebElement
	 */
	WebDriver driver;

	/*
	 * 输入验证码，键盘录入数据 使用方法： static 直接调用即可
	 */
	public static String ReadLine() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str = null;
		System.out.println("请输入验证码:");
		try {
			str = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 单击方法
	 * 
	 * @param driver
	 *            驱动
	 * @param by
	 *            要点击的位置
	 */
	public static void click(WebDriver driver, By by) {
		driver.findElement(by).click();
	}

	/**
	 * 鼠标右击方法
	 * 
	 * @param driver
	 *            驱动
	 * @param by
	 *            要点击的位置
	 */
	public static void rightClick(WebDriver driver, By by) {
		Actions action = new Actions(driver);
		action.contextClick(driver.findElement(by));
	}

	/**
	 * 鼠标左键双击方法
	 * 
	 * @param driver
	 *            驱动
	 * @param by
	 *            要点击的位置
	 */
	public static void doubleClick(WebDriver driver, By by) {
		Actions action = new Actions(driver);
		action.doubleClick(driver.findElement(by));
	}

	/**
	 * 鼠标左键按下不松开
	 * 
	 * @param driver
	 *            驱动
	 * @param by
	 *            要点击的位置
	 */
	public static void holdClick(WebDriver driver, By by) {
		Actions action = new Actions(driver);
		action.release(driver.findElement(by));
	}

	/**
	 * 鼠标移动到制定元素上
	 * 
	 * @param driver
	 *            驱动
	 * @param by
	 *            要点击的位置
	 */
	public static void moveToElement(WebDriver driver, By by) {
		Actions action = new Actions(driver);
		action.moveToElement(driver.findElement(by));
	}

	/**
	 * 将目标元素拖拽到制定元素上
	 * 
	 * @param driver
	 *            驱动
	 * @param by
	 *            要点击的位置
	 */
	public static void dragAndDrop(WebDriver driver, By by) {
		Actions action = new Actions(driver);
		action.dragAndDrop(driver.findElement(by), driver.findElement(by));
	}

	/**
	 * 模拟键盘按下ALT键
	 * 
	 * @param driver
	 *            驱动
	 * @param by
	 *            要点击的位置
	 */
	public static void keyDown(WebDriver driver, By by) {
		Actions action = new Actions(driver);
		action.keyDown(driver.findElement(by), Keys.ALT);// 注：Keys.ALT是模拟ALT键，Enter键是Keys.ENTER
	}
	
	 /**   模拟键盘按 向下键
	 @param driver
	          驱动
	 @param by
	          要点击的位置*/
	public  void keydown(WebDriver driver,By by) {
		Actions action =new Actions(driver);
		action.keyDown(driver.findElement(by),Keys.DOWN );
	}
	
	
	 /**   模拟键盘按 回车键
	 @param driver
	          驱动
	 @param by
	          要点击的位置*/
	public static void keyenter(WebDriver driver,By by) {
		Actions action =new Actions(driver);
		action.keyDown(driver.findElement(by),Keys.ENTER );
	}
	

	/**
	 * 模拟键盘松开ALT键
	 * 
	 * @param driver
	 *            驱动
	 * @param by
	 *            要点击的位置
	 */
	public static void keyUp(WebDriver driver, By by) {
		Actions action = new Actions(driver);
		action.keyUp(driver.findElement(by), Keys.ALT);// 注：模拟键盘操作也可参照addFile中的方法
	}

	/**
	 * 上传附件
	 * 
	 * @param driver
	 * @param by
	 *            上传附件按钮的元素的位置
	 * @param filepath
	 *            自定义上传附件的路径
	 * @throws AWTException
	 * @throws InterruptedException
	 */
       public static void addFile(String filepath)
			throws AWTException, InterruptedException {
		Robot robot = new Robot();
		StringSelection ss = new StringSelection(filepath);// 自定义上传附件的路径
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(2000);
		robot.keyPress(KeyEvent.VK_ENTER);
	}

	/**
	 * 通过不可见的input框，直接输入文件路径
	 * 
	 * @param driver
	 *            传WebDriver driver
	 * @param by
	 *            input框的by参数
	 * @param filepath
	 *            文件的路径
	 * @throws Exception
	 */
	public static void sengKeyFile(WebDriver driver, By by, String filepath)
			throws Exception {
		Thread.sleep(5000);
		WebElement file = driver.findElement(by);
		Thread.sleep(2000);
		file.sendKeys(filepath);
		Thread.sleep(3000);
	}

	/**
	 * 高亮显示方法，用于定位控件位置是否找到正确
	 * 
	 * @param driver
	 * @param element
	 */
	public static void highlightElement(WebDriver driver, WebElement element) {
		for (int i = 0; i < 5; i++) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(
					"arguments[0].setAttribute('style', arguments[1]);",
					element, "color: yellow; border: 2px solid yellow;");
			// js.executeScript("arguments[0].setAttribute('style', arguments[1]);",
			// element, "");//快速闪烁5次
		}
	}
	public static void addFile1(WebDriver driver, WebElement sc, String filepath)
			throws AWTException, InterruptedException {
		
		sc.click();
		Thread.sleep(5000);
		Robot robot = new Robot();
		StringSelection ss = new StringSelection(filepath);// 自定义上传附件的路径
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(2000);
		robot.keyPress(KeyEvent.VK_ENTER);
	}

}
