package org.tim_18.UberApp.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim_18.UberApp.dto.DocumentDTO;
import org.tim_18.UberApp.model.Document;

public class DocumentDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public DocumentDTOMapper(ModelMapper modelMapper) {this.modelMapper = modelMapper;}

    public static Document fromDTOtoDocument(DocumentDTOMapper dto) {return modelMapper.map(dto, Document.class);}

    public static DocumentDTO fromDocumenttoDTO(Document dto) {return modelMapper.map(dto, DocumentDTO.class);}
}
