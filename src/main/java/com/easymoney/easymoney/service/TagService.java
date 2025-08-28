package com.easymoney.easymoney.service;

import com.easymoney.easymoney.model.Tag;
import com.easymoney.easymoney.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TagService {

    @Autowired
    private TagRepository repository;

    public Tag save(Tag tag) {
        return repository.save(tag);
    }

    public Tag findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada con id: " + id));
    }

    public List<Tag> findAll() {
        return repository.findAll();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Tag> searchByLabel(String keyword) {
        return repository.findByLabelContainingIgnoreCase(keyword);
    }

    public List<Tag> findAllById(List<Long> ids) {
        return ids.stream()
                .map(this::findById) // suponiendo que tenés un método findById(Long)
                .filter(Objects::nonNull)
                .toList();
    }
}