package com.pdp.transaction.mapper;

import com.pdp.transaction.api.request.MailRequest;
import com.pdp.transaction.persistent.postgres.entity.MailsEntity;
import org.springframework.stereotype.Component;

@Component
public class MailMapper {

    public MailsEntity toEntity(MailRequest request) {
        var mail = new MailsEntity();
        mail.setEmail(request.getEmail());
        mail.setUserId(request.getUserId());

        return mail;
    }
}
