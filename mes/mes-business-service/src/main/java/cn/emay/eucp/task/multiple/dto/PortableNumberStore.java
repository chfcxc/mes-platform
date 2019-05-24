package cn.emay.eucp.task.multiple.dto;

import java.util.ArrayList;
import java.util.List;

import cn.emay.common.EmayTreeNode;
import cn.emay.eucp.common.moudle.db.system.PortableNumber;

public class PortableNumberStore {

	private EmayTreeNode<Character, PortableNumber> root = new EmayTreeNode<Character, PortableNumber>('1', null);

	public void save(boolean isCover, List<PortableNumber> secs) {
		if (secs == null || secs.size() == 0) {
			return;
		}
		for (PortableNumber number : secs) {
			this.save(isCover, number);
		}
	}

	public void save(boolean isCover, PortableNumber number) {
		if (number == null) {
			return;
		}
		char[] mobilenodes = number.getMobile().trim().toCharArray();
		EmayTreeNode<Character, PortableNumber> parent = root;
		for (char mobilenode : mobilenodes) {
			EmayTreeNode<Character, PortableNumber> node = parent.getChild(mobilenode);
			if (node == null) {
				node = new EmayTreeNode<Character, PortableNumber>(mobilenode, null);
				parent.addChild(node, isCover);
			}
			parent = node;
		}
		parent.setFruit(number);
	}

	public PortableNumber getSectionInfo(String mobile) {
		if (mobile == null || mobile.trim().length() < 11) {
			return null;
		}
		EmayTreeNode<Character, PortableNumber> node = root;
		char[] mobilenodes = mobile.trim().toCharArray();
		for (char mobilenode : mobilenodes) {
			node = node.getChild(mobilenode);
			if (node == null) {
				break;
			}
		}
		if (node != null) {
			PortableNumber portableNumber = (PortableNumber) node.getFruit();
			if (!portableNumber.getIsDelete()) {
				return portableNumber;
			} else {
				return null;
			}
		}
		return null;
	}

	public void delete(String mobile) {
		if (mobile == null || mobile.trim().length() != 11) {
			return;
		}
		EmayTreeNode<Character, PortableNumber> last = root;
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

	public static void main(String[] args) {
		PortableNumberStore p = new PortableNumberStore();
		List<PortableNumber> pns = new ArrayList<PortableNumber>();
		PortableNumber p1 = new PortableNumber();
		p1.setMobile("13111111111");
		p1.setIsDelete(false);
		PortableNumber p2 = new PortableNumber();
		p2.setMobile("13222222222");
		p2.setIsDelete(false);
		PortableNumber p3 = new PortableNumber();
		p3.setMobile("13333333333");
		p3.setIsDelete(false);
		pns.add(p1);
		pns.add(p2);
		pns.add(p3);
		p.save(false, pns);
		System.out.println(null == p.getSectionInfo("13111111111") ? "已删除" : "未删除");
		p.delete("13111111111");
		System.out.println(null == p.getSectionInfo("13111111111") ? "已删除" : "未删除");

	}
}
