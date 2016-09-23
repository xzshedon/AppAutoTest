package BaseMethod;

import static org.testng.AssertJUnit.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;


/**
 * Created by qingxin.zheng on 2015/3/23.
 * 基本操作类
 */
public class BasicOperator{
	
	private static BasicOperator _instance = null;
	private Page _page = null;
	private String _configFile = null;
	private LinkedHashMap<String, String> _dataMap = null; 
	private List<String> _elements = new ArrayList<String>();
	private BasicOperator(String file,String driver,String configFile){
		this._configFile = configFile;
		this._dataMap = readDataXml();
		//this._actionSeq = readActionsXml();
		this._page = new Page(file,driver);
		Iterator<Entry<String, HashMap<String, String>>> iter = this._page.ml.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry) iter.next();
			String actionKey = entry.getKey().toString();
			_elements.add(actionKey) ;
		}
		//this._verify = new VerifyWorker(_page.driver);
	};
	//读取XML数据
	private LinkedHashMap<String, String> readDataXml(){
		LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();
		SAXReader saxReader = new SAXReader();
        Document document = null;
		try {
			document = saxReader.read(new File("config/case-config/" +this._configFile));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // 获取根元素
        Element root = document.getRootElement();        

        // 获取特定名称的子元素
        Element dataset = root.element("dataset");
        String key = null;
        String value = null;
        List<Element> dataItems = dataset.elements();
        for(int i=0;i<dataItems.size();i++){
        	key = dataItems.get(i).attributeValue("id")+"_"+dataItems.get(i).attributeValue("type"); 
        	value = dataItems.get(i).getText();
        	dataMap.put(key, value);
        }
        return dataMap;
	}

	//删除readonly
	private void enableReadOnlyText(WebElement element){
		String key = null;
		if(element.getAttribute("id")!=null){
			key = element.getAttribute("id");
			_page.executeJavaScript("var oText=document.getElementById('"+key+"');oText.removeAttribute('readonly');") ;
		} else if(element.getAttribute("class")!=null){
			key = element.getAttribute("class");
			_page.executeJavaScript("var oText=document.getElementsByClassName('"+key+"');oText.removeAttribute('readonly');") ;
		}else{
			//待定
		}
	}
	//点击textbox
	private void clickTextbox(WebElement element){
		String key = null;
		if(element.getAttribute("id")!=null){
			key = element.getAttribute("id");
			_page.executeJavaScript("var oText=document.getElementById('"+key+"');oText.click()") ;
		} else if(element.getAttribute("class")!=null){
			key = element.getAttribute("class");
			_page.executeJavaScript("var oText=document.getElementById('"+key+"');oText.click()") ;
		}else{
			//待定
		}
	}
	
	public String getElementText(String key) throws Exception{
		return _page.getElement(key).getText();
	}
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
	//获取实例  单例
	public static BasicOperator getInstance(String file,String driver,String configFile){
		//if(_instance==null){
			_instance = new BasicOperator(file,driver,configFile);
		//}
		return _instance;
	} 
	//**? 是否给外部提供?待定
	public Page getPageObj(){
		return this._page;
	}
	//注入数据
	public void fillData() throws DocumentException{
		Iterator iter = _dataMap.entrySet().iterator();
		String key = null;
		String value = null;
		String key_id = null;
		String key_type = null;
		while (iter.hasNext()) {
			Map.Entry<String,String> entry = (Map.Entry) iter.next();
		    key = entry.getKey();
		    key_id = key.split("_")[0];
		    key_type = key.split("_")[1];
		    value = entry.getValue();
		    try {
		    	if(key_type.equals("text")){
		    		_page.sendKeys(_page.getElement(key_id), value);
		    	}else if(key_type.equals("select")){
		    		Thread.sleep(1000);
		    		_page.click(_page.getElement(key_id));
	    			_page.setDropValueByText(_page.getElement(key_id), value);
		    	}else if(key_type.equals("radio")){
		    		_page.click(_page.getElement(key_id));
		    	}else if(key_type.equals("checkbox")){
		    		_page.click(_page.getElement(key_id));
		    	}else if(key_type.equals("readonly")){
		    		enableReadOnlyText(_page.getElement(key_id));
	    			_page.sendKeys(_page.getElement(key_id), value);
		    	}else if(key_type.equals("droptext")){
		    		fillDropText(_page.getElement(key_id),value);
		    	}else if(key_type.equals("clicktext")){
		    		clickTextbox(_page.getElement(key_id));
		    	}else if(key_type.equals("file")){
		    		
		    	}else{
		    		return ;
		    	}
			} catch (Exception e) {
				System.out.print(key_id+" ");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void fillData(int startIndex,int endIndex){
		if(startIndex<0||endIndex>_dataMap.size()){
			return;
		}
		int index = 0;
		Iterator iter = _dataMap.entrySet().iterator();
		String key = null;
		String value = null;
		String key_id = null;
		String key_type = null;
		while (iter.hasNext()) {
			if(index<startIndex){
				index++;
				continue;
			}else if(index>endIndex){
				break;
			}
			Map.Entry<String,String> entry = (Map.Entry) iter.next();
		    key = entry.getKey();
		    key_id = key.split("_")[0];
		    key_type = key.split("_")[1];
		    value = entry.getValue();
		    try {
		    	if(key_type.equals("text")){
		    		_page.sendKeys(_page.getElement(key_id), value);
		    	}else if(key_type.equals("select")){
		    		_page.click(_page.getElement(key_id));
	    			_page.setDropValueByText(_page.getElement(key_id), value);
		    	}else if(key_type.equals("radio")){
		    		_page.click(_page.getElement(key_id));
		    	}else if(key_type.equals("checkbox")){
		    		_page.click(_page.getElement(key_id));
		    	}else if(key_type.equals("readonly")){
		    		enableReadOnlyText(_page.getElement(key_id));
	    			_page.sendKeys(_page.getElement(key_id), value);
		    	}else if(key_type.equals("droptext")){
		    		fillDropText(_page.getElement(key_id),value);
		    	}else if(key_type.equals("clicktext")){
		    		clickTextbox(_page.getElement(key_id));
		    	}else if(key_type.equals("file")){
		    		String FilePath=System.getProperty("user.dir")+File.separator+"File"+File.separator+value;
		    		_page.sendKeysWithoutclean(_page.getElement(key_id), FilePath);
		    	}else{
		    		return ;
		    	}
		    	index++;
			} catch (Exception e) {
				System.out.print(key_id+" ");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void fillData(String dataStrings){
		String[] dataSet = dataStrings.split(",");
		for(int i=0;i<dataSet.length;i++){
			Iterator iter = _dataMap.entrySet().iterator();
			String key = null;
			String value = null;
			String key_id = null;
			String key_type = null;
			while (iter.hasNext()) {
				Map.Entry<String,String> entry = (Map.Entry) iter.next();
			    key = entry.getKey();
			    key_id = key.split("_")[0];
			    key_type = key.split("_")[1];
			    value = entry.getValue();
			    if(dataSet[i].equals(key_id)){
			    	try {
				    	if(key_type.equals("text")){
				    		_page.sendKeys(_page.getElement(key_id), value);
				    	}else if(key_type.equals("select")){
				    		Thread.sleep(1000);
				    		_page.click(_page.getElement(key_id));
			    			_page.setDropValueByText(_page.getElement(key_id), value);
				    	}else if(key_type.equals("radio")){
				    		_page.click(_page.getElement(key_id));
				    	}else if(key_type.equals("checkbox")){
				    		_page.click(_page.getElement(key_id));
				    	}else if(key_type.equals("readonly")){
				    		enableReadOnlyText(_page.getElement(key_id));
			    			_page.sendKeys(_page.getElement(key_id), value);
				    	}else if(key_type.equals("droptext")){
				    		fillDropText(_page.getElement(key_id),value);
				    	}else if(key_type.equals("clicktext")){
				    		clickTextbox(_page.getElement(key_id));
				    	}else if(key_type.equals("file")){
				    		String FilePath=System.getProperty("user.dir")+File.separator+"File"+File.separator+value;
				    		_page.sendKeysWithoutclean(_page.getElement(key_id), FilePath);
				    	}else{
				    		return ;
				    	}
					} catch (Exception e) {
						System.out.print(key_id+" ");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }	
			}
		}
	}
	public void fillData(String name,String text){
		Iterator iter = _dataMap.entrySet().iterator();
		String key = null;
		String key_id = null;
		String key_type = null;
		while (iter.hasNext()) {
			Map.Entry<String,String> entry = (Map.Entry) iter.next();
		    key = entry.getKey();
		    key_id = key.split("_")[0];
		    key_type = key.split("_")[1];
		    if(name.equals(key_id)){
		    	try {
			    	if(key_type.equals("text")){
			    		_page.sendKeys(_page.getElement(key_id), text);
			    	}else if(key_type.equals("select")){
			    		Thread.sleep(1000);
			    		_page.click(_page.getElement(key_id));
		    			_page.setDropValueByText(_page.getElement(key_id), text);
			    	}else if(key_type.equals("radio")){
			    		_page.click(_page.getElement(key_id));
			    	}else if(key_type.equals("checkbox")){
			    		_page.click(_page.getElement(key_id));
			    	}else if(key_type.equals("readonly")){
			    		enableReadOnlyText(_page.getElement(key_id));
		    			_page.sendKeys(_page.getElement(key_id), text);
			    	}else if(key_type.equals("droptext")){
			    		fillDropText(_page.getElement(key_id),text);
			    	}else if(key_type.equals("clicktext")){
			    		clickTextbox(_page.getElement(key_id));
			    	}else{
			    		return ;
			    	}
				} catch (Exception e) {
					System.out.print(key_id+" ");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }	
		}
	}
	//执行动作序列
	public void executeClickSeq(){
		for(int i = 0;i<_elements.size();i++){
			try {
				_page.click(_page.getElement(_elements.get(i)));
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void executeClickSeq(int startIndex,int endIndex){
		for(int i = startIndex;i<=endIndex;i++){
			try {
				//处理confirm
				if(_elements.get(i).toLowerCase().trim().equals("confirm")){
					_page.clickConfirm();
					Thread.sleep(1000);
				}else{
					_page.click(_page.getElement(_elements.get(i)));
					Thread.sleep(1000);
				}				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void executeClickSeq(String actionStrings){
		//逗号分隔
		String[] actionList = actionStrings.split(",");
		for(int i =0;i<actionList.length;i++){
			try{
				if(actionList[i].equals("confirm")){
					_page.clickConfirm();
					Thread.sleep(1000);
				}else{
					_page.click(_page.getElement(actionList[i]));
					Thread.sleep(1000);
				}
			}catch(Exception e){
				System.out.println(actionList[i]);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	
	

	//***************************************************************************待定*******************************************************
	//**? 是否另建一个类？登陆
	public void login(){
		//窗口最大化  待定
		_page.driver.manage().window().maximize();
		try {
			_page.sendKeys(_page.getElement("loginName"), "yiya.lan");
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
	//获取textbox值
	public String getText(String key) throws Exception{
		return _page.getElement(key).getAttribute("value");
	}
	//暂时放在这
	public boolean verifyAlertText(String text) throws InterruptedException{
		  Alert alert = _page.driver.switchTo().alert();  
	      String str = alert.getText();  
	      Thread.sleep(1000);
	      assertEquals(text,str);
	      return str.equals(text);	     
	}
	//修改数据
	public void updateDataSet(String key,String value){
		this._dataMap.put(key, value);
	}
}