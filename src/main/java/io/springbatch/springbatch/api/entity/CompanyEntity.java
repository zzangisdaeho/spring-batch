package io.springbatch.springbatch.api.entity;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEntity {

    @Id
    @GeneratedValue
    private Long companySeq;

    private String companyName;

    private ZonedDateTime updateTime;

    @OneToMany(mappedBy = "company", orphanRemoval = true, cascade = CascadeType.ALL)
    @Builder.Default
    private List<MemberEntity> members = new ArrayList<>();

    public void addMember(MemberEntity member){
        this.members.add(member);
        member.setCompany(this);
    }
}
