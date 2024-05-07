package categoryservice.database;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import categoryservice.sessionFactory.util.HibernateUtil;
import categoryservice.database.dataAccessObjects.IGenericDAO;
import org.hibernate.query.Query;

public  class GenericHibernateDAO<E, PK extends Serializable> implements IGenericDAO<E, PK> {

	
	/**
	 * The class of the pojo being persisted.
	 */
	protected Class<E> entityClass;
	 
        Session getSession() {
            Configuration config = new Configuration();
    
            config.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
            config.setProperty("hibernate.connection.url", "jdbc:mysql://" + System.getenv("DB_PATH"));
            config.setProperty("hibernate.connection.username", System.getenv("DB_USER"));
            config.setProperty("hibernate.connection.password", System.getenv("DB_PASS"));
            config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

            config.addAnnotatedClass(entityClass);

            SessionFactory sessionFactory = config.buildSessionFactory();  
            Session session = sessionFactory.openSession();
            return session;
        }
	
		@SuppressWarnings("unchecked")
		protected GenericHibernateDAO() {
//			ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
//			this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[1];
			final Type thisType = getClass().getGenericSuperclass();
			final Type type;
			if (thisType instanceof ParameterizedType) {
				type = ((ParameterizedType) thisType).getActualTypeArguments()[0];
			} else if (thisType instanceof Class) {
				type = ((ParameterizedType) ((Class) thisType).getGenericSuperclass()).getActualTypeArguments()[0];
			} else {
				throw new IllegalArgumentException("Problem handling type construction for " + getClass());
			}
			
			if (type instanceof Class) {
				this.entityClass = (Class<E>) type;
			} else if (type instanceof ParameterizedType) {
				this.entityClass = (Class<E>) ((ParameterizedType) type).getRawType();
			} else {
				throw new IllegalArgumentException("Problem determining the class of the generic for " + getClass());
			}
	 
		}
	 
		public void saveObject(E entity) { 		
		    Session session = getSession();
	 		try
			{
				session.beginTransaction();
				session.save(entity);
	            session.getTransaction().commit();
			}
			catch (HibernateException e)
			{
				//log.error("Hibernate Exception" + e.getMessage());
				session.getTransaction().rollback();
				throw new RuntimeException(e);
			}

		}
	 
		@SuppressWarnings("unchecked")
		public E getObjectById(PK id) {
					
		    Session session = getSession();
		    
		    session.beginTransaction();

		    E entity = (E) session.get(entityClass, id);
		    session.getTransaction().commit();
		    return (E) entity;
		}

		public E getObjectByName(String name) {
			Session session = getSession();
            session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<E> query = builder.createQuery(entityClass);
			Root<E> root = query.from(entityClass);
			query.select(root).where(builder.equal(root.get("name"), name));

			List<E> resultList = session.createQuery(query).getResultList();
			E entity = null;
			if (!resultList.isEmpty()) {
				entity = resultList.get(0);
			}
			session.getTransaction().commit();
			return entity;
		}
	

		public void deleteObject(E entity) {
		    Session session = getSession();

			try
			{
				session.beginTransaction();
				session.delete(entity);
				session.getTransaction().commit();
			}
			catch (HibernateException e)
			{
				//log.error("Hibernate Exception" + e.getMessage());
				session.getTransaction().rollback();
				throw new RuntimeException(e);
			}
	 
		}
	 
		
		public void deleteById(PK id) {
			    Session session = getSession();

				try
				{
					session.beginTransaction();
				    E entity = (E) session.get(entityClass, id);
					session.delete(entity);
					session.getTransaction().commit();
				}
				catch (HibernateException e)
				{
					//log.error("Hibernate Exception" + e.getMessage());
					session.getTransaction().rollback();
					throw new RuntimeException(e);
				}
		 
			
		}


		@SuppressWarnings("unchecked")
		public List<E> get(E entity) {
			Session session = getSession();
			session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<E> query = builder.createQuery(entityClass);
			Root<E> root = query.from(entityClass);
			query.select(root).where(builder.equal(root, entity));

			Query<E> q = session.createQuery(query);
			List<E> resultList = q.getResultList();

			session.getTransaction().commit();
			return resultList;
		}

		
		@SuppressWarnings("unchecked")
		public List<E> getObjectList() {
			Session session = getSession();
			session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<E> query = builder.createQuery(entityClass);
			Root<E> root = query.from(entityClass);
			query.select(root);

			List<E> resultList = session.createQuery(query).getResultList();

			session.getTransaction().commit();
			return resultList;
		}

		@SuppressWarnings("unchecked")
		public List<E> getSortedList(String sortOrder, String sortProperty) {
			Session session = getSession();
				session.beginTransaction();

				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<E> query = builder.createQuery(entityClass);
				Root<E> root = query.from(entityClass);
				query.select(root);

				if (!sortProperty.isEmpty()) {
					if (sortOrder.equals("asc")) {
						query.orderBy(builder.asc(root.get(sortProperty)));
					} else if (sortOrder.equals("desc")) {
						query.orderBy(builder.desc(root.get(sortProperty)));
					}
				}

				List<E> resultList = session.createQuery(query).getResultList();

				session.getTransaction().commit();
				return resultList;
		}

		public void updateObject(E entity) {
            Session session = getSession();
			try
			{
				session.beginTransaction();
				session.update(entity);
				session.getTransaction().commit();
			}
			catch (HibernateException e)
			{
				//log.error("Hibernate Exception" + e.getMessage());
				session.getTransaction().rollback();
				throw new RuntimeException(e);
			}
		}



}
