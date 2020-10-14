package lyrics;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

class ArtistAndSongStringFormatter {
    private static final Pattern BASE_REGEX = Pattern.compile("\\s?-?\\s?([\\d]{4})?\\s?([rR]emaster(ed)?|[mM]ix).*");
    private Set<Function<String, String>> formatterFunctions;

    ArtistAndSongStringFormatter() {
        this.formatterFunctions = new HashSet<>();
    }

    ArtistAndSongStringFormatter addDelimiterReplaceWhiteSpace(String delimiter) {
        formatterFunctions.add(string -> string.replaceAll(" ", delimiter));
        return this;
    }

    ArtistAndSongStringFormatter addRemoveAllWhiteSpace() {
        formatterFunctions.add(string -> string.replace(" ", ""));
        return this;
    }

    String format(String stringToFormat) {
        String baseFormattedString = BASE_REGEX.matcher(stringToFormat).replaceAll("")
                .replaceAll("(.*)(Live [aA]t.*)", "$1")
                .toLowerCase()
                .trim();
        for (Function<String, String> formatterFn: this.formatterFunctions)
        {
            baseFormattedString = formatterFn.apply(baseFormattedString);
        }
        return baseFormattedString;
    }


}
