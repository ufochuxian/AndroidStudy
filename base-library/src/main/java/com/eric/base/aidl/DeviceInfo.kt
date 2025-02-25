package com.eric.base.aidl

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class DeviceInfo(val versionInfo: VersionInfo): Parcelable

@Parcelize
data class VersionInfo(val versionName: String, val versionCode: Int) : Parcelable
