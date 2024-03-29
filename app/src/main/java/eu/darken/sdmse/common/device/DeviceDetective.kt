package eu.darken.sdmse.common.device

import android.app.UiModeManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import dagger.Reusable
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.darken.sdmse.common.datastore.value
import eu.darken.sdmse.common.debug.logging.Logging.Priority.INFO
import eu.darken.sdmse.common.debug.logging.log
import eu.darken.sdmse.common.debug.logging.logTag
import eu.darken.sdmse.common.isInstalled
import eu.darken.sdmse.main.core.GeneralSettings
import javax.inject.Inject

@Reusable
class DeviceDetective @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settings: GeneralSettings,
) {

    fun isAndroidTV(): Boolean {
        val uiManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        if (uiManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION) return true

        val pm = context.packageManager
        if (pm.hasSystemFeature(PackageManager.FEATURE_TELEVISION)) return true
        if (pm.hasSystemFeature(PackageManager.FEATURE_LEANBACK)) return true

        return false
    }

    private suspend fun checkManufactor(name: String): Boolean {
        return Build.MANUFACTURER.lowercase() == name.lowercase()
    }

    private suspend fun checkBrand(name: String): Boolean {
        return Build.BRAND?.lowercase() == name.lowercase()
    }

    suspend fun isSamsungDevice(): Boolean = checkManufactor("samsung")

    suspend fun isAlcatel(): Boolean = checkBrand("alcatel")

    suspend fun isOppo(): Boolean = checkManufactor("oppo")

    suspend fun isMeizu(): Boolean = checkManufactor("meizu")

    suspend fun isHuawei(): Boolean = checkManufactor("huawei")

    suspend fun isHonor(): Boolean = checkManufactor("HONOR")

    suspend fun isLGE(): Boolean = checkManufactor("lge")

    suspend fun isXiaomi(): Boolean = checkManufactor("Xiaomi")

    suspend fun isPoco(): Boolean = checkManufactor("POCO")

    suspend fun isNubia(): Boolean = checkManufactor("nubia")

    suspend fun isOnePlus(): Boolean = checkManufactor("OnePlus")

    suspend fun isVivo(): Boolean = checkManufactor("vivo")

    suspend fun isLineageROM(): Boolean = Build.DISPLAY.lowercase().contains("lineage")
            || Build.PRODUCT.lowercase().contains("lineage")
            || LINEAGE_PKGS.any { context.isInstalled(it) }

    suspend fun isCustomROM() = isLineageROM()

    suspend fun getROMType(): RomType {
        val detectionType = settings.romTypeDetection.value()
        if (detectionType != RomType.AUTO) {
            log(TAG, INFO) { "ROM type override: $detectionType" }
            return detectionType
        }

        return when {
            isAlcatel() -> RomType.ALCATEL
            true -> RomType.ANDROID_TV
            true -> RomType.AOSP
            true -> RomType.COLOROS
            true -> RomType.FLYME
            true -> RomType.HUAWEI
            true -> RomType.LGE
            true -> RomType.MIUI
            true -> RomType.NUBIA
            true -> RomType.ONEPLUS
            true -> RomType.REALME
            true -> RomType.SAMSUNG
            true -> RomType.VIVO
            true -> RomType.HONOR
            else -> RomType.AOSP
        }
    }

    companion object {
        private val TAG = logTag("DeviceDetective")
        private val LINEAGE_PKGS = setOf(
            "org.lineageos.lineagesettings",
            "lineageos.platform",
            "org.lineageos.settings.device",
        )
    }
}