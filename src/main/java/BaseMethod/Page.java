package BaseMethod;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Set;

import org.ho.yaml.Yaml;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import Config.Config;


public class Page extends Browser{
 
    public WebDriver driver;
 
    private String yamlFile;
 
    public Page(String file,String driver) {
        this.yamlFile = file;
        this.driver = Browser.driver(driver);
        this.getYamlFile( this.yamlFile);
    }
    public Page(String file) {
        this.yamlFile = file;      
        this.getYamlFile( this.yamlFile);
    }
 
    public HashMap<String, HashMap<String, String>> ml;
     
    public HashMap<String, HashMap<String, String>> extendLocator;
   
    @SuppressWarnings("unchecked")
    protected void getYamlFile(String yamlFile) {
        File f = new File("locator/" + yamlFile + ".yaml");
        try {
            ml = Yaml.loadType(new FileInputStream(f.getAbsolutePath()),
                    HashMap.class);
            //读取公用yaml,优先级ml>commonYaml
            f = new File("locator/common.yaml");
            HashMap<String, HashMap<String, String>> commonYaml = Yaml.loadType(new FileInputStream(f.getAbsolutePath()),HashMap.class);
            for(String key : commonYaml.keySet()){
            	if(!ml.containsKey(key)){
            		ml.put(key, commonYaml.get(key));
            	}
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    //后添加yaml
    public void addYamlFile(String yamlFilePath){
    	File f = new File("locator/" + yamlFilePath + ".yaml");
        try {
            //读取公用yaml,优先级ml>commonYaml
            HashMap<String, HashMap<String, String>> objYaml = Yaml.loadType(new FileInputStream(f.getAbsolutePath()),HashMap.class);
            for(String key : objYaml.keySet()){
            	if(!ml.containsKey(key)){
            		ml.put(key, objYaml.get(key));
            	}
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    /*获取上传文件路径
     * 
     */
    public String getScheduleAndGoods(String FilePath) {
    	FilePath=System.getProperty("user.dir")+File.separator+"File"+File.separator+"ScheduleAndGoods"+File.separator;
        return FilePath;
    }    
    public String getfiles(String FilePath) {
    	FilePath=System.getProperty("user.dir")+File.separator+"File"+File.separator+"Contract"+File.separator;
        return FilePath;
    } 
    public void setLocatorVariableValue(String variable, String value){
        Set<String> keys = ml.keySet();
        for(String key:keys){
             String v = ml.get(key).get("value").replaceAll("%"+variable+"%", value);
             ml.get(key).put("value",v);
        }
    }
 
    private String getLocatorString(String locatorString, String[] ss) {
        for (String s : ss) {
            locatorString = locatorString.replaceFirst("%s", s);
        }
        return locatorString;
    }
 
    private By getBy(String type, String value)throws Exception {
        By by = null;
        if (type.equals("id")) {
            by = By.id(value);
        }
        if (type.equals("name")) {
            by = By.name(value);
        }
        if (type.equals("xpath")) {
            by = By.xpath(value);
        }
        if (type.equals("className")) {
            by = By.className(value);
        }
        if (type.equals("linkText")) {
            by = By.linkText(value);
        }
        if(type.equals("cssSelector")){
        	by = By.cssSelector(value);
        }    	
        return by;
    }
 
    private WebElement watiForElement(final By by) {
        WebElement element = null;
        int waitTime = Integer.parseInt(Config.getConfig("waitTime"));
        try {
            element = new WebDriverWait(driver, waitTime)
                    .until(new ExpectedCondition<WebElement>() {
                        public WebElement apply(WebDriver d) {
                            return d.findElement(by);
                        }
                    });
        } catch (Exception e) {
            System.out.println(by.toString() + " is not exist until " + waitTime);
        }
        return element;
    }
 
    public boolean waitElementToBeDisplayed(final WebElement element) {
        boolean wait = false;
        if (element == null)
            return wait;
        try {
            wait = new WebDriverWait(driver, Integer.parseInt(Config
                    .getConfig("waitTime")))
                    .until(new ExpectedCondition<Boolean>() {
                        public Boolean apply(WebDriver d) {
                            return element.isDisplayed();
                        }
                    });
        } catch (Exception e) {
            System.out.println(element.toString() + " is not displayed");
        }
        return wait;
    }
 
    public boolean waitElementToBeNonDisplayed(final WebElement element) {
        boolean wait = false;
        if (element == null)
            return wait;
        try {
            wait = new WebDriverWait(driver, Integer.parseInt(Config
                    .getConfig("waitTime")))
                    .until(new ExpectedCondition<Boolean>() {
                        public Boolean apply(WebDriver d) {
                            return !element.isDisplayed();
                        }
                    });
        } catch (Exception e) {
            System.out.println("Locator [" + element.toString()
                    + "] is also displayed");
        }
        return wait;
    }
 
    private WebElement getLocator(String key, String[] replace, boolean wait) throws Exception {
        WebElement element = null;
        if (ml.containsKey(key)) {
            HashMap<String, String> m = ml.get(key);
       
            String type = m.get("type");
            String value = m.get("value");
            if (replace != null)
                value = this.getLocatorString(value, replace);
            By by = this.getBy(type, value);
            if (wait) {
                element = this.watiForElement(by);
                boolean flag = this.waitElementToBeDisplayed(element);
                if (!flag)
                    element = null;
            } else {
                try {
                    element = driver.findElement(by);
                } catch (Exception e) {
                    element = null;
                }
            }
        } else
            System.out.println("Locator " + key + " is not exist in " + yamlFile
                    + ".yaml");
        return element;
    }
 
    public WebElement getElement(String key) throws Exception {
        return this.getLocator(key, null, true);
    }
 
    public WebElement getElementNoWait(String key) throws Exception {
        return this.getLocator(key, null, false);
    }
 
    public WebElement getElement(String key, String[] replace) throws Exception {
        return this.getLocator(key, replace, true);
    }
 
    public WebElement getElementNoWait(String key, String[] replace) throws Exception {
        return this.getLocator(key, replace, false);
    }
    public String getoxowarehouse(String FilePath) {
    	FilePath=System.getProperty("user.dir")+File.separator+"File"+File.separator+"oxowarehouse"+File.separator;
        return FilePath;
    }    
    /**
     * Created by qingxin.zheng on 2015/3/23.
     * 根据内容选择下拉框选项
     */
    public void setDropValueByText(WebElement element,String text){
    	org.openqa.selenium.support.ui.Select selectTown = new org.openqa.selenium.support.ui.Select(element);
    	selectTown.selectByVisibleText(text);
    }
    public Object executeJavaScript(String js){
    	JavascriptExecutor jsExecutor = (JavascriptExecutor)driver; 
    	return jsExecutor.executeScript(js) ;
    }
    public void clickConfirm(){
    	Alert confirm = driver.switchTo().alert();
    	confirm.accept();
    }
}