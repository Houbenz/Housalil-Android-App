package databases;

import android.support.design.widget.TextInputLayout;
import android.text.InputFilter;
import android.text.Spanned;

import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Houbenz on 05/06/2018.
 */

public class InputFilterRegex implements InputFilter {

    private Pattern mPattern;

    private static final String CLASS_NAME=InputFilter.class.getSimpleName();


    public InputFilterRegex(Pattern pattern){
        if(pattern==null){
            throw  new IllegalArgumentException(CLASS_NAME+" must have a regex");
        };
        mPattern=pattern;
    }

    public InputFilterRegex(String pattern){
        this(Pattern.compile(pattern));
    }
    @Override
    public CharSequence filter(CharSequence source , int start, int end, Spanned dest, int dstart, int dend) {

        Matcher matcher =mPattern.matcher(source);

        if(!matcher.matches()){
            return "";
        }else
        return null;
    }
}
