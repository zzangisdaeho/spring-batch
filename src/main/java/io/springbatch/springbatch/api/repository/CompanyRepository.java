package io.springbatch.springbatch.api.repository;

import io.springbatch.springbatch.api.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
}
