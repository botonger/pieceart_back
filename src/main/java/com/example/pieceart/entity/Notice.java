package com.example.pieceart.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@ToString(exclude = "member")
@NoArgsConstructor //기본 생성자
@AllArgsConstructor //모든 필드값 파라미터로 받는 생성자
//@RequiredArgsConstructor //final, @NonNull 필드값만 받는 생성자
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message="title is required")
    @Column(nullable=false, length=50)
    private String title;

    @NotNull(message="content is required")
    @Column(nullable=false, length=4000)
    private String content;

    @Column
    private String password;

    @Column(name="view_count", columnDefinition = "int default '0'")
    private int viewCount;

    @Column(name="created_date", nullable = false, updatable = false)
    private LocalDate created;

    @Column(name="modified_date")
    private LocalDate modified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @PrePersist //entity 저장 전 실행
    public void onPrePersist(){
        this.created = LocalDate.now();
        this.modified = this.created;
    }
    @PreUpdate  //entity 업데이트 전 실행
    public void onPreUpdate(){
        this.modified = LocalDate.now();
    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setContent(String content){
        this.content = content;
    }
    public void setPassword(String password) { this.password = password; }
    public void increaseViewCount(int viewCount){
        this.viewCount = viewCount;
    }
}


