package cn.emay.eucp.common.auth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.emay.eucp.common.moudle.db.system.Resource;

/**
 * 系统资源树生成器
 *
 * @author Frank
 *
 */
public class ResourceTreeNodeGenerater {

	/**
	 * 生成个人权限树
	 */
	public static ResourceTreeNode genAuthTree(List<Resource> systemResources, List<Resource> authResources) {
		sortSystemResources(systemResources);
		Set<Long> authids = genAuthResourceIds(authResources);
		ResourceTreeNode root = genBaseTree(systemResources, authids,true);
		reverseAuthTree(root);
		genUrltoNavAndMoudle(root);
		return root;
	}

	/**
	 * 排序
	 */
	private static void sortSystemResources(List<Resource> systemResources) {
		Collections.sort(systemResources, new Comparator<Resource>() {
			public int compare(Resource arg0, Resource arg1) {
				int i0 = arg0.getResourceIndex() == null ? 0 : arg0.getResourceIndex();
				int i1 = arg1.getResourceIndex() == null ? 0 : arg1.getResourceIndex();
				if (i0 > i1) {
					return 1;
				} else if (i0 < i1) {
					return -1;
				} else {
					return 0;
				}
			}
		});
	}

	/**
	 * 拿到所有的拥有权限资源ID
	 */
	private static Set<Long> genAuthResourceIds(List<Resource> authResources) {
		Set<Long> set = new HashSet<Long>();
		if(authResources != null){
			for (Resource r : authResources) {
				set.add(r.getId());
			}
		}
		return set;
	}

	/**
	 * 生成权限树[不包含无权限的资源]
	 */
	private static ResourceTreeNode genBaseTree(List<Resource> systemResources, Set<Long> authids,Boolean isNeedAuth) {
		ResourceTreeNode root = new ResourceTreeNode(new Resource());
		for (Resource resource : systemResources) {
			if (!resource.getResourceType().equals(Resource.RESOURCE_TYPE_MOUDLE)) {
				continue;
			}
			ResourceTreeNode node = new ResourceTreeNode(resource);
			root.addChild(node);
		}
		Map<Long, Long> navmoudlemap = new HashMap<Long, Long>();// 导航所属模块map
		for (Resource resource : systemResources) {
			if (!resource.getResourceType().equals(Resource.RESOURCE_TYPE_NAV)) {
				continue;
			}
			ResourceTreeNode moudlenode = root.getChild(resource.getParentResourceId());
			if (moudlenode == null) {
				continue;
			}
			moudlenode.addChild(new ResourceTreeNode(resource));
			navmoudlemap.put(resource.getId(), resource.getParentResourceId());
		}
		Map<Long, Long> pagenavmap = new HashMap<Long, Long>();// 页面所属导航map
		for (Resource resource : systemResources) {
			if (!resource.getResourceType().equals(Resource.RESOURCE_TYPE_PAGE)) {
				continue;
			}
			if(isNeedAuth && !authids.contains(resource.getId())){
				continue;
			}
			Long moudleId = navmoudlemap.get(resource.getParentResourceId());
			if (moudleId == null) {
				continue;
			}
			ResourceTreeNode moudlenode = root.getChild(moudleId);
			if (moudlenode == null) {
				continue;
			}
			ResourceTreeNode navnode = moudlenode.getChild(resource.getParentResourceId());
			if (navnode == null) {
				continue;
			}
			navnode.addChild(new ResourceTreeNode(resource));
			pagenavmap.put(resource.getId(), resource.getParentResourceId());
		}
		for (Resource resource : systemResources) {
			if (!resource.getResourceType().equals(Resource.RESOURCE_TYPE_OPER)) {
				continue;
			}
			if (isNeedAuth && !authids.contains(resource.getId())) {
				continue;
			}
			Long navId = pagenavmap.get(resource.getParentResourceId());
			if (navId == null) {
				continue;
			}
			Long moudleId = navmoudlemap.get(navId);
			if (moudleId == null) {
				continue;
			}
			ResourceTreeNode moudlenode = root.getChild(moudleId);
			if (moudlenode == null) {
				continue;
			}
			ResourceTreeNode navnode = moudlenode.getChild(navId);
			if (navnode == null) {
				continue;
			}
			ResourceTreeNode pagenode = navnode.getChild(resource.getParentResourceId());
			if (pagenode == null) {
				continue;
			}
			pagenode.addChild(new ResourceTreeNode(resource));
		}
		return root;
	}

	/**
	 * 反向推算权限
	 */
	private static void reverseAuthTree(ResourceTreeNode root) {
		for (ResourceTreeNode module : root.getChildren()) {
			if(null == module || null == module.getChildren()){
				continue;
			}
			for (ResourceTreeNode nav : module.getChildren()) {
				if (nav.isLeaf()) {
					module.removeChild(nav.getId());
				}
			}
		}
		for (ResourceTreeNode module : root.getChildren()) {
			if (module.isLeaf()) {
				root.removeChild(module.getId());
			}
		}
	}

	/**
	 * 将模块或者导航的第一个子资源URL赋值给自身
	 */
	private static void genUrltoNavAndMoudle(ResourceTreeNode root) {
		for (ResourceTreeNode module : root.getChildren()) {
			for (ResourceTreeNode nav : module.getChildren()) {
				nav.getResource().setResourcePath(nav.getChildren()[0].getResource().getResourcePath());
			}
		}
		for (ResourceTreeNode module : root.getChildren()) {
			module.getResource().setResourcePath(module.getChildren()[0].getResource().getResourcePath());
		}
	}

	/**
	 * 生成权限数组
	 */
	public static Set<String> genAuthSet(ResourceTreeNode root) {
		Set<String> resourceAuth = new HashSet<String>();
		if (!root.isLeaf()) {
			for (ResourceTreeNode moudle : root.getChildren()) {
				resourceAuth.add(moudle.getResource().getResourceCode());
				for (ResourceTreeNode nav : moudle.getChildren()) {
					resourceAuth.add(nav.getResource().getResourceCode());
					for (ResourceTreeNode page : nav.getChildren()) {
						resourceAuth.add(page.getResource().getResourceCode());
						if (!page.isLeaf()) {
							for (ResourceTreeNode oper : page.getChildren()) {
								resourceAuth.add(oper.getResource().getResourceCode());
							}
						}
					}
				}
			}
		}
		return resourceAuth;
	}
	
	/**
	 * 生成所有权限资源
	 */
	public static List<Resource> genAllAuth(ResourceTreeNode root){
		List<Resource> resourceAuth = new ArrayList<Resource>();
		if (!root.isLeaf()) {
			for (ResourceTreeNode moudle : root.getChildren()) {
				resourceAuth.add(moudle.getResource());
				for (ResourceTreeNode nav : moudle.getChildren()) {
					resourceAuth.add(nav.getResource());
					for (ResourceTreeNode page : nav.getChildren()) {
						resourceAuth.add(page.getResource());
						if (!page.isLeaf()) {
							for (ResourceTreeNode oper : page.getChildren()) {
								resourceAuth.add(oper.getResource());
							}
						}
					}
				}
			}
		}
		return resourceAuth;
	}
	
	/**
	 * 生成资源树，与权限无关
	 * @param systemResources
	 * @return
	 */
	public static ResourceTreeNode genResourceTree(List<Resource> systemResources) {
		sortSystemResources(systemResources);
		ResourceTreeNode root = genBaseTree(systemResources, null,false);
//		genUrltoNavAndMoudle(root);
		return root;
	}

}
