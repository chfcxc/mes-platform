package cn.emay.eucp.common.dto.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.emay.eucp.common.moudle.db.system.Resource;

public class AuthTree implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ResourceDTO> ngvs = new ArrayList<ResourceDTO>();

	public AuthTree() {

	}

	public AuthTree(List<Resource> opers, List<Resource> pages, List<Resource> navigations, List<Resource> moduleTions) {

		Map<Long, List<ResourceDTO>> operdtos = new LinkedHashMap<Long, List<ResourceDTO>>();
		if (opers != null) {
			for (Resource oper : opers) {
				ResourceDTO operdto = new ResourceDTO(oper);
				if (operdtos.containsKey(oper.getParentResourceId())) {
					operdtos.get(oper.getParentResourceId()).add(operdto);
				} else {
					List<ResourceDTO> list = new ArrayList<ResourceDTO>();
					list.add(operdto);
					operdtos.put(oper.getParentResourceId(), list);
				}
			}
		}

		Map<Long, List<ResourceDTO>> pagedtos = new LinkedHashMap<Long, List<ResourceDTO>>();
		Map<String, String> pageIdMap = new HashMap<String, String>();
		if (pages != null) {
			Iterator<Resource> iterator = pages.iterator();
			while (iterator.hasNext()) {
				Resource page = iterator.next();
				if (pageIdMap.containsKey(String.valueOf(page.getId()))) {
					iterator.remove();
					continue;
				}
				pageIdMap.put(String.valueOf(page.getId()), "");
				ResourceDTO pagedto = new ResourceDTO(page);
				pagedto.setResource(operdtos.get(page.getId()));
				if (pagedtos.containsKey(page.getParentResourceId())) {
					pagedtos.get(page.getParentResourceId()).add(pagedto);
				} else {
					List<ResourceDTO> list = new ArrayList<ResourceDTO>();
					list.add(pagedto);
					pagedtos.put(page.getParentResourceId(), list);
				}
			}
		}

		/*
		 * List<ResourceDTO> ngvdtos = new ArrayList<ResourceDTO>(); if
		 * (navigations != null) { for (Resource pagedto : navigations) {
		 * ResourceDTO page = new ResourceDTO(pagedto); List<ResourceDTO>
		 * pageInNav = pagedtos.get(pagedto.getId()); if (pageInNav != null) {
		 * page.setResource(pageInNav); ngvdtos.add(page); } } }
		 */

		Map<Long, List<ResourceDTO>> ngvdtos = new LinkedHashMap<Long, List<ResourceDTO>>();
		Map<String, String> ngvMap = new HashMap<String, String>();
		if (navigations != null) {
			Iterator<Resource> iterator = navigations.iterator();
			while (iterator.hasNext()) {
				Resource ngve = iterator.next();
				if (ngvMap.containsKey(String.valueOf(ngve.getId()))) {
					iterator.remove();
					continue;
				}
				ngvMap.put(String.valueOf(ngve.getId()), "");
				ResourceDTO ngvedto = new ResourceDTO(ngve);
				ngvedto.setResource(pagedtos.get(ngve.getId()));
				if (ngvdtos.containsKey(ngve.getParentResourceId())) {
					ngvdtos.get(ngve.getParentResourceId()).add(ngvedto);
				} else {
					List<ResourceDTO> list = new ArrayList<ResourceDTO>();
					list.add(ngvedto);
					ngvdtos.put(ngve.getParentResourceId(), list);
				}
			}
		}

		List<ResourceDTO> moduldtos = new ArrayList<ResourceDTO>();
		if (moduleTions != null) {
			for (Resource moduldto : moduleTions) {
				ResourceDTO module = new ResourceDTO(moduldto);
				List<ResourceDTO> moduleInNav = ngvdtos.get(moduldto.getId());
				if (moduleInNav != null) {
					module.setResource(moduleInNav);
					moduldtos.add(module);
				}
			}
		}

		this.ngvs = moduldtos;
	}

	public List<ResourceDTO> getNgvs() {
		return ngvs;
	}

	public void setNgvs(List<ResourceDTO> ngvs) {
		this.ngvs = ngvs;
	}

}
