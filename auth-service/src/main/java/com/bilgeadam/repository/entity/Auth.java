package com.bilgeadam.repository.entity;

import com.bilgeadam.repository.enums.ERole;
import com.bilgeadam.repository.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 *      1-Repostiroy
 *      2-service
 *      3-controller katmanları olusturulacak
 *     -register metodu yazılacak ve buna bir end point yazılacak
 *     bu işlemler request dto ile yapılacak, donus tipide bir response dto olsun
 *     @Postmapping ile olsun
 */
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity // DB'deki entity eşleşmesini yapmak için gerekli
// @Table(name = "")
public class Auth extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String email;
    private String password;
    private String activationCode;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ERole role=ERole.USER;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EStatus status=EStatus.PENDING;
}
