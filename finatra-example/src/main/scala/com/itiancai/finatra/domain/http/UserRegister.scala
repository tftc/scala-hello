package com.itiancai.finatra.domain.http

import com.twitter.finatra.request.QueryParam
import com.twitter.finatra.validation.{PastTime, Size}
import org.joda.time.DateTime


case class UserRegisterRequest(
  @Size(min = 1, max = 10) @QueryParam message: String,
  @PastTime @QueryParam startTime:  Option[DateTime]
)