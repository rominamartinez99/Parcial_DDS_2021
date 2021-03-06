package Dominio.Pedidos;

import Dominio.Usuarios.Cliente;
import Dominio.Local.Local;
import Repositorios.Templates.Identificado;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="Pedidos")
public class Pedido extends Identificado {
    Double precioAbonado;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="pedido")
    List<Item> items = new LinkedList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "local")
    Local local;
    @Enumerated(EnumType.ORDINAL)
    EstadoPedido estado = EstadoPedido.PENDIENTE;

    @Column
    LocalDateTime fechaHora;

    String direccion;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cliente")
    Cliente cliente;

    Float puntuacion;
    String detallePuntuacion;

    public Pedido(){}
    public Pedido(double precio, String direccion, Local local, List<Item> items, Cliente cliente){
        this.precioAbonado = precio;
        this.direccion=direccion;
        this.local = local;
        this.items.addAll(items);
        this.cliente = cliente;
    }

    public Double getPrecioAbonado(){
        return precioAbonado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public Boolean delMes(LocalDate fechaActual) {
        return fechaHora.getMonth() == fechaActual.getMonth()
            && fechaHora.getYear() == fechaActual.getYear();
    }

    public Boolean entreFechas(LocalDate min, LocalDate max){
        return fechaHora.isAfter(min.atStartOfDay()) && fechaHora.isBefore(max.atStartOfDay())
            || fechaHora.equals(min.atStartOfDay()) || fechaHora.equals(max.atStartOfDay());
    }

    public Local getLocal() {
        return local;
    }

    public List<Item> getItems(){
        return items;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public String getDireccion() {
        return direccion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente){
        this.cliente=cliente;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setPrecioAbonado(Double precio) {
        this.precioAbonado = precio;
    }

    public void setFechaHora(LocalDateTime fechaHora){
        this.fechaHora = fechaHora;
    }

    public void setLocal(Local local) {
        this.local=local;
    }

    public Float getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Float puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getDetallePuntuacion() {
        return detallePuntuacion;
    }

    public Double getDescuento(){
        return precioBase() - precioAbonado;
    }

    public boolean estaCalificado(){
        return puntuacion!=null;
    }

    public void setDetallePuntuacion(String detallePuntuacion) {
        this.detallePuntuacion = detallePuntuacion;
    }

    public void agregarItem(Item item){
        items.add(item);
    }

    public Double precioBase() {
        return items.stream().mapToDouble(Item::getPrecio).sum();
    }
}
