package cn.emay.eucp.data.service.fms.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypeDto;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypePage;
import cn.emay.eucp.common.moudle.db.fms.FmsBusinessType;
import cn.emay.eucp.data.dao.fms.FmsBusinessTypeDao;
import cn.emay.eucp.data.service.fms.FmsBusinessTypeService;

/** @author dejun
 * @version 创建时间：2019年4月26日 下午4:34:59 类说明 */
@Service("fmsBusinessTypeService")
public class FmsBusinessTypeServiceImpl implements FmsBusinessTypeService {
	@Resource
	private FmsBusinessTypeDao fmsBusinessTypeDao;

	@Override
	public List<FmsBusinessType> findBusinessName() {

		return fmsBusinessTypeDao.findBusinessName();
	}

	@Override
	public Page<FmsBusinesTypePage> findPage(Long busiId, int saveType, Long contentId, int start, int limit) {

		// List<FmsBusinessType> list = fmsBusinessTypeDao.findBusiName(businessTypeName);
		// List<FmsBusinesTypePage> listPage = new ArrayList<>();
		// for (FmsBusinessType fms : list) {
		// List<FmsBusinessType> list2 = fmsBusinessTypeDao.findContent(fms.getId(), content);
		// if (list2.size() > 0) {
		// for (FmsBusinessType fmsBusinessType : list2) {
		// FmsBusinesTypePage fmsBusinesTypePage = new FmsBusinesTypePage();
		// fmsBusinesTypePage.setBusiName(fms.getName());
		// fmsBusinesTypePage.setContentName(fmsBusinessType.getName());
		// fmsBusinesTypePage.setSaveType(fmsBusinessType.getSaveType());
		// listPage.add(fmsBusinesTypePage);
		// }
		// }
		// }
		return fmsBusinessTypeDao.findPage(busiId, saveType, contentId, start, limit);
	}

	@Override
	public void save(FmsBusinessType fmsBusinessType) {
		fmsBusinessTypeDao.save(fmsBusinessType);

	}

	@Override
	public void delete(Long id) {
		fmsBusinessTypeDao.delete(id);
	}

	@Override
	public List<FmsBusinessType> findBusiName(Long busiId) {
		return fmsBusinessTypeDao.findBusiName(busiId);
	}

	@Override
	public List<FmsBusinessType> findContent(Long contentId) {
		return fmsBusinessTypeDao.findContent(contentId);
	}

	@Override
	public void update(FmsBusinessType fmsBusinessType) {
		fmsBusinessTypeDao.update(fmsBusinessType);
	}

	@Override
	public List<FmsBusinesTypeDto> findIds(Set<Long> ids) {
		List<Object[]> list = fmsBusinessTypeDao.findIds(ids);
		List<FmsBusinesTypeDto> list2 = new ArrayList<FmsBusinesTypeDto>();
		if (list != null && list.size() > 0) {
			for (Object[] objects : list) {
				FmsBusinesTypeDto dto = new FmsBusinesTypeDto();
				dto.setId(Long.valueOf(objects[0].toString()));
				dto.setName(objects[1].toString());
				dto.setParentId(Long.valueOf(objects[2].toString()));
				dto.setSaveType(Integer.valueOf(objects[3].toString()));
				dto.setParentName(objects[4].toString());
				list2.add(dto);
			}
		}
		return list2;
	}

	@Override
	public List<FmsBusinessType> findList() {
		return fmsBusinessTypeDao.findAll();
	}

	@Override

	public List<FmsBusinessType> findByid(Set<Long> ids) {
		return fmsBusinessTypeDao.findById(ids);
	}

	@Override
	public List<FmsBusinessType> findByIdAndName(int saveType, String conntentName, String businessType) {
		return fmsBusinessTypeDao.findByIdAndName(saveType, conntentName, businessType);
	}

	@Override
	public FmsBusinessType findbyId(Long id) {
		return fmsBusinessTypeDao.findById(id);
	}

	@Override
	public FmsBusinessType findParentId(Long id) {
		return fmsBusinessTypeDao.findParentId(id);
	}

	@Override
	public List<FmsBusinessType> findContentByBusi(int saveType, Long busiId) {
		return fmsBusinessTypeDao.findContentByBusi(saveType, busiId);
	}

	@Override
	public FmsBusinesTypeDto findbyBusiness(Long id) {
		return fmsBusinessTypeDao.findbyBusiness(id);
	}

}
