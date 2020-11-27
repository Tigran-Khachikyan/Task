package com.example.task.ui.presenters

import android.util.Log
import com.example.task.R
import com.example.task.data.api.RetrofitService
import com.example.task.ui.AlbumsView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
abstract class BaseAlbumsPresenter : MvpPresenter<AlbumsView>() {

    protected val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadAlbums()
        Log.d("jhjsavd64", "onFirstViewAttach loadAlbums")

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    protected abstract fun loadAlbums()
    abstract fun saveOrRemove(id: Int)

    fun refresh() {
        loadAlbums()
        Log.d("jhjsavd64", "refresh loadAlbums")

    }

    fun openAlbum(id: Int) {

    }


}