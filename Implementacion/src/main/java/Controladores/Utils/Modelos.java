package Controladores.Utils;
import Local.Local;
import MediosContacto.Notificacion;
import Pedidos.*;
import Pedidos.Cupones.CuponDescuento;
import Platos.Plato;
import Usuarios.Cliente;
import com.sun.xml.internal.ws.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import Local.CategoriaLocal;

public interface Modelos {

    static List<String> getCategorias(){
        List<String> categorias = Arrays.stream(CategoriaLocal.values())
            .map(Modelos::parseModel)
            .collect(Collectors.toList());

        return categorias;
    }

    static String parseModel(Enum<?> unEnum){
        String string = unEnum.toString().toLowerCase().replace('_', ' ');
        return StringUtils.capitalize(string);
    }

    static String unparseEnum(String s){
        return s.toUpperCase()
            .replace(' ', '_')
            .replace('%', '_');
    }

    static Modelo parseModel(Cliente cliente){
        return new Modelo("mailCliente", cliente.getMail())
            .con("categoriaCliente", cliente.getCategoria().toString())
            .con("descuentosCliente", cliente.getCupones().stream().map(CuponDescuento::getDetalle).collect(Collectors.toList()))
            .con("username", cliente.getUsername())
            .con("direcciones", cliente.getDireccionesConocidas())
            .con("apellidoCliente", cliente.getApellido())
            .con("nombreCliente", cliente.getNombre())
            .con("notificaciones", cliente.getNotificacionesPush())
        ;
    }

    static Modelo parseModel(Local local){
        return new Modelo("nombre", local.getNombre())
            .con("idLocal", local.getId())
            .con("categoriaLocal", parseModel(local.getCategoria()))
            .con("Platos", local.getMenu().stream().map(Modelos::parseModel).collect(Collectors.toList()))
            .con("Direccion", local.getDireccion())
        ;
    }

    static Modelo parseModel(Plato plato){
        return new Modelo("nombre", plato.getNombre())
            .con("precio", plato.getPrecio())
            .con("idPlato", plato.getId())
            .con("descripcion", plato.getDescripcion())
            .con("fotos", plato.getFotos())
        ;
    }

    static Modelo parseModel(Carrito carrito){
        String direccion = carrito.getDireccion();

        return new Modelo("local", carrito.getLocal().getNombre())
            .con("idLocal"       , carrito.getLocal().getId())
            .con("direccionCarrito", direccion)
            .con("items"         , carrito.getItems().stream().map(Modelos::parseModel).collect(Collectors.toList()))
            .con("precioBase"    , carrito.getPrecioBase())
            .con("dtoCategoria"  , carrito.descuentoPorCategoria())
            .con("dtoCupon"      , carrito.descuentoPorCupon())
            .con("precioFinal"   , carrito.getPrecioFinal())
            ;
    }

    static Modelo parseModel(Item item){
        return new Modelo("plato", item.getPlato().getNombre())
            .con("aclaraciones", item.getAclaraciones())
            .con("cantidad", item.getCantidad());
    }

    static Modelo parseModel(Pedido pedido){
        return new Modelo("local", pedido.getLocal().getNombre())
            .con("importe", pedido.getImporte())
            .con("items", pedido.getItems().stream().map(Modelos::parseModel).collect(Collectors.toList()))
            .con("estado", parseModel(pedido.getEstado()))
            .con(parseModel(pedido.getFechaInicio()))
            .con("direccion", pedido.getDireccion())
            .con("pendiente", pedido.getEstado()== EstadoPedido.PENDIENTE);
    }

    static List<Modelo> parseModel(List<Pedido> pedidos){
        int[] x = {1};
        List<Modelo> modelos = pedidos
            .stream()
            .map(Modelos::parseModel)
            .map(m -> m.con("numero", x[0]++))
            .collect(Collectors.toList());

        Collections.reverse(modelos);
        return modelos;
    }

    static Modelo parseModel(LocalDateTime fechaHora){
        return new Modelo(
            "fecha", conDosDigitos(fechaHora.getDayOfMonth())
                    +"/"+ conDosDigitos(fechaHora.getMonthValue())
                    +"/"+fechaHora.getYear())
        .con("hora", conDosDigitos(fechaHora.getHour())
                +":"+fechaHora.getMinute()
        );
    }

    static String conDosDigitos(Integer numero){
        String dosDigitos = numero.toString();
        if(dosDigitos.length()<2) dosDigitos = "0"+dosDigitos;

        return dosDigitos;
    }

    static Modelo parseModel(Notificacion notificacion) {
        return new Modelo("asunto", notificacion.getAsunto())
            .con("cuerpo", notificacion.getCuerpo())
            .con(parseModel(notificacion.getFechaHora()))
        ;
    }
}
