package com.streamsets.sqlrepo.dao;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;

import com.streamsets.exceptions.CustomException;
import com.streamsets.exceptions.CustomExceptionCode;

public class BaseDAO
{
	protected HibernateTemplate hibernateTemplate = null;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate)
	{
		this.hibernateTemplate = hibernateTemplate;
	}

	private static SimpleDateFormat sqlDateFormat = null;

	static
	{
		sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public static synchronized String toDateString(Date t)
	{
		if (t != null)
			return sqlDateFormat.format(t);
		else
			return "";
	}

	public HibernateTemplate getHibernateTemplate()
	{
		return hibernateTemplate;
	}

	public Object load(Class<?> clazz, Serializable id) throws CustomException
	{
		Object obj = hibernateTemplate.get(clazz, id);
		if (obj == null)
			throw new CustomException(CustomExceptionCode.OBJECT_NOT_FOUND, "Object of class: " + clazz + " with id: " + id + " not found");
		return obj;
	}

	public Object load(Class<?> clazz, String key, String value) throws CustomException
	{
		if (key == null || value == null)
			throw new CustomException(CustomExceptionCode.OBJECT_NOT_FOUND, "Object of class: " + clazz + " with unique key: " + key + "and value: " + value + " not found");
		DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
		criteria.add(Restrictions.eq(key, value));
		List<?> objects = hibernateTemplate.findByCriteria(criteria);
		if (objects.size() == 0)
		{
			throw new CustomException(CustomExceptionCode.OBJECT_NOT_FOUND, "Object of class: " + clazz + " with unique key: " + key + " and value: " + value + " not found");
		}
		else
		{
			return objects.get(0);
		}
	}

	public Object loadLocked(Class<?> clazz, Serializable id) throws CustomException
	{
		Object obj = hibernateTemplate.get(clazz, id, LockMode.PESSIMISTIC_WRITE);
		if (obj == null)
			throw new CustomException(CustomExceptionCode.OBJECT_NOT_FOUND, "Object of class: " + clazz + "with id: " + id + " not found");
		return obj;
	}

	public Serializable save(Object obj)
	{
		return hibernateTemplate.save(obj);
	}

	public void update(Object obj)
	{
		hibernateTemplate.update(obj);
	}

	public void saveOrupdate(Object obj)
	{
		hibernateTemplate.saveOrUpdate(obj);
	}

	public void delete(Object id)
	{
		hibernateTemplate.delete(id);
	}

	public List<?> listAll(Class<?> clazz)
	{
		return hibernateTemplate.loadAll(clazz);
	}

	public List<?> list(DetachedCriteria criteria)
	{
		return hibernateTemplate.findByCriteria(criteria);
	}

	public void saveAll(List<?> entities)
	{
		for (Object entity : entities)
		{
			hibernateTemplate.saveOrUpdate(entity);
		}
	}

	public List<?> list(final DetachedCriteria dCriteria, final int startIndex, final int counts)
	{
		return this.hibernateTemplate.execute(new HibernateCallback<List<?>>()
		{
			public List<?> doInHibernate(Session session) throws HibernateException
			{
				Criteria criteria = dCriteria.getExecutableCriteria(session);
				if (startIndex > 0)
					criteria.setFirstResult(startIndex);
				if (counts > 0)
					criteria.setMaxResults(counts);
				return (List<?>) criteria.list();
			}
		});
	}

	public List<?> list(String query, Object[] parms)
	{
		return hibernateTemplate.find(query, parms);
	}

	public int bulkUpdate(String queryString)
	{
		return this.hibernateTemplate.bulkUpdate(queryString);
	}

	public int bulkUpdate(String queryString, Object[] values)
	{
		int count = this.hibernateTemplate.bulkUpdate(queryString, values);
		return count;
	}

	public int executeNativeUpdateQuery(final String updateString, final Object[] values)
	{
		return this.hibernateTemplate.execute(new HibernateCallback<Integer>()
		{
			public Integer doInHibernate(Session session) throws HibernateException
			{
				SQLQuery sQLQuery = session.createSQLQuery(updateString);
				for (int i = 0; i < values.length; i++)
				{
					sQLQuery.setParameter(i, values[i]);
				}
				return sQLQuery.executeUpdate();
			}
		});
	}

	public List<?> executeNativeQuery(final String listQueryString, final Object[] values, final ResultTransformer resultTransformer)
	{
		return this.hibernateTemplate.execute(new HibernateCallback<List<?>>()
		{
			public List<?> doInHibernate(Session session) throws HibernateException
			{
				SQLQuery sQLQuery = session.createSQLQuery(listQueryString);
				sQLQuery.setResultTransformer(resultTransformer);
				for (int i = 0; i < values.length; i++)
				{
					sQLQuery.setParameter(i, values[i]);
				}
				return (List<?>) sQLQuery.list();
			}
		});
	}

	public int executeHQLUpdateQuery(final String updateString, final HashMap<String, Object> parameters)
	{
		return this.hibernateTemplate.execute(new HibernateCallback<Integer>()
		{
			public Integer doInHibernate(Session session) throws HibernateException
			{
				Query sQLQuery = session.createQuery(updateString);
				for (Map.Entry<String, Object> entries : parameters.entrySet())
				{
					Object value = entries.getValue();
					if (Collection.class.isAssignableFrom(value.getClass()))
					{
						sQLQuery.setParameterList(entries.getKey(), (Collection<?>) value);
					}
					else
						sQLQuery.setParameter(entries.getKey(), value);
				}
				return (Integer) sQLQuery.executeUpdate();
			}
		});
	}

	public Timestamp now()
	{
		return this.hibernateTemplate.execute(new HibernateCallback<Timestamp>()
		{
			public Timestamp doInHibernate(Session session) throws HibernateException
			{
				SQLQuery sqlQuery = session.createSQLQuery("select UTC_TIMESTAMP()");
				List<?> list = sqlQuery.list();
				return (Timestamp) list.get(0);
			}
		});
	}

	public static String fromList(List<String> ids)
	{
		String idsAsString = null;
		if (ids == null || ids.size() == 0)
			idsAsString = "";
		else
		{
			StringBuilder idsBuilder = new StringBuilder();
			for (String element : ids)
			{
				if (idsBuilder.length() == 0)
					idsBuilder.append("'" + element + "'");
				else
					idsBuilder.append(",'" + element + "'");
			}
			idsAsString = idsBuilder.toString();
		}
		return idsAsString;
	}

	public static String trimToSize(String value, int size)
	{
		if (value != null && value.length() > size)
			value = value.substring(0, size);
		return value;
	}

	public int executeNativeUpdateQuery(final String updateString)
	{
		return this.hibernateTemplate.execute(new HibernateCallback<Integer>()
		{
			public Integer doInHibernate(Session session) throws HibernateException
			{
				SQLQuery sQLQuery = session.createSQLQuery(updateString);
				return sQLQuery.executeUpdate();
			}
		});
	}
}
