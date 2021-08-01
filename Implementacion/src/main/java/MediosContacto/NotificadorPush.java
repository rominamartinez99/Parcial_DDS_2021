package MediosContacto;

import Usuarios.Cliente;
import Usuarios.Usuario;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("push")
public class NotificadorPush extends MedioDeContacto{

    @Override
    public void notificar(Usuario usuario, Notificacion notificacion) {
        usuario.agregarNotificacionPush(notificacion);
    }

}
