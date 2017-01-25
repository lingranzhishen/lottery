package com.dhu.common.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;

import com.dhu.common.Constants;
import com.dhu.common.ResourceConfig;
import com.dhu.common.util.StringUtil;
import com.dhu.common.util.WebUtil;

/**
 * cas过滤器
 * @author lizehua
 *
 */
public class AuthorizeFilter implements Filter {
	/**
	 * The URL to the CAS Server login.
	 */
	private String casServerLoginUrl;
	/** Defines the parameter to look for for the artifact. */
	private String artifactParameterName = "ticket";

	/** Defines the parameter to look for for the service. */
	private String serviceParameterName = "service";

	/**
	 * Sets where response.encodeUrl should be called on service urls when
	 * constructed.
	 */
	private boolean encodeServiceUrl = true;

	/**
	 * The name of the server. Should be in the following format:
	 * {protocol}:{hostName}:{port}. Standard ports can be excluded.
	 */
	private String serverName;

	/** The exact url of the service. */
	private String service;

	/** Instance of commons logging for logging purposes. */
	protected final Log log = LogFactory.getLog(getClass());

	public String getArtifactParameterName() {
		return artifactParameterName;
	}

	public void setArtifactParameterName(String artifactParameterName) {
		this.artifactParameterName = artifactParameterName;
	}

	public String getServiceParameterName() {
		return serviceParameterName;
	}

	public void setServiceParameterName(String serviceParameterName) {
		this.serviceParameterName = serviceParameterName;
	}

	public boolean isEncodeServiceUrl() {
		return encodeServiceUrl;
	}

	public void setEncodeServiceUrl(boolean encodeServiceUrl) {
		this.encodeServiceUrl = encodeServiceUrl;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getCasServerLoginUrl() {
		return casServerLoginUrl;
	}

	public void setCasServerLoginUrl(String casServerLoginUrl) {
		this.casServerLoginUrl = casServerLoginUrl;
	}

	private String[] securitypassurls;

	private String securitypassurlStr;

	public AuthorizeFilter() {
		super();
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		securitypassurlStr = filterConfig.getInitParameter("securitypassurlStr");
		if (StringUtil.isNotEmpty(securitypassurlStr)) {
			securitypassurls = securitypassurlStr.split(",");
		}
		casServerLoginUrl = ResourceConfig.getString("cas.serverUrl") + "/login";
		serverName = ResourceConfig.getString("cas.clientUrl");
		return;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;
		String urls = request.getRequestURL().toString();
		String quesystr = request.getQueryString();
		if(!StringUtil.isEmpty(quesystr)){
			urls = urls + "?" + quesystr;
		}
		final String serviceUrl = constructServiceUrl(request, response, urls, serverName,
				artifactParameterName, true);
		final String ticket = WebUtil.findParameterValue(request, getArtifactParameterName());
		String dhu_token = WebUtil.getCookieByName(request, Constants.PC_TOKEN_NAME);// pc端token
		if (StringUtil.isNotEmpty(dhu_token)) {
			filterChain.doFilter(request, response);
			return;
		}

		if (StringUtil.isNotEmpty(ticket)) {
			filterChain.doFilter(request, response);
			return;
		}
		// 增加允许通过的url配置
		String thisurl = request.getRequestURI();
		if (securitypassurls != null) {
			for (String url : securitypassurls) {
				if (thisurl.contains(url)) {
					filterChain.doFilter(request, response);
					return;
				}
			}
		}
		// json格式数据
		if (thisurl.endsWith(".json")) {
			response.setHeader("Content-type", "application/json;charset=UTF-8");
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getOutputStream().write("{\"status\":\"fail\",\"message\":\"会话过期,请刷新页面重试\"}".getBytes("UTF-8"));
			return;
		}

		final String modifiedServiceUrl = serviceUrl;

		if (log.isDebugEnabled()) {
			log.debug("Constructed service url: " + modifiedServiceUrl);
		}

		final String urlToRedirectTo = constructRedirectUrl(this.casServerLoginUrl, getServiceParameterName(),
				modifiedServiceUrl);

		if (log.isDebugEnabled()) {
			log.debug("redirecting to \"" + urlToRedirectTo + "\"");
		}

		response.sendRedirect(urlToRedirectTo);
	}

	public String constructRedirectUrl(String casServerLoginUrl2, String string, String serviceUrl) {
		try {
			return casServerLoginUrl + (casServerLoginUrl.indexOf("?") != -1 ? "&" : "?") + serviceParameterName + "="
					+ URLEncoder.encode(serviceUrl, "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 构建请求service
	 * @param request
	 * @param response
	 * @param service
	 * @param serverName
	 * @param artifactParameterName
	 * @param encode
	 * @return
	 */
	public static String constructServiceUrl(final HttpServletRequest request, final HttpServletResponse response,
			final String service, final String serverName, final String artifactParameterName, final boolean encode) {
		if (StringUtil.isNotBlank(service)) {
			return encode ? response.encodeURL(service) : service;
		}

		final StringBuilder buffer = new StringBuilder();

		if (!serverName.startsWith("https://") && !serverName.startsWith("http://")) {
			buffer.append(request.isSecure() ? "https://" : "http://");
		}

		buffer.append(serverName);
		buffer.append(request.getRequestURI());

		if (StringUtil.isNotBlank(request.getQueryString())) {
			final int location = request.getQueryString().indexOf(artifactParameterName + "=");

			if (location == 0) {
				final String returnValue = encode ? response.encodeURL(buffer.toString()) : buffer.toString();
				return returnValue;
			}

			buffer.append("?");

			if (location == -1) {
				buffer.append(request.getQueryString());
			} else if (location > 0) {
				final int actualLocation = request.getQueryString().indexOf("&" + artifactParameterName + "=");

				if (actualLocation == -1) {
					buffer.append(request.getQueryString());
				} else if (actualLocation > 0) {
					buffer.append(request.getQueryString().substring(0, actualLocation));
				}
			}
		}

		final String returnValue = encode ? response.encodeURL(buffer.toString()) : buffer.toString();
		return returnValue;
	}

	@Override
	public void destroy() {
	}

}
