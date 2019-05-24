package cn.emay.eucp.common.auth;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.emay.eucp.common.moudle.db.system.Resource;

/**
 * 系统资源树节点
 * 
 * @author Frank
 *
 */
public class ResourceTreeNode {

	/**
	 * 资源code
	 */
	private Long id;

	/**
	 * 资源
	 */
	private Resource resource;

	/**
	 * 子节点
	 */
	private Map<Long, ResourceTreeNode> children;

	public ResourceTreeNode(Resource resource) {
		this.resource = resource;
		this.id = resource.getId();
	}

	public boolean isLeaf() {
		return children == null || children.size() == 0;
	}

	public Long getId() {
		return id;
	}

	public Resource getResource() {
		return resource;
	}

	public ResourceTreeNode getChild(Long id) {
		if (children == null) {
			return null;
		}
		return children.get(id);
	}

	public ResourceTreeNode[] getChildren() {
		if (children == null) {
			return null;
		}
		return children.values().toArray(new ResourceTreeNode[children.size()]);
	}

	protected void removeChild(Long id) {
		children.remove(id);
	}

	protected void removeChild(ResourceTreeNode node) {
		children.remove(node.getId());
	}

	protected void addChild(ResourceTreeNode node) {
		if (children == null) {
			children = new LinkedHashMap<Long, ResourceTreeNode>();
		}
		children.put(node.getId(), node);
	}

}
