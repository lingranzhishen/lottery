package com.dhu.framework.cache;

import java.io.Serializable;

/**
 * 用于当结果为空时的缓存值
 * 防止缓存穿透
 * @author guofei
 *
 */
public final class EmptyCacheObject implements Serializable {

	private static final long serialVersionUID = 1143918617234601614L;

}
