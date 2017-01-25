package com.dhu.framework.jobmanage;

import com.dhu.framework.jobmanage.dto.ResultDTO;
import com.dhu.framework.jobmanage.dto.TaskDispatchDTO;

public interface ITaskDispatchFacade {
	
	/**
	 * 执行结束提交
	 * @param taskdto
	 * @return
	 */
	ResultDTO getTaskLock(TaskDispatchDTO taskdto);
	
	ResultDTO commitComlete(TaskDispatchDTO taskdto);
	
	ResultDTO actionError(TaskDispatchDTO taskdto,String errorMsg);
	
}
