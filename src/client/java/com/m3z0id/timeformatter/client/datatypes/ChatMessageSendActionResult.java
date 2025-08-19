package com.m3z0id.timeformatter.client.datatypes;

import net.minecraft.util.ActionResult;

public class ChatMessageSendActionResult {
    private final ActionResult result;
    private final String message;

    private ChatMessageSendActionResult(ActionResult result, String message) {
        this.result = result;
        this.message = message;
    }

    public ActionResult getResult() {
        return result;
    }
    public String getMessage() {
        return message;
    }

    public static ChatMessageSendActionResult success(String message) {
        return new ChatMessageSendActionResult(ActionResult.SUCCESS, message);
    }
    public static ChatMessageSendActionResult fail(String message) {
        return new ChatMessageSendActionResult(ActionResult.FAIL, message);
    }
}
