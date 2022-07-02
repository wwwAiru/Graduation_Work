package com.golikov.bank.domain.product;

import com.golikov.bank.domain.product.dto.InvestProductDto;
import com.golikov.bank.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/product")
public class InvestProductRestController {
    @Autowired
    InvestProductServise investProductServise;

    @GetMapping("/all")
    public List<InvestProductDto> allInvestProducts(){
        return investProductServise.findAllRest();
    }

    @GetMapping("/{id}")
    public InvestProductDto investProduct(@PathVariable("id") Long id) throws ResourceNotFoundException{
        return investProductServise.findByIdRest(id);
    }

    @PostMapping("/add")
    public ResponseEntity<InvestProductDto>  investProduct(@Valid @RequestBody InvestProductDto investProductDto) {
        InvestProductDto newProduct = investProductServise.addNewRest(investProductDto);
        HttpHeaders responseHeader = new HttpHeaders();
        return new ResponseEntity<>(newProduct, responseHeader, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<InvestProductDto> investProductUpdate(@PathVariable("id") Long id,
                                                              @Valid @RequestBody InvestProductDto investProductDto)
                                                              throws ResourceNotFoundException {
        investProductServise.findByIdRest(id);
        InvestProductDto editedProduct = investProductServise.saveEditedRest(id, investProductDto);
        HttpHeaders responseHeader = new HttpHeaders();
        return new ResponseEntity<>(editedProduct, responseHeader, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> investProductDelete(@PathVariable("id") Long id) throws ResourceNotFoundException{
        Map<String, Boolean> response = new HashMap<>();
        investProductServise.deleteRest(id);
        response.put("deleted", Boolean.TRUE);
        return response;
    }



}
