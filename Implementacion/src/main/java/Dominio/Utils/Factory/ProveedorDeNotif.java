package Dominio.Utils.Factory;

import Controladores.Utils.Modelos;
import Dominio.Local.*;
import Dominio.Pedidos.Pedido;
import Dominio.Usuarios.MediosContacto.Notificacion;
import Dominio.Pedidos.EstadoPedido;
import Dominio.Local.Platos.Plato;
import Dominio.Usuarios.Categorias.CategoriaCliente;
import Dominio.Usuarios.Cliente;
import Dominio.Usuarios.Encargado;
import Dominio.Usuarios.Usuario;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

public class ProveedorDeNotif {
    public static Notificacion notificacionAscensoDeCategoria(Cliente cliente, CategoriaCliente categoria){
        return new Notificacion("¡Ascendiste de categoria!", espaciado(
            saludar(cliente)
            , "Al alcanzar los"
            , Integer.toString(cliente.getPedidosRealizados().size())
            , "pedidos, has ascendido de categoría a"
            , categoria.getNombre()+"."
            , "¡Felicidades! Sigue así y recibirás más descuentos en las próximas compras"
            )
        ) ;
    }

    public static Notificacion notificacionBienvenida(Usuario nuevoCliente) {
        return new Notificacion("Registro Usuario", espaciado(
            saludar(nuevoCliente)
            , "Le informamos que su usuario"
            , entreComillas(nuevoCliente.getUsername())
            , "ha sido creado correctamente. Le damos la bienvenida a la plataforma y"
            , "le agradecemos por confiar en nuestra plataforma"
            )
        );
    }

    public static Notificacion notificacionResultadoPedido(Usuario usuario, Pedido pedido){
        String estadoPedido = StringUtils.capitalize(pedido.getEstado().name());

        return new Notificacion("Pedido "+estadoPedido.toLowerCase(), espaciado(
            saludar(usuario)
            , "Le informamos que su pedido al local", pedido.getLocal().getNombre(), "ha sido"
            , estadoPedido.toLowerCase()
            )
        );
    }

    public static Notificacion notificacionNuevoPlato(Plato plato, Local local){
        return new Notificacion("Nuevo Plato", espaciado(
            "Le informamos que el local"
            , local.getNombre()
            , "ha sacado a la venta un nuevo plato:"
            , entreComillas(plato.getNombre())+"."
            , "¡Sea el primero en disfrutarlo!"
            )
        );
    }


    public static Notificacion notificacionDescuento(Float descuento, Plato plato, Local local) {
        return new Notificacion("Descuento", espaciado("Hay un descuento de "
            , descuento.toString(),"%", "en el local", local.getNombre(), ". ¡Aprovechalo!"));
    }

    public static Notificacion notificacionCambioDeDireccion(Local local, String direccionAnterior) {
        return new Notificacion("Cambio de Direccion", espaciado(

            "Le informamos que el local"
            , local.getNombre() + ", del que ud. es suscriptor,"
            , "ha cambiado su dirección, de"
            , direccionAnterior
            , "a"
            , local.getDireccion()
            )
        );
    }

    public static Notificacion notificacionSaldoAFavor(Encargado duenio, Double saldo) {
        return new Notificacion("Saldo fin de mes"
            , espaciado(saludar(duenio)
            , "Le informamos que a la fecha ha acumulado un saldo a favor de"
            , "$"+ Modelos.redondear(saldo)
            , "por los descuentos en sus pedidos, el cual será descontado de la cuota mensual,"
            , "cuyo monto próntamente se le hará llegar."
            , "Nuevamente, le agradecemos por confiar en nuestra plataforma")
        );
    }

    // Auxiliares **************************************************************************

    private static String saludar(Usuario usuario) {
        int queHoraEs = LocalDateTime.now().getHour();

        return espaciado(getSaludo(queHoraEs), usuario.getNombre() + ".");
    }

    private static String getSaludo(int queHoraEs){
        if(queHoraEs>=6 &&queHoraEs<=13)
            return "Buenos días";
        else
            return "Buenas "
                + (queHoraEs<=20 && queHoraEs>13? "tardes": "noches");
    }

    private static String entreComillas(String s){

        return "'"+s+"'";
    }


    private static String espaciado(String ... strings){
        return String.join(" ", strings);
    }
}
