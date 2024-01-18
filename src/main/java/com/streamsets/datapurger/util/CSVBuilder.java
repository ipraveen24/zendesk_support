package com.streamsets.datapurger.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVBuilder
{
	private File file = null;

	private int recordCounter = 0;

	private BufferedWriter writer;

	private String nullString = "";

	private char seprator = ',';

	public CSVBuilder(File file, boolean append) throws IOException
	{
		this.file = file;
		this.writer = new BufferedWriter(new FileWriter(this.file, append));
	}

	public CSVBuilder addRow(List<String> columns, boolean recordCounter) throws IOException
	{
		addRow(columns.toArray(new String[columns.size()]), recordCounter);
		return this;
	}

	public void addRow(String[] columns, boolean recordCounter) throws IOException
	{
		this.writer.write(buildRow(columns));
		if (recordCounter)
			this.recordCounter++;
	}

	private String buildRow(String[] columns)
	{
		StringBuilder row = new StringBuilder();
		for (int i = 0; i < columns.length; i++)
		{
			String column = columns[i];
			if (i > 0)
				row.append(seprator);
			if (column == null)
				row.append(nullString);
			else if ((column.indexOf(seprator) >= 0) || (column.indexOf("\n") >= 0) || (column.indexOf("\r") >= 0) || (column.indexOf("\"") >= 0))
			{
				row.append("\"");
				row.append(replaceString(column, "\"", "\"\""));
				row.append("\"");
			}
			else
				row.append(column);
		}
		row.append("\n");
		return row.toString();
	}

	public void close() throws IOException
	{
		this.writer.close();
		if (this.file != null && this.file.length() == 0)
			this.file.delete();
	}

	public int getRecordCounter()
	{
		return recordCounter;
	}

	public File getFile()
	{
		return file;
	}

	private static String replaceString(String s, String sMatch, String sReplace)
	{
		if (sReplace == null)
			sReplace = "";
		if (sMatch == null || "".equals(sMatch) || sMatch.equals(sReplace))
			return s;
		if (s == null || s.equals(""))
		{
			return "";
		}
		int i = 0;
		int j = s.indexOf(sMatch);
		if (j < 0)
		{
			return s;
		}
		StringBuffer sb = new StringBuffer(s.length());
		while (true)
		{
			sb.append(s.substring(i, j));
			sb.append(sReplace);
			i = j + sMatch.length();
			j = s.indexOf(sMatch, i);
			if (j < 0)
			{
				sb.append(s.substring(i));
				break;
			}
		}
		return sb.toString();
	}

	public void flush() throws IOException
	{
		if (writer != null)
			writer.flush();
		else
			throw new IllegalStateException("CSVBuilder not initilaized");
	}
}
