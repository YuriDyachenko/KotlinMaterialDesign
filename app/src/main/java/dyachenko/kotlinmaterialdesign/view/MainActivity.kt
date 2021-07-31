package dyachenko.kotlinmaterialdesign.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dyachenko.kotlinmaterialdesign.R
import dyachenko.kotlinmaterialdesign.view.pod.PODFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PODFragment.newInstance())
                .commitNow()
        }
    }
}