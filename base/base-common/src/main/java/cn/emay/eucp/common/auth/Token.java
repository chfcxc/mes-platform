package cn.emay.eucp.common.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import cn.emay.eucp.common.moudle.db.system.Resource;
import cn.emay.eucp.common.moudle.db.system.User;

/**
 * 系统资源访问令牌<br/>
 * 
 * 令牌有使用期限；用户持令牌进入系统可访问令牌中的授权的系统资源;
 * 
 * @author Frank
 *
 */
public class Token {

	/**
	 * 令牌全局唯一ID
	 */
	private String id;

	/**
	 * 令牌所属用户
	 */
	private User user;

	/**
	 * 资源权限树
	 */
	private ResourceTreeNode tree;
	
	/**
	 * 能够访问的系统
	 */
	private Set<String> authSystems = new HashSet<String>();

	public Token(User user, List<Resource> systemResources, List<Resource> authResources) {
		this.setUser(user);
		this.setId(UUID.randomUUID().toString().toUpperCase().replace("-", ""));
		this.setTree(ResourceTreeNodeGenerater.genAuthTree(systemResources, authResources));
		for(Resource res : authResources){
			authSystems.add(res.getResourceFor());
		}
	}
	
	public Set<String> getResourceAuth() {
		return ResourceTreeNodeGenerater.genAuthSet(tree);
	}
	
	public boolean isAuthSystems(String system){
		return authSystems.contains(system);
	}

	public List<Resource> getAllResourceAuth() {
		return ResourceTreeNodeGenerater.genAllAuth(tree);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ResourceTreeNode getTree() {
		return tree;
	}

	public void setTree(ResourceTreeNode tree) {
		this.tree = tree;
	}

	

}
