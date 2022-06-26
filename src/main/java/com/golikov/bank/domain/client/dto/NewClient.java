package com.golikov.bank.domain.client.dto;

import com.golikov.bank.domain.client.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class NewClient {

    @NotBlank(message = "Обязательное поле")
    @Size(min = 1, max = 100, message = "Введите имя минимум 1 символ, максимум 100")
    private String firstName;

    @NotBlank(message = "Обязательное поле")
    @Size(min = 1, max = 100, message = "Введите фамилию минимум 1 символ, максимум 100")
    private String lastName;

    @NotBlank(message = "Обязательное поле")
    @Size(min = 1, max = 100, message = "Введите отчество минимум 1 символ, максимум 100")
    private String middleName;

    @NotBlank(message = "Обязательное поле")
    @Email(message = "Введите действительный адрес электронной почты")
    private String email;

    @NotBlank(message = "Обязательное поле")
    @Size(min = 8, max = 16, message = "Длина пароля должна быть не менее 8 и не более 16 символов")
    private String password;

    private BigDecimal balance = new BigDecimal(0);

//    private List<Account> accounts;

    private boolean active;

    private Set<Role> roles;

    public NewClient() {
    }


}
