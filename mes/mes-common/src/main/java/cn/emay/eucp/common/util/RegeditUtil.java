package cn.emay.eucp.common.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.emay.eucp.common.dto.fms.serviceCode.SimpleFmsServiceCodeDTO;

/**
 * 注册号实现的工具类
 *
 * @author liwei
 *
 */
public class RegeditUtil {
	private static final int[] SERVICECODE_NET_FIRST = new int[] { 6, 7, 8 }; // 网络版本
	private static final int[] SERVICECODE_LOCAL_FIRST = new int[] { 0, 1, 2, 3, 4, 5, 9 }; // 单机版本

	// 网络版第二位 所对应的特服号位数
	private static final Map<String, Integer> NET_LENGTH_SECOND_MAP = new ConcurrentHashMap<String, Integer>();
	// 网络版第一位集合
	private static final Set<String> NET_FIRST_MAP = new HashSet<String>();

	static {
		NET_LENGTH_SECOND_MAP.put("1", 3);
		NET_LENGTH_SECOND_MAP.put("5", 3);
		NET_LENGTH_SECOND_MAP.put("9", 3);

		NET_LENGTH_SECOND_MAP.put("2", 4);
		NET_LENGTH_SECOND_MAP.put("6", 4);

		NET_LENGTH_SECOND_MAP.put("3", 5);
		NET_LENGTH_SECOND_MAP.put("7", 5);

		NET_LENGTH_SECOND_MAP.put("0", 6);
		NET_LENGTH_SECOND_MAP.put("4", 6);
		NET_LENGTH_SECOND_MAP.put("8", 6);

		for (int i : SERVICECODE_NET_FIRST) {
			NET_FIRST_MAP.add(String.valueOf(i));
		}

	}

	/**
	 * 解析上行的 特服号与扩展码组合体
	 *
	 * @param serviceCodeAndExtendCode
	 * @return {特服号,扩展码}
	 */
	public static String[] praseServiceCodeAndExtendCode(String serviceCodeAndExtendCode) {
		// 如果是3位以下的，肯定不是网络版
		if (serviceCodeAndExtendCode == null || serviceCodeAndExtendCode.length() < 3) {
			return new String[] { serviceCodeAndExtendCode, null };
		}
		String first = serviceCodeAndExtendCode.substring(0, 1);
		boolean isNet = NET_FIRST_MAP.contains(first);
		if (isNet) {
			// 如果是网络版，继续判断第二位确定特服号位数
			String serviceCode = null;
			String extendCode = null;
			String second = serviceCodeAndExtendCode.substring(1, 2);
			int length = NET_LENGTH_SECOND_MAP.get(second);
			if (serviceCodeAndExtendCode.length() <= length) {
				serviceCode = serviceCodeAndExtendCode;
			} else {
				serviceCode = serviceCodeAndExtendCode.substring(0, length);
				extendCode = serviceCodeAndExtendCode.substring(length, serviceCodeAndExtendCode.length());
			}
			return new String[] { serviceCode, extendCode };
		} else {
			// 如果不是网络版，不支持上行待扩展码
			return new String[] { serviceCodeAndExtendCode, null };
		}
	}

	/**
	 * 判断特服号是否是网络版
	 *
	 * @param serviceCode
	 * @return
	 */
	public static boolean checkNetServiceCodes(String serviceCode) {
		if ((serviceCode.startsWith("6") || serviceCode.startsWith("7") || serviceCode.startsWith("8")) && serviceCode.length() > 1) {
			char sec = serviceCode.charAt(1); // 第二位
			if (serviceCode.length() == 6) {
				if (sec == '0' || sec == '4' || sec == '8') {
					return true;
				}
			} else if (serviceCode.length() == 5) {
				if (sec == '3' || sec == '7') {
					return true;
				}
			} else if (serviceCode.length() == 4) {
				if (sec == '2' || sec == '6') {
					return true;
				}
			} else if (serviceCode.length() == 3) {
				if (sec == '1' || sec == '5' || sec == '9') {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断特服号是否是网络版
	 *
	 * @param serviceCode
	 * @return 单机版0、网络版1
	 */
	public static int checkServiceCodeType(String serviceCode) {
		if ((serviceCode.startsWith("6") || serviceCode.startsWith("7") || serviceCode.startsWith("8")) && serviceCode.length() > 1) {
			char sec = serviceCode.charAt(1); // 第二位
			if (serviceCode.length() == 6) {
				if (sec == '0' || sec == '4' || sec == '8') {
					return 1;
				}
			} else if (serviceCode.length() == 5) {
				if (sec == '3' || sec == '7') {
					return 1;
				}
			} else if (serviceCode.length() == 4) {
				if (sec == '2' || sec == '6') {
					return 1;
				}
			} else if (serviceCode.length() == 3) {
				if (sec == '1' || sec == '5' || sec == '9') {
					return 1;
				}
			}
		}
		return 0;
	}

	/**
	 * 批量生成Appid
	 *
	 * @param size
	 * @return
	 */
	// public synchronized static List<String> generAppids(SimpleSmsServiceCodeDTO scDTO) {
	// List<String> sns = new ArrayList<String>();
	// for (int i = 0; i < scDTO.getRangeSize(); i++) { // 不论要生成多少，SN都预生成10倍
	// StringBuilder serial = new StringBuilder();
	// // String section4 = maskSNSerialNo(getRandomDegital(5));
	// String section4 = getNumbersAndLettersRandom(5).toUpperCase();// 随机生成5位的字母数字
	// serial.append(scDTO.getSnType().toUpperCase()).append("-");
	// serial.append(scDTO.getAgentAbbr().toUpperCase()).append("-");
	// serial.append(scDTO.getVersion()).append("-").append(section4);
	// sns.add(serial.toString());
	// }
	// return sns;
	// }

	public static String getNumbersAndLettersRandom(int length) {
		if (length < 2) {
			throw new IllegalArgumentException("长度不能小于2");
		}
		// 数组，用于存放随机字符
		char[] chArr = new char[length];
		// 为了保证必须包含数字、字母
		Random random = new Random();
		chArr[0] = (char) ('0' + random.nextInt(10));
		char tempChar = random.nextInt(2) % 2 == 0 ? 'A' : 'a';
		chArr[1] = (char) (tempChar + random.nextInt(26));
		char[] codes = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
				'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
		// charArr[2..length-1]随机生成codes中的字符
		for (int i = 2; i < length; i++) {
			chArr[i] = codes[random.nextInt(codes.length)];
		}
		// 将数组chArr随机排序
		for (int i = 0; i < length; i++) {
			int r = i + random.nextInt(length - i);
			char temp = chArr[i];
			chArr[i] = chArr[r];
			chArr[r] = temp;
		}
		return new String(chArr);
	}

	/**
	 * 获得一个随机number位数字串
	 *
	 * @return String
	 */
	protected static String getRandomDegital(int number) {
		Random rand = new Random();
		int tmp;
		String digitRandom = "";
		for (int i = 0; i < number; i++) {
			tmp = rand.nextInt(10);
			digitRandom += tmp;
		}
		return digitRandom;
	}

	/**
	 * 生成服务代码
	 *
	 * @param numberSize
	 * @return
	 */
	public static String getServiceCode(int numberSize, int sdkType) {
		if (numberSize < 1 || numberSize > 6) {
			return null;
		}
		if (sdkType == 1 && numberSize < 3) {
			// 网络版的位数不能小于3位数，原因自己看规则文档去
			return null;
		}
		String result = "";
		int firstNumber = getFirstNumber(sdkType);
		StringBuilder lastNumber = new StringBuilder();
		if (firstNumber < 0) {
			return null;
		}
		Random rand = new Random();
		try {
			switch (sdkType) {
			case 0:
				// ---单机版
				for (int len = 1; len < numberSize; len++) {
					lastNumber.append(String.valueOf(rand.nextInt(10)));
				}
				result = firstNumber + lastNumber.toString();
				break;
			case 1:
				// ---网络版
				int sencord = getNetSecondNumber(numberSize);
				if (sencord < 0) {
					return null;
				}
				for (int len = 2; len < numberSize; len++) {
					lastNumber.append(String.valueOf(rand.nextInt(10)));
				}
				result = firstNumber + String.valueOf(sencord) + lastNumber.toString();
				break;
			default:
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

	// ----------------------------------------------------------------
	/**
	 * 根据需要生成的服务代码的个数来生成第二位
	 */
	private static int getNetSecondNumber(int numberSize) {
		int size = numberSize - 2;
		Random random = new Random();
		switch (size) {
		case 1:
			int[] temp1 = new int[] { 1, 5, 9 };
			return temp1[random.nextInt(temp1.length)];
		case 2:
			int[] temp2 = new int[] { 2, 6 };
			return temp2[random.nextInt(temp2.length)];
		case 3:
			int[] temp3 = new int[] { 3, 7 };
			return temp3[random.nextInt(temp3.length)];
		case 4:
			int[] temp4 = new int[] { 0, 4, 8 };
			return temp4[random.nextInt(temp4.length)];
		default:
			return -1;
		}
	}

	private static int getFirstNumber(int sdkType) {
		Random random = new Random();
		int type = 0;

		try {
			switch (sdkType) {
			case 0:
				// ---单机版
				type = random.nextInt(SERVICECODE_LOCAL_FIRST.length);
				return SERVICECODE_LOCAL_FIRST[type];
			case 1:
				// ---网络版
				type = random.nextInt(SERVICECODE_NET_FIRST.length);
				return SERVICECODE_NET_FIRST[type];
			default:
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 将软件序列号的序号部分进行掩码加密。 METONE的前5位(METON)作为掩码 即序列号的后5位序号每位分别与METON的每位的ASCII码相加， 然后减3（保证此整数可转化为大写字母字符）， 最后转为字符。
	 *
	 * @param serial
	 *            软件序列号的序号部分，为5位字符串。
	 * @return 返回经过mask的软件序列号的序号部分，为5位字符串。
	 */
	protected static String maskSNSerialNo(String serial) {
		String maskSerial = null;
		maskSerial = String.valueOf((char) (Integer.parseInt(serial.substring(0, 1)) - 3 + 'M')) + String.valueOf((char) (Integer.parseInt(serial.substring(1, 2)) - 3 + 'E'))
				+ String.valueOf((char) (Integer.parseInt(serial.substring(2, 3)) - 3 + 'T')) + String.valueOf((char) (Integer.parseInt(serial.substring(3, 4)) - 3 + 'O'))
				+ String.valueOf((char) (Integer.parseInt(serial.substring(4)) - 3 + 'N'));
		return maskSerial;
	}

	/**
	 * 生成Appid
	 *
	 * @param size
	 * @return
	 */
	public synchronized static String generAppid(SimpleFmsServiceCodeDTO scDTO) {
		StringBuilder serial = new StringBuilder();
		String section4 = getNumbersAndLettersRandom(5).toUpperCase();// 随机生成5位的字母数字
		serial.append(scDTO.getSnType().toUpperCase()).append("-");
		serial.append(scDTO.getAgentAbbr().toUpperCase()).append("-");
		serial.append(scDTO.getVersion()).append("-").append(section4);
		return serial.toString();
	}

}
