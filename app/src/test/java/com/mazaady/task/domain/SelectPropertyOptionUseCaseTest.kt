package com.mazaady.task.domain

import com.mazaady.task.data.RemoteCategoryDataSource
import com.mazaady.task.model.Option
import com.mazaady.task.model.Property
import com.mazaady.task.model.UCResult
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test


class SelectPropertyOptionUseCaseTest {

    val dataSource = mockk<RemoteCategoryDataSource>()
    val useCase = SelectPropertyOptionUseCase(dataSource)

    val fakeProp = Property("1", "name", null, listOf())
    val fakeProp2 = Property("2", "name", null, listOf())
    val fakeOption = Option("2", "na", false)

    @Test
    fun assert_option_is_selected() = runTest {
        val result = useCase.invoke(listOf(fakeProp), fakeProp, fakeOption)
        when (result) {
            is UCResult.Success -> {
                val selectedId = result.data.first().selectedOption?.id
                assert(selectedId == fakeOption.id)
            }

            is UCResult.Error -> {
                assert(false) { "shouldn't return Error" }
            }
        }
    }

    @Test
    fun assert_option_selected_to_correct_property() = runTest {
        val result = useCase.invoke(listOf(fakeProp, fakeProp2), fakeProp, fakeOption)
        when (result) {
            is UCResult.Success -> {
                val prop = result.data.find { it.id == fakeProp.id }
                val correctProp = prop?.selectedOption?.id == fakeOption.id
                assert(correctProp)
            }

            is UCResult.Error -> {
                assert(false) { "shouldn't return Error" }
            }
        }
    }

    @Test
    fun assert_do_nothing_if_property_not_in_original_data() = runTest {
        val result = useCase.invoke(listOf(), fakeProp, fakeOption)
        when (result) {
            is UCResult.Success -> {
                assert(result.data.isEmpty())
            }

            is UCResult.Error -> {
                assert(false) { "shouldn't return Error" }
            }
        }
    }


}