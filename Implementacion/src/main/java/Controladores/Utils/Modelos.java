package Controladores.Utils;
import Local.Local;
import Pedidos.Carrito;
import Pedidos.Direccion;
import Pedidos.Item;
import Pedidos.Pedido;
import Platos.Plato;
import com.sun.xml.internal.ws.util.StringUtils;

import javax.jws.WebParam;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.stream.Collectors;

public interface Modelos {

    static String parseEnum(Enum<?> unEnum){
        String string = unEnum.toString().toLowerCase().replace('_', ' ');
        return StringUtils.capitalize(string);
    }

    static String unparseEnum(String s){
        return s.toUpperCase()
            .replace(' ', '_')
            .replace('%', '_');
    }

    static Modelo parseModel(Local local){
        return new Modelo("nombre", local.getNombre())
            .con("idLocal", local.getId())
            .con("categorias", local.getCategorias().stream().map(Modelos::parseEnum).collect(Collectors.toList()))
            .con("Platos", local.getMenu().stream().map(Modelos::parseModel).collect(Collectors.toList()))
            .con("Direccion", local.getDireccion().getCalle());
    }

    static Modelo parseModel(Plato plato){
        return new Modelo("nombre", plato.getNombre())
            .con("precio", plato.getPrecio())
            .con("idPlato", plato.getId());
    }

    static Modelo parseModel(Carrito carrito){
        Direccion direccion = carrito.getDireccion();

        return new Modelo("local", carrito.getLocal().getNombre())
            .con("idLocal"       , carrito.getLocal().getId())
            .con("direccion"     , (direccion==null)? null : direccion.getCalle())
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
            .con("estado", pedido.getEstado())
            .con(parseModel(pedido.getFechaInicio()))
            .con("direccion", pedido.getDireccion().getCalle());
    }

    static Modelo parseModel(LocalDateTime fechaHora){
        return new Modelo("fecha", fechaHora.getDayOfMonth()+"/"+fechaHora.getMonthValue()+"/"+fechaHora.getYear());
    }
}