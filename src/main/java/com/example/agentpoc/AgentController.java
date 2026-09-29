package com.example.agentpoc;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgentController {

    private final ChatClient chatClient;
    private final OrderTools orderTools;

    public AgentController(ChatClient.Builder chatClientBuilder, OrderTools orderTools) {
        this.chatClient = chatClientBuilder.build();
        this.orderTools = orderTools;
    }

    @GetMapping("/ask")
    public Map<String, String> ask(
            @RequestParam String question) {

        String answer = chatClient.prompt()
                .system("""
                        You are a customer support agent for a demo e-commerce system.
                        Use the available tools when the question requires order information.
                        For an order investigation, check the order status and payment status.
                        If you find a real problem, create a support ticket.
                        Do not claim that a tool was used unless you actually used it.
                        Give the customer a concise final answer.
                        """)
                .user(question)
                .tools(orderTools)
                .call()
                .content();

        return Map.of("answer", answer);
    }
}
