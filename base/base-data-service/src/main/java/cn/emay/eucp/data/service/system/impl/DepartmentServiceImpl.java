package cn.emay.eucp.data.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.department.DepartmentDTO;
import cn.emay.eucp.common.moudle.db.system.Department;
import cn.emay.eucp.data.dao.system.DepartmentDao;
import cn.emay.eucp.data.dao.system.UserDepartmentAssignDao;
import cn.emay.eucp.data.service.system.DepartmentService;

/**
 * cn.emay.eucp.common.moudle.db.system.Department Service implement
 *
 * @author frank
 */
@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentService {

	@Resource
	private DepartmentDao departmentDao;
	@Resource
	private UserDepartmentAssignDao userDepartmentAssignDao;

	@Override
	public Department findDepartmentById(Long departmentId) {
		return departmentDao.findById(departmentId);
	}

	@Override
	public List<Department> findByParentId(Long parentId,Long enterpriseId) {
		return departmentDao.findByParentId(parentId,enterpriseId);
	}
	@Override
	public List<Map<String, Object>> getDepartTree(Long parentId,Long enterpriseId){
		List<Map<String, Object>> departTree = departmentDao.getDepartTree(parentId,enterpriseId);
		boolean isParent = false;
		for (Map<String, Object> map : departTree) {
			String isTringParent = map.get("isParent").toString();
			if("true".equals(isTringParent)){
				isParent = true;
			}
			map.put("isParent", isParent);
		}
		return departTree;
	}
	@Override
	public Result addDepartment(Department department) {
		departmentDao.save(department);
		return Result.rightResult();
	}

	@Override
	public Long findDepartmentByName(String departmentName, Long id) {
		return departmentDao.findDepartmentByName(departmentName, id);
	}

	@Override
	public Page<DepartmentDTO> findDepartmentByLikeName(Long id, String departmentName,Long enterpriseId,int start, int limit) {
		Page<Department> page = departmentDao.findDepartmentByLikeName(id, departmentName,enterpriseId,start, limit);
		Page<DepartmentDTO> pagedto = new Page<DepartmentDTO>();
		List<DepartmentDTO> listdto = new ArrayList<DepartmentDTO>();
		List<Long> list=new ArrayList<Long>();
		if(page.getList().size()>0){
			for(Department dep : page.getList()){
				list.add(dep.getParentDepartmentId());
			}
			List<Department> depList=departmentDao.findByIds(list);
			Map<Long,String> map=new HashMap<Long,String>();
			for(Department depPar:depList){
				map.put(depPar.getId(), depPar.getDepartmentName());
			}
			for(Department dep : page.getList()){
				DepartmentDTO departmentDTO=new DepartmentDTO(dep, map.get(dep.getParentDepartmentId()));
				listdto.add(departmentDTO);
			}
			pagedto.setList(listdto);
		}else{
			pagedto.setList(null);
		}
		pagedto.setCurrentPageNum(page.getCurrentPageNum());
		pagedto.setLimit(page.getLimit());
		pagedto.setStart(page.getStart());
		pagedto.setTotalCount(page.getTotalCount());
		pagedto.setTotalPage(page.getTotalPage());
		return pagedto;
	}

	@Override
	public Result deleteDepartment(Long departmentId) {
		Department department = this.findDepartmentById(departmentId);
		if (department == null) {
			return Result.badResult("部门不存在");
		}
		department.setIsDelete(true);
		departmentDao.update(department);
		return Result.rightResult(department.getDepartmentName());
	}

	@Override
	public Result modifyDepartment(Department department) {
		departmentDao.update(department);
		return Result.rightResult();
	}

	@Override
	public Long findCountByParentId(Long parentId) {
		return departmentDao.findCountByParentId(parentId);
	}

	@Override
	public Long findCountById(Long id) {
		return departmentDao.findCountById(id);
	}

	@Override
	public List<Department> findByParentIds(List<Long> list) {
		return departmentDao.findByParentIds(list);
	}

	@Override
	public List<Long> findByPath(String str) {
		return departmentDao.findByPath(str);
	}

}