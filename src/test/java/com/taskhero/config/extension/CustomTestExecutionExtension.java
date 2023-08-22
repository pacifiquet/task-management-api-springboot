package com.taskhero.config.extension;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class CustomTestExecutionExtension implements AfterTestExecutionCallback {

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    String testName = context.getDisplayName();
    String result = context.getExecutionException().isPresent() ? "FAIL" : "PASS";
    String textColor = context.getExecutionException().isPresent() ? "\u001B[31m" : "\u001B[32m";

    System.out.println(
        "Test: " + testName + " - Result: " + textColor + " " + result + "\u001B[0m");
  }
}
