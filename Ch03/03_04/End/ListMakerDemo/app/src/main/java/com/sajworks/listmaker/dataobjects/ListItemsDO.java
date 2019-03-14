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

@DynamoDBTable(tableName = "listmakerandroid-mobilehub-2028196425-list_items")

public class ListItemsDO {
    private String _userId;
    private String _itemId;
    private String _item;
    private String _listNameId;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBIndexHashKey(attributeName = "userId", globalSecondaryIndexName = "list_name_index")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBRangeKey(attributeName = "itemId")
    @DynamoDBAttribute(attributeName = "itemId")
    public String getItemId() {
        return _itemId;
    }

    public void setItemId(final String _itemId) {
        this._itemId = _itemId;
    }
    @DynamoDBAttribute(attributeName = "item")
    public String getItem() {
        return _item;
    }

    public void setItem(final String _item) {
        this._item = _item;
    }
    @DynamoDBIndexRangeKey(attributeName = "listNameId", globalSecondaryIndexName = "list_name_index")
    public String getListNameId() {
        return _listNameId;
    }

    public void setListNameId(final String _listNameId) {
        this._listNameId = _listNameId;
    }

    public String getTableName() {
        return "listmakerandroid-mobilehub-2028196425-list_items";
    }

    public Map<String, AttributeValue> toMap() {
        Map<String, AttributeValue> map = new HashMap<>();
        map.put("userId", new AttributeValue(_userId));
        map.put("itemId", new AttributeValue(_itemId));
        map.put("item", new AttributeValue(_item));
        map.put("listNameId", new AttributeValue(_listNameId));
        return map;
    }

    public ListItemsDO() {
    }

    public ListItemsDO(String listString) {

        // Try to split the comma-separated string
        String[] array = listString.split(",");

        // Add the properties
        if (array.length > 2) {
            this._itemId = array[0];
            this._item = array[1];
            this._listNameId = array[2];
        }
    }

    public String toString() {
        return _itemId + "," + _item + "," + _listNameId;
    }

    // This method saves the ListItemsDO object to the shared preferences
    public void saveToSharedPrefs(SharedPreferences sharedPrefs) {

        // Get the list items for this list from the shared preferences
        Set<String> listItemsSet = sharedPrefs.getStringSet(this._listNameId, null);

        // Create an empty array if necessary
        ArrayList<String> listItems = new ArrayList<>();
        if (listItemsSet != null) {
            listItems = new ArrayList<>(listItemsSet);
        }

        // Add the serialized form of this list item to the listItems array
        listItems.add(this.toString());

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putStringSet(this._listNameId, new HashSet<>(listItems));
        editor.apply();
    }

    // This method removes this ListItemsDO object from the shared preferences
    public void removeFromSharedPreferences(SharedPreferences sharedPrefs) {

        SharedPreferences.Editor editor = sharedPrefs.edit();

        // Get the list items from the shared preferences
        Set<String> listItemsSet = sharedPrefs.getStringSet(this._listNameId, null);

        // Remove this list item
        if (listItemsSet != null) {
            ArrayList<String> listItems = new ArrayList<>(listItemsSet);
            if (listItems.contains(this.toString())) {
                listItems.remove(this.toString());
                editor.putStringSet(this._listNameId, new HashSet<>(listItems));
            }
        }

        editor.apply();
    }
}
