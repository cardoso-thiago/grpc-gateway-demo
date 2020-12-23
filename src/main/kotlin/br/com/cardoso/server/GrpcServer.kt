package br.com.cardoso.server

import br.com.cardoso.GreeterGrpcKt
import br.com.cardoso.GreeterOuterClass
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class GrpcServer : GreeterGrpcKt.GreeterCoroutineImplBase() {

    override suspend fun sayHello(request: GreeterOuterClass.HelloRequest): GreeterOuterClass.HelloReply =
            GreeterOuterClass.HelloReply.newBuilder().setMessage("Hello ==> " + request.name).build()

    override suspend fun sayHelloPost(request: GreeterOuterClass.CreateHelloPostRequest): GreeterOuterClass.HelloReply =
            GreeterOuterClass.HelloReply.newBuilder().setMessage("Hello ==> " + request.hello.name).build()
}