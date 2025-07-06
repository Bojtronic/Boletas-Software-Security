package com.funcional.proyecto.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Boletas")
public class Boleta implements IExportModel {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "cliente_id", insertable = false, updatable = false)
    private Cliente cliente;

    @OneToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "servicio_id", insertable = false, updatable = false)
    private BoletaServicio servicio;

    @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "tecnico_id", insertable = false, updatable = false)
    private Usuario tecnico;

    @Column(nullable = false, columnDefinition = "text")
    private String firma;

    @Column(nullable = false, columnDefinition = "smallint")
    protected int active = 1;

    public Boleta() {
    }

    public Boleta(int id, Cliente cliente, BoletaServicio servicio, Usuario tecnico, String firma) {
        this.id = id;
        this.cliente = cliente;
        this.servicio = servicio;
        this.tecnico = tecnico;
        this.firma = firma;
    }

    public Boleta(int id, String tecnico, String cliente, BoletaServicio servicio) {
        this.id = id;
        this.tecnico = new Usuario(tecnico);
        this.cliente = new Cliente(cliente);
        this.servicio = servicio;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public BoletaServicio getServicio() {
        return servicio;
    }

    public void setServicio(BoletaServicio servicio) {
        this.servicio = servicio;
    }

    public Usuario getTecnico() {
        return tecnico;
    }

    public void setTecnico(Usuario tecnico) {
        this.tecnico = tecnico;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public boolean isActive() {
        return active == 1;
    }

    public void setActive(boolean active) {
        this.active = active ? 1 : 0;
    }

    @Override
    public int countHeader() {
        return 7;
    }

    @Override
    public String getHeader(int index) {
        String value = "";
        switch (index) {
            case 0:
                value = String.format("%1$-15s", "# Boleta");
                break;
            case 1:
                value = String.format("%1$-25s", "Técnico");
                break;
            case 2:
                value = String.format("%1$-25s", "Cliente");
                break;
            case 3:
                value = String.format("%1$-30s", "Descripción Servicio");
                break;
            case 4:
                value = String.format("%1$-28s", "Fecha Inicio");
                break;
            case 5:
                value = String.format("%1$-28s", "Fecha Finalizado");
                break;
            case 6:
                value = String.format("%1$-30s", "Productos");
                break;

            default:
                break;
        }
        return value;
    }

    //
    @Override
    public String getValue(int index) {
        String value = "";
        switch (index) {
            case 0:
                value = String.format("%1$-15s", String.valueOf(id));
                break;
            case 1:
                value = String.format("%1$-25s", tecnico.nombre + " " + tecnico.apellidos);
                break;
            case 2:
                value = String.format("%1$-25s", cliente.nombre + " " + cliente.apellidos);
                break;
            case 3:
                value = String.format("%1$-30s", servicio.getServicio().getDescripcion());
                break;
            case 4:
                value = String.format("%1$-28s", servicio.getFInicioToString());
                break;
            case 5:
                value = String.format("%1$-28s", servicio.getFFinalToString());
                break;
            case 6:
                value = String.format("%1$-30s", servicio.getProductosString());
                break;

            default:
                break;
        }
        return value;
    }

    @Override
    public boolean isValueArray(int index) {
        // solo la columan 4 es (productos) son arreglos
        if (index == 6)
            return true;
        return false;
    }

    @Override
    public int countValueArray() {
        return servicio.getProductos().size();
    }

    @Override
    public String getSubValue(int index) {
        String value = "";
        var producto = servicio.getProductos().get(index);
        if (producto != null) {
            value = String.format("%1$-25s", producto.getNombre() + " $" + producto.getPrecio());
        }
        return value;
    }
}
