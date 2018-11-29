package de.mpg.mpdl.mpadmanager.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    private Pattern pattern;
    private Matcher matcher;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String EMAIL_PATTERN_1 = "^.*mpg\\.de$"; 
    private static final String EMAIL_PATTERN_2 = "^.*mpi.*?\\.de$";
    private static final String EMAIL_PATTERN_3 = "^.*gwdg\\.de$";
    private static final String EMAIL_PATTERN_4 = "^.*biblhertz\\.it$";
    private static final String EMAIL_PATTERN_5 = "^.*khi.*?\\.it$";
    private static final String EMAIL_PATTERN_6 = "^.*mpi\\.nl$";
    private static final String EMAIL_PATTERN_7 = "^.*zmaw\\.de$";
    
    private static final String EMAIL_PATTERN_8 = "^.*dkrz\\.de$";
    private static final String EMAIL_PATTERN_9 = "^.*esi-frankfurt\\.de$";
    private static final String EMAIL_PATTERN_10 = "^.*caesar\\.de$";
    private static final String EMAIL_PATTERN_11 = "^.*hda-hd\\.de$";
    private static final String EMAIL_PATTERN_12 = "^.*maxplanckflorida\\.org$";
    private static final String EMAIL_PATTERN_13 = "^.*mpfi\\.org$";
    private static final String EMAIL_PATTERN_14 = "^.*mpfpr\\.de$";
    private static final String EMAIL_PATTERN_15 = "^.*mpi\\.lu$";
    
    private static final String EMAIL_PATTERN_16 = "^.*mpia\\.de$";
    private static final String EMAIL_PATTERN_17 = "^.*mpia-hd\\.de$";


    

    private static List<String> EMAIL_PATTERNS = new ArrayList<>();
    
    static {
    	EMAIL_PATTERNS.add(EMAIL_PATTERN_1);
    	EMAIL_PATTERNS.add(EMAIL_PATTERN_2);
    	EMAIL_PATTERNS.add(EMAIL_PATTERN_3);
    	EMAIL_PATTERNS.add(EMAIL_PATTERN_4);
    	EMAIL_PATTERNS.add(EMAIL_PATTERN_5);
    	EMAIL_PATTERNS.add(EMAIL_PATTERN_6);
    	EMAIL_PATTERNS.add(EMAIL_PATTERN_7);
    	EMAIL_PATTERNS.add(EMAIL_PATTERN_7);

    	EMAIL_PATTERNS.add(EMAIL_PATTERN_8);
    	EMAIL_PATTERNS.add(EMAIL_PATTERN_9);
    	EMAIL_PATTERNS.add(EMAIL_PATTERN_10);
    	EMAIL_PATTERNS.add(EMAIL_PATTERN_11);
    	EMAIL_PATTERNS.add(EMAIL_PATTERN_12);
    	EMAIL_PATTERNS.add(EMAIL_PATTERN_13);
    	EMAIL_PATTERNS.add(EMAIL_PATTERN_14);
    	EMAIL_PATTERNS.add(EMAIL_PATTERN_15);
    	EMAIL_PATTERNS.add(EMAIL_PATTERN_16);
    	EMAIL_PATTERNS.add(EMAIL_PATTERN_17);
    }
    
    
    @Override
    public void initialize(final ValidEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String username, final ConstraintValidatorContext context) {
        return (validateEmail(username));
    }

    private boolean validateEmail(final String email) {
    	for (String email_pattern : EMAIL_PATTERNS) {
            pattern = Pattern.compile(email_pattern);
            matcher = pattern.matcher(email);
            if (matcher.matches()) return true;
    	}
        return false;
    }
}