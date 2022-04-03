package com.cmc.meeron.user.domain;

import com.amazonaws.util.StringUtils;
import com.cmc.meeron.common.domain.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Where(clause = "DELETED=false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String email;

    @Column(length = 20)
    private String name;

    @Column(length = 200)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserProvider userProvider;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private boolean deleted;

    public static User of(String email, String provider, String profileImageUrl) {
        return User.builder()
                .email(email)
                .userProvider(UserProvider.valueOf(provider))
                .profileImageUrl(StringUtils.isNullOrEmpty(profileImageUrl) ? "" : profileImageUrl)
                .role(Role.USER)
                .deleted(false)
                .build();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void quit() {
        email = UUID.randomUUID().toString();
        deleted = true;
    }
}
