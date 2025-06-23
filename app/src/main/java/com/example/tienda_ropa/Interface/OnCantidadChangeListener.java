package com.example.tienda_ropa.Interface;

public interface OnCantidadChangeListener {
    void onIncrementarCantidad(int idPrenda);  // Método para incrementar la cantidad
    void onDisminuirCantidad(int idPrenda);    // Método para disminuir la cantidad
    void onEliminarProducto(int idPrenda);     // Método para eliminar un producto
}
