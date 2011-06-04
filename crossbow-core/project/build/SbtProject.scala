import sbt._
import java.io.File

class SbtProject(info: ProjectInfo) extends DefaultProject(info) {
  // Use locally installed Scala
  override def localScala = defineScala("2.9.0.1-local", new File("/usr/share/scala")) :: Nil
  // Use locally installed ScalaTest
  override def testClasspath =
    super.testClasspath +++ (Path.fromFile("/usr/local/lib/scalatest.jar"))

  // Custom paths
  override def mainScalaSourcePath = "src"
  override def mainResourcesPath = "src-resources"
  override def testScalaSourcePath = "test"
  override def testResourcesPath = "test-resources"
  override def outputDirectoryName = "bin"
  override def defaultJarBaseName = normalizedName+"-"+version.toString

  // Add -unchecked compiler option
  override def compileOptions = super.compileOptions ++ Seq(Unchecked)

  // Run the specified class
  lazy val runOnly = task { args =>
    if(args.length == 1)
      actionConstructor(args(0))
    else
      task { Some("Usage: run-only <com.example.ClassToRun>") }
  }
  def actionConstructor(classToRun: String) = {
    runTask(Some(classToRun), testClasspath).dependsOn(testCompile)
  }
}
