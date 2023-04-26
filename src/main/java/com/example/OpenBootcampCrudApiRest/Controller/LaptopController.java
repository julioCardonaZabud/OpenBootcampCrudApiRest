package com.example.OpenBootcampCrudApiRest.Controller;

import com.example.OpenBootcampCrudApiRest.Entity.LaptopEntity;
import com.example.OpenBootcampCrudApiRest.Repository.LaptopRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Status;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/app")
public class LaptopController {

    @Autowired
    LaptopRepository repository;

    @ApiOperation(value = "Se realiza una consulta que retorna todos los objetos Laptop registrados en la BD")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Devuelve una lista con todas las laptops registradas en BD"),
            @ApiResponse(code = 204, message = "NOT_CONTECT si no hay Laptop registradas en BD")})
    @GetMapping("/laptops")
    public ResponseEntity findAll() {
        ResponseEntity response;
        List<LaptopEntity> laptops = repository.findAll();
        return (laptops.size() != 0) ? new ResponseEntity<>(laptops, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @ApiOperation(value = "Se realiza consulta de Laptop por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Devuelve JSON con la informacion de la laptop encontrad con el ID " +
                    "ingresado"),
            @ApiResponse(code = 404, message = "NOT_FOUND si no hay coincidencias de Laptop con el ID ingresado")})
    @GetMapping("/laptop/{id}")
    public ResponseEntity<LaptopEntity> findById(@PathVariable long id) {
        Optional<LaptopEntity> optional = repository.findById(id);
        return (optional.isPresent()) ? new ResponseEntity<>(optional.get(), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Método para crear una nueva Laptop, Verifica que la laptop ingresada no sea NULL")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Devuelve el objeto laptop creado."),
            @ApiResponse(code = 400, message = "Bad request, Objeto NULL")})
    @PostMapping("laptop")
    public ResponseEntity create(@RequestBody LaptopEntity laptop) {
        if (isNull(laptop) || repository.existsById(laptop.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return (new ResponseEntity<LaptopEntity>(repository.save(laptop), HttpStatus.OK));
    }

    @ApiOperation(value = "Actualiza una Laptop existente, Verifica que la laptop ingresada no sea NULL y que exista")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Devuelve el objeto laptop modificado."),
            @ApiResponse(code = 400, message = "Laptop no existe"),
            @ApiResponse(code = 400, message = "Bad request, Objeto NULL")})
    @PutMapping("laptop")
    public ResponseEntity update(@RequestBody LaptopEntity laptop) {
        if (isNull(laptop) || (!repository.existsById(laptop.getId()))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return (new ResponseEntity<LaptopEntity>(repository.save(laptop), HttpStatus.OK));
    }

    public boolean isNull(LaptopEntity laptop) {
        if (laptop.getBrand() == null && laptop.getDate() == null && laptop.getModel() == null) {
            return true;
        }else{
            return false;
        }
    }
}
