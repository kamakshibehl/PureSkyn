package com.self.PureSkyn.service;

import com.self.PureSkyn.Model.Session;
import com.self.PureSkyn.Model.response.BookingDTO;
import com.self.PureSkyn.repository.SessionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SessionService {
    @Autowired
    private SessionRepo sessionRepo;

    public Session updateSession(Session session) {
        return sessionRepo.save(session);
    }
}
