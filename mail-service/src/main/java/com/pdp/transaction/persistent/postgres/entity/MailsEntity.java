package com.pdp.transaction.persistent.postgres.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("mails")
public class MailsEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("user_id")
    private String userId;

    @Column("email")
    private String email;
}