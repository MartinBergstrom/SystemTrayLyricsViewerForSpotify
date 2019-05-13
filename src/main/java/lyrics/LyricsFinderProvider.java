package lyrics;

import java.util.List;

public interface LyricsFinderProvider {

     /**
      * @return a list of lyrics finders sorted by priority order
      */
     List<LyricsFinder> getAllLyricsFinders();

}
