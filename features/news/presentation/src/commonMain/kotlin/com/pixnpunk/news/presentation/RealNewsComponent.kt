package com.pixnpunk.news.presentation

import com.arkivanov.decompose.ComponentContext

class RealNewsComponent(
    private val componentContext: ComponentContext
) : ComponentContext by componentContext, NewsComponent