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
import org.anuran.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/api")
public class NoteResource {

    @Inject
    NoteService noteService;

    Logger logger = LoggerFactory.getLogger(NoteResource.class);

    @GET()
    @Path("/notes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Note> notes() {
        return noteService.findAll();
    }

    @POST()
    @Path("/notes")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Note addNote(Note note, @Context AwsProxyRequestContext req) {
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
        note.setUsername(username);
        return noteService.add(note);
    }

    @PUT()
    @Path("/notes/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Note updateNote(@PathParam("id") String id, Note note) {
        return noteService.update(Long.parseLong(id), note);
    }

    @GET()
    @Path("/notes/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Note getNote(@PathParam("id") String id) {
        return noteService.get(Long.parseLong(id));
    }

    @DELETE()
    @Path("/notes/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteNote(@PathParam("id") String id) {
        noteService.delete(Long.parseLong(id));
    }
}
