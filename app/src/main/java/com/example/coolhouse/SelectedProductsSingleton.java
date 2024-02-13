package com.example.coolhouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectedProductsSingleton {
    private static SelectedProductsSingleton instance;
    private Map<String, List<Product>> selectedProductsMap = new HashMap<>();
    private List<SelectedProductsChangeListener> listeners = new ArrayList<>();

    private SelectedProductsSingleton() {
        // Private constructor to prevent instantiation
    }

    public static synchronized SelectedProductsSingleton getInstance() {
        if (instance == null) {
            instance = new SelectedProductsSingleton();
        }
        return instance;
    }

    public interface SelectedProductsChangeListener {
        void onSelectedProductsChanged();
    }

    public void addSelectedProductsChangeListener(SelectedProductsChangeListener listener) {
        listeners.add(listener);
    }

    public void removeSelectedProductsChangeListener(SelectedProductsChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (SelectedProductsChangeListener listener : listeners) {
            listener.onSelectedProductsChanged();
        }
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
        notifyListeners();
    }

    public void clearSelectedProducts(String criteria) {
        selectedProductsMap.remove(criteria);
        notifyListeners();
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
        notifyListeners();
    }
    private void notifySelectedProductsChanged() {
        for (SelectedProductsChangeListener listener : listeners) {
            listener.onSelectedProductsChanged();
        }
    }
}
