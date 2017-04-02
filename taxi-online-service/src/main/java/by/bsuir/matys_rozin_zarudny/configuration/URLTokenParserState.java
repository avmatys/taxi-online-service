
package by.bsuir.matys_rozin_zarudny.configuration;

public abstract class URLTokenParserState {

    private URLTokenParser context;

    public URLTokenParserState(URLTokenParser context) {
        this.context = context;
    }

    public abstract void addLeftBracket();

    public abstract void addRightBracket();

    public void addChar(char c) {
        this.getContext().addToTemp(c);
    }

    public URLTokenParser getContext() {
        return this.context;
    }

}
