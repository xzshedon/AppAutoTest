package BaseMethod;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


/**
 * Created by qingxin.zheng on 2015/3/25.
 * 信息验证类
 */
public class VerifyWorker {
	
	private WebDriver _driver = null;
	
	public VerifyWorker(WebDriver driver){
		this._driver = driver;
	}
	//验证table前n行信息
	public void verifyTableInfo(WebElement element,String[] strArr){
		/*
		var oRow = document.getElementsByClassName("tables")[0].tBodies[0].rows[1];
		for(var i = 0;i<oRow.cells.length;i++){
		  if(oRow.cells[i].style['display']!='none'){
		    console.log(oRow.cells[i]);
		  }
		}
		*/
		JavascriptExecutor jsExecutor = (JavascriptExecutor)_driver; 
		String key = null;
		if(element.getAttribute("id")!=null){
			key = element.getAttribute("id");
			jsExecutor.executeScript("", strArr);
		} else if(element.getAttribute("class")!=null){
			key = element.getAttribute("class");
		}else{
			//待定
		}
	}
}
