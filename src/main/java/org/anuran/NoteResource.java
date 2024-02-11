package org.anuran;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.anuran.model.Note;
import org.anuran.service.NoteService;

import java.util.List;

@Path("/api")
public class NoteResource {

    @Inject
    NoteService noteService;

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
    public Note addNote(Note note) {
        //TODO: to get username from token
        String username = "anuran.datta@hotmail.com"; //Hardcoded dummy
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
