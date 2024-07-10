package com.example.workoutapplication;

import java.util.LinkedList;

public class Exercise {

    // TODO: check uniqueness (unique name/description?)

    private String name;
    private String description;
    private String thumbnailID; // Passing the storing/loading of images onto the activity
    // private Workouts[] includedInWorkouts;
    private LinkedList<String> tags; // maybe switch this to pairs or a Tag class (tag name, reference to Tag)

    public Exercise (String name, String description) {
        this.name = name;
        this.description = description;
        tags = new LinkedList<String>();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getThumbnailID()
    {
        return thumbnailID;
    }

    public void setThumbnailID(String thumbnailID)
    {
        this.thumbnailID = thumbnailID;
    }

    // Tag Methods
    public LinkedList<String> getTags()
    {
        return tags;
    }

    public void clearAllTags() {
        tags.clear();
    }

    public void removeTag(String tag)
    {
        tags.remove(tag);
    }

    public void addTag(String tag)
    {
        tags.add(tag);
    }

}
