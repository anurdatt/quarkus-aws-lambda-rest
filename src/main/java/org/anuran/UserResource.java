package org.anuran;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.amazon.lambda.http.model.AwsProxyRequestContext;
import io.quarkus.runtime.util.StringUtil;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import org.anuran.model.Note;
import org.anuran.model.PaginatedResponse;
import org.anuran.service.NoteService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/user")
public class UserResource {

    @Inject
    NoteService noteService;

    Logger logger = LoggerFactory.getLogger(UserResource.class);

    @GET()
    @Path("/notes")
    @Produces(MediaType.APPLICATION_JSON)
    public PaginatedResponse<Note> notes(@QueryParam("title") String title,
                                         @QueryParam("size") String size, @QueryParam("page") String page,
                                         @QueryParam("sort") String sort,
                                         @Context AwsProxyRequestContext req) {
        String userId = "";
        if (req != null) {
            try {
                logger.info("Proxy request context = {}", new ObjectMapper().writeValueAsString(req));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            userId = req.getAuthorizer().getContextValue("userId");
        }
        logger.info("Received in request context, userId = {}", userId);
        String username = "anuran.datta@hotmail.com"; //Hardcoded dummy
        if (!StringUtil.isNullOrEmpty(userId)) {
            username = userId;
        }

        if (StringUtil.isNullOrEmpty(size)) size = "10";
        if (StringUtil.isNullOrEmpty(page)) page = "0";

        String column = null;
        String direction = null;
        if (!StringUtil.isNullOrEmpty(sort)) {
            String[] splits = sort.split(",");
            column = splits[0];
            direction = splits[1];
        }
        if (StringUtil.isNullOrEmpty(column)) column = "id";
        if (StringUtil.isNullOrEmpty(direction)) direction = "desc";

        if (StringUtil.isNullOrEmpty(title)) {
            return noteService.findByUsername(username, Integer.parseInt(size), page, column, direction);
        } else {
            return noteService.findByUsernameAndTitle(username, title, Integer.parseInt(size), page, column, direction);
        }
    }

}
