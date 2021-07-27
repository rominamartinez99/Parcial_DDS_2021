package Utils.Factory;

import MediosContacto.Notificacion;
import Usuarios.Categorias.CategoriaCliente;
import Usuarios.Cliente;
import Usuarios.Usuario;

public class ProveedorDeNotif {
    public static Notificacion notificacionAscensoDeCategoria(Cliente cliente, CategoriaCliente categoria){
        return new Notificacion("Ascendiste de categoria!", "Tu categoria nueva es: "+ categoria.getNombre());
    }

    public static Notificacion notificacionBienvenida(Usuario nuevoCliente) {
        return new Notificacion("Registro Usuario", String.join(" ",
            "Buenos días/tardes/noches", nuevoCliente.getNombre(), ", le informamos que" +
                "su usuario", nuevoCliente.getUsername(), "ha sido creado correctamente. Le damos la bienvenida!")
        );
    }
}