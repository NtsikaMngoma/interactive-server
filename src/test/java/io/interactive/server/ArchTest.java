package io.interactive.server;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("io.interactive.server");

        noClasses()
            .that()
                .resideInAnyPackage("io.interactive.server.service..")
            .or()
                .resideInAnyPackage("io.interactive.server.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..io.interactive.server.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
