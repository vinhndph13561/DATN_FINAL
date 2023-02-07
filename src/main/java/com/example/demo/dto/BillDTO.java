package com.example.demo.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class BillDTO {

	private Long id;
	private String customer;
	private String staff;
	private Date createDay;
	private Double total;
	private String paymentType;
	private String status;
	private String note;
}
