package org.dhis2.usescases.eventsWithoutRegistration.eventCapture.eventCaptureFragment

import io.reactivex.Single
import org.dhis2.R
import org.dhis2.commons.resources.ResourceManager
import org.dhis2.commons.viewmodel.DispatcherProvider
import org.dhis2.usescases.eventsWithoutRegistration.eventCapture.EventCaptureContract
import org.dhis2.usescases.eventsWithoutRegistration.eventCapture.domain.ReOpenEventUseCase
import org.hisp.dhis.android.core.D2
import org.hisp.dhis.android.core.event.EventEditableStatus
import org.hisp.dhis.android.core.event.EventNonEditableReason
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class EventCaptureFormPresenterTest {
    private lateinit var presenter: EventCaptureFormPresenter
    private val activityPresenter: EventCaptureContract.Presenter = mock()
    private val view: EventCaptureFormView = mock()
    private val d2: D2 = mock()
    private val eventUid: String = "random_ID"
    private val nonEditableMessage = "Blocked by completion message"
    private val resourceManager: ResourceManager = mock {
        on { getString(R.string.blocked_by_completion) } doReturn nonEditableMessage
    }
    private val reOpenUseCase: ReOpenEventUseCase = mock()
    private val dispatcherProvider: DispatcherProvider = mock()

    @Before
    fun setUp() {
        presenter = EventCaptureFormPresenter(
            view,
            activityPresenter,
            d2,
            eventUid,
            resourceManager = resourceManager,
            reOpenEventUseCase = reOpenUseCase,
            dispatcherProvider = dispatcherProvider,
        )
    }

    @Test
    fun `Should show save button when event is editable`() {
        val editableStatus = EventEditableStatus.Editable()
        whenever(d2.eventModule()) doReturn mock()
        whenever(d2.eventModule().eventService()) doReturn mock()
        whenever(d2.eventModule().eventService().getEditableStatus(eventUid)) doReturn Single.just(
            editableStatus,
        )

        presenter.showOrHideSaveButton()

        verify(view).showSaveButton()
    }

    @Test
    fun `Should hide save button when event is not editable`() {
        val editableStatus =
            EventEditableStatus.NonEditable(EventNonEditableReason.BLOCKED_BY_COMPLETION)
        whenever(d2.eventModule()) doReturn mock()
        whenever(d2.eventModule().eventService()) doReturn mock()
        whenever(d2.eventModule().eventService().getEditableStatus(eventUid)) doReturn Single.just(
            editableStatus,
        )

        whenever(d2.eventModule().events()) doReturn mock()
        whenever(d2.eventModule().events().uid(eventUid)) doReturn mock()

        presenter.showOrHideSaveButton()

        verify(view).hideSaveButton()
        verify(view).showNonEditableMessage(nonEditableMessage, false)
    }
}
