package com.example.workoutapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.UUID;

public class Exercise {

    // TODO: check uniqueness (unique name/description?)

    private UUID exerciseID; // Class with very low-probably of generating the same "unique ID"
    private String name;
    private String description;
    private String thumbnailID; // Passing the storing/loading of images onto the activity
    // private Workouts[] includedInWorkouts;
    private LinkedList<String> tags; // maybe switch this to pairs or a Tag class (tag name, reference to Tag)

    /**
     * Exercise constructor which takes a JsonString, in the format of .toJsonString()
     * @param JsonString
     * @return an Exercise with attributes based on JsonString
     */
    public static Exercise fromJsonString(String JsonString)
    {
        Exercise resultExercise = new Exercise();

        try
        {
            JSONObject jsonObject = new JSONObject(JsonString);

            String IDString = (String) jsonObject.get("UniqueID");
            resultExercise.setExerciseID(UUID.fromString(IDString));

            resultExercise.setName(jsonObject.get("Name").toString());
            resultExercise.setDescription(jsonObject.get("Description").toString());
            resultExercise.setThumbnailID(jsonObject.get("ThumbnailID").toString());

            JSONArray tagsArray = new JSONArray(jsonObject.get("Tags").toString());
            for (int i=0; i<tagsArray.length(); i++)
            {
                resultExercise.addTag(tagsArray.get(i).toString());
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return resultExercise;
    }

    private Exercise() {
        tags = new LinkedList<String>();
    }

    // Constructor for the creation of "new" exercises, with a name and description as the base
    public Exercise (String name, String description)
    {
        exerciseID = UUID.randomUUID();

        this.name = name;
        this.description = description;
        tags = new LinkedList<String>();
    }

    public Exercise (String uniqueID, String name, String description)
    {
        this.exerciseID = UUID.fromString(uniqueID);
        this.name = name;
        this.description = description;
        tags = new LinkedList<String>();
    }

    // Private setter, intended for use in .fromJsonString() method
    private void setExerciseID(UUID exerciseID)
    {
        this.exerciseID = exerciseID;
    }

    public UUID getExerciseID()
    {
        return this.exerciseID;
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

    // JSON Methods

    // Returns a Json string representation of the current object
    public String toJsonString()
    {
        // TODO: for boiler-plate reasons, maybe try and remove JSONObject and manually convert to strings --> no dependence on org.json
        JSONObject jsonExercise = new JSONObject();

        try
        {
            // Putting exercise's variable values
            jsonExercise.put("UniqueID", this.getExerciseID().toString());
            jsonExercise.put("Name", this.getName());
            jsonExercise.put("Description", this.getDescription());
            jsonExercise.put("ThumbnailID", this.getThumbnailID());

            // JSONArray to hold exercise's tags
            JSONArray array = new JSONArray(this.getTags());
            jsonExercise.put("Tags", array);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null; /** INCREDIBLY IMPORTANT **/
        }

        return jsonExercise.toString();
    }
}
