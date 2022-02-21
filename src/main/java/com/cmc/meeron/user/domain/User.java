package com.cmc.meeron.user.domain;

import com.cmc.meeron.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserProvider userProvider;

    private String name;

    private String nickname;

    private String profileImageUrl;

    private String contactEmail;

    public static User of(String email, String nickname, String provider) {
        return User.builder()
                .email(email)
                .role(Role.USER)
                .userProvider(UserProvider.valueOf(provider))
                .name("")
                .nickname(nickname)
                .build();
    }
}
