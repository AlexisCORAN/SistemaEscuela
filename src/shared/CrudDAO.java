package shared;

import java.util.List;

public interface CrudDAO<T> {

    List<T> listarTodos();

    T obtenerPorId(Object id);

    boolean insertar(T entidad);

    boolean actualizar(T entidad);

    boolean eliminar(Object id);
}