package com.jcrawley.mynotes.repository;

import java.util.List;

public interface CategoryRepository {

    boolean create(String name);
    boolean exists(String name);
    List<String> getFiles(String name);
    boolean delete(String name);
}
