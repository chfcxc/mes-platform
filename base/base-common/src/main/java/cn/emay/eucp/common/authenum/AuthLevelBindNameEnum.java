package cn.emay.eucp.common.authenum;

/**
 * 枚举类型
 * @author Administrator
 *
 */
public enum AuthLevelBindNameEnum {  
    BASIC("基础版", 0), PROFESSIONAL("专业版", 1), ADVANCED("高级版", 2), CUSTOM("定制版", 3); 
	
    private String name; 
    
    private int index;  
    
    private AuthLevelBindNameEnum(String name, int index) {  
        this.name = name;  
        this.index = index;  
    }  
    
    public String getName() {  
        return name;  
    }  
    
    public void setName(String name) {  
        this.name = name;  
    }  
    
    public int getIndex() {  
        return index;  
    }  
    
    public void setIndex(int index) {  
        this.index = index;  
    }  
    
    public static String getName(int index) {  
        for (AuthLevelBindNameEnum c : AuthLevelBindNameEnum.values()) {  
            if (c.getIndex() == index) {  
                return c.name;  
            }  
        }  
        return null;  
    }
}