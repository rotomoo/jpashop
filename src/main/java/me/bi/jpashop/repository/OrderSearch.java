package me.bi.jpashop.repository;

import lombok.Data;
import me.bi.jpashop.domain.OrderStatus;

@Data
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus;
}
