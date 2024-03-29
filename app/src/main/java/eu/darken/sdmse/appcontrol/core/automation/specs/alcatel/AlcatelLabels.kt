package eu.darken.sdmse.appcontrol.core.automation.specs.alcatel

import dagger.Reusable
import eu.darken.sdmse.appcontrol.core.automation.specs.aosp.AOSPLabels14Plus
import eu.darken.sdmse.automation.core.common.AutomationLabelSource
import eu.darken.sdmse.common.debug.logging.logTag
import javax.inject.Inject

@Reusable
class AlcatelLabels @Inject constructor(
    private val labels14Plus: AOSPLabels14Plus,
) : AutomationLabelSource {

    fun getForceStopDynamic(): Set<String> = labels14Plus.getStorageEntryDynamic()

    companion object {
        val TAG: String = logTag("AppControl", "Automation", "Alcatel", "Labels")
    }
}