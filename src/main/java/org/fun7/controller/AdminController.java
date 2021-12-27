package org.fun7.controller;

import org.fun7.model.User;
import org.fun7.service.UserService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/admin/user")
public class AdminController {

    @Inject
    UserService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listUsers() {
        return Response.ok().entity(service.listAllUsers()).build();
    }

    @Path("/{userId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response detailUser(@PathParam("userId") Long userId) {
        var user = service.getUserById(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }

        return Response.ok().entity(user).build();
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid User user) {
        return Response.status(Response.Status.CREATED).entity(service.createUser(user)).build();
    }

    @Path("/{userId}")
    @DELETE
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteUser(@PathParam("userId") Long userId) {
        var user = service.getUserById(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
        service.removeUser(userId);
        return Response.ok().build();
    }

}
