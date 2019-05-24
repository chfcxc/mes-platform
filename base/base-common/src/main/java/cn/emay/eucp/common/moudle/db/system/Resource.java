package cn.emay.eucp.common.moudle.db.system;

/**
 * 系统资源
 * 
 * @author Frank
 *
 */
public class Resource implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public final static String RESOURCE_TYPE_MOUDLE = "MOUDLE";// 模块
	public final static String RESOURCE_TYPE_NAV = "NAV";// 导航
	public final static String RESOURCE_TYPE_PAGE = "PAGE";// 页面
	public final static String RESOURCE_TYPE_OPER = "OPER";// 操作

	//TODO
	public final static String RESOURCE_CLIENT_SERVICECODE = "SYS_CLIENT_SERVICECODE";// 服务号(默认所有角色拥有此权限)

	private Long id;
	private String resourceType;// 资源类型[模块-MOUDLE，导航-NAV，页面-PAGE，操作-OPER]
	private String resourceCode;// 资源CODE
	private String resourceName;// 资源名称
	private String resourcePath;// 资源路径
	private Integer resourceIndex;// 资源顺序
	private String resourceIcon;// 资源图标
	private Long parentResourceId;// 上级资源ID
	private String resourceFor;// 资源所属系统 EucpSystem
	private String resourceForCustomer;// 归属客户类型：0：普通客户；1：代理商
	private String businessType;// 业务类型

	public Resource() {

	}

	public Resource(Resource resource) {
		this.id = resource.getId();
		this.resourceType = resource.resourceType;
		this.resourceCode = resource.resourceCode;
		this.resourceName = resource.resourceName;
		this.resourcePath = resource.resourcePath;
		this.resourceIndex = resource.resourceIndex;
		this.resourceIcon = resource.resourceIcon;
		this.parentResourceId = resource.parentResourceId;
		this.resourceFor = resource.resourceFor;
	}

	public String getResourceType() {
		return this.resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceCode() {
		return this.resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public String getResourceName() {
		return this.resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourcePath() {
		return this.resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public Integer getResourceIndex() {
		return this.resourceIndex;
	}

	public void setResourceIndex(Integer resourceIndex) {
		this.resourceIndex = resourceIndex;
	}

	public String getResourceIcon() {
		return this.resourceIcon;
	}

	public void setResourceIcon(String resourceIcon) {
		this.resourceIcon = resourceIcon;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentResourceId() {
		return parentResourceId;
	}

	public void setParentResourceId(Long parentResourceId) {
		this.parentResourceId = parentResourceId;
	}

	public String getResourceFor() {
		return resourceFor;
	}

	public void setResourceFor(String resourceFor) {
		this.resourceFor = resourceFor;
	}

	public String getResourceForCustomer() {
		return resourceForCustomer;
	}

	public void setResourceForCustomer(String resourceForCustomer) {
		this.resourceForCustomer = resourceForCustomer;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

}