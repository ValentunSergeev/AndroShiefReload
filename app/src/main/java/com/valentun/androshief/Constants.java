package com.valentun.androshief;

public class Constants {

    public static class URL {
        private static final String HOST = "https://androshief.herokuapp.com/";

        /* for future
        public static final String INDEX = HOST + "recipies.json";
        public static final String CREATE = HOST + "recipies.json";
        public static final String COOKBOOK = HOST + "cookbook.json";
        */


        public static final String REGISTER = HOST + "auth/";
        public static final String CATEGORIES = HOST + "categories/";
        static final String SIGN_IN = HOST + "auth/sign_in";
    }


    public static final int ORANGE = 0xFFFF6D00;

    static final String APP_PREFERENCES = "USER";

    public static final int GALLERY_REQUEST = 1;

    static final int MAX_QUALITY = 100;

    static final long TOKEN_LIFESPAN = 2 * 7 * 24 * 60 * 60 * 1000; //milliseconds in 2 weeks
//    static final long TOKEN_LIFESPAN = 1000; //1 sec to test token refreshing

    public enum AUTH_MODE {
        SIGN_IN, REAUTHORIZE
    }
}
