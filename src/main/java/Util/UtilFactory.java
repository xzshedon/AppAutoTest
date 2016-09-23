package Util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class UtilFactory {
	public static Object getUtilInstance(String className){
		Object instance = null;
		try {
			Class cls = Class.forName("com.vip.VIP_UI.util."+className);
			instance =cls.newInstance();
		} catch (ClassNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return instance;
	}
	public static Object getUtilInstance(String className,Object ...agrs) {  
        Class cls=null;  
        try {  
            cls = Class.forName("com.vip.VIP_UI.util."+className);  
        } catch (ClassNotFoundException e1) {  
            // TODO Auto-generated catch block  
            return null;  
        }  
        Constructor[] constructors = cls.getConstructors();  
        Object instance=null;  
        for(Constructor cons:constructors){  
            Class <?>[] clses=cons.getParameterTypes();  
            if(clses.length>0){  
                boolean isThisConstructor=true;  
                for(int i=0;i<clses.length;i++){  
                    Class c=clses[i];   
                    if(! c.isInstance(agrs[i]) ){  
                        isThisConstructor=false;  
                    }  
                }  
                if(isThisConstructor){  
                    try {  
                        instance=cons.newInstance(agrs);  
                        break;  
                    } catch (IllegalArgumentException e) {  
                        // TODO Auto-generated catch block  
                        e.printStackTrace();  
                    } catch (InvocationTargetException e) {  
                        // TODO Auto-generated catch block  
                        e.printStackTrace();  
                    } catch (InstantiationException e) {  
                        // TODO Auto-generated catch block  
                        e.printStackTrace();  
                    } catch (IllegalAccessException e) {  
                        // TODO Auto-generated catch block  
                        e.printStackTrace();  
                    }  
                }else{  
                    continue;  
                }  
                  
            }  
        }  
        return instance;  
    }  
}
