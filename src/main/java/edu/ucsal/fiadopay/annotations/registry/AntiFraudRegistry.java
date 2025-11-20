package edu.ucsal.fiadopay.annotations.registry;

import edu.ucsal.fiadopay.annotations.AntiFraud;

import java.util.ArrayList;
import java.util.List;

public class AntiFraudRegistry {

    private static final List<AntiFraud> validators = new ArrayList<>();

    public static void register(AntiFraud validator) {
        validators.add(validator);
    }

    public static List<AntiFraud> all() {
        return validators;
    }
}
