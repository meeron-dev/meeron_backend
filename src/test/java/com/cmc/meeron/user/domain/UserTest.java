package com.cmc.meeron.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.cmc.meeron.user.UserFixture.USER;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = USER;
    }

    @DisplayName("유저 성함 저장 / 성공")
    @Test
    void user_patch_email_success() throws Exception {

        // given
        String name = "성함";

        // when
        user.setName(name);

        // then
        assertEquals(name, user.getName());
    }

    @DisplayName("유저 생성 - 성공 / profileImageUrl이 null인 경우")
    @Test
    void of_success_profile_image_url_is_null() throws Exception {

        // given
        String email = "test@test.com";
        String provider = "APPLE";

        // when
        User user = User.of(email, provider, null);

        // then
        assertAll(
                () -> assertEquals(email, user.getEmail()),
                () -> assertEquals(provider, user.getUserProvider().getProvider()),
                () -> assertEquals("", user.getProfileImageUrl())
        );
    }

    @DisplayName("유저 생성 - 성공 / profileImageUrl이 null이 아닌 경우")
    @Test
    void of_success_profile_image_url_is_not_null() throws Exception {

        // given
        String email = "test@test.com";
        String provider = "APPLE";
        String profileImageUrl = "test.url.com";

        // when
        User user = User.of(email, provider, profileImageUrl);

        // then
        assertAll(
                () -> assertEquals(email, user.getEmail()),
                () -> assertEquals(provider, user.getUserProvider().getProvider()),
                () -> assertEquals(profileImageUrl, user.getProfileImageUrl())
        );
    }
}
