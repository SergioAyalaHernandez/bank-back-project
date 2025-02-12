package com.sergio.bank.service.impl;
import com.sergio.bank.dto.MessageDto;
import com.sergio.bank.service.MessagePublisherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class MessagePublisherServiceImpl implements MessagePublisherService {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    public MessagePublisherServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishAccountMessage(String transactionType, String accountId, String userId, boolean status) {
        MessageDto message = new MessageDto();
        message.setIdEntidad(accountId);
        message.setFecha(LocalDateTime.now().toString());
        message.setMensaje("Se realizó una " + transactionType + " en la cuenta " + accountId + " por el usuario " + userId);
        message.setRecurso("cuenta");
        message.setEstado(status);

        rabbitTemplate.convertAndSend(queueName, message);
    }
}
