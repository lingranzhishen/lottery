package com.dhu.common;

public class Constants {

	public static final String SESSION_USER = "session_user";
	public static final long JSVERSION = System.currentTimeMillis();

	// development | test | production
	public static final String PRODUCTION = "production";
	public static final String TEST = "test";
	public static final String DEVELOPMENT = "development";

	public static final String STATUS_OK = "ok";
	public static final String STATUS_FAIL = "fail";

	public static final String DEFAULT_CITY = "sh";// 系统默认的城市
	public static final String ALL_CITY = "sh";// 系统所有上线的城市

	public static final String HEAD_FUFFIX = "_150x200.jpg";// 头像的后缀
	public static final String PC_TOKEN_NAME = "dhu_token";

	public static final int MAX_ODD_MISS = 10;// 最大奇数遗漏
	public static final int MAX_EVEN_MISS = 10;// 最大奇数遗漏
	public static final int MAX_BIG_MISS = 10;// 最大大遗漏
	public static final int MAX_SMALL_MISS = 10;// 最大小遗漏
	public static final int EVEN_TYPE=0;
	public static final int ODD_TYPE=1;
	public static final int SMALL_TYPE=4;
	public static final int BIG_TYPE=5;
	public static final int[] MISS_TYPE={EVEN_TYPE,ODD_TYPE,SMALL_TYPE,BIG_TYPE};
	public static final String[] DIGIT_NAME={"个","十","百","千","万"};
}
