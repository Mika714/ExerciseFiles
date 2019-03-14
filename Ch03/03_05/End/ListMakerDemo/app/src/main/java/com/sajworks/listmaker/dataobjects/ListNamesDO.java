package com.sajworks.listmaker.dataobjects;

import android.content.SharedPreferences;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "listmakerandroid-mobilehub-2028196425-list_names")

public class ListNamesDO {
    private String _userId;
    private String _nameId;
    private String _name;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBRangeKey(attributeName = "nameId")
    @DynamoDBAttribute(attributeName = "nameId")
    public String getNameId() {
        return _nameId;
    }

    public void setNameId(final String _nameId) {
        this._nameId = _nameId;
    }
    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return _name;
    }

    public void setName(final String _name) {
        this._name = _name;
    }

    public String getTableName() {
        return "listmakerandroid-mobilehub-2028196425-list_names";
    }

    public Map<String, AttributeValue> toMap() {
        Map<String, AttributeValue> map = new HashMap<>();
        map.put("userId", new AttributeValue(_userId));
        map.put("nameId", new AttributeValue(_nameId));
        map.put("name", new AttributeValue(_name));
        return map;
    }

    public ListNamesDO() {
    }

    public ListNamesDO(String listString) {

        // Try to split the comma-separated string
        String[] array = listString.split(",");

        // Add the properties
        if (array.length > 1) {
            this._nameId = array[0];
            this._name = array[1];
        }
    }

    public String toString() {
        return _nameId + "," + _name;
    }

    // This method saves the ListNamesDO object to the shared preferences
    public void saveToSharedPrefs(SharedPreferences sharedPrefs) {

        // Get the list names from the shared preferences
        Set<String> listNamesSet = sharedPrefs.getStringSet("listNames", null);

        // Create an empty array if necessary
        ArrayList<String> listNames = new ArrayList<>();
        if (listNamesSet != null) {
            listNames = new ArrayList<>(listNamesSet);
        }

        // Add the serialized form of this list object to the listNames array
        listNames.add(this.toString());

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putStringSet("listNames", new HashSet<>(listNames));
        editor.putStringSet(this._nameId, new HashSet<String>());
        editor.apply();
    }

    // This method removes this ListNamesDO object and all its list items from the shared perferences
    public void removeFromSharedPreferences(SharedPreferences sharedPrefs) {

        SharedPreferences.Editor editor = sharedPrefs.edit();

        // Get the list names from the shared preferences
        Set<String> listNamesSet = sharedPrefs.getStringSet("listNames", null);

        // Remove this list
        if (listNamesSet != null) {
            ArrayList<String> listNames = new ArrayList<>(listNamesSet);
            if (listNames.contains(this.toString())) {
                listNames.remove(this.toString());
                editor.putStringSet("listNames", new HashSet<>(listNames));
            }
        }

        // Remove any items for this list
        Set<String> listItemsSet = sharedPrefs.getStringSet(this._nameId, null);
        if (listItemsSet != null) {
            editor.remove(this._nameId);
        }

        editor.apply();
    }
}
