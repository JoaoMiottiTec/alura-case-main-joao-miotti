package br.com.alura.projeto.course;

import java.util.regex.Pattern;

public final class CodeValidator {
    private static final Pattern P = Pattern.compile("^[a-z-]{4,10}$");

    public static boolean isValidated(String code) {
        return code != null && P.matcher(code).matches();
    }

    private CodeValidator() {}
}