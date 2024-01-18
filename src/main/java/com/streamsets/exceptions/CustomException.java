package com.streamsets.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CustomException extends Exception
{
	private static final long serialVersionUID = 1L;

	private CustomExceptionCode errorCode = CustomExceptionCode.INTERNAL_SERVER_ERROR;

	transient private String stackTraceString;

	public CustomException(CustomExceptionCode errorCode, String message)
	{
		super(message);
		this.errorCode = errorCode;
	}

	public CustomException(CustomExceptionCode errorCode, String message, Throwable tw)
	{
		super(message);
		this.errorCode = errorCode;
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		writer.write("ErrorCode: " + this.getErrorCode());
		writer.write(" ErrorMessage: " + this.getLocalizedMessage());
		tw.printStackTrace(printWriter);
		this.stackTraceString = writer.toString();
		try
		{
			writer.close();
		} catch (Exception ioe)
		{
		}
	}

	public CustomExceptionCode getErrorCode()
	{
		return errorCode;
	}

	public String getCode()
	{
		return errorCode.name();
	}

	@Override
	public String getMessage()
	{
		return "ErrorCode: " + this.getErrorCode() + " ErrorMessage: " + super.getMessage();
	}

	@Override
	public void printStackTrace()
	{
		if (stackTraceString != null)
			System.out.println(this.stackTraceString);
		else
			super.printStackTrace();
	}
}
