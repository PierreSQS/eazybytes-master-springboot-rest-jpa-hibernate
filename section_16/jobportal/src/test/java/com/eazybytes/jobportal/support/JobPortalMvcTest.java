package com.eazybytes.jobportal.support;

import com.eazybytes.jobportal.config.web.WebConfig;
import com.eazybytes.jobportal.exception.GlobalExceptionHandler;
import com.eazybytes.jobportal.security.JobPortalSecurityConfig;
import com.eazybytes.jobportal.security.PathsConfig;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Composed annotation that bundles the standard slice configuration used by
 * every controller MVC test in this project:
 * <ul>
 *   <li>{@link WebMvcTest} – limits the Spring context to the specified controller</li>
 *   <li>{@link AutoConfigureRestTestClient} – auto-configures a {@code RestTestClient}</li>
 *   <li>{@link Import} – brings in the shared security &amp; web infrastructure beans</li>
 * </ul>
 *
 * <p>Usage:
 * <pre>
 * {@literal @}JobPortalMvcTest(MyController.class)
 * class MyControllerMvcTest extends AbstractControllerMvcTest { … }
 * </pre>
 *
 * <p>Tests that need <em>additional</em> configuration (e.g. AOP auto-configuration or
 * extra {@code @Import}s) simply add those annotations alongside this one.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@WebMvcTest
@AutoConfigureRestTestClient
@Import({
        WebConfig.class,
        PathsConfig.class,
        JobPortalSecurityConfig.class,
        GlobalExceptionHandler.class
})
public @interface JobPortalMvcTest {

    /**
     * The controller class(es) to test — forwarded to {@link WebMvcTest#value()}.
     */
    @AliasFor(annotation = WebMvcTest.class, attribute = "value")
    Class<?>[] value() default {};

    /**
     * Alias for {@link WebMvcTest#controllers()}.
     */
    @AliasFor(annotation = WebMvcTest.class, attribute = "controllers")
    Class<?>[] controllers() default {};
}

