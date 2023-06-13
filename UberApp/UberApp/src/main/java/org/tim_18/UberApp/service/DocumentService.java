package org.tim_18.UberApp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.exception.DocumentNotFoundException;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.Document;
import org.tim_18.UberApp.repository.DocumentRepository;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;

@Service("documentService")
@Transactional
public class DocumentService {
    @Autowired
    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {this.documentRepository = documentRepository;}
    public Document addDocument(Document document) {return documentRepository.save(document);}
    public void deleteDocumentById(int id) {documentRepository.deleteDocumentById(id);}
    public HashSet<Document> findByDriverId(Integer id){return documentRepository.findByDriverId(id);}
    public Document updateDocument(Document document) {return documentRepository.save(document);}
    public Document findDocumentById(Integer id) {
        return documentRepository.findDocumentById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document by id " + id + " was not found"));
    }

}
