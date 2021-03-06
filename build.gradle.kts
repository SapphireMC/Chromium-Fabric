plugins {
	java
	`maven-publish`
	alias(libs.plugins.loom)
	alias(libs.plugins.licenser)
}

val archivesBaseName = project.properties["archivesBaseName"].toString() + libs.versions.minecraft.get()
val sodiumCompatibility = project.properties["sodiumCompatibility"].toString().toBoolean()
val irisCompatibility = project.properties["irisCompatibility"].toString().toBoolean()

repositories {
	mavenCentral()
	maven("https://maven.shedaniel.me/")
	maven("https://maven.terraformersmc.com/releases")
	maven("https://api.modrinth.com/maven") {
		content {
			includeGroup("maven.modrinth")
		}
	}
}

dependencies {
	minecraft(libs.minecraft)
	mappings(libs.fabric.mappings) { artifact { classifier = "v2" } }
	modImplementation(libs.fabric.loader)
	listOf(
		"fabric-key-binding-api-v1",
		"fabric-lifecycle-events-v1",
		"fabric-networking-api-v1"
	).forEach {
		modImplementation(fabricApi.module(it, libs.versions.fabric.get()))?.let { it1 -> include(it1) }
	}

	if (sodiumCompatibility) {
		modImplementation(libs.mod.sodium)
		implementation("org.joml:joml:1.10.4")
	}
	if (irisCompatibility) {
		modImplementation(libs.mod.iris)
		implementation("org.anarres:jcpp:1.4.14")
	}
	modImplementation("com.terraformersmc:modmenu:4.0.0")
	include(modImplementation("me.shedaniel.cloth:cloth-config-fabric:7.0.72") {
		exclude(group = "net.fabricmc.fabric-api")
	})

	modRuntimeOnly(libs.fabric.api)

	compileOnly("org.projectlombok:lombok:1.18.24")
	annotationProcessor("org.projectlombok:lombok:1.18.24")
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
}

license {
	include("**/io/sapphiremc/chromium/**")

	header(project.file("HEADER"))
	newLine(false)
}

sourceSets {
	if (sodiumCompatibility) {
		create("sodiumCompatibility") {
			java {
				compileClasspath += main.get().compileClasspath
				compileClasspath += main.get().output
			}
		}
	}
	if (irisCompatibility) {
		create("irisCompatibility") {
			java {
				compileClasspath += main.get().compileClasspath
				compileClasspath += main.get().output
			}
		}
	}

	main {
		java {
			if (sodiumCompatibility) {
				runtimeClasspath += getByName("sodiumCompatibility").output
			}
			if (irisCompatibility) {
				runtimeClasspath += getByName("irisCompatibility").output
			}
		}
	}
}

tasks {
	withType<JavaCompile> {
		options.encoding = Charsets.UTF_8.name()
		options.release.set(17)
	}

	withType<Javadoc> {
		options.encoding = Charsets.UTF_8.name()
	}

	withType<ProcessResources> {
		filteringCharset = Charsets.UTF_8.name()
	}

	withType<Jar> {
		archiveBaseName.set(archivesBaseName)
	}

	remapJar {
		archiveBaseName.set(archivesBaseName)
	}

	processResources {
		inputs.property("version", project.version)

		filesMatching("fabric.mod.json") {
			expand("version" to project.version)

			if (!sodiumCompatibility) {
				filter {
					it.replace("mixins.chromium.compat.sodium.json", "mixins.empty.sodium.json")
				}
			}
			if (!irisCompatibility) {
				filter {
					it.replace("mixins.chromium.compat.iris.json", "mixins.empty.iris.json")
				}
			}
		}

		if (sodiumCompatibility) {
			exclude("mixins.empty.sodium.json")
		}
		if (irisCompatibility) {
			exclude("mixins.empty.iris.json")
		}
	}

	jar {
		from("LICENSE")

		if (sodiumCompatibility) {
			from(sourceSets["sodiumCompatibility"].output) {
				filesMatching("*refmap.json") {
					name = "chromium-sodium-compat-refmap.json"
				}
			}
		}
		if (irisCompatibility) {
			from(sourceSets["irisCompatibility"].output) {
				filesMatching("*refmap.json") {
					name = "chromium-iris-compat-refmap.json"
				}
			}
		}
	}

	runClient {
		jvmArgs?.add("-Dmixin.debug.export=true")
	}
}