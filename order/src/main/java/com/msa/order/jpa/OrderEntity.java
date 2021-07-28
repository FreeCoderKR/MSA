package com.msa.order.jpa;


import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "orders")
public class OrderEntity implements Serializable {
    //data의 마샬 언마샬링을 위해 serializable(직렬화)해준다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120, unique = true)
    private String productId;
    @Column(nullable = false)
    private int qty;
    @Column(nullable = false)
    private int unitPrice;
    @Column(nullable = false)
    private int totalPrice;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false, unique = true)
    private String orderId;
    @Column(nullable = false, updatable = false, insertable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private Date createdAt;
}
