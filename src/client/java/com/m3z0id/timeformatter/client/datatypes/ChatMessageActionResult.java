package com.m3z0id.timeformatter.client.datatypes;

import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public class ChatMessageActionResult {
    private final ActionResult result;
    private final Text message;

    private ChatMessageActionResult(ActionResult result, Text message) {
        this.result = result;
        this.message = message;
    }

    public ActionResult getResult() {
        return result;
    }
    public Text getMessage() {
        return message;
    }

    public static ChatMessageActionResult success(Text message) {
        return new ChatMessageActionResult(ActionResult.SUCCESS, message);
    }
    public static ChatMessageActionResult fail(Text message) {
        return new ChatMessageActionResult(ActionResult.FAIL, message);
    }
}
