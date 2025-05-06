package io.mosip.testrig.residentui.utility;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SetTestName {
    int idx() default 0;
}