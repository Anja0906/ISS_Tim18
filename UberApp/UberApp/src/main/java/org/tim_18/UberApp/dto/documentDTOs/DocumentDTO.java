package org.tim_18.UberApp.dto.documentDTOs;


import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.tim_18.UberApp.model.Document;

import java.util.HashSet;
@Data
public class DocumentDTO {
    private Integer id;
    @Length(max = 100)
    private String name;
    private String documentImage;
    private Integer driverId;

    public DocumentDTO() {}

    public DocumentDTO(Document document){
        this(document.getId(),document.getName(),
             document.getDocumentImage(), document.getDriver().getId());
    }
    public DocumentDTO(Integer id, String name, String documentImage, Integer driverId) {
        this.id                 = id;
        this.name               = name;
        this.documentImage      = documentImage;
        this.driverId           = driverId;
    }

    public HashSet<DocumentDTO> makeDocumentsDTO(HashSet<Document> documents){
        HashSet<DocumentDTO>documentDTOS = new HashSet<>();
        for(Document document: documents){
            documentDTOS.add(new DocumentDTO(document));
        }
        return documentDTOS;
    }
}
