package Controladores.Locales;

import Controladores.Templates.SignupController;
import Controladores.Utils.Modelo;
import Controladores.Utils.Modelos;
import Dominio.Local.CategoriaLocal;
import Dominio.Usuarios.Encargado;
import Dominio.Local.Local;
import Repositorios.RepoEncargados;
import Repositorios.RepoLocales;

import java.util.*;

public class LocalSignupController extends SignupController<Encargado> {
    private RepoLocales repoLocales;

    public LocalSignupController(RepoEncargados repoContactos, RepoLocales repoLocales){
        super(repoContactos);
        this.repoLocales = repoLocales;
    }

    @Override
    protected Encargado crearUsuario(Map<String, String> req) {
        Local local = crearLocal(req);

        Encargado contacto = new Encargado(
            req.get("username")
            , req.get("password")
            , req.get("nombre")
            , req.get("apellido")
            , req.get("mail")
            , local
        );

        withTransaction(()->repoLocales.agregar(local));
        return contacto;
    }

    private Local crearLocal(Map<String, String> req){
        return new Local(
            req.get("nombreLocal")
            , req.get("calleLocal")
            , CategoriaLocal.valueOf(Modelos.unparseEnum(req.get("categoriaLocal")))
        );
    }

    @Override
    protected Modelo modeloBase(){
        return super.modeloBase()
            .con("local", true)
            .con("categorias", Modelos.getCategorias());
    }
}