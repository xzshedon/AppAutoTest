package BaseMethod;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xalan.xsltc.dom.SAXImpl.NamespaceWildcardIterator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
public class basicMethod {
	/**本文档提供了如下方法：、
	 * 		 
	 */
	static WebDriver driver;
	
	/*输入验证码，键盘录入数据
	 * 使用方法： static  直接调用即可
	 */
	public static  String ReadLine() {
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
	
	/*随机生成N个0-9，a-z,A-Z的N个字符
	 * 用于文本框值唯一且不支持特殊字符情况
	 * 缺点：可能重复。
	 * 优点：不需要文本框支持特殊字符
	 * n尽量往大写，避免重复
	 * 用法：char[] c = RandomNum(5); 
		   String s = String.valueOf(c);
	 */
	public static char[] RandomNum(int n) {
		char c = 0;		
		char[] a = new char[n];
		//String s ;
		// 把0-9、a-z、A-Z全存在一个字符串里
		StringBuffer array = new StringBuffer();
		for (int i = 0; i <= 9; i++) {
			array.append(i);
		}
		for (int i = (int) 'a'; i <= (int) 'z'; i++) {
			array.append((char) i);
		}
		for (int i = (int) 'A'; i <= (int) 'Z'; i++) {
			array.append((char) i);
		}
		int length = array.length();

		for (int i = 0; i < n; i++) {
			c = array.charAt((int) (Math.random() * length));			
		//	System.out.print(c);
			a[i]=c;		
			//System.out.print(a[i]);
		}
		return a;
	}
	/*
	 * 读取当前系统时间
	 * 用于文本框唯一且支持特殊字符情况
	 * 用法：String d = Date();
	 */
	public static String Date() {
		Date date = new Date();
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return time;
	}
	
	/*
	 * 时间控件的操作，CRM这边的时间控件全部试用
	 */
	public static void timeSelect(){
	driver.findElement(By.id("sTime")).click(); // 点击开始时间
	driver.switchTo().frame(driver.findElement(By.xpath("//iframe"))); // 跳转到元素“今天”所在的frame
	driver.findElement(By.id("dpTodayInput")).click(); // 选择今天
	driver.switchTo().defaultContent();
	driver.findElement(By.id("eTime")).click(); // 点击结束时间
	driver.switchTo().frame(driver.findElement(By.xpath("//iframe")));
	driver.findElement(By.id("dpTodayInput")).click(); // 选择今天
	driver.switchTo().defaultContent();
	}
	/*
	 * 只读属性时间控件的操作
	 * 移除只读属性
	 */
	public static void timeReadOnly(){
		  JavascriptExecutor removeAttribute = (JavascriptExecutor)driver;  
	         //remove readonly attribute
	         removeAttribute.executeScript("var setDate=document.getElementById(\"train_date\");setDate.removeAttribute('readonly');") ;	         
		}
	/*创建一个UTF-8的文件（可以将一个文件转换成UTF-8格式）
	 * @param content 文件内容
	 * @param FileName 文件路径+名称("D:\\17_sale1.html")
	 */
	public static void File(String content , String FileName) throws Exception{
		File filename = new File(FileName);
		/*
		 * *
		 * 读取文件的方法
		 * FileInputStream fis=null;
		 * InputStreamReader isr=null;
		 * BufferedReader bufr=null;
		 * fis=new FileInputStream(filename/文件路径加名字);
		 * isr=new InputStreamReader(fis);
		 * bufr= new BufferedReader(isr);
		 * string read;
		 * while ((read = bufr.readLine()) != null) {
               System.out.println(read);
            }
            bufr.close();
            
		 */

		FileOutputStream fos = null;
		OutputStreamWriter osw = null;		
		try {
			fos=new FileOutputStream(filename);
			osw = new OutputStreamWriter(fos,"UTF-8");
			 
			osw.write(content);	
		 
		} catch (IOException e1) {		
		e1.printStackTrace();
		} finally{
		if(osw!=null){
		try {
		osw.close();
		} catch (IOException e2) {
		e2.printStackTrace();
		}
		}
		}		
	}
	
	/*连接数据库
	 * 适用于审批等直接需要修改数据库的操作
	 * user="root";// MySQL配置时的用户名
	 * Url = "jdbc:mysql://192.168.42.22:3306/crm_zhip";// URL指向要访问的数据库名message_old
	 * password = // MySQL配置时的密码
	 */

	public static void sql(String sql,String Url,String user,String password) throws IOException,
			ClassNotFoundException, SQLException {
	   
		Class.forName("com.mysql.jdbc.Driver");// 加载驱动程序
		Connection connection = DriverManager
				.getConnection(Url, user, password);// 连续数据库
		if (!connection.isClosed()) {
			System.out.println("成功连接数据库!");
		}
		Statement statement = connection.createStatement();// statement用来执行SQL语句
		ResultSet rs = statement.executeQuery(sql);// 执行SQL语句并返回结果
		ResultSetMetaData rsmd = rs.getMetaData();
         int colCount=rsmd.getColumnCount();
		while (rs.next()) {
//			if(rs.getString(3) != null)
			for(int i=1;i<=colCount;i++)
			{
			   System.out.print(rs.getString(i)+" ");
			}
			System.out.println();			
		}
		rs.close();
		connection.close();
	
	}
	
	
	//输出数据库的制定列。
public static List<String> sql(String sql,String Url,String user,String password,int column) throws IOException,
     ClassNotFoundException, SQLException {

Class.forName("com.mysql.jdbc.Driver");// 加载驱动程序
Connection connection = DriverManager
		.getConnection(Url, user, password);// 连续数据库
if (!connection.isClosed()) {
	System.out.println("成功连接数据库!");
}
Statement statement = connection.createStatement();// statement用来执行SQL语句
ResultSet rs = statement.executeQuery(sql);// 执行SQL语句并返回结果
//ResultSetMetaData rsmd = rs.getMetaData();
// int colCount=rsmd.getColumnCount();
 List<String> result= new ArrayList<String>();
while (rs.next()) {
//	if(rs.getString(3) != null)
//	for(int i=1;i<=colCount;i++)
	 
	   result.add(rs.getString(column));
	      
 		
}
rs.close();
connection.close();
return result;

}
//输出数据库的制定行的制定列

public static String sql(String sql, String Url, String user, String password, int row, int column)
		throws IOException, ClassNotFoundException, SQLException {
	String result = null;
	Class.forName("com.mysql.jdbc.Driver");// 加载驱动程序
	Connection connection = DriverManager.getConnection(Url, user, password);// 连续数据库
	if (!connection.isClosed()) {
		System.out.println("成功连接数据库!");
	}
	Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);// statement用来执行SQL语句
	ResultSet rs = statement.executeQuery(sql);// 执行SQL语句并返回结果集
	rs.absolute(row);
	result = rs.getString(column);
	
	rs.close();
	connection.close();
	return result;
}
	/*
	 * 创建CSV文件
	 * path:("D:\\8.csv")创建的csv文件的保存路径
	 * header:（"encry_phone"+",mess_content"+",param1"+",param2,param3\r\n"）CSV 表头，第一行，用法参照括号内
	 * content:(",${param1}您好，唯品会送您生日礼物 ${param2}，${param3}。,"+i+",iPhone6,iPad\r\n") 第二行-第N行需要填写的内容
	 */
	 public static void csvCreat(String path,String header,String content) {
		  try {
		   FileWriter fw = new FileWriter(path);
		   fw.write(header);
		   for (int i = 701; i <= 800; i++) {
		    StringBuffer str = new StringBuffer();		    
		     str.append(i+content);	    
		    fw.write(str.toString());
		    fw.flush();
		   }
		   fw.close();
		   System.out.println("创建成功完成");
		  } catch (IOException e) {
		   e.printStackTrace();
		  }
		 }
	/*
	 * MySql增删改查
	 */
	 public static int updateSql(String sql,String Url,String user,String password) throws SQLException, ClassNotFoundException
		
		{
			
			Class.forName("com.mysql.jdbc.Driver");// 加载驱动程序
			Connection connection = DriverManager
					.getConnection(Url, user, password);// 连续数据库
			if (!connection.isClosed()) {
				System.out.println("成功连接数据库!");
			}
			Statement statement = connection.createStatement();// statement用来执行SQL语句
			int rs = statement.executeUpdate(sql);// 执行SQL语句并返回结果集
			
			
			connection.close();
			return rs;
			
			
		}
	 
	 
	 
	//连接数据库，从数据库中查询数据
//		public static List SelectSQL(String sql,String Url,String user,String password) throws IOException,
//		ClassNotFoundException, SQLException {
//			     List list = new ArrayList();
//			     Class.forName("com.mysql.jdbc.Driver");// 加载驱动程序
//	             Connection connection = DriverManager.getConnection(Url, user, password);// 连续数据库
//	                    if (!connection.isClosed()) {
//		           System.out.println("成功连接数据库!");
//		           }
//	             Statement statement = connection.createStatement();// statement用来执行SQL语句
//	             ResultSet rs = statement.executeQuery(sql);// 执行SQL语句并返回结果集
//	             java.sql.ResultSetMetaData md = rs.getMetaData();
//
//	             int columnCount = md.getColumnCount();
//
//	             while (rs.next()) {
//
//	                 Map rowData = new HashMap();
//
//	                 for (int i = 1; i <= columnCount; i++) {
//	                     rowData.put(i, rs.getObject(i));
//	               
//	                 }
//
//	                 list.add(rowData);
//
//	             }
//	             connection.close();
//	             return list;
//	}

	 
	 /**
	 获取档期日记并+1天
	  * */
	 public static String getDate() throws InterruptedException{

	 	int year;
	 	int month;
	 	int day;
	 	int hour;
	 	int minute;
	 	int second;
	 	 Calendar    c    =    Calendar.getInstance();   
	      c.setTime(new    java.util.Date());   
	        year=    c.get(Calendar.YEAR);   
	       month=    c.get(Calendar.MONTH)+1;   
	        day =    c.get(Calendar.DAY_OF_MONTH)+1;   
	       hour     =    c.get(Calendar.HOUR_OF_DAY);   
	       minute =    c.get(Calendar.MINUTE);   
	       second =    c.get(Calendar.SECOND); 
	 String time =year+"-"+month+"-"+day;
	      
	       return time;
	 	
	 	
	 }
	 /**
	   * 远程 执行命令并返回结果调用过程 是同步的（执行完才会返回）
	   * @param host	主机名
	   * @param user	用户名
	   * @param psw	密码
	   * @param port	端口
	   * @param command	命令
	   * @return
	   */

public static String exec(String command){

	
	String host="172.32.2.6";
  String user="root";
  String psw="qazwsx";
  int port=22;
	String result="";
   Session session =null;
   ChannelExec openChannel =null;
   try {
     JSch jsch=new JSch();
     session = jsch.getSession(user, host, port);
     java.util.Properties config = new java.util.Properties();
     config.put("StrictHostKeyChecking", "no");
     session.setConfig(config);
     session.setPassword(psw);
     session.connect();
     openChannel = (ChannelExec) session.openChannel("exec");
     openChannel.setCommand(command);
     int exitStatus = openChannel.getExitStatus();
     System.out.println(exitStatus);
     openChannel.connect();  
           InputStream in = openChannel.getInputStream();  
           BufferedReader reader = new BufferedReader(new InputStreamReader(in));  
           String buf = null;
           while ((buf = reader.readLine()) != null) {
           	result+= new String(buf.getBytes("gbk"),"UTF-8")+"    <br>\r\n";  
           }  
   } 
   catch (JSchException e) {
     result+=e.getMessage();
   }
   catch (IOException e) {
	     result+=e.getMessage();
	   }
   finally{
     if(openChannel!=null&&!openChannel.isClosed()){
       openChannel.disconnect();
     }
     if(session!=null&&session.isConnected()){
       session.disconnect();
     }
   }
   return result;
 }

	public static List<String> SelectSQL(String sql, String url, String user,
			String password) throws SQLException, ClassNotFoundException {
		  List list = new ArrayList();
		  List<String> list1 = new ArrayList();
		     Class.forName("com.mysql.jdbc.Driver");// 加载驱动程序
          Connection connection = DriverManager.getConnection(url, user, password);// 连续数据库
                 if (!connection.isClosed()) {
	           System.out.println("成功连接数据库!");
	           }
          Statement statement = connection.createStatement();// statement用来执行SQL语句
          ResultSet rs = statement.executeQuery(sql);// 执行SQL语句并返回结果集
          java.sql.ResultSetMetaData md = rs.getMetaData();

          int columnCount = md.getColumnCount();
          
          Map rowData = new HashMap();
          while (rs.next()) {

              

              for (int i = 1; i <= columnCount; i++) {
                  rowData.put(i, rs.getObject(i));
                  list1.add(rs.getObject(i).toString());
            
              }

              list.add(rowData);

          }
          connection.close();
          return list1;
		//return null;
	}
}
