//package com.example.task.ui.presenters
//
//import android.util.Log
//import com.example.task.R
//import com.example.task.data.api.RetrofitService
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.schedulers.Schedulers
//
//class AllAlbumsPresenter : BaseAlbumsPresenter() {
//
//    override fun saveOrRemove(id: Int) {
//        TODO("Not yet implemented")
//    }
//
//    override fun loadAlbums() {
//
//        Log.d("jhjsavd64", "loadAlbums")
//
//        viewState.showProgress()
//        viewState.showStatus(R.string.loading)
//
//        compositeDisposable.add(RetrofitService.create().getAlbumsAsync()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                { result ->
//                    Log.d("jhjsavd64", "result : $result")
//                    viewState.hideProgress()
//                    viewState.showAlbumList(result)
//                    Log.d("jhjsavd64", "viewState.showAlbumList(result)")
//                },
//                {
//                    Log.d("jhjsavd64", "error : ${it.message}")
//                    viewState.hideProgress()
//                    viewState.showStatus(R.string.error_loading)
//                }
//            )
//        )
//    }
//}