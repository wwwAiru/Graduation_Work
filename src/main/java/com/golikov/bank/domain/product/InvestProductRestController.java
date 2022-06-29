package com.golikov.bank.domain.product;

import com.golikov.bank.domain.account.AccountService;
import com.golikov.bank.domain.product.dto.InvestProductResp;
import com.golikov.bank.domain.product.mapper.InvestProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/product")
public class InvestProductRestController {
    @Autowired
    InvestProductServise investProductServise;

    @Autowired
    AccountService accountService;


    @GetMapping("/all")
    public List<InvestProductResp> allInvestProducts(){
        return investProductServise.findAllRest();
    }

    @GetMapping("/{id}")
    public InvestProductResp investProduct(@PathVariable("id") Long id){
        return investProductServise.findByIdRest(id);
    }

    @PutMapping("/{id}")
    public InvestProductResp investProductEdit(@PathVariable("id") InvestProduct investProduct,
                                               @Valid @RequestBody InvestProductResp investProductResp){
        // тут будет обработка сохранения
        return investProductServise.findByIdRest(investProduct.getId());
    }


}
