package com.bracketcove.postrainer

import com.bracketcove.postrainer.dependencyinjection.AndroidMovementProvider
import com.bracketcove.postrainer.movementlist.MovementListContract
import com.bracketcove.postrainer.movementlist.MovementListEvent
import com.bracketcove.postrainer.movementlist.MovementListLogic
import com.wiseassblog.common.MovementRepositoryException
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Movement
import com.wiseassblog.domain.repository.IMovementRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MovementListLogicTest {

    private val view: MovementListContract.View = mockk(relaxed = true)
    private val provider: AndroidMovementProvider = mockk()
    private val repository: IMovementRepository = mockk()

    val logic = MovementListLogic(view,
        provider,
        Dispatchers.Unconfined
    )

    private fun getMovement() = Movement(
        "Passive Hang",
        listOf("Chest, Shoulders, Arms, Back"),
        "2 Sets/Day",
        "30-60 Seconds",
        true,
        listOf("im_passive_one","im_passive_two","im_passive_three"),
        "Easy",
        "www.youtube.com",
        "description_passive_hang",
        "instruction_passive_hang"
    )
    
    @BeforeEach
    fun setUp() {
        clearAllMocks()
        every { provider.movementRepository } returns repository
    }

    /**
     *Get Movements: When Movement List feature's onStart is called, get current alarms
     * 1. Request list of Movements from Use Case
     * 2. Update view
     */
    @Test
    fun `On Start get Movements`() {
        val MOVEMENT_LIST: List<Movement> = listOf(getMovement(), getMovement())

        coEvery { repository.getMovements() } returns ResultWrapper.build { MOVEMENT_LIST }

        logic.handleEvent(MovementListEvent.OnStart)

        verify(exactly = 1) { view.setMovementListData(MOVEMENT_LIST) }
        coVerify { repository.getMovements() }
    }

    @Test
    fun `On Start get Movements error`() {
        coEvery { repository.getMovements() } returns ResultWrapper.build { throw MovementRepositoryException }

        logic.handleEvent(MovementListEvent.OnStart)

        verify(exactly = 1) { view.showMessage(ERROR_GENERIC) }
        verify(exactly = 1) { view.startRemindersView() }
        coVerify { repository.getMovements() }
    }

    /**
     *User wants to look at settings
     * 1. tell view to navigate to settings
     */
    @Test
    fun `On Settings Click`() {

        logic.handleEvent(MovementListEvent.OnSettingsClick)

        verify(exactly = 1) { view.startSettingsView() }
    }

    /**
     *User wants to look at Movements
     * 1. tell view to navigate to movements
     */
    @Test
    fun `On Reminders Click`() {
        logic.handleEvent(MovementListEvent.OnRemindersClick)

        verify(exactly = 1) { view.startRemindersView() }
    }

    /**
     * Uses wishes to see a detailed view of a particular movementId
     */
    @Test
    fun `On Movement Click`(){
        val MOVEMENT = getMovement()

        logic.handleEvent(MovementListEvent.OnMovementClick(MOVEMENT.name))

        verify { view.startMovementView(MOVEMENT.name) }
    }
}