package com.dhu.framework.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 系统帮助类工具方法
 * <p/>
 *
 * @author guofei 2013-12-24
 */
public class IPUtil {

    private final static Logger logger = LoggerFactory.getLogger(IPUtil.class);

    private static Set<String> localIpSet = null;
    
    static{
    	localIpSet = getLocalIps();
    }
    /**
     * 获取当前服务器的IP
     *
     * @return 服务器IP
     */
    public static String getLocalIp() {
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            logger.warn(e.getMessage(), e);
        }
        return ip != null ? ip.getHostAddress() : "";
    }

    
    public static Set<String> getLocalIpSet(){
    	return localIpSet;
    }
    
    /**
     * 获得本机IP地址 集合 （多网卡多ip的情况）
     *
     * @return Set<String>
     */
    private static Set<String> getLocalIps() {
        Set<String> ips = new HashSet<String>();
        Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
        try {
            Enumeration<NetworkInterface> networkList = NetworkInterface.getNetworkInterfaces();
            while (networkList.hasMoreElements()) {
                Enumeration<InetAddress> list = networkList.nextElement().getInetAddresses();
                while (list.hasMoreElements()) {
                    String ip = list.nextElement().getHostAddress();
                    if (pattern.matcher(ip).matches() && !"127.0.0.1".equals(ip)) {
                    	ips.add(ip);
                    }
                }
            }
        } catch (SocketException e) {
            logger.warn(e.getMessage(), e);
        }
        return ips;
    }
    
    /**
     * 获取请求客户端的真实IP地址
     * 像移动网关一样，iisforward这个ISAPI过滤器也会对request对象进行再包装，
     * 附加一些WLS要用的头信息。
     * 这种情况下，直接用request.getRemoteAddr() 是无法取到真正的客户IP的
     *
     * @return 客户端IP
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String ip = request.getHeader("Cdn-Src-Ip");//增加CDN获取ip
        if (isUnknown(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (isUnknown(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (isUnknown(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isUnknown(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isUnknown(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (isUnknown(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (isUnknown(ip)) {
                ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    private static boolean isUnknown(String ip){
    	return (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip));
    }
    
}
