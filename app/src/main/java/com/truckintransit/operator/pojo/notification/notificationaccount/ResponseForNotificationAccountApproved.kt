package com.truckintransit.operator.pojo.notification.notificationaccount

data class ResponseForNotificationAccountApproved(
    val description: String,
    val id: String,
    val title: String,
    val type: String,
    val pushtype: String
)