package org.anuran.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.runtime.annotations.RegisterForReflection;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

@RegisterForReflection
@DynamoDbBean
public class Note {

    private Long id;
    private String title;
    private String text;
    @JsonIgnore
    private String username;


    public Note() {
    }

    @DynamoDbPartitionKey
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public Map<String, AttributeValue> note() {
        return Map.of("id", AttributeValue.builder().n(String.valueOf(this.id)).build(),
                "title", AttributeValue.builder().s(this.title).build(),
                "text", AttributeValue.builder().s(this.text).build(),
                "username", AttributeValue.builder().s(this.username).build());
    }
    public void setUsername(String username) {
        this.username = username;
    }


}
