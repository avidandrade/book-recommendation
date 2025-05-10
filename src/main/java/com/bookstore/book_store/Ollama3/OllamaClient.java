package com.bookstore.book_store.Ollama3;

import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.ChatCompletionsClientBuilder;
import com.azure.ai.inference.models.ChatCompletions;
import com.azure.ai.inference.models.ChatCompletionsOptions;
import com.azure.ai.inference.models.ChatRequestMessage;
import com.azure.ai.inference.models.ChatRequestSystemMessage;
import com.azure.ai.inference.models.ChatRequestUserMessage;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class OllamaClient {

    private final ChatCompletionsClient client;
    private final String model;

    public OllamaClient(
        @Value("${github.model.token}") String token,
        @Value("${github.model.endpoint}") String endpoint,
        @Value("${github.model.model}") String model
    ) {
        this.model = model;
        this.client = new ChatCompletionsClientBuilder()
                .credential(new AzureKeyCredential(token))
                .endpoint(endpoint)
                .buildClient();
    }

    public String callModel(String prompt) {
        List<ChatRequestMessage> messages = Arrays.asList(
                new ChatRequestSystemMessage(""),
                new ChatRequestUserMessage(prompt)
        );

        ChatCompletionsOptions options = new ChatCompletionsOptions(messages);
        options.setModel(model);

        ChatCompletions completions = client.complete(options);
        return completions.getChoices().get(0).getMessage().getContent();
    }
}

