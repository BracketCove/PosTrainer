package com.bracketcove.postrainer

import com.bracketcove.postrainer.dependencyinjection.AndroidMovementProvider
import com.bracketcove.postrainer.movement.MovementContract
import com.bracketcove.postrainer.movement.MovementEvent
import com.bracketcove.postrainer.movement.MovementLogic
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Movement
import com.wiseassblog.domain.repository.IMovementRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MovementLogicTest {

    val repo: IMovementRepository = mockk()
    val view: MovementContract.View = mockk(relaxed = true)
    val viewModel: MovementContract.ViewModel = mockk(relaxed = true)
    val provider: AndroidMovementProvider = mockk()

    val logic = MovementLogic(view, viewModel, provider, Dispatchers.Unconfined)

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
        every { provider.movementRepository } returns repo
    }

    /**
     * OnStart: Get the appropriate Movement from the backend based on the ID passed in for OnStart. If successful, the
     * next step is to call every function on the Fragment with the appropriate data, à la Martin Fowler's Passive View.
     */
    @Test
    fun `On Start valid arguments`(){
        val MOVEMENT = getMovement()

        coEvery { repo.getMovementById(MOVEMENT.name) } returns ResultWrapper.build { MOVEMENT }

        logic.handleEvent(MovementEvent.OnStart(MOVEMENT.name))

        coVerify( exactly = 1 ) { repo.getMovementById(MOVEMENT.name) }
        coVerify( exactly = 1 ) { viewModel.setMovement(MOVEMENT) }
        verify( exactly = 1) { view.setTootlbarTitle(MOVEMENT.name) }
        verify( exactly = 1) { view.setParallaxImage(MOVEMENT.imageResourceNames) }
        verify( exactly = 1) { view.setTargets(MOVEMENT.targets) }
        verify( exactly = 1) { view.setFrequency(MOVEMENT.frequency)}
        verify( exactly = 1) { view.setIsTimed(MOVEMENT.isTimeBased)}
        verify( exactly = 1) { view.setTimeOrRepetitions(MOVEMENT.timeOrRepetitions)}
        verify( exactly = 1) { view.setDifficulty(MOVEMENT.difficulty)}
        verify( exactly = 1) { view.setDescription(MOVEMENT.descriptionResourceName)}
        verify( exactly = 1) { view.setInstructions(MOVEMENT.instructionsResourceName)}

    }


    @Test
    fun `On Start invalid arguments`(){
        logic.handleEvent(MovementEvent.OnStart(""))

        verify { view.showMessage(ERROR_GENERIC) }
        verify { view.startMovementListView() }
    }

    @Test
    fun `On Show Video Click`(){

    }

}
