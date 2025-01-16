package org.example;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
	public static void main (String[] args) {

		List<Product> listProducts1 = List.of(
			new Product(1L, "La minacchia fantasma", "Boys", 150),
			new Product(2L, "Topo Gigio", "Books", 20.00),
			new Product(3L, "Le ali della Libertà", "Films", 35.00),
			new Product(4L, "Attack on Titans Season 1", "Books", 150.00)
		);
		List<Product> listProducts2 = List.of(
			new Product(1L, "Squid Game", "Baby", 60.00),
			new Product(2L, "Pippo", "Boys", 80.00),
			new Product(3L, "Batman", "Baby", 20.00),
			new Product(4L, "Attack on Titans Season 1", "Books", 300.00)
		);

		Customer customer1 = new Customer(1L, "Alice", 2);
		Customer customer2 = new Customer(2L, "Bob", 1);

		List<Order> listOfOrder = List.of(
			new Order(0L, "Delivered", LocalDate.of(2025, 1, 7), LocalDate.of(2025, 1, 15), listProducts1, customer2),
			new Order(1L, "Pending", LocalDate.of(2021, 2, 10), LocalDate.of(2021, 2, 15), listProducts2, customer1)
		);

		//es1
		Map<Customer, List<Order>> orderByCustomer = listOfOrder.stream().collect(Collectors.groupingBy(order -> order.customer));
		orderByCustomer.forEach((customer, customerOrders) -> {
			System.out.println("Cliente " + customer.name);
			customerOrders.forEach(order -> System.out.println(" Ordine ID " + order.id));
		});

		//es2
		Map<Customer, Double> totalBuy = listOfOrder.stream()
			.collect(Collectors.groupingBy(order -> order.customer,
				Collectors.summingDouble(order -> order.products.stream().mapToDouble(Product::getPrice).sum())));

		totalBuy.forEach((customer, totalSales) ->
			System.out.println("Cliente: " + customer.name + ", Totale Acquistato: " + totalSales));


		//es3
		double maxPrice = listProducts1.stream().mapToDouble(p -> p.price).max().orElse(0);

		List<Product> mostExpensive = listProducts1.stream().filter(product -> product.price == maxPrice).toList();
		System.out.println("\nI prodotti più costosi sono: " + mostExpensive);

//es4
		double averagePrice = listOfOrder.stream().mapToDouble(order -> order.products.stream().mapToDouble(product -> product.price).sum()).average().orElse(0);
		System.out.println("\nil costo medio degli importi negli ordini è: " + averagePrice);

		//es5
		System.out.println("\n Stampo in base alla categoria:");
		Map<String, Double> categoryTotals = listProducts1.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.summingDouble(Product::getPrice)));
		categoryTotals.forEach((category, totals) ->
			System.out.println("Categoria: " + category + ", Totale: " + totals + "€"));

		//es6
		String filePath = "prodotti.txt";
		try {
			salvaProdottiSuDisco(listProducts1, "prodotti.txt");
			System.out.println("\nProdotti salvati sul disco");
			List<Product> prodottiLetti = leggiProdottiDaDisco(filePath);
			System.out.println("\nProdotti letti da disco:");
			prodottiLetti.forEach(System.out::println);
		} catch (IOException e) {
			System.err.println("Errore durante il salvataggio dei prodotti: " + e.getMessage());
		}
	}

	public static void salvaProdottiSuDisco (List<Product> products, String filePath) throws IOException {

		String data = products.stream()
			.map(p -> p.name + "@" + p.category + "@" + p.price)
			.collect(Collectors.joining("#"));


		FileUtils.writeStringToFile(new File(filePath), data, "UTF-8");
	}

	public static List<Product> leggiProdottiDaDisco (String filePath) throws IOException {
		String data = FileUtils.readFileToString(new File(filePath), "UTF-8");


		String[] prodottiArray = data.split("#");
		List<Product> products = new ArrayList<>();

		for (String prodotto : prodottiArray) {
			String[] campi = prodotto.split("@");
			String name = campi[0];
			String category = campi[1];
			double price = Double.parseDouble(campi[2]);
			products.add(new Product(null, name, category, price));
		}
		return products;
	}

}
