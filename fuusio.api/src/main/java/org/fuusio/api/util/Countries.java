package org.fuusio.api.util;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class Countries {

    private static final List<Country> sCountries = new ArrayList<Country>();

    private static void createCountries() {
        final Locale[] locales = Locale.getAvailableLocales();

        for (final Locale locale : locales) {
            final String iso = locale.getISO3Country();
            final String code = locale.getCountry();
            final String name = locale.getDisplayCountry();

            if (!"".equals(iso) && !"".equals(code) && !"".equals(name)) {
                sCountries.add(new Country(iso, code, name));
            }
        }

        Collections.sort(sCountries, new CountryComparator());
    }

    public static List<Country> getCountries() {
        if (sCountries.isEmpty()) {
            createCountries();
        }
        return sCountries;
    }

    public static String[] getCountryNames() {
        final List<Country> countries = getCountries();
        final String[] names = new String[countries.size()];

        int index = 0;

        for (final Country country : countries) {
            names[index++] = country.getName();
        }

        return names;
    }

    static class CountryComparator implements Comparator<Country> {

        private final Collator mComparator;

        CountryComparator() {
            mComparator = Collator.getInstance();
        }

        @Override
        public int compare(final Country pCountry1, final Country pCountry2) {
            return mComparator.compare(pCountry1.getName(), pCountry2.getName());
        }
    }
}
