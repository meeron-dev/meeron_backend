package com.cmc.meeron.support.security;

import com.cmc.meeron.user.domain.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = WithMockJwtSecurityContextFactory.class)
public @interface WithMockJwt {

    long id() default 1L;

    String email() default "test1@test.com";

    Role role() default Role.USER;
}
