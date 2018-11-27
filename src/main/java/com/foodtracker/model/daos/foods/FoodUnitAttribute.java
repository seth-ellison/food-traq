package com.foodtracker.model.daos.foods;

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
@Table(name="food_unit_attributes")
@GenericGenerator(name = "uuid", strategy = "uuid2")
public class FoodUnitAttribute {
	private String attributesId;
	private FoodUnit foodUnit;
	private String name;
	private String value;
	
	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name="attributes_id", unique = true, nullable = false)
	public String getAttributesId() {
		return attributesId;
	}
	
	@ManyToOne
	@JoinColumn(name = "food_unit_id", nullable = false)
	@JsonBackReference
	public FoodUnit getFoodUnit() {
		return foodUnit;
	}
	
	@Column(name = "name", length=100)
	public String getName() {
		return name;
	}
	
	@Column(name = "value", length=1000)
	public String getValue() {
		return value;
	}

	public void setAttributesId(String attributesId) {
		this.attributesId = attributesId;
	}

	public void setFoodUnit(FoodUnit foodUnit) {
		this.foodUnit = foodUnit;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
