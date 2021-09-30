package com.jcrawley.mynotes.repository;

import com.jcrawley.mynotes.list.ListItem;

import java.util.List;

public interface CategoryRepository {

    long create(String name);
    boolean exists(String name);
    List<ListItem> getCategories();
    boolean delete(String name);
}
