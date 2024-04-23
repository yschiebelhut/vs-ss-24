package productservice.model;


import javax.persistence.*;

/**
 * This class contains details about products.
 */
@Entity
@Table(name = "product")
public class Product implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "price")
	private double price;

	
	// @ManyToOne
	@JoinColumn(name = "category_id")
	private int categoryId;

	@Column(name = "details")
	private String details;

	public Product(Product product) {
		this.name = product.name;
		this.price = product.price;
		this.categoryId = product.categoryId;
		this.details = product.details;
	}

	public Product(String name, double price, int category) {
		this.name = name;
		this.price = price;
		this.categoryId = category;
	}

	public Product(String name, double price, int category, String details) {
		this.name = name;
		this.price = price;
		this.categoryId = category;
		this.details = details;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getCategory() {
		return this.categoryId;
	}

	public void setCategory(int category) {
		this.categoryId = category;
	}

	public String getDetails() {
		return this.details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
