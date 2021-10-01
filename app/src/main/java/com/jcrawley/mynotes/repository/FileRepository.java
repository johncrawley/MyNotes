package com.jcrawley.mynotes.repository;

import com.jcrawley.mynotes.list.ListItem;

import java.util.List;

public interface FileRepository {

    boolean exists(String filename, long categoryId);
    boolean create(String filename, long categoryId);
    List<ListItem> getFiles(long categoryId);
    boolean delete(String filename, long categoryId);
    String getFilepath(String filename, long categoryId);

}
