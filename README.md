# SWIM Model Archetype

Maven archetype that generates a complete SWIM **data model** project with JAXB bindings, XSD validation, and thread-safe unmarshalling. The generated project follows the same structure as `swim-aixm-model` and `swim-fixm-model-ed254`.

## Prerequisites

- JDK 21
- Maven 3.9+
- The `swim-developer` parent POM installed in the local Maven repository

## Install the Archetype

```bash
cd swim-model-archetype
mvn clean install
```

## Generate a New Model

```bash
mvn archetype:generate \
  -DarchetypeGroupId=com.github.swim-developer \
  -DarchetypeArtifactId=swim-model-archetype \
  -DarchetypeVersion=1.0.0-SNAPSHOT \
  -DgroupId=com.github.swim-developer \
  -DartifactId=swim-fixm-ffice-model \
  -Dversion=1.0.0-SNAPSHOT \
  -Dpackage=aero.fixm.ffice \
  -DmodelName=ffice \
  -DmodelDisplayName="FF-ICE" \
  -DmodelPrefix=Ffice \
  -DrootSchema=FficeMessage.xsd \
  -DdataStandard=FIXM \
  -DinteractiveMode=false
```

## Parameters

| Parameter | Description | Example |
|---|---|---|
| `modelName` | Lowercase identifier used in binding filenames and test resource directories | `ffice` |
| `modelDisplayName` | Human-readable name for README and project description | `FF-ICE` |
| `modelPrefix` | PascalCase prefix for generated class names (UnmarshallerPool, XsdValidator) | `Ffice` |
| `rootSchema` | Entry point XSD filename for JAXB code generation | `FficeMessage.xsd` |
| `dataStandard` | Data exchange standard family (used in project name and description) | `FIXM` |

Standard Maven parameters (`groupId`, `artifactId`, `version`, `package`) are also supported.

## Post-Generation

After generating the project, make the Maven wrapper executable:

```bash
cd swim-fixm-ffice-model
chmod +x mvnw
```

## Sync and Install

Run once before building to pull the project and install its only dependency (`swim-developer-root`) into the local Maven repository:

```bash
make sync
```

Or run each step individually:

```bash
make pull          # git pull --ff-only on this project
make pull-deps     # clone or pull swim-developer-root from GitHub
make install-deps  # install swim-developer-root into local Maven repo
```

## Verify

Compile the generated project to confirm everything is wired correctly:

```bash
./mvnw compile
```

## What Gets Generated

The archetype produces **3 Java classes** + configuration files, organized for JAXB code generation:

### Validation Classes (hand-written, preserved during regeneration)

| Class | Purpose |
|---|---|
| `{Prefix}UnmarshallerPool` | Thread-safe unmarshaller pool with XSD validation and XXE prevention |
| `{Prefix}XsdValidator` | Lightweight wrapper for standalone XSD validation |

### Test Classes

| Class | Purpose |
|---|---|
| `{Prefix}UnmarshallerPoolTest` | Unit tests for null/empty/invalid XML handling |

### Configuration Files

| File | Purpose |
|---|---|
| `pom.xml` | Parent POM, JAXB dependencies, `generate-xjc` profile |
| `src/main/resources/bindings/{modelName}.xjb` | JAXB namespace-to-package mappings (TODO) |
| `src/main/resources/schemas/` | Empty directory for XSD schemas |
| `Makefile` | Build targets: `sync`, `pull`, `pull-deps`, `install-deps`, `install`, `test`, `sonar`, `security-deps` |
| `README.md` | Project documentation |

## Setup After Generation

1. Copy your XSD schemas into `src/main/resources/schemas/`
2. Update `src/main/resources/bindings/{modelName}.xjb` with namespace-to-package mappings
3. Update the `clean-generated-sources` section in `pom.xml` with the generated package directories
4. Update `{Prefix}UnmarshallerPool` constructor with the generated `ObjectFactory` classes
5. Run `./mvnw process-sources -Pgenerate-xjc` to generate JAXB classes
6. Add test XML samples to `src/test/resources/`

Use the existing **swim-fixm-model-ed254** and **swim-aixm-model** as reference implementations. Their binding files, schema organization, and validation classes demonstrate the patterns expected by each TODO.

## Technology Stack

- **Java**: 21
- **Jakarta XML Binding API**: 4.0.5
- **GlassFish JAXB Runtime**: 4.0.7
- **JAXB Maven Plugin**: 4.0.12
