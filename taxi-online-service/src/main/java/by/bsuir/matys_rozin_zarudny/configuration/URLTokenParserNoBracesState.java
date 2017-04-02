
package by.bsuir.matys_rozin_zarudny.configuration;


public class URLTokenParserNoBracesState extends URLTokenParserState {

    public URLTokenParserNoBracesState(URLTokenParser context) {
        super(context);
    }

    @Override
    public void addLeftBracket() {
        this.getContext().changeState(
                new URLTokenParserPotentialToken(
                        this.getContext()));
        this.getContext().addToURL();
    }

    @Override
    public void addRightBracket() {}
}
