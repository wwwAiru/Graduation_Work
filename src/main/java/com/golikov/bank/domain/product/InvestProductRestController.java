package com.golikov.bank.domain.product;

import com.golikov.bank.domain.product.dto.InvestProductDto;
import com.golikov.bank.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public InvestProductDto investProduct(@PathVariable("id") Long id){
        return investProductServise.findByIdRest(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvestProductDto> investProductUpdate(@PathVariable("id") Long id,
                                                              @Valid @RequestBody InvestProductDto investProductDto)
                                                              throws ResourceNotFoundException {
        InvestProductDto productDtoOrErr = investProductServise.findByIdRest(id);
        InvestProductDto editedproduct = investProductServise.saveRest(id, investProductDto);
        HttpHeaders responseHeader = new HttpHeaders();
        return new ResponseEntity<>(editedproduct, responseHeader, HttpStatus.OK);
    }


}
