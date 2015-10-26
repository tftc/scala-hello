package com.itiancai.finatra.controller

import com.itiancai.finatra.domain.http.UserRegisterRequest
import com.twitter.finagle.httpx.Request
import com.twitter.finatra.http.Controller


class ExampleController extends Controller{

  get("/users/hi") { request: UserRegisterRequest =>
    info("hi")
    response.ok.header("a", "b").json(request)

  }

  post("/users/hi"){ request:Request =>



  }




}
