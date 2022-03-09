package ust.tad.analysismanager.analysistask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalysisTaskSender {
    
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private HeadersExchange headers;
    //private TopicExchange topic;
    
    
    public void send(AnalysisTask analysisTask) throws JsonProcessingException, AmqpException {
        //String key = analysisTask.getTechnology().getName().toString() + "." + analysisTask.getAnalysisType().toString();
        ObjectMapper objectMapper = new ObjectMapper();

        Message message = MessageBuilder.withBody(objectMapper.writeValueAsString(analysisTask).getBytes())
            .setContentType(MessageProperties.CONTENT_TYPE_JSON)
            .setHeader("technology", analysisTask.getTechnology().getName().toString())
            .setHeader("analysisType", analysisTask.getAnalysisType().toString())
            .build();

        //String message = objectMapper.writeValueAsString(analysisTask);
        //template.convertAndSend(topic.getName(), key, message);
        template.convertAndSend(headers.getName(), "", message);
    }
}