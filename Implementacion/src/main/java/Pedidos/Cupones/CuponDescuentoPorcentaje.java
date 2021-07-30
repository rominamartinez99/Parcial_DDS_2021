package Pedidos.Cupones;

import Pedidos.Carrito;
import Usuarios.Cliente;

public class CuponDescuentoPorcentaje implements CuponDescuento {
    public CuponDescuentoPorcentaje(float porcentaje, int cuantosPedidos){
        this.porcentaje = porcentaje;
        this.porCuantosPedidos = cuantosPedidos;
    }

    private float porcentaje;
    private int porCuantosPedidos;
    private int usos = 0;

    @Override
    public Double calcularSobre(double precio){
        return precio*porcentaje;
    }

    @Override
    public String getDetalle(){
        return porcentaje+"% en "+ porCuantosPedidos +" pedido/s ("+usosRestantes()+" restantes)";
    }

    @Override
    public void notificarUso(Cliente cliente, Carrito carrito){
        usos++;
        if(usosRestantes()==0){
            cliente.quitarDescuento(this);
        }
    }

    public int usosRestantes(){
        return porCuantosPedidos - usos;
    }
}
