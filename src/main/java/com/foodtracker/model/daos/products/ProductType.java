package com.foodtracker.model.daos.products;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="product_types")
@GenericGenerator(name = "uuid", strategy = "uuid2")
public class ProductType {
	
	private String productId;
	private String productType;
	private Set<ProductTypeRequiredAttribute> requiredAttributes;
	
	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name="product_type_id", unique = true, nullable = false)
	public String getProductId() {
		return productId;
	}
	
	@Column(name="product_type", length=200)
	public String getProductType() {
		return productType;
	}
	
	@OneToMany(mappedBy = "productType", 
	    cascade = CascadeType.ALL, 
	    orphanRemoval = true)
	public Set<ProductTypeRequiredAttribute> getRequiredAttributes() {
		return requiredAttributes;
	}
	
	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	public void setRequiredAttributes(Set<ProductTypeRequiredAttribute> requiredAttributes) {
		this.requiredAttributes = requiredAttributes;
	}
}
