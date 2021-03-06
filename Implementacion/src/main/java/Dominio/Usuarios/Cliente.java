package Dominio.Usuarios;

import Dominio.Local.Local;
import Dominio.Pedidos.Carrito;
import Dominio.Pedidos.Pedido;
import Dominio.Usuarios.Categorias.CategoriaCliente;
import Dominio.Usuarios.Categorias.Primerizo;

import javax.persistence.*;
import java.util.*;

@Entity
@DiscriminatorValue("c")
public class Cliente extends Usuario {
    public Cliente(String usuario, String contrasenia, String nombre, String apellido, String mail, String direccion){
        super(usuario, contrasenia, nombre, apellido, mail);
        direccionesConocidas.add(direccion);
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente", orphanRemoval = true)
    private List<Carrito> carritos = new LinkedList<>();

    @ElementCollection
    @JoinTable(name="DireccionXCliente", joinColumns=@JoinColumn(name="cliente"))
    @Column(name="direccion")
    private Set<String> direccionesConocidas = new HashSet<>();
    @OneToMany (cascade = CascadeType.ALL, mappedBy = "cliente")
    private List<Pedido> pedidosRealizados = new LinkedList<>();

    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name="categoria")
    public CategoriaCliente categoria = new Primerizo();

    public Cliente() {}

    public Carrito getCarrito(Local local) {
        Optional<Carrito> carrito = carritos.stream().filter(c->c.getLocal().matchId(local.getId())).findFirst();
        if(!carrito.isPresent()){
            Carrito nuevoCarrito = new Carrito(this, local);
            carritos.add(nuevoCarrito);
            return nuevoCarrito;
        }
        return carrito.get();
    }

    public Set<String> getDireccionesConocidas() {
        return direccionesConocidas;
    }

    public CategoriaCliente getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaCliente categoria) {
        this.categoria = categoria;
    }

    public List<Pedido> getPedidosRealizados() {
        return pedidosRealizados;
    }

    public void agregarPedido(Pedido pedido) {
        pedidosRealizados.add(pedido);
        categoria.notificarPedido(pedido, this);
    }


    public Double descuentoPorCategoria(Double precio) {
        return categoria.descuentoPorCategoria(precio);
    }

    public void agregarDireccion(String direccion) {
        direccionesConocidas.add(direccion);
    }

    private List<Carrito> getCarritos() {
        return carritos;
    }

    private void setCarritos(List<Carrito> carritos) {
        this.carritos = carritos;
    }

    public void devolverCarrito(Carrito carrito) {
        carritos.removeIf(carrito::matchId);
    }
}

