package com.onwelo.practice.bts.fds;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.onwelo.practice.bts.entity.Transfer;
import com.onwelo.practice.bts.utils.Currency;
import com.onwelo.practice.bts.utils.TransferStatus;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;

@EnableKafka
@Configurable
@Component
public class TransferValidatorEUR {
    private static org.slf4j.Logger Logger = LoggerFactory.getLogger(TransferValidatorEUR.class);
    private final static String topicReceive = "make-transfer";

    @KafkaListener(topics = topicReceive, groupId = "transfer2")
    public void receive(String aLong) throws IOException {
        Logger.info("Kafka: eur validator");
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Transfer.class, new TransferDeserializer());
        mapper.registerModule(module);
        Transfer transfer = mapper.readValue(aLong, Transfer.class);

        send(validateTransfer(transfer));
    }


    private String validateTransfer(Transfer transfer) {

        if (transfer.getCurrency().equals(Currency.EUR)) {
            if (transfer.getValue().compareTo(BigDecimal.valueOf(4000)) > 0) {
                return transfer.getId() + ",1," + TransferStatus.CANCELED.toString();
            } else {
                return transfer.getId() + ",1," + TransferStatus.APPROVED.toString();
            }
        } else {
            return transfer.getId() + ",1," + TransferStatus.APPROVED.toString();
        }
    }

    private void send(String status) {
        new TransferProducer().sendStatus(status);
    }
}