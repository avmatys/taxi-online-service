package by.bsuir.matys_rozin_zarudny.configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class URLTokenParser {

    String result = "";
    String token = "";
    Map<String, String> tokens;
    URLTokenParserState state;

    public URLTokenParser(Map<String, String> tokens) {
        this.tokens = tokens;
    }

    public void changeState(URLTokenParserState newState) {
        state = newState;
    }

    public String tokenize(String line) {
        
        this.state = new URLTokenParserNoBracesState(this);

        for (int i = 0; i < line.length(); i++) {
            char read = line.charAt(i);

            if (read == '{') {
                state.addLeftBracket();
            } else if (read == '}') {
                state.addRightBracket();
            } else {
                state.addChar(read);
            }
        }
        
        // add final part to url
        this.addToURL();
        
        return this.result;
    }

    public String getTemp(){
        return this.token;
    }
    
    public void setToken(String token){
        this.token = token;
    }
    
    public String getKey(String key){
        return this.tokens.get(key);
    }
    
    public void addToTemp(char c) {
        token += c;
    }

    public void addToURL() {
        if (token != null && !token.equals("")) {
            result += token;
            token = "";
        }
    }
}
