package com.board.study.domain.member.repository;

import com.board.study.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity,Long> {

    @Query("SELECT COUNT(m) >0 FROM MemberEntity m WHERE m.loginId = :loginId")
    boolean existsByLoginId(@Param("loginId") String loginId);

    @Query("SELECT COUNT(m) >0 FROM MemberEntity m WHERE m.nickname = :nickname ")
    boolean existsByNickname(@Param("nickname") String nickname);

    @Query("SELECT COUNT(m) >0 FROM MemberEntity m WHERE m.email = :email ")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT COUNT(m) >0 FROM MemberEntity m WHERE m.phone = :phone ")
    boolean existsByPhone(@Param("phone") String phone);

    Optional<MemberEntity> findByLoginId(String loginId);
}
