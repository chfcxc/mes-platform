package cn.emay.eucp.common.moudle.db.system;

/**
 * 
 * 客户权限
 * 
 */
public class EnterpriseAuth implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public static final int AUTH_LEVEL_BASIC = 0;// 基础版
	public static final int AUTH_LEVEL_PROFESSIONAL = 1;// 专业版
	public static final int AUTH_LEVEL_PREMIUM = 2;// 高级版
	
	public static final String AUTH_TYPE_PAGE = "PAGE";// 页面
	public static final String AUTH_TYPE_COLUMNS = "COLUMNS";//列
	
	public static final String FOR_SERVICE_SMS = "SMS";// 短信服务
	public static final String FOR_SERVICE_SYS = "SYS";//系统服务
	public static final String FOR_SERVICE_FLOW = "FLOW";//流量服务

	private Long id;
	private String authCode;// 权限CODE
	private String parentAuthCode;// 上级权限CODE
	private String authType;// 权限类型[PAGE-页面，COLUMNS-列]
	private Integer authLevel;// 权限级别[0-基础版,1-专业版,2-高级版]
	private String forService;// 所属服务[SMS-短信,SYS-系统,FLOW-流量]

	public EnterpriseAuth(){
		
	}
	
	public EnterpriseAuth(String authCode,String parentAuthCode,String authType,String forService){
		this.authCode=authCode;
		this.parentAuthCode=parentAuthCode;
		this.authType=authType;
		this.forService=forService;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getParentAuthCode() {
		return parentAuthCode;
	}

	public void setParentAuthCode(String parentAuthCode) {
		this.parentAuthCode = parentAuthCode;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public Integer getAuthLevel() {
		return authLevel;
	}

	public void setAuthLevel(Integer authLevel) {
		this.authLevel = authLevel;
	}

	public String getForService() {
		return forService;
	}

	public void setForService(String forService) {
		this.forService = forService;
	}

}
