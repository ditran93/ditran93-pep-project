package Controller;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerAccountHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserIdHandler);

        return app;
    }

    private void getAllMessagesByUserIdHandler(Context ctx)  {
      
        String accountIdStr = ctx.pathParam("account_id");
        int accountId = Integer.parseInt(accountIdStr);

        List<Message> messages = messageService.getAllMessagesByUserId(accountId);

        ctx.json(messages);
    }

    private void updateMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new  ObjectMapper();
        String messageIdStr = ctx.pathParam("message_id");
        int messageId = Integer.parseInt(messageIdStr);

        Message bodyMap = mapper.readValue(ctx.body(), Message.class);

        Message updatedMessage = messageService.updateMessageById(messageId, bodyMap.getMessage_text());

        if(updatedMessage != null) {
            ctx.json(mapper.writeValueAsString(updatedMessage));
        } else {
            ctx.status(400);
        }
    }

    private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new  ObjectMapper();
        String messageIdStr = ctx.pathParam("message_id");
        int messageId = Integer.parseInt(messageIdStr);

        Message message = messageService.deleteMessage(messageId);
        if(message != null) {
            ctx.json(mapper.writeValueAsString(message));
        }
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new  ObjectMapper();
        String messageIdStr = ctx.pathParam("message_id");
        int messageId = Integer.parseInt(messageIdStr);

        Message message = messageService.getMessageById(messageId);
        if(message != null) {
            ctx.json(mapper.writeValueAsString(message));
        }
    }

    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message resultMessage = messageService.addMessage(message);
        if(resultMessage != null) {
            ctx.json(mapper.writeValueAsString(resultMessage));
        } else {
            ctx.status(400);
        }
    }

    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new  ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loginAccount = accountService.login(account);
        if(loginAccount != null) {
            ctx.json(mapper.writeValueAsString(loginAccount));
        } else {
            ctx.status(401);
        }

    }

    private void registerAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.registerAccount(account);
        if(addedAccount != null) {
            ctx.json(mapper.writeValueAsString(addedAccount));
        } else {
            ctx.status(400);
        }
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


}