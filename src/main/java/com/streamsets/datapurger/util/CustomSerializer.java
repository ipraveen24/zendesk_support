package com.streamsets.datapurger.util;

import java.util.ArrayList;
import java.util.List;

public class CustomSerializer
{
	public static final String _NULL_ = new String(new byte[] { 1 });

	public static final String _EOS_ = new String(new byte[] { 7 });

	private CustomSerializer()
	{
	}

	public static String fromList(List<String> list)
	{
		String fieldValue = null;
		if (list == null || list.size() == 0)
			fieldValue = CustomSerializer._NULL_;
		else
		{
			StringBuffer buffer = new StringBuffer();
			for (String element : list)
			{
				buffer.append(CustomSerializer._EOS_);
				buffer.append(element);
			}
			fieldValue = buffer.deleteCharAt(0).toString();
		}
		return fieldValue;
	}

	public static ArrayList<String> toList(String value)
	{
		ArrayList<String> list = new ArrayList<String>();
		if (!isEmpty(value))
		{
			String[] elements = value.split(CustomSerializer._EOS_);
			for (String element : elements)
				list.add(element);
		}
		return list;
	}

	private static boolean isEmpty(String value)
	{
		if (value == null || value.trim().length() == 0 || value.equals(CustomSerializer._NULL_))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
