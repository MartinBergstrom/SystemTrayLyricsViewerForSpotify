package lyrics;

class ArtistAndSongStringFormatter {
    private String myFormattedString;

    ArtistAndSongStringFormatter(String myFormattedString) {
        this.myFormattedString = myFormattedString.replaceAll("[rR]emastered", "")
                .replaceAll("-", "")
                .replaceAll("(.*)(Live [aA]t.*)", "$1")
                .toLowerCase()
                .trim();
    }

    ArtistAndSongStringFormatter addDelimiter(String delimiter) {
        myFormattedString = myFormattedString.replaceAll(" ", delimiter);
        return this;
    }

    ArtistAndSongStringFormatter addReplaceAlloperation(String regex, String replace) {
        myFormattedString = myFormattedString.replaceAll(regex, replace);
        return this;
    }

    ArtistAndSongStringFormatter addRemoveAllWhiteSpace() {
        myFormattedString = myFormattedString.replace(" ", "");
        return this;
    }

    String format() {
        return myFormattedString;
    }


}
