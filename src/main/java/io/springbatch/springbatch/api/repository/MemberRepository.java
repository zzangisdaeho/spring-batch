package io.springbatch.springbatch.api.repository;

import io.springbatch.springbatch.api.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
}
