package org.anuran.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.anuran.model.Note;
import org.anuran.model.PaginatedResponse;
import org.anuran.util.DDBUtil;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class NoteService {

    private DynamoDbTable<Note> noteTable;

    Logger logger = Logger.getLogger("NoteService");

    @Inject
    NoteService(DDBUtil ddbUtil) {
        DynamoDbEnhancedClient enhancedClient = ddbUtil.getEnhancedDDBClient();
        noteTable = enhancedClient.table("Note", TableSchema.fromBean(Note.class));
    }

    public List<Note> findAll() {
        return noteTable.scan().items().stream().collect(Collectors.toList());
    }

    private PaginatedResponse<Note> findPaginated(ScanEnhancedRequest.Builder requestBuilder,
                                                  int pageSize, String lastEvaluatedKey,
                                                  String sortColumn, String dir) {
        ScanEnhancedRequest request = requestBuilder
//                .exclusiveStartKey(lastEvaluatedKey != null ?
//                        Map.of("id", AttributeValue.builder().n(lastEvaluatedKey).build())
//                        : null)
                .limit(pageSize)
                .build();

        PageIterable<Note> pagedResults = noteTable.scan(request);

        List<Page> pages = new ArrayList<>();
//        noteTable.scan(request).stream()
//                .peek(p -> logger.info("Items in page : " + p.count()))
//                .forEach(p -> pages.add(p));

        pagedResults.stream()
                .peek(p -> logger.info("Items in page : " + p.count()))
                .forEach(p -> pages.add(p));

        List<Page> sortedPages = new ArrayList<>();

        Comparator<Note> byKey = null;
        if (sortColumn.equalsIgnoreCase("id"))
            byKey = Comparator.comparing(Note::getId);
        else if (sortColumn.equalsIgnoreCase("title"))
            byKey = Comparator.comparing(Note::getTitle);
        else if (sortColumn.equalsIgnoreCase("text"))
            byKey = Comparator.comparing(Note::getText);

        if (dir.equalsIgnoreCase("desc"))
            byKey = byKey.reversed();

        int totalElements = 0;
        for (int i=0; i<pages.size(); i++) {
            if(pages.get(i).count() == 0) continue;
            totalElements += pages.get(i).count();
            sortedPages.add(Page.builder(Note.class)
                    .items(pagedResults.items()
                            .stream().sorted(byKey)
                            .collect(Collectors.toList()).subList(i*pageSize, i*pageSize + pages.get(i).count()))
                    .build());
        }
        logger.info("total elements: = " + totalElements);

        int totalPages = sortedPages.size();
        logger.info("total pages: = " + totalPages);

        boolean first = false;
        boolean last = false;
        List<Note> notes = new ArrayList<>();
        for (int i=0; i<totalPages; i++) {
            if (Integer.parseInt(lastEvaluatedKey) == i) {
                notes.addAll(sortedPages.get(i).items());
                if (i == 0)
                    first=true;
                if (i == (totalPages - 1))
                    last = true;
            }
        }
        int numberOfElements = notes.size();
        boolean isEmpty = notes.isEmpty();

        return new PaginatedResponse<Note>(notes, totalElements, totalPages,
                pageSize, numberOfElements, Integer.parseInt(lastEvaluatedKey),
                first, last, isEmpty);

    }

//    public PaginatedResponse<Note> findByUsername(String name, int pageSize, String lastEvaluatedKey) {
//        ScanEnhancedRequest request = ScanEnhancedRequest.builder()
//                .exclusiveStartKey(lastEvaluatedKey != null ?
//                        Map.of("id", AttributeValue.builder().n(lastEvaluatedKey).build())
//                        : null)
//                .consistentRead(true)
////                .addAttributeToProject()
//                .filterExpression(Expression.builder()
//                        .expression("#name = :inputname")
//                        .expressionNames(Map.of("#name", "username"))
//                        .expressionValues(Map.of(":inputname",
//                                AttributeValue.builder().s(name).build()))
//                        .build())
//                .limit(pageSize)
//                .build();
//
//        PageIterable<Note> pagedResults = noteTable.scan(request);
//        long pageCount = pagedResults.stream().count();
//        logger.info("page count: = " + pageCount);
//
//        List<Note> notes = new ArrayList<>();
////        List<Map<String, AttributeValue>> ;
//        List <String> lastEvaluatedKeys = new ArrayList<>();
//
//        pagedResults.items().forEach(note -> {
//            notes.add(note);
////            lastEvaluatedKeys.add(note.note());
//            lastEvaluatedKeys.add(note.getId().toString());
//        });
//
//        // Calculate pagination information
//        int totalElements = notes.size(); // This may not be accurate for large tables
//        int totalPages = (int) pageCount; // For Scan, there's only one page
//        logger.info("total elements: = " + totalElements);
//
//        // Retrieve the last evaluated key for the next page
////        Map<String, AttributeValue> nextToken = lastEvaluatedKeys.isEmpty() ? null : lastEvaluatedKeys.get(lastEvaluatedKeys.size() - 1);
////        String nextTokenString = nextToken != null ? nextToken.toString() : null;
//        String nextTokenString = lastEvaluatedKeys.isEmpty() ? null : lastEvaluatedKeys.get(lastEvaluatedKeys.size() - 1);
//        logger.info("next Token String: = " + nextTokenString);
//
//        return new PaginatedResponse<Note>(notes, totalElements, totalPages, pageSize, nextTokenString);
//
//
//    }

    public PaginatedResponse<Note> findByUsername(String name, int pageSize, String lastEvaluatedKey, String sortColumn, String dir) {
        ScanEnhancedRequest.Builder builder = ScanEnhancedRequest.builder()
                .consistentRead(true)
//                .addAttributeToProject()
                .filterExpression(Expression.builder()
                        .expression("#name = :inputname")
                        .expressionNames(Map.of("#name", "username"))
                        .expressionValues(Map.of(":inputname",
                                AttributeValue.builder().s(name).build()))
                        .build());

        return findPaginated(builder, pageSize, lastEvaluatedKey, sortColumn, dir);
    }
    public PaginatedResponse<Note> findByUsernameAndTitle(String name, String title,
                                                          int pageSize, String lastEvaluatedKey, String sortColumn, String dir) {

        ScanEnhancedRequest.Builder builder = ScanEnhancedRequest.builder()
                .consistentRead(true)
//                .addAttributeToProject()
                .filterExpression(Expression.builder()
                        .expression("#name = :inputname and contains(title, :title)")
                        .expressionNames(Map.of("#name", "username"))
                        .expressionValues(Map.of(":inputname",
                                AttributeValue.builder().s(name).build(),
                                ":title",
                                AttributeValue.builder().s(title).build()))
                        .build());

        return findPaginated(builder, pageSize, lastEvaluatedKey, sortColumn, dir);
    }

    public Note add(Note note) {
//        String id = UUID.randomUUID().toString();
        Long id = new Date().getTime();
        note.setId(id);
        noteTable.putItem(note);
        return note;
    }

    public Note update(Long id, Note note) {
        note.setId(id);
        UpdateItemEnhancedRequest request = UpdateItemEnhancedRequest
                .builder(Note.class)
                .ignoreNulls(true).item(note).build();
        return noteTable.updateItem(request);
    }

    public Note get(Long id) {
        Key partitionKey = Key.builder().partitionValue(id).build();
        return noteTable.getItem(partitionKey);
    }

    public Note delete(Long id) {
        Key partitionKey = Key.builder().partitionValue(id).build();
        return noteTable.deleteItem(partitionKey);
    }

}
