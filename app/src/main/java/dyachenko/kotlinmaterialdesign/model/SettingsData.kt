package dyachenko.kotlinmaterialdesign.model

import dyachenko.kotlinmaterialdesign.R

object SettingsData {
    const val PREFERENCE_NAME = "Settings"
    const val CURRENT_THEME_PREF_NAME = "THEME"

    const val THEME_PURPLE_ID = R.style.Theme_KotlinMaterialDesign_Purple
    const val THEME_TEAL_ID = R.style.Theme_KotlinMaterialDesign_Teal
    const val THEME_PURPLE = 0
    const val THEME_TEAL = 1

    const val TODAY_PHOTO = 0
    const val YESTERDAY_PHOTO = 1
    const val DAY_BEFORE_YESTERDAY_PHOTO = 2

    var CURRENT_THEME = THEME_PURPLE
    var DAY_OF_PHOTO = TODAY_PHOTO
}