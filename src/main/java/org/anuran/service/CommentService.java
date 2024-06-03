package org.anuran.service;

import io.quarkus.runtime.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.anuran.model.Comment;
import org.anuran.util.DDBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class CommentService {

    private DynamoDbTable<Comment> commentTable;

    Logger logger = LoggerFactory.getLogger(CommentService.class);


    @Inject
    public CommentService(DDBUtil ddbUtil) {
        DynamoDbEnhancedClient enhancedClient = ddbUtil.getEnhancedDDBClient();
        commentTable = enhancedClient.table("Comment", TableSchema.fromBean(Comment.class));
    }


    public List<Comment> findAll() {
        return commentTable.scan().items().stream().collect(Collectors.toList());
    }

    public List<Comment> findBySourceApp(String sourceApp) {
        return commentTable.scan(s -> s
                        .consistentRead(true)
                        .filterExpression(Expression.builder()
                                .expression("sourceApp = :sourceApp")
                                .expressionValues(Map.of(":sourceApp", AttributeValue.builder()
                                        .s(sourceApp)
                                        .build()))
                                .build()))
                .items().stream().collect(Collectors.toList());
    }

    public List<Comment> findBySourceId(Long sourceId) {
        return commentTable.scan(s -> s
                        .consistentRead(true)
                        .filterExpression(Expression.builder()
                                .expression("sourceId = :sourceId")
                                .expressionValues(Map.of(":sourceId", AttributeValue.builder()
                                        .n(String.valueOf(sourceId))
                                        .build()))
                                .build()))
                .items().stream().collect(Collectors.toList());
    }

    public List<Comment> findByParentId(Long parentId) {
        return commentTable.scan(s -> s
                        .consistentRead(true)
                        .filterExpression(Expression.builder()
                                .expression("parentId = :parentId")
                                .expressionValues(Map.of(":parentId", AttributeValue.builder()
                                        .n(String.valueOf(parentId))
                                        .build()))
                                .build()))
                .items().stream().collect(Collectors.toList());
    }

    public Comment get(Long id) {
        Key partitionKey = Key.builder().partitionValue(id).build();
        return commentTable.getItem(partitionKey);
    }

    public Comment update(Long id, Comment comment) {
        comment.setId(id);
        UpdateItemEnhancedRequest request = UpdateItemEnhancedRequest
                .builder(Comment.class)
                .ignoreNulls(true).item(comment).build();
        return commentTable.updateItem(request);
    }

    public Comment add(Comment comment) {
//        String id = UUID.randomUUID().toString();
        Long did = new Date().getTime();
        comment.setId(did);
        commentTable.putItem(comment);
        return comment;
    }

    public Comment addWithSourceApp(Comment comment, String sourceApp) {
        if (!StringUtil.isNullOrEmpty(sourceApp))
            comment.setSourceApp(sourceApp);
        return add(comment);
    }

    public Comment delete(Long id) {
        Key partitionKey = Key.builder().partitionValue(id).build();
        return commentTable.deleteItem(partitionKey);
    }
}