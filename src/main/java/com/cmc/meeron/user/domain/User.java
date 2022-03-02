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

    @Column(nullable = false, unique = true, length = 200)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserProvider userProvider;

    @Column(length = 20)
    private String name;

    @Column(length = 30)
    private String nickname;

    @Column(length = 200)
    private String profileImageUrl;

    @Column(length = 200)
    private String contactEmail;

    @Column(length = 25)
    private String phone;

    public static User of(String email, String nickname, String provider) {
        return User.builder()
                .email(email)
                .role(Role.USER)
                .userProvider(UserProvider.valueOf(provider))
                .nickname(nickname)
                .build();
    }
}
