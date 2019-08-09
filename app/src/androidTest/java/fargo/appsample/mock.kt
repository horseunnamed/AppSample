package fargo.appsample

import org.mockito.Mockito.mock

inline fun <reified T> mocked(): T =
    mock(T::class.java)