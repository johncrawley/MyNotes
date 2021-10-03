package com.jcrawley.mynotes.repository;

import com.jcrawley.mynotes.list.ListItem;

import java.util.List;

public interface DocumentLinesRepository {
   List<ListItem> getDocumentLines(long documentId);
   void add(String contents, long documentId);
   void edit(long id, String contents);
   void delete(long id);
}
