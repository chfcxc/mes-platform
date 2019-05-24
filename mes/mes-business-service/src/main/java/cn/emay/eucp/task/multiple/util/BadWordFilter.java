package cn.emay.eucp.task.multiple.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import cn.emay.common.EmayTreeNode;

public class BadWordFilter {

	private EmayTreeNode<Character, Boolean> root = new EmayTreeNode<Character, Boolean>('0', false);

	private char[] rules = {};

	public BadWordFilter(char[] rules, String[] keys) {
		this.rules = rules;
		Arrays.sort(rules);
		for (String key : keys) {
			char[] keychars = key.toCharArray();
			EmayTreeNode<Character, Boolean> node = root;
			for (char kc : keychars) {
				if (Arrays.binarySearch(rules, kc) > -1) {
					continue;
				}
				EmayTreeNode<Character, Boolean> child = node.getChild(kc);
				if (child == null) {
					child = new EmayTreeNode<Character, Boolean>(kc, false);
					node.addChild(child);
				}
				node = child;
			}
			node.setFruit(true);
		}
	}

	public BadWordFilter(char[] rules, Set<String> keys) {
		this.rules = rules;
		Arrays.sort(rules);
		for (String key : keys) {
			char[] keychars = key.toCharArray();
			EmayTreeNode<Character, Boolean> node = root;
			for (char kc : keychars) {
				if (Arrays.binarySearch(rules, kc) > -1) {
					continue;
				}
				EmayTreeNode<Character, Boolean> child = node.getChild(kc);
				if (child == null) {
					child = new EmayTreeNode<Character, Boolean>(kc, false);
					node.addChild(child);
				}
				node = child;
			}
			node.setFruit(true);
		}
	}

	public boolean filter(String content) {
		char[] chars = content.toCharArray();
		boolean isBad = false;
		for (int i = 0; i < chars.length; i++) {
			char ch = chars[i];
			if (Arrays.binarySearch(rules, ch) > -1) {
				continue;
			}
			EmayTreeNode<Character, Boolean> node = root.getChild(ch);
			if (node == null) {
				continue;
			}
			// String bad = "" + ch;
			boolean fruit = node.getFruit();
			for (int j = i + 1; j < chars.length; j++) {
				char cn = chars[j];
				if (Arrays.binarySearch(rules, cn) > -1) {
					continue;
				}
				node = node.getChild(cn);
				if (node == null) {
					break;
				} else {
					// bad += cn;
					fruit = node.getFruit();
					if (fruit) {
						break;
					}
				}
			}
			if (fruit) {
				isBad = true;
				break;
			} else {
				continue;
			}
		}
		return isBad;
	}

	public Set<String> filterBackDic(String content) {
		char[] chars = content.toCharArray();
		StringBuffer blackDic = new StringBuffer();
		EmayTreeNode<Character, Boolean> parent = null;
		Set<String> blackDicSet = new HashSet<String>();
		for (int i = 0; i < chars.length; i++) {
			char ch = chars[i];
			if (Arrays.binarySearch(rules, ch) > -1) {
				continue;
			}
			EmayTreeNode<Character, Boolean> node = root.getChild(ch);
			if (node == null) {
				continue;
			}
			boolean fruit = node.getFruit();
			if (fruit) {
				blackDic = new StringBuffer();
				blackDic.append(node.getId());
				blackDicSet.add(blackDic.toString());
				continue;
			}
			for (int j = i + 1; j < chars.length; j++) {
				char cn = chars[j];
				if (Arrays.binarySearch(rules, cn) > -1) {
					continue;
				}
				node = node.getChild(cn);
				if (node == null) {
					break;
				} else {
					fruit = node.getFruit();
					if (fruit) {
						blackDic = new StringBuffer();
						blackDic.append(node.getId());
						parent = node.getParent();
						while (null != parent && null != parent.getParent()) {
							blackDic.append(parent.getId());
							parent = parent.getParent();
						}
						blackDicSet.add(blackDic.reverse().toString());
						break;
					}
				}
			}
		}
		return blackDicSet;
	}

	public static void main(String[] args) throws IOException {
		// 间隔规则
		// char[] rules = ",.~!@#$%^&*(){}[]; '，:-_\\/".toCharArray();
		// // 读取敏感词组
		// String[] keys = { "010", "傻逼", "fuck", "共产党", "操你妈", "发票", "办证", "小姐", "找小姐", "姐妹" };
		// // 读取测试内容
		// String content = "sdfsdfsewwk0398g3io面覅哦操 你屄色戒让我既公司的放松id发觉我iriofmd找s找姐dfsdfsewwk0398g3io面覅哦色戒让我既-道fuck傻";
		// content = "a010b";
		// // 初始化
		// long time0 = System.nanoTime();
		// BadWordFilter f = new BadWordFilter(rules, keys);
		// System.out.println("time1:" + (System.nanoTime() - time0));
		// // 1000此过滤验证
		// long time1 = System.nanoTime();
		// boolean ok = f.filter(content);
		// System.out.println(ok);
		// System.out.println("time2:" + (System.nanoTime() - time1));
		// System.out.println(f.filterBackDic(content));
	}

}
