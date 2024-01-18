package com.streamsets.datapurger.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVParser
{
	private BufferedReader reader;

	private char seprator = ',';

	private String line = "";

	private int nbLines = 0;

	private String oneRes;

	private CSVParser(BufferedReader reader)
	{
		this.reader = reader;
	}

	private String[] splitLine() throws IOException
	{
		nbLines = 0;
		ArrayList<String> al = new ArrayList<String>();
		line = nextLine();
		if (line == null)
			return null;
		nbLines = 1;
		int pos = 0;
		while (pos < line.length())
		{
			pos = findNextComma(pos);
			al.add(oneRes);
			pos++;
		}
		if (line.length() > 0 && line.charAt(line.length() - 1) == seprator)
		{
			al.add("");
		}
		return al.toArray(new String[al.size()]);
	}

	private int findNextComma(int p) throws IOException
	{
		char c;
		int i;
		oneRes = "";
		c = line.charAt(p);
		// empty field
		if (c == seprator)
		{
			oneRes = "";
			return p;
		}
		// not escape char
		if (c != '"')
		{
			i = line.indexOf(seprator, p);
			if (i == -1)
				i = line.length();
			oneRes = line.substring(p, i);
			return i;
		}
		// start with "
		p++;
		StringBuffer sb = new StringBuffer(200);
		while (true)
		{
			c = readNextChar(p);
			p++;
			// not a "
			if (c != '"')
			{
				sb.append(c);
				continue;
			}
			// ", last char -> ok
			if (p == line.length())
			{
				oneRes = sb.toString();
				return p;
			}
			c = readNextChar(p);
			p++;
			// "" -> just print one
			if (c == '"')
			{
				sb.append('"');
				continue;
			}
			// ", -> return
			if (c == seprator)
			{
				oneRes = sb.toString();
				return p - 1;
			}
			throw new IOException("Unexpected token found");
		}
	}

	private char readNextChar(int p) throws IOException
	{
		if (p == line.length())
		{
			String newLine = reader.readLine();
			if (newLine == null)
				throw new IOException("Error occured while parsing");
			line += "\n" + newLine;
			nbLines++;
		}
		return line.charAt(p);
	}

	private String nextLine() throws IOException
	{
		do
		{
			line = reader.readLine();
			if (line == null)
				return null;
		} while (line.trim().equals(""));
		return line;
	}

	public int getLineCount()
	{
		return nbLines;
	}

	public static ArrayList<List<String>> parse(File csvFile) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(csvFile));
		try
		{
			CSVParser parser = new CSVParser(reader);
			String[] res;
			ArrayList<List<String>> records = new ArrayList<List<String>>();
			while ((res = parser.splitLine()) != null)
			{
				List<String> tokens = new ArrayList<String>();
				for (int i = 0; i < res.length; i++)
				{
					tokens.add(res[i]);
				}
				records.add(tokens);
			}
			return records;
		} finally
		{
			reader.close();
		}
	}
}
