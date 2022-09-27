package com.netchar.pixally.infrastructure

import kotlinx.coroutines.Dispatchers

class CoroutineDispatcher {
    val io = Dispatchers.IO
    val default = Dispatchers.Default
    val unconfined = Dispatchers.Unconfined
    val main = Dispatchers.Main
}