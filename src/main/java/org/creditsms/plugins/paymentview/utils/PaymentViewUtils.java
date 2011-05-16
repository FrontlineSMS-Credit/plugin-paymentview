package org.creditsms.plugins.paymentview.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.frontlinesms.FrontlineUtils;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.springframework.util.StringUtils;

public class PaymentViewUtils extends FrontlineUtils {
	public static String accountsAsString(Collection<Account> accounts,
			String groupsDelimiter) {
		String str_accounts = "";
		for (Iterator<Account> ai = accounts.iterator(); ai.hasNext();) {
			str_accounts += ai.next().getAccountNumber();
			if (ai.hasNext()) {
				str_accounts += groupsDelimiter;
			}
		}
		return str_accounts;
	}

	public static String toCamelCase(String str) {
		if (str != null) {
			String[] strings = str.split(" ");
			str = "";
			for (int i = 0; i < strings.length; i++) {
				strings[i] = strings[i].toLowerCase();
				if (i != 0) {
					strings[i] = StringUtils.capitalize(strings[i]);
				}
				str += strings[i];
			}
			return str;
		}
		return null;
	}

	public static String[] splitByCharacterTypeCamelCase(String str) {
		return splitByCharacterType(str, true);
	}

	// From Apache Commons
	private static String[] splitByCharacterType(String str, boolean camelCase) {
		if (str == null) {
			return null;
		}
		if (str.length() == 0) {
			return new String[0];
		}
		char[] c = str.toCharArray();
		List<String> list = new ArrayList<String>();
		int tokenStart = 0;
		int currentType = Character.getType(c[tokenStart]);
		for (int pos = tokenStart + 1; pos < c.length; pos++) {
			int type = Character.getType(c[pos]);
			if (type == currentType) {
				continue;
			}
			if (camelCase && type == Character.LOWERCASE_LETTER
					&& currentType == Character.UPPERCASE_LETTER) {
				int newTokenStart = pos - 1;
				if (newTokenStart != tokenStart) {
					list.add(new String(c, tokenStart, newTokenStart
							- tokenStart));
					tokenStart = newTokenStart;
				}
			} else {
				list.add(new String(c, tokenStart, pos - tokenStart));
				tokenStart = pos;
			}
			currentType = type;
		}
		list.add(new String(c, tokenStart, c.length - tokenStart));
		return (String[]) list.toArray(new String[list.size()]);
	}

	public static String getReadableFieldName(String fieldName) {
		String str = StringUtils.capitalize(fieldName);
		String[] strArr = splitByCharacterTypeCamelCase(str);
		StringBuilder str2 = new StringBuilder(2);
		for (String s : strArr) {
			str2.append(s);
			str2.append(" ");
		}
		return str2.toString().trim();
	}

	public static String getFormLabel(String fieldName) {
		return getReadableFieldName(fieldName) + ":";
	}

	public static String getMarkerFromString(String readableName) {
		if (readableName != null) {
			readableName = readableName.toLowerCase();
			readableName = StringUtils.replace(readableName, " ", "_");
			readableName = "${" + readableName + "}";
			return readableName;
		}
		return "";
	}
}
