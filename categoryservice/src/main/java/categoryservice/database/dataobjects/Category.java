package categoryservice.database.dataobjects;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

/**
 * This class contains details about categories.
 */
@Entity
@Table(name = "category")
public class Category implements java.io.Serializable {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

    @Column(name = "name", nullable = false)
    @NotBlank
	private String name;

	public Category() {
	}

	public Category(String name) {
		this.name = name;
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


}
