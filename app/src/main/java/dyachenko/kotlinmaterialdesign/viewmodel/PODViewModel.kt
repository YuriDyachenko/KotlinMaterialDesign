package dyachenko.kotlinmaterialdesign.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dyachenko.kotlinmaterialdesign.BuildConfig
import dyachenko.kotlinmaterialdesign.R
import dyachenko.kotlinmaterialdesign.model.PODResponseData
import dyachenko.kotlinmaterialdesign.model.PODRetrofitImpl
import dyachenko.kotlinmaterialdesign.util.ResourceProvider
import dyachenko.kotlinmaterialdesign.viewmodel.PODState.Loading
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PODViewModel : ViewModel() {
    private val liveDataToObserve: MutableLiveData<PODState> = MutableLiveData()
    private val retrofitImpl = PODRetrofitImpl()
    var resourceProvider: ResourceProvider? = null

    fun getLiveData() = liveDataToObserve

    fun getPODFromServer(dateString: String) {
        liveDataToObserve.value = Loading
        val apiKey: String = BuildConfig.NASA_API_KEY
        retrofitImpl.getRetrofitImpl().getPOD(apiKey, dateString).enqueue(object :
            Callback<PODResponseData> {
            override fun onResponse(
                call: Call<PODResponseData>,
                response: Response<PODResponseData>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    liveDataToObserve.value = PODState.Success(response.body()!!)
                } else {
                    val message = response.message()
                    if (message.isNullOrEmpty()) {
                        liveDataToObserve.value =
                            PODState.Error(Throwable(getString(R.string.error_server_msg)))
                    } else {
                        liveDataToObserve.value = PODState.Error(Throwable(message))
                    }
                }
            }

            override fun onFailure(call: Call<PODResponseData>, t: Throwable) {
                liveDataToObserve.value = PODState.Error(
                    Throwable(t.message ?: getString(R.string.error_request_msg))
                )
            }
        })
    }

    private fun getString(id: Int) = resourceProvider?.getString(id)
}