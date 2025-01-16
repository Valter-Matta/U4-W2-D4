package org.example;

public class Product {
	long id;
	String name;
	String category;
	double price;

	public Product (long id, String name, String category, double price) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.price = price;
	}

	public long getId () {
		return id;
	}

	public String getCategory () {
		return category;
	}

	public String getName () {
		return name;
	}

	public double getPrice () {
		return price;
	}

	@Override
	public String toString () {
		return "id prodotto:" + id + "\nTitolo: " + name + "\nCategoria: " + category + "\nPrezzo: " + price + "€";
	}


}
