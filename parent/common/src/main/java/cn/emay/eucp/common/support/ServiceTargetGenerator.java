package cn.emay.eucp.common.support;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import cn.emay.common.encryption.HexByte;
import cn.emay.eucp.common.excepiton.EucpRuntimeException;

/**
 * 应用唯一标识码生成器
 * 
 * @author Frank
 * 
 */
public class ServiceTargetGenerator {

	private static String target = null;

	/**
	 * 生成唯一应用码
	 * 
	 * @return
	 */
	public static String getServiceTarget() {
		if (target != null) {
			return target;
		}
		Set<String> ips = getAllIPAddress();
		if (ips == null || ips.size() == 0) {
			throw new EucpRuntimeException("server network ip address has no .");
		}
		String ipstr = "[";
		for (String ip : ips) {
			ipstr += ip + ",";
		}
		ipstr = ipstr.substring(0,ipstr.length() - 1) +  "]";
		String home = getJavaRunHome();
		target = ipstr + ":" + home;
		return target;

	}

	/**
	 * 获取当前应用运行目录
	 * 
	 * @return
	 */
	private static String getJavaRunHome() {
		return ServiceTargetGenerator.class.getClassLoader().getResource("").getPath();
	}

	/**
	 * 获取所有IP
	 * 
	 * @return
	 */
	private static Set<String> getAllIPAddress() {
		try {
			Set<String> list = new HashSet<String>();
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress ip = (InetAddress) addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address && !ip.getHostAddress().equals("127.0.0.1")) {
						list.add(ip.getHostAddress());
					}
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取所有MAC
	 * 
	 * @return
	 */
	protected static Set<String> getAllHardwareAddress() {
		try {
			Set<String> list = new HashSet<String>();
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress ia = (InetAddress) addresses.nextElement();
					byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
					if (null == mac) {
						continue;
					}
					String macStr = HexByte.byte2Hex(mac);
					if (macStr.length() == 12) {
						list.add(macStr);
					}
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
