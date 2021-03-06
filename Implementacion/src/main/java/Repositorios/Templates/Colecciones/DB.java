package Repositorios.Templates.Colecciones;

import Repositorios.Templates.Identificable;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import java.util.List;
import java.util.Optional;

public class DB<T extends Identificable>
    implements Coleccion<T>
    , WithGlobalEntityManager, EntityManagerOps, TransactionalOps

    {
        private Class<T> clase;

        public DB(Class<T> clase){
            this.clase = clase;
        }

        public List<T> getAll(){
            return entityManager()
                .createQuery("from "+clase.getSimpleName())
                .getResultList();
        }

        @Override
        public void eliminar(T elem) {
            entityManager().remove(elem);
        }

        @Override
        public void borrarTodo() {
            getAll().forEach(entityManager()::remove);
        }

        public void agregar(T elem){
            entityManager().persist(elem);
        }

        /***********************************************************/
         public Optional<T> find(Long id){
             T elem = entityManager().find(clase, id);
             return Optional.ofNullable(elem);
         }
    }
