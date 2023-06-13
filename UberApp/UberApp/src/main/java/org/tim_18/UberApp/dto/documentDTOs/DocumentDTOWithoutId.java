package org.tim_18.UberApp.dto.documentDTOs;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.tim_18.UberApp.model.Document;
@Data
public class DocumentDTOWithoutId {

    private String name;
    @Length(max = 100)
    private String image;
    private Integer driverId;

    public DocumentDTOWithoutId() {}

    public DocumentDTOWithoutId(Document document){
        this(document.getName(), document.getDocumentImage(), document.getDriver().getId());
    }
    public DocumentDTOWithoutId(String name, String image, Integer driverId) {
        this.name       = name;
        this.image      = image;
        this.driverId   = driverId;
    }
}
