package com.nitoelchidoceti.ciceroneguias.JavaMail;

import java.util.Random;

public class Utils {
    public static final  String EMAIL = "juanpiszrgz@gmail.com";
    public static final String PASSWORD = "ildcutujofmovlqf";
    private static final String ALLOWED_CHARACTERS ="0123456789QWERTYUIOPASDFGHJKLZXCVBNM";

    public static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }
}
