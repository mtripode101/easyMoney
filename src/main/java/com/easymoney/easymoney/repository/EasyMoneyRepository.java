package com.easymoney.easymoney.repository;

import com.easymoney.easymoney.model.EasyMoney;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface EasyMoneyRepository extends JpaRepository<EasyMoney, Long> {
    List<EasyMoney> findByDateBetween(LocalDate start, LocalDate end);
    
    List<EasyMoney> findByDescriptionContainingIgnoreCase(String keyword);
}

