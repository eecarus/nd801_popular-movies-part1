package com.example.android.popularmovies.moviedb;

public class Configuration {

    public static class Images {
        private String baseUrl;
        private String secureBaseUrl;
        private String[] backdropSizes = new String[0];
        private String[] logoSizes = new String[0];
        private String[] posterSizes = new String[0];
        private String[] profileSizes = new String[0];
        private String[] stillSizes = new String[0];

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getSecureBaseUrl() {
            return secureBaseUrl;
        }

        public void setSecureBaseUrl(String secureBaseUrl) {
            this.secureBaseUrl = secureBaseUrl;
        }

        public String[] getBackdropSizes() {
            return backdropSizes;
        }

        public void setBackdropSizes(String[] backdropSizes) {
            this.backdropSizes = backdropSizes;
        }

        public String[] getLogoSizes() {
            return logoSizes;
        }

        public void setLogoSizes(String[] logoSizes) {
            this.logoSizes = logoSizes;
        }

        public String[] getPosterSizes() {
            return posterSizes;
        }

        public void setPosterSizes(String[] posterSizes) {
            this.posterSizes = posterSizes;
        }

        public String[] getProfileSizes() {
            return profileSizes;
        }

        public void setProfileSizes(String[] profileSizes) {
            this.profileSizes = profileSizes;
        }

        public String[] getStillSizes() {
            return stillSizes;
        }

        public void setStillSizes(String[] stillSizes) {
            this.stillSizes = stillSizes;
        }
    }

    private Images images = new Images();
    private String[] changeKeys = new String[0];

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public String[] getChangeKeys() {
        return changeKeys;
    }

    public void setChangeKeys(String[] changeKeys) {
        this.changeKeys = changeKeys;
    }
}