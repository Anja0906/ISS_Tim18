package org.tim_18.UberApp.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim_18.UberApp.dto.MessageDTO;
import org.tim_18.UberApp.model.Message;

public class MessageDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public MessageDTOMapper(ModelMapper modelMapper) {this.modelMapper = modelMapper;}

    public static Message fromDTOtoMessage(MessageDTOMapper dto) {return modelMapper.map(dto, Message.class);}

    public static MessageDTO fromMessageToDTO(Message dto) {return modelMapper.map(dto, MessageDTO.class);}
}
