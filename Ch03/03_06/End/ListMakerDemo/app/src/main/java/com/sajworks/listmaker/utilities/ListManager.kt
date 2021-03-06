package com.sajworks.listmaker.utilities

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression
import com.amazonaws.services.dynamodbv2.model.*
import com.sajworks.listmaker.dataobjects.ListItemsDO
import com.sajworks.listmaker.dataobjects.ListNamesDO
import java.util.*

class ListManager {

    companion object {

        @JvmField var listNames = ArrayList<ListNamesDO>()
        @JvmField var lists = HashMap<String, ArrayList<ListItemsDO>>()

        // This method retrieves the list for the given listNameId or returns the first list in the array if no matching list is found
        fun getListForId(listNameId: String) : ListNamesDO {

            for (thisList in listNames) {
                if (thisList.nameId == listNameId) {
                    return thisList
                }
            }

            // If control reaches this point, then no list with the given listNameId was found, so return the first list in the array
            if (listNames.size > 0) {
                return listNames[0]
            }

            // If there weren't any items in the lists array at all, then simply return a new ListNamesDO object
            else {
                return ListNamesDO()
            }
        }

        // This method retrieves the names of the existing lists
        fun requestListNames(context: Context) {

            // Reset the listNames array
            listNames = ArrayList()

            // Get a DynamoDBMapper object
            val dbMapper = AWSProvider.getInstance().getDynamoDBMapper()

            // Get the userId
            val userId = AWSProvider.getInstance().getIdentityManager().cachedUserID

            // Create a template ListNamesDO object to use as the basis for a query
            val template = ListNamesDO()
            template.userId = userId

            // Create a query to retrieve all the list names for this userId
            val query = DynamoDBQueryExpression<ListNamesDO>().withHashKeyValues(template)

            // Perform the query
            val result = dbMapper.query(ListNamesDO::class.java, query)

            // Iterate over the results and collect the list names and list ids into an array
            val iterator = result.iterator()
            while (iterator.hasNext()) {
                val list = iterator.next()
                listNames.add(list)
            }

            // Send a broadcast to let any interested objects know that the list names have been retrieved
            val intent = Intent("listNamesDownloaded")
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }

        // This method retrieves the list items for the given list
        fun requestListItems(list: ListNamesDO, context: Context) {

            // Get a DynamoDBMapper object
            val dbMapper = AWSProvider.getInstance().getDynamoDBMapper()

            // Get the userId
            val userId = AWSProvider.getInstance().getIdentityManager().cachedUserID

            // Create a template ListItemsDO object to use as the basis for a query
            val template = ListItemsDO()
            template.userId = userId

            // Create a range key condition to find list items with the matching listNameId
            val rangeKeyCondition = Condition()
            rangeKeyCondition.withComparisonOperator(ComparisonOperator.EQ)
                    .withAttributeValueList(AttributeValue().withS(list.nameId))

            // Create a query to retrieve all the list items for this listNameId
            val query = DynamoDBQueryExpression<ListItemsDO>()
                    .withHashKeyValues(template)
                    .withConsistentRead(false)
                    .withRangeKeyCondition("listNameId", rangeKeyCondition)

            // Perform the query
            val result = dbMapper.query(ListItemsDO::class.java, query)

            // Iterate over the results and collect the list names into an array
            var listItems = ArrayList<ListItemsDO>()
            val iterator = result.iterator()
            while (iterator.hasNext()) {
                val listItem = iterator.next()
                listItems.add(listItem)
            }

            // Add the listItems array to the lists map
            lists[list.nameId] = listItems

            // Send a broadcast to let any interested objects know that the list items have been retrieved
            val intent = Intent("listItemsDownloaded")
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }

        // This method creates a new list
        fun createList(listName: String, context: Context) {

            // Get an AmazonDynamoDBClient object
            val dbClient = AWSProvider.getInstance().getAmazonDynamoDBClient()

            // Get the userId
            val userId = AWSProvider.getInstance().getIdentityManager().cachedUserID

            // Create a new ListNamesDO object
            val newList = ListNamesDO()
            newList.userId = userId
            newList.nameId = UUID.randomUUID().toString()
            newList.name = listName

            // Add the new list locally
            listNames.add(newList)
            lists[newList.nameId] = ArrayList()

            // Create a request to add this list
            val putItemRequest = PutItemRequest(newList.tableName, newList.toMap())
            val putItemResult = dbClient.putItem(putItemRequest)

            // Send a broadcast to let any interested objects know that the list has been added
            val intent = Intent("listAdded")
            intent.putExtra("listNameId", newList.nameId)
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }

        // This method deletes a list and all of its items
        fun deleteList(list: ListNamesDO, context: Context) {

            // Get an AmazonDynamoDBClient object
            val dbClient = AWSProvider.getInstance().getAmazonDynamoDBClient()

            // Create a map with the primary key and the sort key for the list_names table
            val map = HashMap<String, AttributeValue>()
            map["userId"] = AttributeValue(list.userId)
            map["nameId"] = AttributeValue(list.nameId)

            // Create a request to delete this list
            val deleteItemRequest = DeleteItemRequest(ListNamesDO().tableName, map)
            val deleteItemResult = dbClient.deleteItem(deleteItemRequest)

            // Delete all the items for this list
            if (lists[list.nameId] != null) {
                for (thisListItem in lists[list.nameId]!!) {

                    // Create a map with the primary key and the sort key for the list_items table
                    val map = HashMap<String, AttributeValue>()
                    map["userId"] = AttributeValue(thisListItem.userId)
                    map["itemId"] = AttributeValue(thisListItem.itemId)

                    // Create a request to delete this list
                    val thisDeleteItemRequest = DeleteItemRequest(ListItemsDO().tableName, map)
                    dbClient.deleteItem(thisDeleteItemRequest)
                }
            }

            // Delete the list items locally
            lists.remove(list.nameId)

            // Delete the list locally
            listNames.remove(list)

            // Send a broadcast to let any interested objects know that the list has been deleted
            val intent = Intent("listRemoved")
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }

        // This method creates a new list item
        fun createListItem(listItem: String, list: ListNamesDO, context: Context) {

            // Get an AmazonDynamoDBClient object
            val dbClient = AWSProvider.getInstance().getAmazonDynamoDBClient()

            // Get the userId
            val userId = AWSProvider.getInstance().getIdentityManager().cachedUserID

            // Create a new ListItemsDO object
            val newItem = ListItemsDO()
            newItem.userId = userId
            newItem.listNameId = list.nameId
            newItem.itemId = UUID.randomUUID().toString()
            newItem.item = listItem

            // Add the new list item locally
            var listItems = lists[list.nameId] as ArrayList<ListItemsDO>
            listItems.add(newItem)
            lists[list.nameId] = listItems

            // Create a request to add this list item
            val putItemRequest = PutItemRequest(newItem.tableName, newItem.toMap())
            val putItemResult = dbClient.putItem(putItemRequest)

            // Send a broadcast to let any interested objects know that the list item has been added
            val intent = Intent("listItemAdded")
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }

        // This method deletes a given list item
        fun deleteListItem(listItem: ListItemsDO, context: Context) {

            // Get an AmazonDynamoDBClient object
            val dbClient = AWSProvider.getInstance().getAmazonDynamoDBClient()

            // Create a map with the primary key and the sort key for the list_items table
            val map = HashMap<String, AttributeValue>()
            map["userId"] = AttributeValue(listItem.userId)
            map["itemId"] = AttributeValue(listItem.itemId)

            // Delete the list item locally
            val list = ListManager.getListForId(listItem.listNameId)
            var listItems = lists[list.nameId] as ArrayList<ListItemsDO>
            listItems.remove(listItem)
            lists[list.nameId] = listItems

            // Create a request to delete this list
            val deleteItemRequest = DeleteItemRequest(ListItemsDO().tableName, map)
            val deleteItemResult = dbClient.deleteItem(deleteItemRequest)

            // Send a broadcast to let any interested objects know that the list item has been deleted
            val intent = Intent("listItemRemoved")
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }
    }
}