package cn.emay.eucp.common.dto.auth;

import java.util.ArrayList;
import java.util.List;

import cn.emay.eucp.common.moudle.db.system.Resource;

/**
 * 系统资源DTO
 * 
 * @author lijunjian
 *
 */
public class ResourceDTO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public final static String RESOURCE_TYPE_MOUDLE = "MOUDLE";// 模块
	public final static String RESOURCE_TYPE_NAV = "NAV";// 导航
	public final static String RESOURCE_TYPE_PAGE = "PAGE";// 页面
	public final static String RESOURCE_TYPE_OPER = "OPER";// 操作

	public final static String RESOURCE_FOR_MANAGE = "MANAGE";// 管理端
	public final static String RESOURCE_FOR_CLIENT = "CLIENT";// 客户端

	private Long id;
	private String resourceType;// 资源类型[模块-MOUDLE，导航-NAV，页面-PAGE，操作-OPER]
	private String resourceCode;// 资源CODE
	private String resourceName;// 资源名称
	private String resourcePath;// 资源路径
	private Integer resourceIndex;// 资源顺序
	private String resourceIcon;// 资源图标
	private Long parentResourceId;// 上级资源ID
	private String resourceFor;// 资源所属[MANAGE-管理端，CLIENT-客户端]

	private List<ResourceDTO> resource = new ArrayList<ResourceDTO>();// 所有资源id

	public ResourceDTO() {

	}

	public ResourceDTO(Resource resource) {
		this.id = resource.getId();
		this.resourceType = resource.getResourceType();
		this.resourceCode = resource.getResourceCode();
		this.resourceName = resource.getResourceName();
		this.resourcePath = resource.getResourcePath();
		this.resourceIndex = resource.getResourceIndex();
		this.resourceIcon = resource.getResourceIcon();
		this.parentResourceId = resource.getParentResourceId();
		this.resourceFor = resource.getResourceFor();
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

	public List<ResourceDTO> getResource() {
		return resource;
	}

	public void setResource(List<ResourceDTO> resource) {
		this.resource = resource;
	}
}