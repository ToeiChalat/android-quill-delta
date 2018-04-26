package com.chalat.androidquilldelta

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 *
 * Created by Chalat Chansima on 3/22/18.
 *
 */
data class DeltaItem (
        @SerializedName("insert")       val insert: String,
        @SerializedName("attributes")   val attributes: HashMap<String, Any>?
)