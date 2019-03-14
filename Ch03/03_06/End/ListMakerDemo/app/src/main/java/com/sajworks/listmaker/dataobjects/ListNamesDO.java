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
}
