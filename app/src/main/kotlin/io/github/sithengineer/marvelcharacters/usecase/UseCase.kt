package io.github.sithengineer.marvelcharacters.usecase

import io.github.sithengineer.marvelcharacters.usecase.UseCase.RequestValues
import io.github.sithengineer.marvelcharacters.usecase.UseCase.ResponseValue

interface UseCase<in Q : RequestValues, out R : ResponseValue> {

  fun execute(request: Q): R

  /**
   * Data passed to a request.
   */
  interface RequestValues

  /**
   * Data received from a request.
   */
  interface ResponseValue
}