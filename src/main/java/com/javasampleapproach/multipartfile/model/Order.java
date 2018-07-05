package com.javasampleapproach.multipartfile.model;

public class Order {
	private String Id;
	private String Region;
	private String name;
	private String Item;
	private String Units;
	private String UnitCost;
	private String Total;

	public Order() {
		super();
	}

	public Order(String id, String region, String name, String item, String units, String unitCost, String total) {
		super();
		Id = id;
		Region = region;
		this.name = name;
		Item = item;
		Units = units;
		UnitCost = unitCost;
		Total = total;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getRegion() {
		return Region;
	}

	public void setRegion(String region) {
		Region = region;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getItem() {
		return Item;
	}

	public void setItem(String item) {
		Item = item;
	}

	public String getUnits() {
		return Units;
	}

	public void setUnits(String units) {
		Units = units;
	}

	public String getUnitCost() {
		return UnitCost;
	}

	public void setUnitCost(String unitCost) {
		UnitCost = unitCost;
	}

	public String getTotal() {
		return Total;
	}

	public void setTotal(String total) {
		Total = total;
	}

	@Override
	public String toString() {
		return "Order [Id=" + Id + ", Region=" + Region + ", name=" + name + ", Item=" + Item + ", Units=" + Units
				+ ", UnitCost=" + UnitCost + ", Total=" + Total + "]";
	}

}
