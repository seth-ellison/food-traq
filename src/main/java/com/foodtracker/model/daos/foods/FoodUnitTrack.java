package com.foodtracker.model.daos.foods;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="food_unit_tracking")
@GenericGenerator(name = "uuid", strategy = "uuid2")
public class FoodUnitTrack {
	private String trackingId;
	private FoodUnit foodUnit;
	private double lat;
	private double lng;
	private Timestamp timestamp;
	
	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name="tracking_id", unique = true, nullable = false)
	public String getTrackingId() {
		return trackingId;
	}
	
	@ManyToOne
	@JoinColumn(name = "food_unit_id", nullable = false)
	public FoodUnit getFoodUnit() {
		return foodUnit;
	}
	
	@Column(name = "lat")
	public double getLat() {
		return lat;
	}
	
	@Column(name = "lng")
	public double getLng() {
		return lng;
	}
	
	@Column(name = "timestamp")
	public Timestamp getTimestamp() {
		return timestamp;
	}
	
	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}
	
	public void setFoodUnit(FoodUnit foodUnit) {
		this.foodUnit = foodUnit;
	}
	
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	public void setLng(double lng) {
		this.lng = lng;
	}
	
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
