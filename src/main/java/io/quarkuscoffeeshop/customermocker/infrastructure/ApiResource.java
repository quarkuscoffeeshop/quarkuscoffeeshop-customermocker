package io.quarkuscoffeeshop.customermocker.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/api")
public class ApiResource {

    final Logger logger = LoggerFactory.getLogger(ApiResource.class);

    @Inject
    MockerService mockerService;

    @POST
    @Path("/start")
    public Response startMocking() {
        logger.info("starting");
        mockerService.start();
        return Response.ok().build();
    }

    @POST
    @Path("/stop")
    public Response stopMocking() {
        logger.info("stopping");
        mockerService.stop();
        return Response.ok().build();
    }

    @GET
    @Path("/running")
    public Response isRunning() {
        logger.info("returning status {}", mockerService.isRunning());
        return Response.ok(mockerService.isRunning()).build();
    }

    @POST
    @Path("/dev")
    public Response setVolumeToDev() {
        logger.info("setting volume to Dev");
        mockerService.setToDev();
        return Response.ok().build();
    }

    @POST
    @Path("/slow")
    public Response setVolumeToSlow() {
        logger.info("setting volume to Slow");
        mockerService.setToSlow();
        return Response.ok().build();
    }

    @POST
    @Path("/moderate")
    public Response setVolumeToModerate() {
        logger.info("setting volume to Moderate");
        mockerService.setToModerate();
        return Response.ok().build();
    }

    @POST
    @Path("/busy")
    public Response setVolumeToBusy() {
        logger.info("setting volume to Busy");
        mockerService.setToBusy();
        return Response.ok().build();
    }

    @POST
    @Path("/weeds")
    public Response setVolumeToWeeds() {
        logger.info("setting volume to Weeds");
        mockerService.setToWeeds();
        return Response.ok().build();
    }
}
