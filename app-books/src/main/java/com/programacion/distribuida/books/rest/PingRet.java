package com.programacion.distribuida.books.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/ping")
public class PingRet {

    @GET
    public String ping() {
        return "Pong";
    }
}
