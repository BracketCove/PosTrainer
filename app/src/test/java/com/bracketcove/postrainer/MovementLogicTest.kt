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
    val viewModel: MovementContract.ViewModel = mockk()
    val provider: AndroidMovementProvider = mockk()

    val logic = MovementLogic(view, viewModel, provider, Dispatchers.Unconfined)

    val IMAGE_RESOURCE_ONE = "im_passive_one"
    val IMAGE_RESOURCE_TWO = "im_passive_two"
    val IMAGE_RESOURCE_THREE = "im_passive_three"

    private fun getMovement() = Movement(
        "Passive Hang",
        listOf("Chest, Shoulders, Arms, Back"),
        "2 Sets/Day",
        "30-60 Seconds",
        true,
        listOf(IMAGE_RESOURCE_ONE, IMAGE_RESOURCE_TWO, IMAGE_RESOURCE_THREE),
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
    fun `On Start valid arguments`() {
        val MOVEMENT = getMovement()

        coEvery { repo.getMovementById(MOVEMENT.name) } returns ResultWrapper.build { MOVEMENT }

        logic.handleEvent(MovementEvent.OnStart(MOVEMENT.name))

        coVerify(exactly = 1) { repo.getMovementById(MOVEMENT.name) }
        coVerify(exactly = 1) { viewModel.setMovement(MOVEMENT) }
        verify(exactly = 1) { view.setTootlbarTitle(MOVEMENT.name) }
        verify(exactly = 1) { view.setParallaxImage(MOVEMENT.imageResourceNames[0]) }
        verify(exactly = 1) { view.setTargets(MOVEMENT.targets) }
        verify(exactly = 1) { view.setFrequency(MOVEMENT.frequency) }
        verify(exactly = 1) { view.setIsTimed(MOVEMENT.isTimeBased) }
        verify(exactly = 1) { view.setTimeOrRepetitions(MOVEMENT.timeOrRepetitions) }
        verify(exactly = 1) { view.setDifficulty(MOVEMENT.difficulty) }
        verify(exactly = 1) { view.setDescription(MOVEMENT.description) }
        verify(exactly = 1) { view.setInstructions(MOVEMENT.instructions) }
        verify(exactly = 1) { view.hideProgressBar() }

    }


    @Test
    fun `On Start invalid arguments`() {
        logic.handleEvent(MovementEvent.OnStart(""))

        verify { view.showMessage(ERROR_GENERIC) }
        verify { view.startMovementListView() }
    }

    /**
     * When the user clicks on the parallax image, we want to cycle to the next image.
     * ViewModel Tracks it's own index
     *
     * 1. Request list of imageResources from ViewModel
     * 2. Request current index from ViewModel
     * 3a. If list has next available image, get that (index +1)
     * 4a. Give to the view
     */
    @Test
    fun `on Parallax Image Click has next`() {
        val MOVEMENT = getMovement()
        val INDEX = 0

        every { viewModel.getCurrentIndex() } returns INDEX
        every { viewModel.getMovement() } returns MOVEMENT
        every { viewModel.getImageResource(INDEX + 1) } returns MOVEMENT.imageResourceNames[1]

        logic.handleEvent(MovementEvent.OnImageClick)

        verify(exactly = 1) { viewModel.getCurrentIndex() }
        verify(exactly = 1) { viewModel.getMovement() }
        verify(exactly = 1) { viewModel.getImageResource(INDEX + 1) }
        verify(exactly = 1) { view.setParallaxImage(IMAGE_RESOURCE_TWO) }
    }

    /**
     * 3b. If index is last, get first (index 0)
     * 4b. Give to the View
     */
    @Test
    fun `on Parallax Image Click last item`() {
        val MOVEMENT = getMovement()
        val INDEX = 2

        every { viewModel.getCurrentIndex() } returns INDEX
        every { viewModel.getMovement() } returns MOVEMENT
        every { viewModel.getImageResource(0) } returns MOVEMENT.imageResourceNames[0]

        logic.handleEvent(MovementEvent.OnImageClick)

        verify(exactly = 1) { viewModel.getCurrentIndex() }
        verify(exactly = 1) { viewModel.getMovement() }
        verify(exactly = 1) { viewModel.getImageResource(0) }
        verify(exactly = 1) { view.setParallaxImage(IMAGE_RESOURCE_ONE) }
    }

    @Test
    fun `On Show Video Click`() {

    }

}
