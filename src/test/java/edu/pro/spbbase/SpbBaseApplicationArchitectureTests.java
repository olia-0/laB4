package edu.pro.spbbase;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import edu.pro.spbbase.model.Item;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.RequestPredicates;
import org.w3c.dom.Entity;

import java.lang.reflect.Modifier;

import static com.tngtech.archunit.core.domain.JavaModifier.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static org.springframework.data.mongodb.core.aggregation.Fields.fields;
import static org.springframework.web.servlet.function.RequestPredicates.methods;

@SpringBootTest
class SpbBaseApplicationArchitectureTests {
    private JavaClasses applicationClasses;
    @BeforeEach
    void init(){
        applicationClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_ARCHIVES)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
                .importPackages("edu.pro.spbbase");
    }


    @Test
    void shouldFollowLayerArchitecture() {
        layeredArchitecture()
                .layer("Controller").definedBy("..controller..")
                .layer("Service").definedBy("..service..")
                .layer("Repository").definedBy("..repository..")
                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller","Service")
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
                .check(applicationClasses);

    }
    @Test
    void shouldNotHaveCyclicDependencies() {
        ArchRule rule = slices().matching("..edu.pro.spbbase.(*)..")
                .should().beFreeOfCycles();
        rule.check(applicationClasses);
    }


    ///CONTROLLER                            ///////////////////////////////////////////////////
    @Test
    void controllerClassesShouldBeAQnnotatedByController(){
        classes().that().resideInAPackage("..controller..")
                .should()
                .beAnnotatedWith(RestController.class)
                .andShould()
                .beAnnotatedWith(RequestMapping.class)
                .check(applicationClasses);
    }
    @Test
    void controllerClassesShouldBeNamedController(){
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .haveSimpleNameEndingWith("Controller")
                .check(applicationClasses);
    }
    @Test
    void controllerClassesShouldNotBeInterfaces() {
        classes().that().resideInAPackage("..controller..")
                .should()
                .notBeInterfaces()
                .check(applicationClasses);
    }
    @Test
    void controllerClassesShouldNotHaveRepositoryInjection() {
        classes().that().resideInAPackage("..controller..")
                .should().notHaveSimpleName("Repository")
                .check(applicationClasses);
    }
    @Test
    void controllerClassesShouldHavePrivateFields() {
        ArchRuleDefinition.fields().that().areDeclaredInClassesThat().resideInAPackage("..controller..")
                .should().bePrivate()
                .check(applicationClasses);
    }
    @Test
    void controllerClassesShouldNotHavePublicFields() {
        ArchRuleDefinition.fields().that().areDeclaredInClassesThat().resideInAPackage("..controller..")
                .should().notBePublic()
                .check(applicationClasses);
    }

    //
    @Test
    void controllerClassesShouldNotHaveProtectedFields() {
        ArchRuleDefinition.fields().that().areDeclaredInClassesThat().resideInAPackage("..controller..")
                .should().notBeProtected()
                .check(applicationClasses);
    }
    @Test
    void controllerClassesShouldNotHavePrivateMethod() {
        ArchRuleDefinition.methods().that().areDeclaredInClassesThat().resideInAPackage("..controller..")
                .should().notBePrivate()
                .check(applicationClasses);
    }
    @Test
    void controllerClassesShouldHavePublicMethod() {
        ArchRuleDefinition.methods().that().areDeclaredInClassesThat().resideInAPackage("..controller..")
                .should().bePublic()
                .check(applicationClasses);
    }
    @Test
    void controllerClassesShouldNotHaveProtectedMethod() {
        ArchRuleDefinition.methods().that().areDeclaredInClassesThat().resideInAPackage("..controller..")
                .should().notBeProtected()
                .check(applicationClasses);
    }
    @Test
    void controllerShouldNotBePrivate() {
        classes().that().resideInAPackage("..controller..")
                .should().notBePrivate()
                .check(applicationClasses);
    }
    @Test
    void controllerShouldNotBeProtected() {
        classes().that().resideInAPackage("..controller..")
                .should().notBeProtected()
                .check(applicationClasses);
    }
    @Test
    void controllerShouldBePublic() {
        classes().that().resideInAPackage("..controller..")
                .should().bePublic()
                .check(applicationClasses);
    }
    ///MODEL
    @Test
    void modelClassesShouldBeAnnotatedWithEntity() {
        classes().that().resideInAPackage("..model..")
                .should()
                .beAnnotatedWith(Document.class)
                .check(applicationClasses);
    }
    @Test
    void modelClassesShouldNotBeInterfaces() {
        classes().that().resideInAPackage("..model..")
                .should()
                .notBeInterfaces()
                .check(applicationClasses);
    }

    @Test
    void modelClassesShouldHavePrivateFields() {
        ArchRuleDefinition.fields().that().areDeclaredInClassesThat().resideInAPackage("..model..")
                .should().bePrivate()
                .check(applicationClasses);
    }
    @Test
    void modelClassesShouldNotHavePublicFields() {
        ArchRuleDefinition.fields().that().areDeclaredInClassesThat().resideInAPackage("..model..")
                .should().notBePublic()
                .check(applicationClasses);
    }

    //
    @Test
    void modelClassesShouldNotHaveProtectedFields() {
        ArchRuleDefinition.fields().that().areDeclaredInClassesThat().resideInAPackage("..model..")
                .should().notBeProtected()
                .check(applicationClasses);
    }
    @Test
    void modelClassesShouldNotHavePrivateMethod() {
        ArchRuleDefinition.methods().that().areDeclaredInClassesThat().resideInAPackage("..model..")
                .should().notBePrivate()
                .check(applicationClasses);
    }
    @Test
    void modelClassesShouldHavePublicMethod() {
        ArchRuleDefinition.methods().that().areDeclaredInClassesThat().resideInAPackage("..model..")
                .should().bePublic()
                .check(applicationClasses);
    }
    @Test
    void modelClassesShouldNotHaveProtectedMethod() {
        ArchRuleDefinition.methods().that().areDeclaredInClassesThat().resideInAPackage("..model..")
                .should().notBeProtected()
                .check(applicationClasses);
    }
    ///
    @Test
    void modelShouldNotBePrivate() {
        classes().that().resideInAPackage("..model..")
                .should().notBePrivate()
                .check(applicationClasses);
    }
    @Test
    void modelShouldNotBeProtected() {
        classes().that().resideInAPackage("..model..")
                .should().notBeProtected()
                .check(applicationClasses);
    }
    @Test
    void modelShouldBePublic() {
        classes().that().resideInAPackage("..model..")
                .should().bePublic()
                .check(applicationClasses);
    }
    ///SERVICE
    @Test
    void serviceClassesShouldNotBeInterfaces() {
        classes().that().resideInAPackage("..service..")
                .should()
                .notBeInterfaces()
                .check(applicationClasses);
    }
    @Test
    void serviceClassesShouldBeNamedService() {
        classes()
                .that().resideInAPackage("..service..")
                .should()
                .haveSimpleNameEndingWith("Service")
                .check(applicationClasses);
    }

    @Test
    void serviceClassesShouldBeAnnotatedWithService() {
        classes().that().resideInAPackage("..service..")
                .should()
                .beAnnotatedWith(Service.class)
                .check(applicationClasses);
    }
    @Test
    void serviceShouldNotDependOnControllerLevel(){
        noClasses().that().resideInAPackage("..service..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..controller..")
                //.because(" out of rules")
                .check(applicationClasses);
    }
    @Test
    void serviceClassesShouldHavePrivateFields() {
        ArchRuleDefinition.fields().that().areDeclaredInClassesThat().resideInAPackage("..service..")
                .should().bePrivate()
                .check(applicationClasses);
    }
    @Test
    void serviceClassesShouldNotHavePublicFields() {
        ArchRuleDefinition.fields().that().areDeclaredInClassesThat().resideInAPackage("..service..")
                .should().notBePublic()
                .check(applicationClasses);
    }

    //
    @Test
    void serviceClassesShouldNotHaveProtectedFields() {
        ArchRuleDefinition.fields().that().areDeclaredInClassesThat().resideInAPackage("..service..")
                .should().notBeProtected()
                .check(applicationClasses);
    }
    @Test
    void serviceClassesShouldNotHavePrivateMethod() {
        ArchRuleDefinition.methods().that().areDeclaredInClassesThat().resideInAPackage("..service..")
                .should().notBePrivate()
                .check(applicationClasses);
    }
    @Test
    void serviceClassesShouldNotHaveProtectedMethod() {
        ArchRuleDefinition.methods().that().areDeclaredInClassesThat().resideInAPackage("..service..")
                .should().notBeProtected()
                .check(applicationClasses);
    }

    ///
    @Test
    void serviceShouldNotBePrivate() {
        classes().that().resideInAPackage("..service..")
                .should().notBePrivate()
                .check(applicationClasses);
    }
    @Test
    void serviceShouldNotBeProtected() {
        classes().that().resideInAPackage("..service..")
                .should().notBeProtected()
                .check(applicationClasses);
    }
    @Test
    void serviceShouldBePublic() {
        classes().that().resideInAPackage("..service..")
                .should().bePublic()
                .check(applicationClasses);
    }
    ///REPOSITORY
    @Test
    void repositoryClassesShouldBeNamedRepository() {
        classes()
                .that().resideInAPackage("..repository..")
                .should()
                .haveSimpleNameEndingWith("Repository")
                .check(applicationClasses);
    }

    @Test
    void repositoryClassesShouldBeAnnotatedWithRepository() {
        classes().that().resideInAPackage("..repository..")
                .should()
                .beAnnotatedWith(Repository.class)
                .check(applicationClasses);
    }
    ///
    @Test
    void repositoryClassesShouldExtendMongoRepository() {
        classes()
                .that().resideInAPackage("..repository..")
                .should().beAssignableTo(MongoRepository.class)
                .check(applicationClasses);
    }
    @Test
    void repositoryClassesShouldBeInterfaces() {
        classes().that().resideInAPackage("..repository..")
                .should()
                .beInterfaces()
                .check(applicationClasses);
    }
    ///
    @Test
    void repositoryShouldNotBePrivate() {
        classes().that().resideInAPackage("..repository..")
                .should().notBePrivate()
                .check(applicationClasses);
    }
    @Test
    void repositoryShouldNotBeProtected() {
        classes().that().resideInAPackage("..repository..")
                .should().notBeProtected()
                .check(applicationClasses);
    }
    @Test
    void repositoryShouldBePublic() {
        classes().that().resideInAPackage("..repository..")
                .should().bePublic()
                .check(applicationClasses);
    }








}
