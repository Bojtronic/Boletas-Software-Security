package com.funcional.proyecto.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.funcional.proyecto.databases.Reportes;
import com.funcional.proyecto.models.IBoleta;
import com.funcional.proyecto.models.IExportModel;
import com.funcional.proyecto.models.IUsuario;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Base64;

@Controller
public class ReportController extends SeguridadControllers {
    private final Reportes Reportes;

    public ReportController(IBoleta BoletaRespository, IUsuario usuarioRepository) {
        super(usuarioRepository);
        Reportes = new Reportes(BoletaRespository);
    }

    @RequestMapping(value = "reporte/main", method = RequestMethod.GET)
    public String requestMethodName(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        model.addAttribute("messageError", "");

        return "reporte/index";
    }

    @RequestMapping(value = "reporte/ServiciosRealizados", method = RequestMethod.GET)
    public String reporteServiciosR(
            @RequestParam(value = "finial", required = false) String finicial,
            @RequestParam(value = "ffinal", required = false) String ffinal,
            @CookieValue(value = "_id", defaultValue = "") String cookieValue,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        LocalDateTime FInicio = finicial.isEmpty() ? null : LocalDateTime.parse(finicial.concat("T00:00"));
        LocalDateTime FFinal = ffinal.isEmpty() ? null : LocalDateTime.parse(ffinal.concat("T23:59"));

        var data = Reportes.serviciosRecibidos(FInicio, FFinal);
        List<IExportModel> exportData = new ArrayList<>();
        data.stream().forEach(b -> {
            exportData.add(b);
        });
        model.addAttribute("data", data);
        model.addAttribute("archivoTXT", exportFormatTXT(exportData, "Reporte - Servicios Realizados",""));
        model.addAttribute("archivoCSV", exportFormatCSV(exportData, "Reporte - Servicios Realizados",""));

        return "reporte/serviciosRealizados";
    }

    @RequestMapping(value = "reporte/UsuarioRecurrentes", method = RequestMethod.GET)
    public String reporteUsuariosR(
            @RequestParam(value = "finial", required = false) String finicial,
            @RequestParam(value = "ffinal", required = false) String ffinal,
            @CookieValue(value = "_id", defaultValue = "") String cookieValue,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        LocalDateTime FInicio = finicial.isEmpty() ? null : LocalDateTime.parse(finicial.concat("T00:00"));
        LocalDateTime FFinal = ffinal.isEmpty() ? null : LocalDateTime.parse(ffinal.concat("T23:59"));

        var data = Reportes.usuarioRecurrentes(FInicio, FFinal);
        List<IExportModel> exportData = new ArrayList<>();
        data.getData().stream().forEach(b -> {
            exportData.add(b);
        });
        model.addAttribute("data", data);
        model.addAttribute("archivoTXT", exportFormatTXT(exportData, "Reporte - Usuario Recurrentes", String.format("Frecuencia de Visitas: %s", String.valueOf(data.getFrecuenciaVisitas()))));
        model.addAttribute("archivoCSV", exportFormatCSV(exportData, "Reporte - Usuario Recurrentes", String.format("Frecuencia de Visitas: %s", String.valueOf(data.getFrecuenciaVisitas()))));

        return "reporte/usuarioRecurrentes";
    }

    @RequestMapping(value = "reporte/ProductosUtilizados", method = RequestMethod.GET)
    public String reporteProductosU(
            @RequestParam(value = "finial", required = false) String finicial,
            @RequestParam(value = "ffinal", required = false) String ffinal,
            @CookieValue(value = "_id", defaultValue = "") String cookieValue,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        LocalDateTime FInicio = finicial.isEmpty() ? null : LocalDateTime.parse(finicial.concat("T00:00"));
        LocalDateTime FFinal = ffinal.isEmpty() ? null : LocalDateTime.parse(ffinal.concat("T23:59"));        

        var data = Reportes.productosUtilizados(FInicio, FFinal);
        List<IExportModel> exportData = new ArrayList<>();
        data.getProductos().stream().forEach(b -> {
            exportData.add(b);
        });
        model.addAttribute("data", data);
        model.addAttribute("archivoTXT", exportFormatTXT(exportData, "Reporte - Productos Utilizados", String.format("Costo Total: %s", String.valueOf(data.getCostoTotal()))));
        model.addAttribute("archivoCSV", exportFormatCSV(exportData, "Reporte - Productos Utilizados", String.format("Costo Total: %s", String.valueOf(data.getCostoTotal()))));
        return "reporte/productosUtilizados";
    }

    @RequestMapping(value = "reporte/RendimientoTecnicos", method = RequestMethod.GET)
    public String reporteRendimientoT(
            @RequestParam(value = "finial", required = false) String finicial,
            @RequestParam(value = "ffinal", required = false) String ffinal,
            @CookieValue(value = "_id", defaultValue = "") String cookieValue,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        LocalDateTime FInicio = finicial.isEmpty() ? null : LocalDateTime.parse(finicial.concat("T00:00"));
        LocalDateTime FFinal = ffinal.isEmpty() ? null : LocalDateTime.parse(ffinal.concat("T23:59"));        
        
        var data = Reportes.RendimientoTecnicos(FInicio, FFinal);
        List<IExportModel> exportData = new ArrayList<>();
        data.stream().forEach(b -> {
            exportData.add(b);
        });
        model.addAttribute("data", data);
        model.addAttribute("archivoTXT", exportFormatTXT(exportData, "Reporte - Rendimiento Técnicos", ""));
        model.addAttribute("archivoCSV", exportFormatCSV(exportData, "Reporte - Rendimiento Técnicos", ""));

        return "reporte/rendimientoTecnicos";
    }

    @RequestMapping(value = "reporte/TiempoRespuesta", method = RequestMethod.GET)
    public String reporteTiempoR(
            @RequestParam(value = "finial", required = false) String finicial,
            @RequestParam(value = "ffinal", required = false) String ffinal,
            @CookieValue(value = "_id", defaultValue = "") String cookieValue,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        LocalDateTime FInicio = finicial.isEmpty() ? null : LocalDateTime.parse(finicial.concat("T00:00"));
        LocalDateTime FFinal = ffinal.isEmpty() ? null : LocalDateTime.parse(ffinal.concat("T23:59"));

        model.addAttribute("data", Reportes.TiempoRespuesta(FInicio, FFinal));

        var data = Reportes.TiempoRespuesta(FInicio, FFinal);
        List<IExportModel> exportData = new ArrayList<>();
        data.stream().forEach(b -> {
            exportData.add(b);
        });
        model.addAttribute("data", data);
        model.addAttribute("archivoTXT", exportFormatTXT(exportData, "Reporte - Tiempo Respuesta", ""));
        model.addAttribute("archivoCSV", exportFormatCSV(exportData, "Reporte - Tiempo Respuesta", ""));

        return "reporte/tiempoRespuesta";
    }

    private String exportFormatTXT(List<IExportModel> source, String titulo, String label) {
        if (source.isEmpty())
            return "";

        try {
            FileWriter fw;
            PrintWriter pw;
            File archivo = new File(".\\", String.format("%s.%s", titulo, "txt"));
            fw = new FileWriter(archivo.getPath(), false);
            pw = new PrintWriter(fw);

            pw.append(String.format("%s", titulo));
            pw.append("\n");            
            if(!label.isEmpty()){
                pw.append(String.format("%s", label));
                pw.append("\n");
            }
            var header = source.get(0);
            for (int i = 0; i < header.countHeader(); i++) {
                pw.append(header.getHeader(i));
            }
            pw.append("\n");
            for (int i = 0; i < source.size(); i++) {
                var linea = source.get(i);
                for (int fila = 0; fila < linea.countHeader(); fila++) {
                    if (linea.isValueArray(fila)) {
                        for (int index = 0; index < linea.countValueArray(); index++) {
                            if (index == 0)
                                pw.append(linea.getSubValue(index));
                            else {
                                pw.append("\n");
                                pw.append(String.format("%1$-65s", "-"));
                                pw.append(linea.getSubValue(index));
                            }
                        }
                    } else {
                        pw.append(linea.getValue(fila));
                    }
                }
                pw.append("\n");
            }

            fw.close();

            byte[] bytes = Files.readAllBytes(archivo.toPath());
            String b64 = Base64.getEncoder().encodeToString(bytes);
            return b64;
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    private String exportFormatCSV(List<IExportModel> source, String titulo, String label) {
        if (source.isEmpty())
            return "";

        try {
            FileWriter fw;
            PrintWriter pw;
            File archivo = new File(".\\", String.format("%s.%s", titulo, "csv"));
            fw = new FileWriter(archivo.getPath(), false);
            pw = new PrintWriter(fw);

            pw.append(String.format("%s", titulo));
            pw.append("\n");
            if(!label.isEmpty()){
                pw.append(String.format("%s", label));
                pw.append("\n");
            }
            
            var header = source.get(0);
            for (int i = 0; i < header.countHeader(); i++) {
                pw.append(header.getHeader(i).trim());
                if (i != header.countHeader() - 1)
                    pw.append(";");
            }
            pw.append("\n");
            boolean notSubItem = false;
            for (int i = 0; i < source.size(); i++) {
                var linea = source.get(i);
                String pre = "";
                for (int fila = 0; fila < linea.countHeader(); fila++) {
                  
                    if (linea.isValueArray(fila)) {
                        notSubItem = true;
                        for (int index = 0; index < linea.countValueArray(); index++) {
                            String rowData = "";
                            rowData += linea.getSubValue(index).trim();
                            if (index != linea.countValueArray() - 1)
                                rowData += ";";// pw.append(";");
                            
                            pw.append(pre + rowData);
                            pw.append("\n");
                        }                        
                    }
                    else{
                        pre += linea.getValue(fila).trim();
                        // pw.append(linea.getValue(fila).trim());
                        if (fila != linea.countHeader() - 1)
                            pre += ";";// pw.append(";");
                    }
                    if(fila == linea.countHeader() - 1)
                    if(!notSubItem){
                        pw.append(pre);
                    }
                }
                
                pw.append("\n");
            }

            fw.close();

            byte[] bytes = Files.readAllBytes(archivo.toPath());
            String b64 = Base64.getEncoder().encodeToString(bytes);
            return b64;
        } catch (Exception e) {
            return e.getMessage();
        }

    }
}
