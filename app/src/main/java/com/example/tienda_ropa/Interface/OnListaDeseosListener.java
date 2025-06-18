package com.example.tienda_ropa.Interface;

public interface OnListaDeseosListener {
    void onAgregarCarrito(int idPrenda);  // Método para agregar a carrito
    void onEliminarProducto(int idPrenda);    // Método para eliminar producto de la lista de deseos
}
