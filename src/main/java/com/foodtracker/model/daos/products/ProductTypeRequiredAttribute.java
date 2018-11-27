package com.foodtracker.model.daos.products;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="product_type_required_attributes")
@GenericGenerator(name = "uuid", strategy = "uuid2")
public class ProductTypeRequiredAttribute {

	private String requiredAttributesId;
	private ProductType productType;
	private String name;
	private String value;
	
	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name="required_attributes_id", unique = true, nullable = false)
	public String getRequiredAttributesId() {
		return requiredAttributesId;
	}
	
	
	@ManyToOne
	@JoinColumn(name = "product_type_id", nullable = false)
	@JsonBackReference
	public ProductType getProductType() {
		return productType;
	}

	@Column(name = "name", length=100)
	public String getName() {
		return name;
	}
	
	@Column(name = "value", length=1000)
	public String getValue() {
		return value;
	}
	
	public void setRequiredAttributesId(String requiredAttributesId) {
		this.requiredAttributesId = requiredAttributesId;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
