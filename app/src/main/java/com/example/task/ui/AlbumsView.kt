package com.example.task.ui

import com.example.task.model.Album
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface AlbumsView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class, tag = "show_hide_image")
    fun showAlbumList(albums: List<Album>)

    @StateStrategyType(AddToEndSingleStrategy::class, tag = "show_hide_image")
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class, tag = "show_hide_image")
    fun hideProgress()

    @StateStrategyType(OneExecutionStateStrategy::class, tag = "show_hide_image")
    fun showStatus(textRes: Int)

    @StateStrategyType(OneExecutionStateStrategy::class, tag = "show_hide_image")
    fun hideStatus()
}