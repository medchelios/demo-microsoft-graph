package com.tmoh.demomicrosoftgraph.service;

import com.microsoft.graph.models.*;
import com.microsoft.graph.requests.MessageCollectionPage;
import com.tmoh.demomicrosoftgraph.client.GraphClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class EmailService {
    private final GraphClient graphClient;

    @Value("${email}")
    private  String email;

    @Autowired
    public EmailService(GraphClient graphClient) {
        this.graphClient = graphClient;
    }

    public Message createMessage(String subject,
                                 String bodyContent,
                                 String recipient) {
        // TODO: recipient can be a list of recipient emails

        var client = graphClient.client();

        Message message = new Message();

        message.subject = subject;
        message.importance = Importance.NORMAL;
        ItemBody body = new ItemBody();
        body.contentType = BodyType.HTML;
        body.content = bodyContent;
        message.body = body;

        LinkedList<Recipient> toRecipientsList = new LinkedList<>();
        Recipient toRecipients = new Recipient();
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.address = recipient;
        toRecipients.emailAddress = emailAddress;
        toRecipientsList.add(toRecipients);
        message.toRecipients = toRecipientsList;

        return client.users(email)
                .messages()
                .buildRequest()
                .post(message);
    }


    public List<Message> listMessages() {
        var client = graphClient.client();

        MessageCollectionPage messageCollectionPage = client.users(email)
                .messages()
                .buildRequest()
                .select("sender,from,subject,body")
                .expand("attachments")
                .get();

        assert messageCollectionPage != null;
        return new ArrayList<>(messageCollectionPage.getCurrentPage());
    }

    public Message moveMessage(String idMessage) {
        var client = graphClient.client();

        return client.users(email)
                .messages(idMessage)
                .move(MessageMoveParameterSet
                        .newBuilder()
                        .withDestinationId("destinationId")
                        .build())
                .buildRequest()
                .post();

    }
    public Message deleteMessage(String idMessage) {
        var client = graphClient.client();

        return client.users(email)
                .messages(idMessage)
                .buildRequest().delete();
    }
}
