package com.tmoh.demomicrosoftgraph.scheduler;

import com.tmoh.demomicrosoftgraph.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@EnableScheduling
@ConditionalOnExpression("${scheduler.active}")
public class EmailScheduler {

    private final EmailService emailService;
    private final ExecutorService executorService;


    public EmailScheduler(EmailService emailService,
                          @Value("${scheduler.threadPoolSize}") int threadPoolSize) {
        this.emailService = emailService;
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    @Scheduled(fixedDelayString = "${scheduler.fixedDelay}")
    public void emailScheduler() {

        executorService.submit(() -> {
            //Create message
            var emailCreated = emailService.createMessage(
                    "Test microsfit graph",
                    "They were <b>awesome</b>!",
                    "test@guineelog.gn");

            if (emailCreated != null) {
                log.info("Message created");
            } else {
                log.debug("error to create message");
            }


            //Fetch All message
            var emails = emailService.listMessages();

            if (null == emails) {
                log.info("No emails ws found");
            } else {
                log.info("List of messages, {}", new ArrayList<>(emails));
            }

            //Move message
            emails.forEach(email -> emailService.moveMessage(email.id));

            //Delete Message
            emails.forEach(email -> emailService.deleteMessage(email.id));
        });

    }
}
