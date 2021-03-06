package db;

import Controladores.Utils.Transaccional;
import Dominio.Local.Local;
import Dominio.Usuarios.Encargado;
import Dominio.Usuarios.MediosContacto.MedioDeContacto;
import Dominio.Usuarios.MediosContacto.Notificacion;
import Dominio.Usuarios.MediosContacto.NotificadorMail;
import Dominio.Usuarios.MediosContacto.NotificadorPush;
import Dominio.Pedidos.EstadoPedido;
import Dominio.Pedidos.Item;
import Dominio.Pedidos.Pedido;
import Dominio.Local.Platos.Combo;
import Dominio.Local.Platos.Plato;
import Dominio.Local.Platos.PlatoSimple;
import Repositorios.Templates.Colecciones.DB;
import Repositorios.Templates.Identificable;
import Dominio.Usuarios.Categorias.CategoriaCliente;
import Dominio.Usuarios.Categorias.Frecuente;
import Dominio.Usuarios.Cliente;
import Dominio.Usuarios.Usuario;
import Dominio.Utils.Factory.ProveedorDeLocales;
import Dominio.Utils.Factory.ProveedorDeNotif;
import Dominio.Utils.Factory.ProveedorDePlatos;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class InserterTest implements Transaccional {
    @BeforeClass
    public static void cleanBefore(){
        new InserterTest().clean();
    }

    @After
    public void clean(){
        withTransaction(()->{
            deleteFrom(Notificacion.class);
            deleteFrom(MedioDeContacto.class);
            deleteFrom(Item.class);
            deleteFrom(Pedido.class);
            deleteFrom(Plato.class);
            deleteFrom(Local.class);
            deleteFrom(Usuario.class);
        });

        entityManager().clear();
        entityManager().close();
    }

    @Test
    public void estaVacio(){
        Predicate<Class<?>> vacio = clase -> new DB(clase).getAll().isEmpty();

        assert vacio.test(Cliente.class);
        assert vacio.test(Encargado.class);
        assert vacio.test(Pedido.class);
        assert vacio.test(Local.class);
        assert vacio.test(Plato.class);
    }

    @Test
    public void insertarCliente(){
        Cliente cliente = new Cliente();
        CategoriaCliente frecuente = new Frecuente();
        cliente.setCategoria(frecuente);
        cliente.setNombre("federico");
        cliente.setApellido("martinez");
        cliente.setUsername("fm");
        cliente.setPassword("90");
        cliente.setMail("fede@gmail.com");

        cliente.agregarNotificacionPush(ProveedorDeNotif.notificacionBienvenida(cliente));

        NotificadorPush notificadorPush = new NotificadorPush();
        NotificadorMail notificadorMail =new NotificadorMail();

        List<MedioDeContacto> medios = Arrays.asList(notificadorMail,notificadorPush);

        cliente.setMediosDeContacto(medios);

        assertInsertable(cliente);
    }

    @Test
    public void insertarUnLocal(){
        assertInsertable(ProveedorDeLocales.cincoEsquinas());
    }

    @Test
    public void insertarUnItem(){
        Item item1 = new Item(ProveedorDePlatos.hamburguesa(),200,  "-");
        assertInsertable(item1);
    }

    @Test
    public void insertarUnPlato(){
        PlatoSimple platoSimple = new PlatoSimple();
        platoSimple.setPrecioBase(897.5);
        platoSimple.setNombre("hamburguesa");
        platoSimple.setDescripcion("Medallon de carne entre panes");
        platoSimple.setIngredientes(Arrays.asList("pan", "carne"));

        PlatoSimple platoSimple2 = new PlatoSimple();
        platoSimple2.setPrecioBase(90.5);
        platoSimple2.setNombre("papas noisette");
        platoSimple2.setDescripcion("papas redondas");
        platoSimple2.setIngredientes(Arrays.asList("papas sin cascara"));

        Combo combo = new Combo();
        combo.setNombre("hamburguesa con papas");
        combo.setPlatos(Arrays.asList(platoSimple,platoSimple2));

        assertInsertable(combo);
    }

    @Test
    public void insertarUnPedido(){
        Local local = ProveedorDeLocales.localSinPlatos();
        PlatoSimple plato1 = new PlatoSimple("alto guiso", "-", 15.0, new ArrayList<>());
        Plato plato2 = new PlatoSimple("chocotorta", "torta de chocolate", 210.0, new ArrayList<>());

        local.agregarPlato(plato1);
        local.agregarPlato(plato2);

        List<Item> items = new ArrayList<>();
        items.add(new Item(plato1, 2, null));
        items.add(new Item(plato2, 2, "sin cascara"));

        Pedido pedido = new Pedido();
        pedido.setEstado(EstadoPedido.PENDIENTE);
        Cliente cliente = new Cliente("matiasanz", "123", "matias", "godinez", "_@mail.com", "Mi casa");
        pedido.setPrecioAbonado(550.98);
        pedido.setCliente(cliente);
        pedido.setDireccion("rivadavia 8973");
        pedido.setItems(items);
        pedido.setLocal(local);

        withTransaction(()->{
            entityManager().persist(pedido);
        });

        assertNotNull(pedido.getId());
    }

    private void assertInsertable(Identificable persistible){
        withTransaction(()->{
            entityManager().persist(persistible);
        });

        assertNotNull(persistible.getId());

        withTransaction(()->{
            entityManager().remove(persistible);
        });
    }

    private void deleteFrom(Class<?> entity){
        new DB(entity).borrarTodo();
    }
}
