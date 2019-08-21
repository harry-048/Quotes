package com.rstream.teenwallpapers;

public class QuotesKeyVal {
    String formatedName;
    String quoteKey;

    public String getFormatedName() {
        return formatedName;
    }

    public void setFormatedName(String formatedName) {
        this.formatedName = formatedName;
    }

    public String getQuoteKey() {
        return quoteKey;
    }

    public void setQuoteKey(String quoteKey) {
        this.quoteKey = quoteKey;
    }

    public QuotesKeyVal(String formatedName, String quoteKey) {
        this.formatedName = formatedName;
        this.quoteKey = quoteKey;
    }
}
