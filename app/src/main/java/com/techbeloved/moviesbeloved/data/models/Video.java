package com.techbeloved.moviesbeloved.data.models;

public interface Video {
    String getId();

    /**
     *
     * @return the movie Id that has the video
     */
    int getMovieId();

    /**
     * @return key which is the youtube id
     */
    String getKey();
    String getName();
    String getSite();

    /**
     * Whether it is trailer or video clip aor whatever
     *
     * @return
     */
    String getType();

    /**
     * Sets the movie key or youtube Id
     * @param key is the youtube id for the video
     */
    void setKey(String key);

    /**
     *
     * @param name is the name of the video on the given site
     */
    void setName(String name);

    void setSite(String site);

    /**
     * @param type is the type of the video whether trailer or feature or clip, etc
     */
    void setType(String type);
}
