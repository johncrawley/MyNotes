package com.jcrawley.mynotes.repository;

public interface FileRepository {

    boolean exists(String filename, String category);
    boolean create(String filename, String category);
    boolean delete(String filename, String category);
    String getFilepath(String filename, String category);

}
