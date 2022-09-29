package com.netchar.pixally.infrastructure.retrofit.exception

import java.io.IOException

class NoInternetException() : IOException("No internet available, please check your connected WIFi or Data")