package Clientes;

import Dominio.Usuarios.MediosContacto.NotificadorPush;
import Dominio.Pedidos.Pedido;
import Dominio.Usuarios.Categorias.*;
import Dominio.Usuarios.Cliente;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class CategoriasTest {
    Primerizo primerizo = new Primerizo();
    Ocasional ocasional = new Ocasional();
    Habitual habitual = new Habitual();
    Frecuente frecuente = new Frecuente();

    Cliente cliente;

    @Before
    public void init() {
        cliente = new Cliente();
        cliente.agregarMedioDeContacto(new NotificadorPush());
    }

    @Test
    public void clienteAlCrearseObtieneCategoriaInicial() {
        assertEquals(primerizo.getNombre(), cliente.getCategoria().getNombre());
    }

    @Test
    public void dePrimerizoPasaAOcasional(){
        int pedidosPrimerizo = Primerizo.pedidosParaCambio;
        subirDeCategoriaValidando(primerizo, cliente, pedidosPrimerizo);
        assertEquals(ocasional.getNombre(), cliente.getCategoria().getNombre());
        assertEquals(1, cliente.getNotificacionesPush().size());
    }

    @Test
    public void deOcasionalPasaAFrecuente(){
        int pedidosOcasional = Ocasional.pedidosParaCambio;
        cliente.setCategoria(new Ocasional());
        subirDeCategoriaValidando(ocasional, cliente, pedidosOcasional);
        assertEquals(frecuente.getNombre(), cliente.getCategoria().getNombre());
        assertEquals(1, cliente.getNotificacionesPush().size());
    }

    @Test
    public void deFrecuentePasaAHabitual(){
        int pedidosFrecuente = Frecuente.pedidosParaCambio;
        cliente.setCategoria(new Frecuente());
        subirDeCategoriaValidando(frecuente, cliente, pedidosFrecuente);

        assertEquals(habitual.getNombre(), cliente.getCategoria().getNombre());
        assertEquals(1, cliente.getNotificacionesPush().size());
    }

    @Test
    public void primerizoNoTieneNingunDescuento(){
        rangoPrecios(1, 10000).forEach(
            precio ->
            assertEquals(0.0, primerizo.descuentoPorCategoria(precio), 0)
        );
    }

    @Test
    public void habitualCalculaBienElDescuento(){

        BiConsumer<Integer, Boolean> assertDescuento = (cantidad, debeAplicarlo) -> {
            habitual.setPedidosHechos(cantidad);
            Function<Double, Double> descuentoPorPrecio = p-> debeAplicarlo? p: 0;
            assertDescuentoPorPrecio(habitual, descuentoPorPrecio);
        };

        assertDescuento.accept(0, true);

        for(int i=1; i<Habitual.cadaCuantosDescuento; i++){
            assertDescuento.accept(i, false);
        }

        assertDescuento.accept(Habitual.cadaCuantosDescuento, true);
    }

    @Test
    public void frecuenteCalculaBienElDescuento(){
        Frecuente categoria = frecuente;
        double maxError = 0.00001;
        assertEquals(10.0, categoria.descuentoPorCategoria(100.0), maxError);
        assertEquals(50.0, categoria.descuentoPorCategoria(500.0), maxError);
        assertEquals(100.0, categoria.descuentoPorCategoria(1000.0), maxError);
    }

    @Test
    public void ocasionalCalculaBienElDescuento(){
        Double minimoDescuento = Ocasional.precioMinimoDescuento;
        rangoPrecios(0, minimoDescuento).forEach(
            precio -> assertEquals(0, ocasional.descuentoPorCategoria(precio), 0)
        );

        rangoPrecios(minimoDescuento, 10000).forEach(
            precio -> assertEquals(precio*0.15, ocasional.descuentoPorCategoria(precio), 0.0001)
        );
    }

    private void assertDescuentoPorPrecio(CategoriaCliente categoria, Function<Double, Double> descuentoEsperado){
        rangoPrecios(0, 10000).forEach(
            precio -> assertEquals(descuentoEsperado.apply(precio), categoria.descuentoPorCategoria(precio), 0)
        );
    }

    private List<Double> rangoPrecios(double min, double max){
        return max<=min? new LinkedList<>()
         : Stream.concat(Stream.of(min), rangoPrecios(min+100, max).stream()).collect(Collectors.toList());
    }

    private void subirDeCategoriaValidando(CategoriaCliente categoriaActual, Cliente cliente, int pedidosCambio){
        for(int i=1; i<=pedidosCambio; i++){
            assertEquals(categoriaActual.getNombre(), cliente.getCategoria().getNombre());
            cliente.agregarPedido(new Pedido());
        }
    }
}