package Controladores.Cliente;
import Controladores.Autenticador;
import Controladores.Utils.Modelo;
import Controladores.Utils.Modelos;
import Controladores.Utils.Templates;
import Repositorios.RepoLocales;
import Usuarios.Cliente;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import Local.*;

import static Controladores.Utils.Modelos.parseModel;

public class HomeController {
    private Autenticador<Cliente> autenticador;
    private RepoLocales repoLocales;

    public HomeController(Autenticador<Cliente> autenticador, RepoLocales repoLocales){
        this.autenticador = autenticador;
        this.repoLocales = repoLocales;
    }

    public ModelAndView getHome(Request req, Response res){
        Cliente usuario = autenticador.getUsuario(req);
        Modelo modelo = parseModel(usuario)
            .con("Locales", armarTop(rankingLocales()))
            .con("Categorias", armarTop(rankingCategorias()))
        ;

        return new ModelAndView(modelo, Templates.HOME);
    }

    private List<Object> armarTop(List<?> lista){
        return lista.stream().limit(10).collect(Collectors.toList());
    }

    private List<Local> rankingLocales(){
        return repoLocales.ordenadosPor(this::cantidadPedidosMensuales);
    }

    private List<String> rankingCategorias(){
        return pedidosPorCategoria().entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .map(Modelos::parseModel)
            .collect(Collectors.toList());
    }

    private Map<CategoriaLocal, Integer> pedidosPorCategoria(){
        Map<CategoriaLocal, Integer> pedidosPorCategoria = new HashMap<>();

        repoLocales.stream().forEach(local-> {
            int cantidadLocal = cantidadPedidosMensuales(local);

            local.getCategorias().forEach(categoria -> {
                int acumulados = pedidosPorCategoria.getOrDefault(categoria, 0);
                pedidosPorCategoria.put(categoria, acumulados + cantidadLocal);
            });
        });

        return pedidosPorCategoria;
    }

    private Integer cantidadPedidosMensuales(Local local){
        return local.pedidosDelMes(LocalDate.now()).size();
    }
}
