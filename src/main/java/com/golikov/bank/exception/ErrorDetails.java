package com.golikov.bank.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * класс для создания ошибок с детальной информацией
 */
@Data
@AllArgsConstructor
public class ErrorDetails {

	private Date timestamp;

	private String message;

	private String details;
}
