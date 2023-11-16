package com.example.praca.repository;

import com.example.praca.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Daniel Lezniak
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {
}
