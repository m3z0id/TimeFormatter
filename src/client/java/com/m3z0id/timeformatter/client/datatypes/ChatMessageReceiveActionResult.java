package com.m3z0id.timeformatter.client.datatypes;

import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public class ChatMessageReceiveActionResult {
    private final ActionResult result;
    private final Text message;

    private ChatMessageReceiveActionResult(ActionResult result, Text message) {
        this.result = result;
        this.message = message;
    }

    public ActionResult getResult() {
        return result;
    }
    public Text getMessage() {
        return message;
    }

    public static ChatMessageReceiveActionResult success(Text message) {
        return new ChatMessageReceiveActionResult(ActionResult.SUCCESS, message);
    }
    public static ChatMessageReceiveActionResult fail(Text message) {
        return new ChatMessageReceiveActionResult(ActionResult.FAIL, message);
    }
}
