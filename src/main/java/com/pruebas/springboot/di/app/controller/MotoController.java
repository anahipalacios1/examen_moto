package com.pruebas.springboot.di.app.controller;

import com.pruebas.springboot.di.app.models.Moto;
import com.pruebas.springboot.di.app.models.Proveedor;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/motos")
public class MotoController{

    @GetMapping("/listar")
    public String listarMoto(Model model) {
        MotoManager motoManager = new MotoManager();
        List<Moto> motos = motoManager.getAllMoto();
        model.addAttribute("titulo", "Lista de Motos");
        model.addAttribute("idmoto", "ID moto");
        model.addAttribute("idproveedor", "ID Proveedor");
        model.addAttribute("monto", "Monto a eliminar");
        model.addAttribute("motos", motos);
        return "moto-template/listar";
    }
    
    @GetMapping("/agregar")
    public String agregarMoto(Model model) {
        Moto moto = new Moto();
        model.addAttribute("titulo", "Agregar Moto");
        model.addAttribute("moto", moto);
        model.addAttribute("error", new HashMap<>());
        return "moto-template/agregar";
    }

    @PostMapping("/agregar")
    public String agregarMotoProc(@Valid Moto moto, BindingResult result, Model model,
                                   @RequestParam(name="idproveedor") int idproveedor) throws SQLException {
        model.addAttribute("titulo", "Falta datos");
        if(result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->{
                errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
            });
            model.addAttribute("error", errores);
            return "moto-template/agregar";
        }
        MotoManager motoManager = new MotoManager();
        moto = motoManager.add(idproveedor);
        model.addAttribute("idmoto", "ID Moto");
        model.addAttribute("idproveedor", "ID Proveedor");
        model.addAttribute("monto", "Monto");
        model.addAttribute("moto", moto);
        return "moto-template/resultado";
    }
    
    
    @GetMapping("/buscar")
    public String buscarMoto(Model model) {
        Moto moto = new Moto();
        model.addAttribute("titulo", "Buscar moto");
        model.addAttribute("moto", moto);
        model.addAttribute("error", new HashMap<>());
        return "moto-template/buscar";
    }

    @PostMapping("/buscar")
    public String buscarPro(@Valid Moto moto, BindingResult result, Model model,
                                 @RequestParam(name= "idmoto") int idmoto) throws SQLException {

        if(result.hasGlobalErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->{
                errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
            });
            model.addAttribute("titulo", "Debe ser numero entero");
            model.addAttribute("error", errores);
            return "moto-template/buscar";
        }
        
        MotoManager motoManager = new MotoManager();
        moto = motoManager.getByid(idmoto);
        
        model.addAttribute("idmoto", "ID Moto");
        model.addAttribute("idproveedor", "ID proveedor");
        model.addAttribute("monto", "Monto");
        model.addAttribute("titulo", "Moto Encontrada");
        model.addAttribute("moto", moto);
        return "moto-template/resultado";
    }
    
    @GetMapping("/modificar")
    public String modificarMoto(Model model) {
        Moto moto = new Moto();
        model.addAttribute("titulo", "Modificar proveedor Habilitado");
        model.addAttribute("moto", moto);
        model.addAttribute("error", new HashMap<>());
        return "moto-template/modificar";
    }

    @PostMapping("/modificar")
    public String modificarMotoProc(@Valid Moto moto, BindingResult result, Model model,
                            @RequestParam(name="idmoto") int idmoto,
                            @RequestParam(name="monto") int monto) {
        model.addAttribute("titulo", "Falta datos");
        if(result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->{
                errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
            });
            model.addAttribute("titulo", "Modificar proveedor Habilitado");
            model.addAttribute("error", errores);
            return "moto-template/modificar";
        }
        MotoManager motoManager = new MotoManager();
        motoManager.modify(idmoto,monto);
        moto.setIdmoto(idmoto);
        moto.setMonto(monto);        
        model.addAttribute("idmoto", "ID moto");
        model.addAttribute("monto", "Monto a Pagar");
        model.addAttribute("moto", moto);
        model.addAttribute("titulo","moto modificado ");
        return "moto-template/resultado2";
    }

    @GetMapping("/eliminar")
    public String eliminarMoto(Model model) {
        Moto moto = new Moto();
        model.addAttribute("titulo", "Buscar Moto");
        model.addAttribute("moto", moto);
        model.addAttribute("error", new HashMap<>());
        return "moto-template/eliminar";
    }

    @PostMapping("/eliminar")
    public String eliminarMotoPro(@Valid Moto moto, BindingResult result, Model model,
                            @RequestParam(name= "idmoto") int idmoto) throws SQLException {

        if(result.hasGlobalErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->{
                errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
            });
            model.addAttribute("titulo", "Debe ser numero entero");
            model.addAttribute("error", errores);
            return "moto-template/eliminar";
        }
        
        MotoManager motoManager = new MotoManager();
        moto = motoManager.getByid(idmoto);

        if(motoManager.delete(idmoto)==false){
            model.addAttribute("idmoto", "ID moto");
            model.addAttribute("idproveedor", "ID proveedor");
            model.addAttribute("monto", "Monto a eliminar");
            model.addAttribute("titulo","No se puede eliminar, la moto esta en la lista de compras de Proveedores");
            model.addAttribute("moto", moto);
        }else{
            if(moto==null){
                model.addAttribute("idmoto", "");
                model.addAttribute("idproveedor", "");
                model.addAttribute("monto", "");
                model.addAttribute("titulo","La moto no esta en la base de datos");
                model.addAttribute("moto", moto);
            }
            else{
                model.addAttribute("idmoto", "ID moto");
                model.addAttribute("idproveedor", "ID proveedor");
                model.addAttribute("monto", "Monto a eliminar");
                model.addAttribute("titulo","La moto se ha eliminado de la base de datos");
                model.addAttribute("moto", moto);
            }
        }

        return "moto-template/resultado";

    }
}
