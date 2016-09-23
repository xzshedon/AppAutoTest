package TestCases;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import junit.framework.Assert;

public class BiyoApp {

	static Logger log = Logger.getLogger(BiyoApp.class);
	private AndroidDriver driver;

	@Test
	public void order() throws Exception {

		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);// 最多等待10秒，如果还是找不到下个元素则会报错停止脚本
		List<WebElement> words = driver.findElements(By.className("android.widget.RelativeLayout"));
		words.get(7).click(); // 点击APP首页，我的
		try {
			WebElement setpn = driver.findElement(By.id("cn.sudiyi.app.client:id/zone_setting"));
			setpn.isDisplayed();
			System.out.println("user is login");
			Logout();
			Login();

		} catch (NoSuchElementException e) {

			snapshot((TakesScreenshot) driver, "loginui.png");
			System.out.println("未找到元素id");
			System.out.println("user not is login");
			Login();
		}

		Sale();

	}

	/**
	 * 登录
	 *
	 */
	public void Login() {

		driver.findElementByAndroidUIAutomator("new UiSelector().text(\"登录\")").click();// 点击登录
		WebElement username = driver.findElement(By.id("cn.sudiyi.app.client:id/phone"));
		username.click();
		username.sendKeys("18910711055"); // 输入用户名
		WebElement pwd = driver.findElement(By.id("cn.sudiyi.app.client:id/password"));
		pwd.click();
		pwd.sendKeys("123456"); // 输入密码
		driver.hideKeyboard();
		driver.findElement(By.id("cn.sudiyi.app.client:id/btn_login")).click(); // 点击登录按钮
		log.info("**********************登录必有APP成功**************************");
		Assert.assertTrue(driver.findElement(By.id("cn.sudiyi.app.client:id/zone_setting")).isDisplayed());


	}

	/**
	 * 退出
	 *
	 */
	public void Logout() {

		driver.findElement(By.id("cn.sudiyi.app.client:id/zone_setting")).click(); // 点击设置按钮
		driver.findElement(By.id("cn.sudiyi.app.client:id/logout")).click();// 点击退出按钮
		log.info("**********************退出必有APP成功**************************");
		List<WebElement> words = driver.findElements(By.className("android.widget.RelativeLayout"));
		words.get(7).click(); // 点击APP首页，我的

	}

	/**
	 * 购买商品
	 *
	 */
	public void Sale() {

		driver.findElement(By.id("cn.sudiyi.app.client:id/account_collect_goods")).click();// 点击收藏
		WebElement goods = driver.findElementByAndroidUIAutomator("new UiSelector().text(\"哈哈测试008（自动化）\")");// 点击“哈哈测试008（自动化）商品”
		goods.click();
		driver.findElement(By.id("cn.sudiyi.app.client:id/btn_goods_details_shopping")).click();// 点击购买按钮
		driver.findElement(By.id("cn.sudiyi.app.client:id/order_submit")).click();// 点击提交订单

	}

	/**
	 * 向上滑动
	 *
	 * @author Young
	 * @param driver
	 * @param during
	 */
	public void swipeToUp(AndroidDriver driver, int during) {
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width / 2, height * 3 / 4, width / 2, height / 4, during);
		// wait for page loading
	}

	/**
	 * 向下滑动
	 *
	 * @author Young
	 * @param driver
	 * @param during
	 */
	public void swipeToDown(AndroidDriver driver, int during) {
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width / 2, height / 4, width / 2, height * 3 / 4, during);
		// wait for page loading
	}

	/**
	 * 截图
	 *
	 * @author Young
	 * @param drivername
	 * @param filename
	 */
	public static void snapshot(TakesScreenshot drivername, String filename) {
		// this method will take screen shot ,require two parameters ,one is
		// driver name, another is file name

		String currentPath = System.getProperty("user.dir"); // get current work
																// folder
		File scrFile = drivername.getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy
		// somewhere
		try {
			System.out.println("save snapshot path is:" + currentPath + "/" + filename);
			FileUtils.copyFile(scrFile, new File(currentPath + "\\" + filename));
		} catch (IOException e) {
			System.out.println("Can't save screenshot");
			e.printStackTrace();
		} finally {
			System.out.println("screen shot finished, it's in " + currentPath + " folder");
		}
	}

	/**
	 * 点击屏幕
	 *
	 */
	// public static void clickScreen(int x, int y, int duration,
	// AndroidDriver drivers) {
	// JavascriptExecutor js = (JavascriptExecutor) drivers;
	// HashMap<String, Integer> tapObject = new HashMap<String, Integer>();
	// tapObject.put("x", x);
	// tapObject.put("y", y);
	// tapObject.put("duration", duration);
	// js.executeScript("mobile: tap", tapObject);
	// }

	@BeforeMethod
	public void setUp() throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
		capabilities.setCapability("platformName", "Android"); // 手机OS
		capabilities.setCapability("deviceName", "Android Emulator");
		capabilities.setCapability("platformVersion", "6.0.1"); // 真机的Android版本
		capabilities.setCapability("appPackage", "cn.sudiyi.app.client"); // Android应用的包名
		capabilities.setCapability("appActivity", "cn.sudiyi.app.client.misc.StartActivity"); // 启动的Android的Activity名称
		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

	}

	@AfterMethod
	public void tearDown() throws Exception {
		driver.quit();
	}
}
