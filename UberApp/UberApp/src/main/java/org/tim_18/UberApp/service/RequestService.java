package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.dto.userDTOs.UserDTO;
import org.tim_18.UberApp.model.Request;
import org.tim_18.UberApp.repository.RequestRepository;

import java.util.List;

@Service("requestService")
public class RequestService {
    @Autowired
    private RequestRepository requestRepository;

    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public Request save(Request request) {
        return requestRepository.save(request);
    }

    public Page<Request> findByUserId(int id, Pageable pageable) {
        return requestRepository.findByUserId(id, pageable);
    }

    public void deleteByUserId(int id){
        requestRepository.deleteById(id);
    }


    public List<Request> findAll(){
        return requestRepository.findAll();
    }

    public Request makeRequest(UserDTO userDTO){
        return this.save(new Request(userDTO.getId(), userDTO.getName(),
                                    userDTO.getSurname(), userDTO.getProfilePicture(),
                                    userDTO.getTelephoneNumber(), userDTO.getEmail(),
                                    userDTO.getAddress()));
    }
}
