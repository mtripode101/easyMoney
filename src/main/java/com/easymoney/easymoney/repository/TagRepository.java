package com.easymoney.easymoney.repository;

import com.easymoney.easymoney.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findByLabelContainingIgnoreCase(String keyword);

    Tag findByLabel(String label);
}