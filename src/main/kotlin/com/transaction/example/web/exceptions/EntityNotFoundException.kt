package com.transaction.example.web.exceptions

class EntityNotFoundException(
  override val message: String? = "Entity not found",
  override val cause: Throwable? = null,
) : RuntimeException(
  message,
  cause,
)
