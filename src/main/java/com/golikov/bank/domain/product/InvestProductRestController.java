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
    private InvestProductServise investProductServise;

    /**
     *  возвращает информацию о всех инвестиционных продуктах
     */
    @GetMapping("/all")
    public ResponseEntity<List<InvestProductDto>> allInvestProducts(){
        List<InvestProductDto> investProductDtos = investProductServise.findAllRest();
        HttpHeaders responseHeader = new HttpHeaders();
        return new ResponseEntity<>(investProductDtos, responseHeader, HttpStatus.OK);
    }

    /**
     *  возвращает информацию об инвестиционном продукте
     *  @param id - идентификатор инвестиционного продукта
     *  если продукт не найден, выбрасывается исключение
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvestProductDto> investProduct(@PathVariable("id") Long id) throws ResourceNotFoundException{
        InvestProductDto investProductDto = investProductServise.findByIdRest(id);
        HttpHeaders responseHeader = new HttpHeaders();
        return new ResponseEntity<>(investProductDto, responseHeader, HttpStatus.OK);
    }

    /**
     *  добавляет новый инвестиционный продукт
     *  @param investProductDto - тело запроса содежит информацию о продукте
     *  если продукт не найден, выбрасывается исключение
     */
    @PostMapping("/add")
    public ResponseEntity<InvestProductDto>  investProduct(@Valid @RequestBody InvestProductDto investProductDto) {
        InvestProductDto newProduct = investProductServise.addNewRest(investProductDto);
        HttpHeaders responseHeader = new HttpHeaders();
        return new ResponseEntity<>(newProduct, responseHeader, HttpStatus.OK);
    }

    /**
     *  обновляет инвестиционный продукт
     *  @param id - идентификатор инвестиционного продукта
     *  @param investProductDto - тело запроса содежит информацию о продукте
     *  если продукт не найден, выбрасывается исключение
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvestProductDto> investProductUpdate(@PathVariable("id") Long id,
                                                              @Valid @RequestBody InvestProductDto investProductDto)
                                                              throws ResourceNotFoundException {
        investProductServise.findByIdRest(id);
        InvestProductDto editedProduct = investProductServise.saveEditedRest(id, investProductDto);
        HttpHeaders responseHeader = new HttpHeaders();
        return new ResponseEntity<>(editedProduct, responseHeader, HttpStatus.OK);
    }

    /**
     *  удаляет инвестиционный продукт
     *  @param id - идентификатор инвестиционного продукта
     *  если продукт не найден, выбрасывается исключение
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> investProductDelete(@PathVariable("id") Long id) throws ResourceNotFoundException{
        investProductServise.deleteRest(id);
        HttpHeaders responseHeader = new HttpHeaders();
        return new ResponseEntity<>("Продукт с id: "+ id + " удалён", responseHeader, HttpStatus.OK);
    }



}
