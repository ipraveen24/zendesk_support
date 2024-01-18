package com.streamsets.sqlrepo.persistence;

import org.hibernate.TransactionException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.CannotCreateTransactionException;

import com.streamsets.exceptions.CustomException;
import com.streamsets.exceptions.CustomExceptionCode;

public class TransactionFailureInterceptor implements ThrowsAdvice
{
	public void afterThrowing(Exception tw) throws Throwable
	{
		tw.printStackTrace();
		Throwable nestedCause = tw.getCause();
		if (tw instanceof CustomException)
			throw tw;
		else
		{
			if (tw instanceof java.net.SocketTimeoutException || tw instanceof java.net.SocketException || tw instanceof JDBCConnectionException || tw instanceof org.springframework.dao.DataAccessResourceFailureException || tw instanceof CannotCreateTransactionException)
			{
				throw new CustomException(CustomExceptionCode.CONNECTION_ERROR, tw.getMessage(), tw);
			}
			else if (nestedCause != null && (nestedCause instanceof TransactionException || nestedCause instanceof JDBCConnectionException || nestedCause instanceof com.mysql.jdbc.CommunicationsException))
			{
				throw new CustomException(CustomExceptionCode.CONNECTION_ERROR, nestedCause.getMessage(), nestedCause);
			}
			else if (tw instanceof DataIntegrityViolationException || tw instanceof org.springframework.dao.InvalidDataAccessResourceUsageException)
			{
				if (nestedCause != null)
				{
					if (nestedCause instanceof org.hibernate.PropertyValueException)
						throw new CustomException(CustomExceptionCode.NOT_NULL_CONSTRAINT_VIOLATION, "not-null property references a null or transient", nestedCause);
					else if (nestedCause instanceof org.hibernate.exception.ConstraintViolationException || nestedCause instanceof org.hibernate.exception.DataException)
					{
						Throwable nestedCauseLevel2 = nestedCause.getCause();
						if (nestedCauseLevel2 != null && nestedCauseLevel2 instanceof java.sql.BatchUpdateException)
						{
							String message = nestedCauseLevel2.getMessage();
							if (message.matches(".*Duplicate entry.*"))
								throw new CustomException(CustomExceptionCode.UNIQUE_CONSTRAINT_VIOLATION, message, nestedCauseLevel2);
							else if (message.matches(".*Cannot add or update a child row: a foreign key constraint fails.*"))
								throw new CustomException(CustomExceptionCode.FOREIGN_KEY_CONSTRAINT_VIOLATION, message, nestedCauseLevel2);
						}
						if (nestedCauseLevel2 != null && nestedCauseLevel2 instanceof java.sql.BatchUpdateException)
						{
							throw new CustomException(CustomExceptionCode.SIZE_CONSTRAINT_VIOLATION, nestedCauseLevel2.getMessage(), nestedCauseLevel2);
						}
					}
				}
				throw new CustomException(CustomExceptionCode.DATA_INTEGRITY_VIOLATION, tw.getMessage(), tw.getCause());
			}
			else if (nestedCause != null && nestedCause instanceof org.hibernate.id.IdentifierGenerationException)
			{
				throw new CustomException(CustomExceptionCode.IDENTIFIER_GENERATION_EXCEPTION, "INVALID ID :" + nestedCause.getMessage(), nestedCause);
			}
			else if (nestedCause != null && nestedCause instanceof org.hibernate.exception.LockAcquisitionException)
			{
				throw new CustomException(CustomExceptionCode.LOCK_EXCEPTION, "LOCK EXCEPTION :" + nestedCause.getMessage(), nestedCause);
			}
			throw new CustomException(CustomExceptionCode.INTERNAL_SERVER_ERROR, tw.getMessage(), tw);
		}
	}
}