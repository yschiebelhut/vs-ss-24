package productservice.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMin;

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
    @NotBlank
	private String name;

	@Column(name = "price")
    @DecimalMin(value = "0", inclusive = false)
	private double price;

	
	@Column(name = "category_id")
    @Min(1)
	private int category_id;

	@Column(name = "details")
	private String details;

	public Product(Product product) {
		this.name = product.name;
		this.price = product.price;
		this.category_id = product.category_id;
		this.details = product.details;
	}

	public Product(String name, double price, int category) {
		this.name = name;
		this.price = price;
		this.category_id = category;
	}

	public Product(String name, double price, int category, String details) {
		this.name = name;
		this.price = price;
		this.category_id = category;
		this.details = details;
	}

	public Product() {

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
		return this.category_id;
	}

	public void setCategory(int category) {
		this.category_id = category;
	}

	public String getDetails() {
		return this.details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
