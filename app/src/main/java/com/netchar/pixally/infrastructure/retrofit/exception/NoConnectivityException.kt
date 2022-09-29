package com.netchar.pixally.infrastructure.retrofit.exception

import java.io.IOException

class NoConnectivityException : IOException("No network available, please check your WiFi or Data connection")

