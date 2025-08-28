package com.easymoney.easymoney.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String label;

    @ManyToMany(mappedBy = "tags")
    private List<EasyMoney> easyMoneyEntries;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<EasyMoney> getEasyMoneyEntries() {
        return easyMoneyEntries;
    }

    public void setEasyMoneyEntries(List<EasyMoney> easyMoneyEntries) {
        this.easyMoneyEntries = easyMoneyEntries;
    }
}
