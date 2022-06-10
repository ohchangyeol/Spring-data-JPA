package jpabook.jpashop.domain;

import lombok.*;

@Getter @Setter
public class OrderSearch {

    private String memberName;
    private OrderStatus status;

}
