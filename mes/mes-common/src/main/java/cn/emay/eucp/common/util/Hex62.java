package cn.emay.eucp.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Hex62 {

	public static String to62(long number) {
		if (number <= 0) {
			return "0";
		}
		Long rest = number;
		Stack<Character> stack = new Stack<Character>();
		StringBuilder result = new StringBuilder();
		while (rest != 0) {
			Long res = rest / Hex62Common.HEX_62;
			int env = new Long(rest % Hex62Common.HEX_62).intValue();
			Character cha = Hex62Common.getCharIndex(env);
			stack.add(cha);
			rest = res;
		}
		while (!stack.isEmpty()) {
			result.append(stack.pop());
		}
		return result.toString();
	}

	public static long to10(String hex62) {
		if (hex62 == null || hex62.trim().length() == 0) {
			return 0L;
		}
		long result = 0L;
		char[] chars = hex62.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char cha = chars[i];
			Integer ind = Hex62Common.getIndexChar(cha);
			if (ind == null) {
				return 0L;
			}
			result += Math.pow(Hex62Common.HEX_62, chars.length - i - 1) * ind;
		}
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		System.out.println("start" + System.currentTimeMillis());
//		for(long i = 0l; i < 99999999999l; i ++){
//			String hex62 = to62(i);
//			long end = to10(hex62);
//			if(i != end){
//				System.out.println(i);
//			}
//		}
//		System.out.println("end" + System.currentTimeMillis());
		
		long number = 9999999999L;
		String hex62 = to62(number);
		long end = to10("ZZZZ");
		System.out.println(number);
		System.out.println(hex62);
		System.out.println(end);
	}

}

class Hex62Common {

	public final static long HEX_62;

	private final static char[] CHARS_62;

	private final static Integer[] INTS_62;

	private final static Map<Character, Integer> MAP;

	static {
		CHARS_62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
				.toCharArray();
		HEX_62 = CHARS_62.length;
		INTS_62 = new Integer[CHARS_62.length];
		MAP = new HashMap<Character, Integer>();
		for (int i = 0; i < CHARS_62.length; i++) {
			INTS_62[i] = i;
			MAP.put(CHARS_62[i], i);
		}
	}

	public static Character getCharIndex(int index) {
		return CHARS_62[index];
	}

	public static Integer getIndexChar(char cha) {
		return MAP.get(cha);
	}

}