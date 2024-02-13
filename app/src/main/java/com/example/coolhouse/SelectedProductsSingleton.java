package com.example.coolhouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectedProductsSingleton {
    private static SelectedProductsSingleton instance;
    private Map<String, List<Product>> selectedProductsMap = new HashMap<>();

    private SelectedProductsSingleton() {
        // Private constructor to prevent instantiation
    }

    public static synchronized SelectedProductsSingleton getInstance() {
        if (instance == null) {
            instance = new SelectedProductsSingleton();
        }
        return instance;
    }

    public List<Product> getSelectedProducts(String criteria) {
        if (selectedProductsMap.containsKey(criteria)) {
            return selectedProductsMap.get(criteria);
        }
        return new ArrayList<>();
    }

    public void addSelectedProduct(String criteria, Product product) {
        List<Product> selectedProducts = getSelectedProducts(criteria);
        selectedProducts.add(product);
        selectedProductsMap.put(criteria, selectedProducts);
    }

    public void clearSelectedProducts(String criteria) {
        selectedProductsMap.remove(criteria);
    }

    public void loadSelectedProducts(String criteria, List<Product> productList) {
        List<Product> selectedProducts = getSelectedProducts(criteria);
        for (Product product : productList) {
            for (Product selectedProduct : selectedProducts) {
                if (selectedProduct.name.equals(product.name)) {
                    product.isSelected = selectedProduct.isSelected;
                    break;
                }
            }
        }
    }

    public void saveSelectedProducts(String criteria, List<Product> productList) {
        List<Product> selectedProducts = new ArrayList<>();
        for (Product product : productList) {
            if (product.isSelected) {
                selectedProducts.add(product);
            }
        }
        selectedProductsMap.put(criteria, selectedProducts);
    }
}
