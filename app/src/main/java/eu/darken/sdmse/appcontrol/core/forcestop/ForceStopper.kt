package eu.darken.sdmse.appcontrol.core.forcestop

import dagger.Reusable
import eu.darken.sdmse.common.ModeUnavailableException
import eu.darken.sdmse.common.coroutine.AppScope
import eu.darken.sdmse.common.coroutine.DispatcherProvider
import eu.darken.sdmse.common.debug.logging.Logging.Priority.INFO
import eu.darken.sdmse.common.debug.logging.log
import eu.darken.sdmse.common.debug.logging.logTag
import eu.darken.sdmse.common.pkgs.features.Installed
import eu.darken.sdmse.common.pkgs.pkgops.PkgOps
import eu.darken.sdmse.common.sharedresource.HasSharedResource
import eu.darken.sdmse.common.sharedresource.SharedResource
import eu.darken.sdmse.common.sharedresource.adoptChildResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

@Reusable
class ForceStopper @Inject constructor(
    @AppScope private val appScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider,
    private val pkgOps: PkgOps,
) : HasSharedResource<Any> {

    override val sharedResource = SharedResource.createKeepAlive(TAG, appScope + dispatcherProvider.IO)

    suspend fun forceStop(installId: Installed.InstallId) {
        log(TAG, INFO) { "forceStop($installId)" }
        adoptChildResource(pkgOps)
        try {
            pkgOps.forceStop(installId.pkgId)
        } catch (e: ModeUnavailableException) {

        }
    }

    companion object {
        private val TAG = logTag("AppControl", "ForceStopper")
    }
}