package com.jcrawley.mynotes.repository;

import com.jcrawley.mynotes.list.ListItem;

import java.util.List;

public interface DocumentRepository {

    boolean exists(String filename, long categoryId);
    boolean create(String filename, long categoryId);
    List<ListItem> getFiles(long categoryId);
    void delete(long documentId);
    String getFilepath(String filename, long categoryId);
    void deleteAllWithCategoryId(long categoryId);

}
