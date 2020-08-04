package com.juno.nasaphotooftheday.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.juno.nasaphotooftheday.R
import com.juno.nasaphotooftheday.data.repository.ApiClient
import com.juno.nasaphotooftheday.data.model.ApiModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ApiViewModel :ViewModel(){

    lateinit var subscription:Disposable
    var mutableLiveData:MutableLiveData<ApiModel> = MutableLiveData()
    val errorMessage : MutableLiveData<Int> = MutableLiveData()

    fun getDatafromApi(apikey:String,date:String) {

        if(date.trim().length==0){

            subscription=ApiClient.create().getData(apikey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {result -> onRetrieveSuccess(result) },
                    { onRetrieveError() }
                        )
        }else{
            subscription=ApiClient.create().getDataDate(apikey,date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> onRetrieveSuccess(result) },
                    { onRetrieveError() }
                          )
        }
      }

    public fun getLiveData() : LiveData<ApiModel>{
        return mutableLiveData
    }

    private fun onRetriveStart() {
    }

    private fun onRetrieveFinish() {
    }
    private fun onRetrieveSuccess(result: ApiModel) {
        mutableLiveData.postValue(result)
    }

    private fun onRetrieveError() {
        errorMessage.value= R.string.post_error
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}