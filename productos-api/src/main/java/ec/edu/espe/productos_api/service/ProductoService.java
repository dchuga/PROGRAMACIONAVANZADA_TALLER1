package ec.edu.espe.productos_api.service;

import ec.edu.espe.productos_api.exception.ProductoInvalidoException;
import ec.edu.espe.productos_api.exception.ProductoNoEncontradoException;
import ec.edu.espe.productos_api.model.Producto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductoService {

    private final Map<Long, Producto> memoria = new ConcurrentHashMap<>();
    private final AtomicLong contadorId = new AtomicLong(0);

    public ProductoService() {
        // Semilla de productos para realizar pruebas iniciales
        registrar(new Producto(null, "Laptop Dell Inspiron", 950.0, 10));
        registrar(new Producto(null, "Mouse Inalambrico Logitech", 25.0, 40));
        registrar(new Producto(null, "Teclado Mecanico Redragon", 65.0, 15));
    }

    public List<Producto> obtenerTodos() {
        return new ArrayList<>(memoria.values());
    }

    public Producto obtenerPorId(Long id) {
        Producto producto = memoria.get(id);
        if (producto == null) {
            throw new ProductoNoEncontradoException(id);
        }
        return producto;
    }

    public Producto registrar(Producto producto) {
        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new ProductoInvalidoException("El precio del producto debe ser mayor a cero.");
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new ProductoInvalidoException("El stock no puede ser negativo.");
        }

        long nuevoId = contadorId.incrementAndGet();
        producto.setId(nuevoId);
        memoria.put(nuevoId, producto);
        return producto;
    }

    public void eliminarPorId(Long id) {
        if (!memoria.containsKey(id)) {
            throw new ProductoNoEncontradoException(id);
        }
        memoria.remove(id);
    }
}