package com.example.pieceart.security.dto;

import lombok.*;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Log4j2
public class MemberDTO {
    private String email;
    private String name;
    private String password;
    private boolean isGoogle;
}

