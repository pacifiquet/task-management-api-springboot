package com.taskhero.config.archtest;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;

@AnalyzeClasses(
    packages = "com.pacifique.todoapp",
    importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureRulesDateTimeTest {
  //  @ArchTest
  //  public static final ArchRule RESTRICT_TIME_MOCKING =
  //      noClasses()
  //          .should()
  //          .callMethod(Time.class, "useMockTime", LocalDateTime.class, ZoneId.class)
  //          .because(
  //              "Method Time.useMockTime designed only for test purpose and can be used only in
  // the tests");
  //
  //  @ArchTest
  //  public static final ArchRule RESTRICT_USAGE_OF_LOCAL_DATE_TIME_NOW =
  //      noClasses()
  //          .should()
  //          .callMethod(LocalDateTime.class, "now")
  //          .because(
  //              "Use Time.currentDateTime methods instead of as it gives opportunity of mocking
  // time in tests");
  //
  //  @ArchTest
  //  public static final ArchRule RESTRICT_USAGE_OF_LOCAL_TIME_NOW =
  //      noClasses()
  //          .should()
  //          .callMethod(LocalTime.class, "now")
  //          .because(
  //              "Use Time.currentTime methods instead of as it gives opportunity of mocking time
  // in tests");
  //

  //
  //  @ArchTest
  //  public static final ArchRule RESTRICT_USAGE_OF_ZONED_DATE_TIME_NOW =
  //      noClasses()
  //          .should()
  //          .callMethod(ZonedDateTime.class, "now")
  //          .because(
  //              "Use Time.currentZonedDateTime methods instead of as it gives opportunity of
  // mocking time in tests");
  //
  //  @ArchTest
  //  public static final ArchRule RESTRICT_USAGE_OF_INSTANCE_NOW =
  //      noClasses()
  //          .should()
  //          .callMethod(Instant.class, "now")
  //          .because(
  //              "Use Time.currentInstant methods instead of as it gives opportunity of mocking
  // time in tests");
  //  @ArchTest
  //  public static final ArchRule RESTRICT_USAGE_OF_OLD_DATE_API =
  //      classes()
  //          .that()
  //          .areNotAssignableTo(OldTimeApiAdaptor.class)
  //          .should()
  //          .onlyAccessClassesThat()
  //          .areNotAssignableTo(Date.class)
  //          .because(
  //              "java.util.Date is class from the old Date API. "
  //                  + "Please use new Date API from the package java.time.* "
  //                  + "In case when you need current date/time use wrapper class
  // com.pacifique.todoapp.utils.Time");
}
