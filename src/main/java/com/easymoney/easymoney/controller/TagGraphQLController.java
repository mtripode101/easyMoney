package com.easymoney.easymoney.controller;

import com.easymoney.easymoney.model.Tag;
import com.easymoney.easymoney.model.EasyMoney;
import com.easymoney.easymoney.service.TagService;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TagGraphQLController {

    private final TagService tagService;

    public TagGraphQLController(TagService tagService) {
        this.tagService = tagService;
    }

    // 🔍 Queries

    @QueryMapping
    public List<Tag> allTags() {
        return tagService.findAll();
    }

    @QueryMapping
    public Tag tagById(@Argument Long id) {
        return tagService.findById(id);
    }

    @QueryMapping
    public List<Tag> searchTags(@Argument String keyword) {
        return tagService.searchByLabel(keyword);
    }

    // 🧩 Resolver de relación: Tag → EasyMoney

    @SchemaMapping(typeName = "Tag", field = "easyMoneyEntries")
    public List<EasyMoney> resolveEntries(Tag tag) {
        return tag.getEasyMoneyEntries();
    }

    // ✏️ Mutations

    @MutationMapping
    public Tag createTag(@Argument String label) {
        Tag tag = new Tag();
        tag.setLabel(label);
        return tagService.save(tag);
    }

    @MutationMapping
    public Boolean deleteTag(@Argument Long id) {
        tagService.delete(id);
        return true;
    }
}