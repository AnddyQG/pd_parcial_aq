package com.programacion.distribuida.authors.rest;

import com.programacion.distribuida.authors.db.Author;
import com.programacion.distribuida.authors.repo.AuthorRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AuthorRest {
    @Inject
    AuthorRepo authorRepo;

    @Inject
    @ConfigProperty(name = "quarkus.http.port")
    Integer httpPort;

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Integer id) {
        var obj = authorRepo.findByIdOptional(id);
        if (obj.isEmpty()) {

            // Si no se encuentra el autor, se devuelve un error 404
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        // Si si existe return instancia del autor
        return Response.ok(obj.get()).build();
    }


    @GET
    public List<Author> findAll() {
        return authorRepo.listAll();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, Author author) {
        var obj = authorRepo.findByIdOptional(id);
        if (obj.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        authorRepo.update(id, author);
        return Response.ok(obj.get()).build();
    }

    @GET
    @Path("/find/{isbn}")
    public List<Author> findByBook(@PathParam("isbn") String isbn) {

        Config config = ConfigProvider.getConfig();


        config.getConfigSources().forEach(
                obj -> System.out.printf(
                        "%d -> %s\n", obj.getOrdinal(), obj.getName()
                )
        );

        // Busco autores de un libro por su ISBN
        var ret = authorRepo.findByBook(isbn);

        return ret.stream().map(obj -> {
            String newName = String.format("%s (%d)", obj.getName(), httpPort);
            obj.setName(newName);
            return obj;
        }).toList();
    }
}
