package com.rstream.dailyquotes;

public class QuotesNames {
    private String quoteName;
    private String quoteId;

    public String getQuoteName() {
        return quoteName;
    }

    public void setQuoteName(String quoteName) {
        this.quoteName = quoteName;
    }

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    public QuotesNames(String quoteName, String quoteId) {
        this.quoteName = quoteName;
        this.quoteId = quoteId;
    }
}
