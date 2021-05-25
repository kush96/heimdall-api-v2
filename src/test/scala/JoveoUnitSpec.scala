//import akka.actor.ActorSystem
//import org.scalatest.color
//
//abstract class JoveoUnitSpec extends FlatSpec with Matchers with OptionValues with Inside with Inspectors with
//  BeforeAndAfterAll with BeforeAndAfter with Retries {
//
//  if(JoveoConfig.getCurrentEnvironment == JoveoEnvironment.Prod) throw new Exception(s"You are using PROD environment for running specs.")
//
//  implicit val actorSystem: ActorSystem = ActorSystem("TestSystem")
//
//  override def beforeAll() = {
//    if(JoveoConfig.getCurrentEnvironment == JoveoEnvironment.Prod) throw new Exception(s"You are using PROD environment for running specs.")
//    else if (JoveoConfig.getCurrentEnvironment == JoveoEnvironment.Staging) throw new Exception(s"You are using STAGING environment for running specs.")
//
//    super.beforeAll()
//  }
//
//  override def withFixture(test: NoArgTest) = {
//    if (isRetryable(test))
//      withRetry { super.withFixture(test) }
//    else
//      super.withFixture(test)
//  }
//
//  def before(fun : => scala.Any) : scala.Unit = {
//    if(JoveoConfig.getCurrentEnvironment == JoveoEnvironment.Prod) throw new Exception(s"You are using PROD environment for running specs.")
//    super.before(fun)
//  }
//
//}