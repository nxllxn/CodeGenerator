package com.nxllxn.codegenerator.jdbc.java;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JavaReservedWords {

    private static Set<String> RESERVED_WORDS;

    static {
        String[] words = { "abstract", 
                "assert", 
                "boolean", 
                "break", 
                "byte", 
                "case", 
                "catch", 
                "char", 
                "class", 
                "const", 
                "continue", 
                "default", 
                "do", 
                "double", 
                "else", 
                "enum", 
                "extends", 
                "final", 
                "finally", 
                "float", 
                "for", 
                "goto", 
                "if", 
                "implements", 
                "import", 
                "instanceof", 
                "int", 
                "interface", 
                "long", 
                "native", 
                "new", 
                "package", 
                "private", 
                "protected", 
                "public", 
                "return", 
                "short", 
                "static", 
                "strictfp", 
                "super", 
                "switch", 
                "synchronized", 
                "this", 
                "throw", 
                "throws", 
                "transient", 
                "try", 
                "void", 
                "volatile", 
                "while" 
        };

        RESERVED_WORDS = new HashSet<>(words.length);
        RESERVED_WORDS.addAll(Arrays.asList(words));
    }

    public static boolean containsWord(String word) {
        return !StringUtils.isBlank(word) && RESERVED_WORDS.contains(word);
    }

    /**
     * Utility class - no instances allowed.
     */
    private JavaReservedWords() {
    }
}
