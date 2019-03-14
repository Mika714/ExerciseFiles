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
}
