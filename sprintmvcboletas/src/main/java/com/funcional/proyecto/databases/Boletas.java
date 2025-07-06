package com.funcional.proyecto.databases;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;


import com.funcional.proyecto.models.Boleta;
import com.funcional.proyecto.models.BoletaRequest;
import com.funcional.proyecto.models.BoletaServicio;
import com.funcional.proyecto.models.Cliente;
import com.funcional.proyecto.models.IBoleta;
import com.funcional.proyecto.models.IBoletaServicio;
import com.funcional.proyecto.models.Servicio;
import com.funcional.proyecto.models.ServicioRequest;
import com.funcional.proyecto.models.Usuario;

public class Boletas extends BaseModel {

    private final IBoleta repository;
    private final IBoletaServicio servicios;    

    public Boletas(IBoleta boletaRepository, IBoletaServicio servicioRepository) {
        this.repository = boletaRepository;
        this.servicios = servicioRepository;
    }    

    public List<Boleta> List(int id, String tecnico, String cliente, String descripcion, String productos,
            LocalDateTime finicio, LocalDateTime ffinal) {
                Iterable<Boleta> source = repository.findAll();
                List<Boleta> lista = new ArrayList<Boleta>();
                source.forEach(lista::add);
        return lista.stream().filter(b -> (id == 0 || b.getId() == id) &&
                (Compare(tecnico, b.getTecnico().getNombre()) || Compare(tecnico, b.getTecnico().getApellidos())) &&
                (Compare(cliente, b.getCliente().getNombre()) || Compare(cliente, b.getCliente().getApellidos())) &&
                (Compare(descripcion, b.getServicio().getServicio().getDescripcion())) &&
                (Compare(productos, b.getServicio().getProductos())) &&
                (CompareDates(finicio, b.getServicio(), FindFecha.after)) &&
                (CompareDates(ffinal, b.getServicio(), FindFecha.before)) &&
                b.isActive())
                .toList();
    }

    public void Delete(int id) {
        Optional<Boleta> boleta = repository.findById(id);
        if(boleta.isPresent()){
            Boleta entity = boleta.get();
            entity.setActive(false);
            repository.save(entity);
        }
    }

    public void Update(int id, BoletaRequest request) {
        Optional<Boleta> boleta = repository.findById(id);
        if(boleta.isPresent()){            
            Boleta entity = boleta.get();
            BoletaServicio servicio = boleta.get().getServicio();
            Cliente cliente = boleta.get().getCliente();
            Usuario tecnico = boleta.get().getTecnico();
            ServicioRequest sq = request.getServicio();
            Servicio serv = servicio.getServicio();
            serv = new Servicio();
            serv.setId(sq.getId());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            if(!sq.getFechaInicio().isEmpty()){
                var fecha = sq.getFechaInicio().replace('T', ' ');
                LocalDateTime dateTime = LocalDateTime.parse(fecha, formatter);
                servicio.setFInicio(dateTime);
            }

            if(!sq.getFechaFinal().isEmpty()){
                var fecha = sq.getFechaFinal().replace('T', ' ');
                LocalDateTime dateTime = LocalDateTime.parse(fecha, formatter);
                servicio.setFFinal(dateTime);
            }
            servicio.setProductos(null);            
            if(request.getServicio().getId() != servicio.getServicio().getId()){
                servicio.setServicio(new Servicio());
                servicio.getServicio().setId(request.getServicio().getId());;
            }
            servicios.save(servicio);
            
            servicio.setProductos(sq.getProductos());
            servicios.save(servicio);

            if(!request.getIdCliente().isEmpty()){
                cliente = new Cliente();
                cliente.setId(Integer.parseInt(request.getIdCliente()));
            }
               
            if(!request.getIdTecnico().isEmpty()){
                tecnico = new Usuario();
                tecnico.setId(Integer.parseInt(request.getIdTecnico()));
            }
               
            
             entity.setCliente(cliente);
             entity.setTecnico(tecnico);
             entity.setServicio(servicio);
             repository.save(entity);
        }
    }

    public void Nuevo(BoletaRequest request) {
        Boleta entity = new Boleta();
        BoletaServicio servicio = new BoletaServicio();
        servicio = request.getServicio().getClassTo();      
        servicios.save(servicio);

        Cliente cliente = new Cliente();
        Usuario tecnico = new Usuario();
        if(!request.getIdCliente().isEmpty())
             cliente.setId(Integer.parseInt(request.getIdCliente()));
        if(!request.getIdTecnico().isEmpty())
             tecnico.setId(Integer.parseInt(request.getIdTecnico()));
        entity.setCliente(cliente);
        entity.setTecnico(tecnico);
        entity.setServicio(servicio);
        entity.setFirma(request.getFirma());
        
        repository.save(entity);
        

        
    }

    public Boleta get(int id) {
        Optional<Boleta> boleta = repository.findById(id);
        if(boleta.isPresent()){
            return boleta.get();
        }        
        return null;
    }

    private boolean CompareDates(LocalDateTime value, BoletaServicio servicios, FindFecha ff){
        if(value == null)
            return true;
        
        if(ff == FindFecha.after){
            return servicios.getFInicio().isAfter(value) || servicios.getFInicio().equals(value);
        }
        else if(ff == FindFecha.before){
            return servicios.getFFinal().isBefore(value) || servicios.getFFinal().equals(value);
        }

        return false;
    }


}
