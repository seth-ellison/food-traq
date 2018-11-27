package com.foodtracker.model.daos.foods;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.foodtracker.model.daos.products.ProductType;
import com.foodtracker.model.daos.users.User;

@Entity
@Table(name="food_units")
@GenericGenerator(name = "uuid", strategy = "uuid2")
public class FoodUnit implements Serializable {
	private static final long serialVersionUID = 5132988642201611556L;
	private String foodUnitId;
	private User user;
	private ProductType productType;
	private String description;
	private Timestamp created;
	private double mass;
	private Date expiry;
	private Set<FoodUnitAttribute> attributes;
	
	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name="food_unit_id", unique = true, nullable = false)
	public String getFoodUnitId() {
		return foodUnitId;
	}
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return user;
	}
	
	@ManyToOne
	@JoinColumn(name = "product_type_id", nullable = false)
	public ProductType getProductType() {
		return productType;
	}
	
	@Column(name="unit_desc", length = 1000)
	public String getDescription() {
		return description;
	}
	
	@Column(name="created")
	public Timestamp getCreated() {
		return created;
	}
	
	@Column(name="mass")
	public double getMass() {
		return mass;
	}
	
	@Column(name="expiry")
	public Date getExpiry() {
		return expiry;
	}
	
	@OneToMany(mappedBy = "foodUnit", 
	        cascade = CascadeType.ALL, 
	        orphanRemoval = true)
	public Set<FoodUnitAttribute> getAttributes() {
		return attributes;
	}

	public void setFoodUnitId(String foodUnitId) {
		this.foodUnitId = foodUnitId;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}
	
	public void setAttributes(Set<FoodUnitAttribute> attributes) {
		this.attributes = attributes;
	}
}
