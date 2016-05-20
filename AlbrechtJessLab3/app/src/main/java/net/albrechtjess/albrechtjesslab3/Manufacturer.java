package net.albrechtjess.albrechtjesslab3;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jess on 4/14/2016.
 */
public class Manufacturer implements Serializable {
    protected String mName;
    protected ArrayList<String> models;
    public Manufacturer(String name)
    {
        mName = name;
        models = new ArrayList<>();
    }
    public String getName()
    {
        return mName;
    }
    public String getModelNameatPosition(int position)
    {
        return models.get(position);
    }
    public void deleteModel(int position)
    {
        models.remove(position);
    }
    public int getTotalModels()
    {
        return models.size();
    }
    public void addModel(String newModel)
    {
        models.add(newModel);
    }
}
