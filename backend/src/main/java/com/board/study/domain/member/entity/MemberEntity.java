package com.board.study.domain.member.entity;

import com.board.study.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "member_sq")
    @SequenceGenerator(name = "member_sq",sequenceName = "MEMBER_SQ",allocationSize = 1)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "login_id",unique = true,nullable = false,length = 100)
    private String loginId;

    @Column(nullable = false,length = 100)
    private String password;

    @Column(unique = true,nullable = false,length = 100)
    private String nickname;

    @Column(unique = true,nullable = false,length = 100)
    private String email;

    @Column(unique = true,nullable = false,length = 20)
    private String phone;

    @Column(length = 100)
    private String provider;

    @Column(name = "provider_id",length = 1100)
    private String providerId;

    @Column(name = "deleted_yn",nullable = false,length = 1)
    @Builder.Default
    private String deletedYn = "N";
}
