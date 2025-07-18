package com.programacion.distribuida.books.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import org.modelmapper.ModelMapper;

@ApplicationScoped
public class MapperService {

    @Produces
    @ApplicationScoped
    public ModelMapper mapper(){
        return new ModelMapper();
    }
}
