package io.springbatch.springbatch.api.entity;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity {

    @Id
    @GeneratedValue
    private Long memberSeq;

    private String memberName;

    private ZonedDateTime updateTime;

    @ManyToOne
    @JoinColumn(name = "companySeq")
    private CompanyEntity company;
}
