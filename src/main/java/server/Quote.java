package server;

public class Quote {
    private String quote;
    private String author;
    private String date;

    public Quote(String quote, String author, String date) {
        this.quote = quote;
        this.author = author;
        this.date = date;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Quote){
            if (this.quote.equals(((Quote) obj).quote) && this.author.equals(((Quote) obj).author)) return true;
        }
        return false;
    }

    public String getQuote() {
        return quote;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "\"" + quote + "\" by " + author + " on " + date + ".";
    }
}
