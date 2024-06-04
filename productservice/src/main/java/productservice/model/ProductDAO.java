package productservice.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;


public class ProductDAO extends GenericHibernateDAO<Product, Integer> {

//	public List<Product> getProductListByCriteria(String searchDescription,
//			Double searchMinPrice, Double searchMaxPrice){
//
//	    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		Transaction transaction = null;
//		List<Product> productList = null;
//
//	    try {			transaction = session.beginTransaction();
//			Criteria crit = session.createCriteria(Product.class);
//
//			// Define Search HQL:
//			if (searchDescription != null && searchDescription.length() > 0)
//			{	// searchValue is set:
//				searchDescription = "%"+searchDescription+"%";
//				crit.add(Restrictions.ilike("details", searchDescription ));
//			}
//
//			if (( searchMinPrice != null) && ( searchMaxPrice != null)) {
//					crit.add(Restrictions.between("price", searchMinPrice, searchMaxPrice));
//				}
//			else 	if( searchMinPrice != null) {
//					crit.add(Restrictions.ge("price", searchMinPrice));
//					}
//			else if ( searchMaxPrice != null) {
//					crit.add(Restrictions.le("price", searchMaxPrice));
//			}
//
//			productList = crit.list();
//
//			transaction.commit();
//
//		} catch (Exception e) {
//			transaction.rollback();
//			e.printStackTrace();
//		}
//	    return productList;
//	}

    public List<Product> getProductListByCriteria(String searchDescription, Double searchMinPrice, Double searchMaxPrice) {
        List<Product> productList = new ArrayList<>();

        Session session = getSession();
        session.beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        query.select(root);

        List<Predicate> filters = new ArrayList<>();

        if (searchDescription != null && !searchDescription.isEmpty()) {
            query.where(builder.and(
                    builder.or(
                    	builder.like(root.get("details"), "%" + searchDescription + "%"),
                    	builder.like(root.get("name"), "%" + searchDescription + "%")
                    ),
                    builder.between(root.get("price"), searchMinPrice, searchMaxPrice))
            );

			System.out.println("Search query build: " + query);
        }
//        if (searchMinPrice != null && searchMaxPrice != null) {
//            query.where(builder.between(root.get("price"), searchMinPrice, searchMaxPrice));
//        } else if (searchMinPrice != null) {
//            query.where(builder.ge(root.get("price"), searchMinPrice));
//        } else if (searchMaxPrice != null) {
//            query.where(builder.le(root.get("price"), searchMaxPrice));
//        }

        Query<Product> q = session.createQuery(query);
        productList = q.getResultList();

        System.out.println("Product List after search: " + productList);

        session.getTransaction().commit();

        return productList;
    }


}
