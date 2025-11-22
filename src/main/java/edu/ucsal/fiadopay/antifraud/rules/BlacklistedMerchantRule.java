package edu.ucsal.fiadopay.antifraud.rules;

import edu.ucsal.fiadopay.annotations.AntiFraud;
import edu.ucsal.fiadopay.domain.Payment;

import java.util.Set;

@AntiFraud(value = "Blacklisted Merchant Rule")
public class BlacklistedMerchantRule {

    private static final Set<String> BLACKLIST = Set.of(
            "MERC123",
            "BLOCKED001",
            "FRAUDSTORE"
    );

    public boolean check(Payment p) {
        return !BLACKLIST.contains(p.getMerchantId());
    }
}
