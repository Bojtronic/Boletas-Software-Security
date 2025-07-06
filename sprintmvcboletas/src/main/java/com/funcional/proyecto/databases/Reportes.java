package com.funcional.proyecto.databases;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.ChronoUnit;

import com.funcional.proyecto.models.Boleta;
import com.funcional.proyecto.models.BoletaServicio;
import com.funcional.proyecto.models.Cliente;
import com.funcional.proyecto.models.IBoleta;
import com.funcional.proyecto.models.Producto;
import com.funcional.proyecto.models.ProductosUtilizadosView;
import com.funcional.proyecto.models.ProductosUtilizadosViewList;
import com.funcional.proyecto.models.RendimientoTecnicosView;
import com.funcional.proyecto.models.TiempoRespuestaView;
import com.funcional.proyecto.models.UsuarioRecurrentesViewList;
import com.funcional.proyecto.models.UsuarioRecurrentesView;

public class Reportes extends BaseModel {

    private final IBoleta repository;

    public Reportes(IBoleta boletaRepository) {
        this.repository = boletaRepository;
    }

    class UsuarioR {
        private Cliente cliente;
        private List<BoletaServicio> servicios = new ArrayList<>();

        public Cliente getCliente() {
            return cliente;
        }

        public void setCliente(Cliente cliente) {
            this.cliente = cliente;
        }

        public List<BoletaServicio> getServicio() {
            return servicios;
        }

        public void setServicio(List<BoletaServicio> servicios) {
            this.servicios = servicios;
        }

        public void addServicio(BoletaServicio servicio) {
            this.servicios.add(servicio);
        }

        public String getServicioMasComun() {
            var comun = new BoletaServicio();
            long mayor = 0;
            for (int index = 0; index < servicios.size(); index++) {
                var serv = servicios.get(index);
                long valor = servicios.stream().filter(s -> s.getId() == serv.getId()).count();
                if (valor > mayor)
                    comun = serv;
            }
            return comun.getServicio().getDescripcion();
        }
    }

    public List<Boleta> serviciosRecibidos(LocalDateTime finicio, LocalDateTime ffinal) {
        Iterable<Boleta> source = repository.findAll();
        List<Boleta> lista = new ArrayList<Boleta>();
        source.forEach(lista::add);

        return lista.stream().filter(b -> CompareDates(finicio, b.getServicio(), FindFecha.after) &&
                CompareDates(ffinal, b.getServicio(), FindFecha.before) &&
                b.isActive()).toList();
    }

    public UsuarioRecurrentesView usuarioRecurrentes(LocalDateTime finicio, LocalDateTime ffinal) {
        Iterable<Boleta> source = repository.findAll();
        List<Boleta> lista = new ArrayList<Boleta>();
        source.forEach(lista::add);

        lista = lista.stream().filter(b -> CompareDates(finicio, b.getServicio(), FindFecha.after) &&
                CompareDates(ffinal, b.getServicio(), FindFecha.before) &&
                b.isActive()).toList();

        UsuarioRecurrentesView result = new UsuarioRecurrentesView();
        List<UsuarioR> data = new ArrayList<>();
        List<UsuarioRecurrentesViewList> resultList = new ArrayList<>();

        int countSerivicios = 0;
        for (int index = 0; index < lista.size(); index++) {
            var boleta = lista.get(index);
            ++countSerivicios;
            if (data.size() == 0) {
                var u = new UsuarioR();
                u.setCliente(boleta.getCliente());
                u.addServicio(boleta.getServicio());
                data.add(u);
                continue;
            }

            if (data.stream().anyMatch(d -> d.cliente.getId() == boleta.getCliente().getId())) {
                data.stream().filter(d -> d.cliente.getId() == boleta.getCliente().getId()).findFirst().get()
                        .addServicio(boleta.getServicio());
            }
        }

        int countClientes = 0;
        for (int index = 0; index < data.size(); index++) {
            var cliente = data.get(index);
            var view = new UsuarioRecurrentesViewList();
            view.setNombreCliente(cliente.getCliente().getNombre() + " " + cliente.getCliente().getApellidos());
            view.setCantidadServicios(cliente.servicios.size());
            view.setServicioComun(cliente.getServicioMasComun());
            resultList.add(view);
            ++countClientes;
        }

        result.setData(resultList);
        result.setFrecuenciaVisitas(countSerivicios / countClientes);
        return result;
    }

    public ProductosUtilizadosViewList productosUtilizados(LocalDateTime finicio, LocalDateTime ffinal) {
        Iterable<Boleta> source = repository.findAll();
        List<Boleta> lista = new ArrayList<Boleta>();
        source.forEach(lista::add);

        lista = lista.stream().filter(b -> CompareDates(finicio, b.getServicio(), FindFecha.after) &&
                CompareDates(ffinal, b.getServicio(), FindFecha.before) &&
                b.isActive()).toList();

        var result = new ProductosUtilizadosViewList();
        List<ProductosUtilizadosView> data = new ArrayList<>();
        double costoTotal = 0;
        for (int index = 0; index < lista.size(); index++) {
            var boleta = lista.get(index);
            var servicio = boleta.getServicio();
            var productos = servicio.getProductos();

            if (data.isEmpty()) {
                for (int cursor = 0; cursor < productos.size(); cursor++) {
                    var p = getProductoView(servicio, productos.get(cursor));
                    data.add(p);
                    costoTotal += p.getPrecio();
                }
            } else if (!data.isEmpty()) {
                for (int cursor = 0; cursor < productos.size(); cursor++) {
                    var idprod = productos.get(cursor).getId();
                    if (data.stream()
                            .anyMatch(d -> d.getIdServicio() == servicio.getId() && d.getIdProducto() == idprod)) {
                        var p = data.stream()
                                .filter(d -> d.getIdServicio() == servicio.getId() && d.getIdProducto() == idprod)
                                .findFirst().get();
                        p.setCantidad(p.getCantidad() + 1);
                    } else {
                        var p = getProductoView(servicio, productos.get(cursor));
                        data.add(p);
                    }
                    costoTotal += productos.get(cursor).getPrecio();
                }
            }
        }

        result.setCostoTotal(costoTotal);
        result.setProductos(data);
        return result;
    }

    public List<RendimientoTecnicosView> RendimientoTecnicos(LocalDateTime finicio, LocalDateTime ffinal) {
        Iterable<Boleta> source = repository.findAll();
        List<Boleta> lista = new ArrayList<Boleta>();
        source.forEach(lista::add);

        lista = lista.stream().filter(b -> CompareDates(finicio, b.getServicio(), FindFecha.after) &&
                CompareDates(ffinal, b.getServicio(), FindFecha.before) &&
                b.isActive()).toList();
        List<RendimientoTecnicosView> result = new ArrayList<>();

        for (int index = 0; index < lista.size(); index++) {
            var boleta = lista.get(index);
            var servicio = boleta.getServicio();
            if (result.isEmpty()) {
                result.add(getTecnicosView(boleta, servicio));
            } else if (!result.isEmpty()) {
                if (result.stream().anyMatch(b -> b.getTecnico() == boleta.getTecnico())) {
                    var data = result.stream().filter(b -> b.getTecnico() == boleta.getTecnico()).findFirst().get();
                    data.setCantidadServicios(data.getCantidadServicios() + 1);
                    data.setDuracionServicios(index);
                    data.setDuracionServicios(data.getDuracionServicios() + calculateTime(servicio.getFInicio(), servicio.getFFinal()));
                    result.add(data);
                } else {
                    result.add(getTecnicosView(boleta, servicio));
                }
            }
        }

        result.stream().forEach(data -> {
            data.setPromedioDuracionServicios(data.getDuracionServicios() / data.getCantidadServicios());
        });

        return result;
    }

    public List<TiempoRespuestaView> TiempoRespuesta(LocalDateTime finicio, LocalDateTime ffinal){
        Iterable<Boleta> source = repository.findAll();
        List<Boleta> lista = new ArrayList<Boleta>();
        source.forEach(lista::add);

        lista = lista.stream().filter(b -> CompareDates(finicio, b.getServicio(), FindFecha.after) &&
                CompareDates(ffinal, b.getServicio(), FindFecha.before) &&
                b.isActive()).toList();
        List<TiempoRespuestaView> result = new ArrayList<>();

        for(int index = 0; index < lista.size(); index++){
            var boleta = lista.get(index);
            var servicio = boleta.getServicio();

            if(result.isEmpty()){
                result.add(getTiempoRespuesta(boleta, servicio));
            }else if(!result.isEmpty()){
                if(result.stream().anyMatch(t -> t.getTecnico().getId() == boleta.getTecnico().getId())){
                    var data = result.stream().filter(t -> t.getTecnico().getId() == boleta.getTecnico().getId()).findFirst().get();
                    data.setCantidadServicios(data.getCantidadServicios() + 1);
                    data.setPromedioRespuesta(data.getPromedioRespuesta() + calculateTime(servicio.getFInicio(), servicio.getFFinal()));
                }else{
                    result.add(getTiempoRespuesta(boleta, servicio));
                }
            }
        }

        result.forEach(r -> {
            r.setPromedioRespuesta(r.getPromedioRespuesta() / r.getCantidadServicios());
            if(r.getPromedioRespuesta() <= 2)
                r.setEstandar("Rápido");
            if(r.getPromedioRespuesta() >= 2)
                r.setEstandar("Promedio");
            if(r.getPromedioRespuesta() >= 4)
                r.setEstandar("Lento");
        });
       

        return result;
    }

    private TiempoRespuestaView getTiempoRespuesta(Boleta boleta, BoletaServicio servicio){
        var data = new TiempoRespuestaView();
        data.setTecnico(boleta.getTecnico());
        data.setCantidadServicios(1);
        data.setPromedioRespuesta(calculateTime(servicio.getFInicio(), servicio.getFFinal()));
        return data;
    }

    private RendimientoTecnicosView getTecnicosView(Boleta boleta, BoletaServicio servicio) {
        var data = new RendimientoTecnicosView();
        data.setTecnico(boleta.getTecnico());
        data.setCantidadServicios(1);
        data.setDuracionServicios(calculateTime(servicio.getFInicio(), servicio.getFFinal()));
        return data;
    }

    private long calculateTime(LocalDateTime inicio, LocalDateTime terminado) {
        if(terminado == null){
            terminado = LocalDateTime.now();
        }
        return ChronoUnit.HOURS.between(inicio, terminado);
    }

    private ProductosUtilizadosView getProductoView(BoletaServicio servicio, Producto producto) {
        var p = new ProductosUtilizadosView();
        p.setIdServicio(servicio.getId());
        p.setServicio(servicio.getServicio().getDescripcion());
        p.setIdProducto(producto.getId());
        p.setProducto(producto.getNombre());
        p.setPrecio(producto.getPrecio());
        p.setCantidad(1);
        return p;
    }

    private boolean CompareDates(LocalDateTime value, BoletaServicio servicios, FindFecha ff) {
        if (value == null)
            return true;

        if (ff == FindFecha.after) {
            return servicios.getFInicio().isAfter(value) || servicios.getFInicio().equals(value);
        } else if (ff == FindFecha.before) {
            if (!servicios.getFFinalHasValue())
                return true;
            return servicios.getFFinal().isBefore(value) || servicios.getFFinal().equals(value);
        }

        return false;
    }

}
