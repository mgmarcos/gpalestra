package teste;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class test_runner {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(teste_Localidade.class, teste_palestra.class, teste_CaixaPreta.class);
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
      System.out.println(result.wasSuccessful());
   }
}