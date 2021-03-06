package Dominio.Usuarios;

import Dominio.Usuarios.MediosContacto.MedioDeContacto;
import Dominio.Usuarios.MediosContacto.Notificacion;
import Repositorios.Templates.Identificado;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name="Usuarios")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo")
public abstract class Usuario extends Identificado {
    public Usuario(){}
    public Usuario(String username, String contrasenia, String nombre, String apellido, String mail){
        this.username=username;
        this.password=contrasenia;
        this.nombre=nombre;
        this.apellido=apellido;
        this.mail=mail;
    }

    private String username;
    private String password;
    public String mail;

    public String nombre;
    private String apellido;

    @OneToMany (cascade = CascadeType.ALL)
    @JoinColumn(name="usuario")
    private List<MedioDeContacto> mediosDeContacto = new ArrayList<>();
    @OneToMany (cascade = CascadeType.ALL)
    @JoinColumn(name="usuario")
    private List<Notificacion> notificacionesPush = new LinkedList<>();

    //Credenciales
    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    //Datos
    public String getNombre() {
        return nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public String getMail() { return mail; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setMediosDeContacto(List<MedioDeContacto> mediosDeContacto) {
        this.mediosDeContacto = mediosDeContacto;
    }

    //Notificaciones
    public List<Notificacion> getNotificacionesPush() {
        return notificacionesPush;
    }

    public void agregarNotificacionPush(Notificacion notificacion) {
        notificacionesPush.add(notificacion);
    }

    public void agregarMedioDeContacto(MedioDeContacto nuevoMedio){
        requireNonNull(nuevoMedio);
        mediosDeContacto.add(nuevoMedio);
    }

    public void notificar(Notificacion mensaje){
        mediosDeContacto.forEach(n->n.notificar(this,mensaje));
    }

    public List<MedioDeContacto> getMediosDeContacto() {
        return mediosDeContacto;
    }
}


