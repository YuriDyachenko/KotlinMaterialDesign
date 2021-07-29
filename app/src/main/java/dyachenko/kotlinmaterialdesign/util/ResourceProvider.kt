package dyachenko.kotlinmaterialdesign.util

import android.content.Context

class ResourceProvider(private val context: Context) {
    fun getString(id: Int) = context.getString(id)
}