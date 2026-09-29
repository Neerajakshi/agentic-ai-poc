package com.example.agentpoc;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class OrderTools {

    private final AtomicInteger ticketSequence = new AtomicInteger(1000);

    @Tool(description = "Get the current shipping/order status for an order. Use this when the customer asks about an order or delivery.")
    public String getOrderStatus(
            @ToolParam(description = "The order ID, for example 1001") String orderId) {

        System.out.println("[TOOL EXECUTED] getOrderStatus(" + orderId + ")");

        if ("1001".equals(orderId)) {
            return "Order 1001: status=DELAYED, carrier=DemoExpress, expectedDelivery=2026-09-30";
        }

        if ("1002".equals(orderId)) {
            return "Order 1002: status=SHIPPED, carrier=DemoExpress, expectedDelivery=2026-10-01";
        }

        return "Order " + orderId + ": status=UNKNOWN";
    }

    @Tool(description = "Get the payment status for an order. Use this when investigating why an order has a problem.")
    public String getPaymentStatus(
            @ToolParam(description = "The order ID, for example 1001") String orderId) {

        System.out.println("[TOOL EXECUTED] getPaymentStatus(" + orderId + ")");

        if ("1001".equals(orderId)) {
            return "Order 1001: paymentStatus=FAILED, reason=Card authorization failed";
        }

        if ("1002".equals(orderId)) {
            return "Order 1002: paymentStatus=PAID";
        }

        return "Order " + orderId + ": paymentStatus=UNKNOWN";
    }

    @Tool(description = "Create a customer support ticket. Use this only when there is a real problem that requires support intervention.")
    public String createSupportTicket(
            @ToolParam(description = "A concise description of the customer issue") String issue) {

        int ticketNumber = ticketSequence.incrementAndGet();
        System.out.println("[TOOL EXECUTED] createSupportTicket(issue=\"" + issue + "\")");

        return "Support ticket created successfully: TICKET-" + ticketNumber;
    }
}
