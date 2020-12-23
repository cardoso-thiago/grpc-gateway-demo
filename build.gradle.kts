import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
	id("org.springframework.boot") version "2.4.1"
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
	kotlin("jvm") version "1.4.21"
	kotlin("plugin.spring") version "1.4.21"
	id("com.google.protobuf") version "0.8.13"
}

val grpcVersion by extra("1.32.1")
val grpcKotlinVersion by extra("1.0.0")
val protobufVersion by extra("3.13.0")

group = "br.com.cardoso"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenLocal()
	mavenCentral()
	jcenter()
	google()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("net.devh:grpc-server-spring-boot-starter:2.10.1.RELEASE")
	implementation(kotlin("stdlib"))
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8")
	implementation("javax.annotation:javax.annotation-api:1.3.2")

	api("com.google.protobuf:protobuf-java-util:${rootProject.ext["protobufVersion"]}")
	api("io.grpc:grpc-kotlin-stub:${rootProject.ext["grpcKotlinVersion"]}")
}

protobuf {
	protoc {
		artifact = "com.google.protobuf:protoc:${rootProject.ext["protobufVersion"]}"
	}
	plugins {
		id("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java:${rootProject.ext["grpcVersion"]}"
		}
		id("grpckt") {
			artifact = "io.grpc:protoc-gen-grpc-kotlin:${rootProject.ext["grpcKotlinVersion"]}:jdk7@jar"
		}
	}
	generateProtoTasks {
		all().forEach {
			it.plugins {
				id("grpc")
				id("grpckt")
			}
		}
	}
}

sourceSets {
	getByName("main").java.srcDirs(
			"src/main/kotlin",
			"build/generated/source/proto/main/java",
			"build/generated/source/proto/main/grpc",
			"build/generated/source/proto/main/grpckt")
	getByName("main").proto.srcDirs(
			"src/main/kotlin",
			"build/generated/source/proto/main/java",
			"build/generated/source/proto/main/grpc",
			"build/generated/source/proto/main/grpckt")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
