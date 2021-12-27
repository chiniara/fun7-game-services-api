package org.fun7.controller;

import org.fun7.model.requests.CheckServicesQueryRequest;
import org.fun7.service.CheckServicesService;
import org.fun7.service.UserService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/check-services")
public class CheckServicesController {

    @Inject
    CheckServicesService service;

    @Inject
    UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response checkServices(@Valid @BeanParam CheckServicesQueryRequest request) {
        var user = userService.getUserByIdWithLock(request.getUserId());
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }

        return Response.ok().entity(service.checkServicesStatus(request, user)).build();
    }
}
