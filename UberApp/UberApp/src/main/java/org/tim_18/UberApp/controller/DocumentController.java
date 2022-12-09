package org.tim_18.UberApp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.model.Document;
import org.tim_18.UberApp.service.DocumentService;
import java.util.List;

@RestController
@RequestMapping("/document")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }
    @GetMapping("/all")
    public ResponseEntity<List<Document>> getAllDocuments () {
        List<Document> documents = documentService.findAllDocuments();
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Document> getDocumentById (@PathVariable("id") Integer id) {
        Document document = documentService.findDocumentById(id);
        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Document> addDocument(@RequestBody Document document) {
        Document newDocument = documentService.addDocument(document);
        return new ResponseEntity<>(newDocument, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Document> updateDocument(@RequestBody Document document) {
        Document updateDocument = documentService.updateDocument(document);
        return new ResponseEntity<>(updateDocument, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable("id") Integer id) {
        documentService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
