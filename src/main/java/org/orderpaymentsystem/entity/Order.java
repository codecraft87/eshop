package org.orderpaymentsystem.entity;

import java.time.Instant;

import org.orderpaymentsystem.common.enums.OrderStatus;
import org.orderpaymentsystem.dto.OrderDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="USER_ID")
	private String userId;
	
	@Enumerated(EnumType.STRING)
	@Column(name="ORDER_STATUS")
	private OrderStatus status;
	
	@Column(name="AMOUNT")
	private double amount;
	
	@Column(name = "CREATED_AT")
	private Instant createdAt;
	
	@Column(name = "UPDATED_AT")
	private Instant updatedAt;
	
	public void set(OrderDTO dto) {
		if(dto.getOrderId()!=null) {
			this.id = dto.getOrderId();
		}
		this.amount = dto.getAmount();
		this.userId = dto.getUserId();
	}
	
	public static Order getOrderEntity(OrderDTO dto) {
		Order order = new Order();
		order.set(dto);
		return order;
	}
}
