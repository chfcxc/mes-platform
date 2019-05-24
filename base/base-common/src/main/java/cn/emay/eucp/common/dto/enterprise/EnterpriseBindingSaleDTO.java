package cn.emay.eucp.common.dto.enterprise;

import java.io.Serializable;

public class EnterpriseBindingSaleDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long systemEnterpriseId;// 企业ID
	private Long systemUserId;// 销售人员ID

	public EnterpriseBindingSaleDTO (){
		 
	}
	
	public EnterpriseBindingSaleDTO (Long systemEnterpriseId,Long systemUserId){
		 this.systemEnterpriseId = systemEnterpriseId;
		 this.systemUserId = systemUserId;
	}
	
	public Long getSystemEnterpriseId() {
		return systemEnterpriseId;
	}

	public void setSystemEnterpriseId(Long systemEnterpriseId) {
		this.systemEnterpriseId = systemEnterpriseId;
	}

	public Long getSystemUserId() {
		return systemUserId;
	}

	public void setSystemUserId(Long systemUserId) {
		this.systemUserId = systemUserId;
	}

}
