package com.example.pieceart.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @PositiveOrZero
    private int currentPrice;

    @Column
    private LocalDateTime bidDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="works_id")
    private Works works;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @PrePersist //entity 저장 전 실행
    public void onPrePersist(){
        this.bidDate = LocalDateTime.now();
    }

    public void setCurrentPrice(int currentPrice){
        this.currentPrice = currentPrice;
    }
}
