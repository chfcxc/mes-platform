package cn.emay.eucp.sale.web.controller.golable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.constant.GlobalConstants;
import cn.emay.eucp.common.moudle.db.system.Export;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.ExportService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.constant.CommonConstants;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 * 我的导出
 *
 */
@RequestMapping("/export")
@Controller
public class ExportController {

	@Resource
	private ExportService exportService;
	private Logger log = Logger.getLogger(ExportController.class);

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "golable/export";
	}

	/**
	 * 列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		User currentUser = WebUtils.getCurrentUser(request, response);
		int start = RequestUtils.getIntParameter(request, "start", GlobalConstants.DEFAULT_PAGE_START);
		int limit = RequestUtils.getIntParameter(request, "limit", GlobalConstants.DEFAULT_PAGE_LIMIT);
		Page<Export> page = exportService.findPage(start, limit, currentUser.getId(), CommonConstants.LOCAL_SYSTEM, null);
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}

	/**
	 * 下载
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/download")
	public void download(HttpServletRequest request, HttpServletResponse response) {
		InputStream fis = null;
		OutputStream os = null;
		try {
			Long id = RequestUtils.getLongParameter(request, "id", 0l);
			Export export = exportService.findById(id);
			User currentUser = WebUtils.getCurrentUser(request, response);
			if (null == export || export.getUserId().longValue() != currentUser.getId().longValue()) {
				ResponseUtils.outputWithJson(response, Result.badResult("参数不正确"));
				return;
			}
			File file = new File(export.getPath());
			fis = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[1024 * 10];
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=gb2312");
			response.addHeader("Content-Disposition", "attachment;filename=" + new String((file.getName()).getBytes("gb2312"), "iso8859-1"));
			response.addHeader("Content-Length", "" + file.length());
			os = new BufferedOutputStream(response.getOutputStream());
			int length;
			while ((length = fis.read(buffer)) != -1) {
				os.write(buffer, 0, length);
			}
			os.flush();
		} catch (Exception e) {
			log.error("下载异常:", e);
		} finally {
			try {
				if (null != fis) {
					fis.close();
				}
				if (null != os) {
					os.close();
				}
			} catch (IOException e) {
				log.error("下载异常:", e);
			}
		}
	}

}
