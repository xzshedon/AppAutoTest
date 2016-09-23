package BaseMethod;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;

public class Browser {
	public static WebDriver driver;

	static Logger log = Logger.getLogger(Browser.class);

	public Browser() {

		// this.browserID = browserID;
		this.log = log.getLogger(Browser.class.getName());

	}

	/**
	 * 验证元素是否存在
	 *
	 */
	public boolean isElementExsit(WebDriver driver, By locator) {
		boolean flag = false;
		try {
			WebElement element = driver.findElement(locator);
			flag = null != element;
		} catch (NoSuchElementException e) {
			System.out.println("Element:" + locator.toString()
					+ " is not exsit!");
		}
		return flag;
	}

	/**
	 * 获取当前页面.
	 */
	public String getLocation() {
		return driver.getCurrentUrl();
	}

	/**
	 * 回退历史页面。
	 */
	public void back() {
		driver.navigate().back();

	}

	/**
	 * 获取WebDriver实例, 调用未封装的函数.
	 */

	public static WebDriver driver(String type) {
		if (type == "ie") {
			log.info("**************************测试开始执行**************************");
			log.info("**************************读取IE**************************");
			System.setProperty("webdriver.ie.driver",
					".\\driver\\IEDriverServer.exe");
			driver = new InternetExplorerDriver();
			return driver;
		} else if (type == "chrome") {
			System.setProperty("webdriver.chrome.driver",
					".\\driver\\chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			return driver;

		} else {
			System.setProperty("webdriver.firefox.bin",
					"C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
			ProfilesIni pi = new ProfilesIni();
			FirefoxProfile profile = pi.getProfile("default");
			driver = new FirefoxDriver(profile);
			return driver;
		}

	}

	/**
	 * 刷新页面。
	 * 
	 */
	public void refresh() {
		driver.navigate().refresh();
	}

	/**
	 * 通过navigate进入新页面
	 */
	public void switchTo(String Url) {
		driver.navigate().to(Url);
	}

	/**
	 * 获取页面标题.
	 */
	public String getTitle() {
		return driver.getTitle();
	}

	/**
	 * 退出Selenium.
	 */
	public void quit() {
		try {
			driver.quit();
		} catch (Exception e) {
			System.err.println("Error happen while quit selenium :"
					+ e.getMessage());
		}
	}

	/**
	 * 设置如果查找不到Element时的默认最大等待时间。
	 */
	public void setTimeout(int seconds) {
		driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
	}

	/**
	 * 获取WebDriver实例, 调用未封装的函数.
	 */
	public WebDriver getDriver() {
		return driver;
	}

	// Element 函數//

	/**
	 * 判断页面内是否存在Element.
	 */
	public boolean isElementPresent(WebElement element) {
		try {
			if (element != null) {
				return true;
			} else {
				return false;
			}
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * 判断Element是否可见.
	 */
	public boolean isVisible(WebElement element) {
		return element.isDisplayed();
	}

	/**
	 * 在Element中输入文本内容.
	 */
	public void sendKeys(WebElement element, String text) {
		element.clear();
		element.sendKeys(text);
	}

	/**
	 * 输入前不清理
	 * 
	 * @param element
	 * @param text
	 */
	public void sendKeysWithoutclean(WebElement element, String text) {

		element.sendKeys(text);
	}

	/**
	 * 点击Element.
	 */
	public void click(WebElement element) {
		element.click();
	}

	/**
	 * 选中Element.
	 */
	public void check(WebElement element) {
		if (!element.isSelected()) {
			element.click();
		}
	}

	/**
	 * 取消Element的选中.
	 */
	public void uncheck(WebElement element) {
		if (element.isSelected()) {
			element.click();
		}
	}

	/**
	 * 判断Element有否被选中.
	 */
	public boolean isChecked(WebElement element) {
		return element.isSelected();
	}

	/**
	 * 返回Select Element,可搭配多种后续的Select操作. eg.
	 * s.getSelect(by).selectByValue(value);
	 */
	public Select getSelect(WebElement element) {
		return new Select(element);
	}

	/**
	 * 获取Element的文本.
	 */
	public String getText(WebElement element) {
		return element.getText();
	}

	/**
	 * 获取Input框的value.
	 */
	public String getValue(WebElement element) {
		return element.getAttribute("value");
	}
/**
 * 跳转iframe
 */
	public void switchToiframe(WebElement element) {
		driver.switchTo().frame(element);
		
	}
	
	
	
	/**
	 * 跳转回默认的iframe
	 */
	public void switchTodefultiframe() {
	driver.switchTo().defaultContent();
	}
	/**
	 * 通过title切换洌览器标签页
	 */
	public boolean switchToWindow(WebDriver driver, String windowTitle) {
		boolean flag = false;
		try {
			String currentHandle = driver.getWindowHandle();
			Set<String> handles = driver.getWindowHandles();
			System.out.println(handles);
			for (String s : handles) {
				if (s.equals(currentHandle)) {

					System.out.println(s + "s");
					continue;
				} else {
					driver.switchTo().window(s);
					if (driver.getTitle().contains(windowTitle)) {
						flag = true;
						System.out.println("Switch to window: " + windowTitle
								+ " successfully!");
						break;
					} else
						continue;
				}
			}
		} catch (NoSuchWindowException e) {
			System.out.printf("Window: " + windowTitle + " cound not found!",
					e.fillInStackTrace());
			flag = false;
		}
		return flag;
	}

}
