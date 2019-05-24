package cn.emay.eucp.task.multiple.dto;

import java.util.List;

import cn.emay.common.EmayTreeNode;
import cn.emay.eucp.common.moudle.db.fms.FmsBlacklist;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class BlackListStore {

	private EmayTreeNode root = new EmayTreeNode("root", null);

	public void save(boolean isCover, String mobile) {
		if (mobile == null || mobile.trim().length() != 11) {
			return;
		}
		char[] mobilenodes = mobile.trim().toCharArray();
		EmayTreeNode parent = root;
		for (char mobilenode : mobilenodes) {
			EmayTreeNode node = parent.getChild(mobilenode);
			if (node == null) {
				node = new EmayTreeNode(mobilenode, null);
				parent.addChild(node, isCover);
			}
			parent = node;
		}
	}

	public void save(boolean isCover, String... mobiles) {
		if (mobiles == null || mobiles.length == 0) {
			return;
		}
		for (String mobile : mobiles) {
			this.save(isCover, mobile);
		}
	}

	public void save(boolean isCover, List<FmsBlacklist> blackNumbers) {
		if (blackNumbers == null || blackNumbers.size() == 0) {
			return;
		}
		for (FmsBlacklist li : blackNumbers) {
			this.save(isCover, li.getMobile());
		}
	}

	public void delete(String mobile) {
		if (mobile == null || mobile.trim().length() != 11) {
			return;
		}
		EmayTreeNode last = root;
		char[] mobilenodes = mobile.trim().toCharArray();
		for (char mobilenode : mobilenodes) {
			last = last.getChild(mobilenode);
			if (last == null) {
				return;
			}
		}
		do {
			last.getParent().removeChild(last.getId());
			last = last.getParent();
		} while (last != null && (last.getChildren() == null || last.getChildren().size() == 0) && (last.getParent() != null));
	}

	public boolean isBlackMobile(String mobile) {
		if (mobile == null || mobile.trim().length() != 11) {
			return false;
		}
		boolean is = true;
		EmayTreeNode node = root;
		char[] mobilenodes = mobile.trim().toCharArray();
		for (char mobilenode : mobilenodes) {
			node = node.getChild(mobilenode);
			if (node == null) {
				is = false;
				break;
			}
		}
		return is;
	}

	public static void main(String[] args) {
		BlackListStore s = new BlackListStore();
		s.save(false, "15538853919", "15538853914", "15538853918", "15538853917", "15538853916", "15538853915");
		System.out.println(s.isBlackMobile("15538853919"));
		System.out.println(s.isBlackMobile("15538853918"));
		System.out.println(s.isBlackMobile("15538853917"));
		System.out.println(s.isBlackMobile("15538853916"));
		System.out.println(s.isBlackMobile("15538853915"));
		System.out.println(s.isBlackMobile("15538853914"));
		System.out.println(s.isBlackMobile("15538853913"));
		System.out.println(s.isBlackMobile("15538853912"));
		System.out.println(s.isBlackMobile("15538853911"));
		s.delete("15538853919");
		s.delete("1553885");
		s.delete("15538853917");
		System.out.println(s.isBlackMobile("15538853919"));
		System.out.println(s.isBlackMobile("15538853918"));
		System.out.println(s.isBlackMobile("15538853917"));
		System.out.println(s.isBlackMobile("15538853916"));
		System.out.println(s.isBlackMobile("15538853915"));
		System.out.println(s.isBlackMobile("15538853914"));
		System.out.println(s.isBlackMobile("15538853913"));
		System.out.println(s.isBlackMobile("15538853912"));
		System.out.println(s.isBlackMobile("15538853911"));
	}

}
