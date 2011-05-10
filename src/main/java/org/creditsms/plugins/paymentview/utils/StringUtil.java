package org.creditsms.plugins.paymentview.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

public final class StringUtil {

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

	/**
	 * <p>
	 * Splits a String by Character type as returned by
	 * <code>java.lang.Character.getType(char)</code>. Groups of contiguous
	 * characters of the same type are returned as complete tokens, with the
	 * following exception: if <code>camelCase</code> is <code>true</code>, the
	 * character of type <code>Character.UPPERCASE_LETTER</code>, if any,
	 * immediately preceding a token of type
	 * <code>Character.LOWERCASE_LETTER</code> will belong to the following
	 * token rather than to the preceding, if any,
	 * <code>Character.UPPERCASE_LETTER</code> token.
	 * 
	 * @param str
	 *            the String to split, may be <code>null</code>
	 * @param camelCase
	 *            whether to use so-called "camel-case" for letter types
	 * @return an array of parsed Strings, <code>null</code> if null String
	 *         input
	 * @since 2.4
	 */
	// From Apache Commons
	private static String[] splitByCharacterType(String str, boolean camelCase) {
		if (str == null) {
			return null;
		}
		if (str.length() == 0) {
			return new String[0];
		}
		char[] c = str.toCharArray();
		List list = new ArrayList();
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

	public static String getFormFieldName(String fieldName) {
		return fieldName;
	}

	public static String getFieldFromFormField(String formFieldName) {
		return "";
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
