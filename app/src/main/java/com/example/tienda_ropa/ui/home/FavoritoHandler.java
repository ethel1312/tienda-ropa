package com.example.tienda_ropa.ui.home;

import com.example.tienda_ropa.model.AgregarListaDeseosReq;
public interface FavoritoHandler {
    void agregarAFavoritos(AgregarListaDeseosReq req);
    void eliminarDeFavoritos(int idPrenda);

}
