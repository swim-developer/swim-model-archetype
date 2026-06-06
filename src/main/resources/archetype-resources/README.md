#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
${symbol_pound} ${artifactId}

JAXB bindings for ${dataStandard} schemas used by the ${modelDisplayName} service.

${symbol_pound}${symbol_pound} What This Project Provides

- Java classes generated from ${dataStandard} XSD schemas via JAXB (XJC)
- Thread-safe unmarshaller pool with XSD validation (${modelPrefix}UnmarshallerPool)
- Standalone XSD validator (${modelPrefix}XsdValidator)
- Secure XML parsing (XXE prevention)

${symbol_pound}${symbol_pound} Prerequisites

- JDK 21
- Maven 3.9+
- `swim-developer` parent POM installed in local Maven repository

${symbol_pound}${symbol_pound} Install

```bash
./mvnw clean install -DskipTests
```

${symbol_pound}${symbol_pound} Regenerate JAXB Classes

After modifying XSD schemas or binding files:

```bash
./mvnw process-sources -Pgenerate-xjc
```

This deletes old generated packages, runs XJC against `${rootSchema}`, and copies the generated classes into `src/main/java/`.

Hand-written validation classes in the `validation` package are preserved.

${symbol_pound}${symbol_pound} Setup (after archetype generation)

1. Copy your XSD schemas into `src/main/resources/schemas/`
2. Update `src/main/resources/bindings/${modelName}.xjb` with namespace-to-package mappings
3. Update the `clean-generated-sources` section in `pom.xml` with the package directories to clean
4. Update `${modelPrefix}UnmarshallerPool` constructor with the generated ObjectFactory classes
5. Run `./mvnw process-sources -Pgenerate-xjc` to generate classes
6. Add test XML samples to `src/test/resources/`

${symbol_pound}${symbol_pound} Technology

| Component | Version |
|-----------|---------|
| Java | 21 |
| Jakarta XML Binding API | 4.0.5 |
| GlassFish JAXB Runtime | 4.0.7 |
| JAXB Maven Plugin | 4.0.12 |

${symbol_pound}${symbol_pound} License

Licensed under the [Apache License 2.0](LICENSE).
