package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.*;
import Service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.*;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("register", this::registerAccountHandler);
        app.post("login", this::loginAccountHandler);
        app.post("messages", this::createMessageHandler);
        app.get("messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("messages/{message_id}", this::deleteMessageByIdHandler);
        app.get("accounts/{account_id}/messages", this::getMessagesByPosted_byHandler);
        app.patch("messages/{message_id}", this::updateMessageByIdHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account newAccount = accountService.registerAccount(account);
        if (newAccount != null) {
            ctx.json(mapper.writeValueAsString(newAccount));
        } else {
            ctx.status(400);
        }
    }

    private void loginAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account credentials = mapper.readValue(ctx.body(), Account.class);
        Account account = accountService.loginAccount(credentials.getUsername(), credentials.getPassword());
        if (account != null) {
            ctx.json(mapper.writeValueAsString(account));
        } else {
            ctx.status(401);
        }
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message newMessage = messageService.createMessage(message);
        if (newMessage != null) {
            ctx.json(mapper.writeValueAsString(newMessage));
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx) {
        ctx.json(messageService.getAllMessages());
    }

    private void getMessageByIdHandler(Context ctx) {
        try {
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(message_id);
            if (message != null) {
                ctx.json(message);
            }
        } catch(NumberFormatException e) {
            System.out.println("Invalid id format: " + e.getMessage());
            ctx.status(400);
        }
    }

    private void deleteMessageByIdHandler(Context ctx) {
        try {
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.deleteMessageById(message_id);
            if (message != null) {
                ctx.json(message);
            }
        } catch(NumberFormatException e) {
            System.out.println("Invalid id format: " + e.getMessage());
            ctx.status(400);
        } 
    }

    private void getMessagesByPosted_byHandler(Context ctx) {
        try {
            int posted_by = Integer.parseInt(ctx.pathParam("account_id"));
            ctx.json(messageService.getMessagesByPosted_by(posted_by));
        } catch(NumberFormatException e) {
            System.out.println(e.getMessage());
            ctx.status(400);
        }
    }

    private void updateMessageByIdHandler(Context ctx)  throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> bodyMessage = mapper.readValue(ctx.body(), new TypeReference<Map<String, String>>() {});
        if (!bodyMessage.containsKey("message_text") || bodyMessage.get("message_text").length() > 255 || bodyMessage.get("message_text").length() == 0) {
            ctx.status(400);
        }
        try {
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.updateMessageById(message_id, bodyMessage.get("message_text"));
            if (message == null) {
                ctx.status(400);
            } else {
                ctx.json(message);
            }
        } catch(NumberFormatException e) {
            System.out.println(e.getMessage());
            ctx.status(400);
        }
    }
}