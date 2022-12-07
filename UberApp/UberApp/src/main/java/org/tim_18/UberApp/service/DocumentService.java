package org.tim_18.UberApp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.Document;
import org.tim_18.UberApp.repository.DocumentRepository;

import java.util.HashSet;
import java.util.List;

@Service("documentService")

public class DocumentService {
    @Autowired
    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document addDocument(Document document) {
        return documentRepository.save(document);
    }

    public void remove(Integer id) {documentRepository.deleteById(id);}

    public List<Document> findAllDocuments() {
        return documentRepository.findAll();
    }

    public HashSet<Document> findByDriverId(Integer id){
        return documentRepository.findByDriverId(id);
    }
    public Document updateDocument(Document document) {
        return documentRepository.save(document);
    }

    public Document findDocumentById(Integer id) {
        return documentRepository.findDocumentById(id)
                .orElseThrow(() -> new UserNotFoundException("Driver by id " + id + " was not found"));
    }

}
