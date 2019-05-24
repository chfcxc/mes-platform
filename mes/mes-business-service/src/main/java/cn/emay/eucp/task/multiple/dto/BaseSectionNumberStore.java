package cn.emay.eucp.task.multiple.dto;

import cn.emay.common.EmayTreeNode;
import cn.emay.eucp.common.moudle.db.system.BaseSectionNumber;

public class BaseSectionNumberStore {

	private EmayTreeNode<Character, BaseSectionNumber> root = new EmayTreeNode<Character, BaseSectionNumber>('1', null);

	public void save(BaseSectionNumber number) {
		if (number == null) {
			return;
		}
		char[] mobilenodes = number.getNumber().trim().toCharArray();
		EmayTreeNode<Character, BaseSectionNumber> parent = root;
		for (char mobilenode : mobilenodes) {
			EmayTreeNode<Character, BaseSectionNumber> node = parent.getChild(mobilenode);
			if (node == null) {
				node = new EmayTreeNode<Character, BaseSectionNumber>(mobilenode, null);
				parent.addChild(node, false);
			}
			parent = node;
		}
		parent.setFruit(number);
	}

	public BaseSectionNumber getSectionInfo(String mobile) {
		if (mobile == null || mobile.trim().length() < 11) {
			return null;
		}
		mobile = mobile.trim();
		BaseSectionNumber bsn = null;
		if (mobile.startsWith("170")) {
			bsn = getByLength(mobile, 4);
			if (bsn == null) {
				bsn = getByLength(mobile, 3);
			}
		} else {
			bsn = getByLength(mobile, 3);
			if (bsn == null) {
				bsn = getByLength(mobile, 4);
			}
		}
		return bsn;
	}

	private BaseSectionNumber getByLength(String mobile, int length) {
		String number = mobile.substring(0, length);
		EmayTreeNode<Character, BaseSectionNumber> node = root;
		char[] mobilenodes = number.trim().toCharArray();
		for (char mobilenode : mobilenodes) {
			node = node.getChild(mobilenode);
			if (node == null) {
				break;
			}
		}
		if (node != null) {
			return (BaseSectionNumber) node.getFruit();
		}
		return null;
	}

	public void delete(String mobile) {
		if (mobile == null || mobile.trim().length() != 11) {
			return;
		}
		EmayTreeNode<Character, BaseSectionNumber> last = root;
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
		BaseSectionNumberStore s = new BaseSectionNumberStore();

		BaseSectionNumber bs1 = new BaseSectionNumber();
		bs1.setNumber("139");
		bs1.setOperatorCode("CMCC");
		BaseSectionNumber bs2 = new BaseSectionNumber();
		bs2.setNumber("155");
		bs2.setOperatorCode("CMCC");
		BaseSectionNumber bs3 = new BaseSectionNumber();
		bs3.setNumber("180");
		bs3.setOperatorCode("CMCC");
		BaseSectionNumber bs4 = new BaseSectionNumber();
		bs4.setNumber("1702");
		bs4.setOperatorCode("CMCC");
		BaseSectionNumber bs5 = new BaseSectionNumber();
		bs5.setNumber("1703");
		bs5.setOperatorCode("CMCC");
		BaseSectionNumber bs6 = new BaseSectionNumber();
		bs6.setNumber("1704");
		bs6.setOperatorCode("CMCC");

		s.save(bs1);
		s.save(bs2);
		s.save(bs3);
		s.save(bs4);
		s.save(bs5);
		s.save(bs6);

		System.out.println(s.getSectionInfo("15538853919").getOperatorCode());
		System.out.println(s.getSectionInfo("13938853919").getOperatorCode());
		System.out.println(s.getSectionInfo("18038853919").getOperatorCode());
		System.out.println(s.getSectionInfo("17018853919"));
		System.out.println(s.getSectionInfo("17028853919").getOperatorCode());
		System.out.println(s.getSectionInfo("17038853919").getOperatorCode());
		System.out.println(s.getSectionInfo("17048853919").getOperatorCode());
	}

}
