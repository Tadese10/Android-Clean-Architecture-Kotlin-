package com.example.cleanarchitecture.business.domain.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DateUtil
@Inject
constructor(
    private val dateFormat: SimpleDateFormat
){

    //date format: "2019-07-23 HH:mm:ss
    /*
    Firestore date using Timestamp
    Sqlite date
     */

    fun removeTimeFromDateString(sd: String): String{
        return sd.substring(0, sd.indexOf(" "))
    }

    //Convert firebase Timestamp to string date
    fun convertFirebaseTimestampToStringDate(ts:Timestamp): String{
        return dateFormat.format(ts.toDate())
    }

    //Convert String timestamp to timestamp
    fun convertStringDateToFirebaseTimeStamp(date: String): Timestamp{
        return Timestamp(dateFormat.parse(date))
    }

    fun getCurrentTimeStamp(): String{
        return dateFormat.format(Date())
    }
}