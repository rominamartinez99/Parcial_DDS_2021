package Platos;

import Repositorios.Templates.Identificable;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public abstract class Plato extends Identificable {
    String nombre;
    List<String> fotos = new LinkedList<>();
    boolean disponible = false;

    public Plato(String nombre){
        this.nombre=nombre;
    }

    public String getNombre(){
        return nombre;
    }
    public List<String> getFotos(){
        return fotos;
    }

    public boolean estaDisponible(){
        return disponible;
    }

    public abstract Double getPrecio();
    public abstract String getDescripcion();

    public boolean mismoNombre(Plato plato) {
        return getNombre().equalsIgnoreCase(plato.getNombre());
    }
}
