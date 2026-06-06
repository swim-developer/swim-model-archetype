# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Maven archetype that generates a complete SWIM data model project with JAXB bindings, XSD validation, and thread-safe unmarshalling. Generated projects follow the same structure as `swim-aixm-model` and `swim-fixm-model-ed254`.

This is **not** a runnable application — it is an archetype (project template). The source under `src/main/resources/archetype-resources/` contains Velocity templates (`${modelPrefix}`, `${package}`, etc.) that produce real Java files when a user runs `mvn archetype:generate`.

## Build Commands

```bash
# Install the archetype into local Maven repo
mvn clean install

# Generate a new model project from the archetype
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

There are no tests to run in this archetype project itself. Tests exist only inside the generated projects.

## Prerequisites

- JDK 21
- Maven 3.9+
- `swim-developer` parent POM installed locally (from `swim-developer-root` sibling repo)

## Architecture

### Archetype Metadata

`src/main/resources/META-INF/maven/archetype-metadata.xml` defines the 5 required properties (`modelName`, `modelDisplayName`, `modelPrefix`, `rootSchema`, `dataStandard`) and the filesets that control which template files are included, which are filtered (Velocity-processed), and which are packaged into the target Java package.

### Template Files (Velocity)

All templates live under `src/main/resources/archetype-resources/` and use Velocity syntax (`${variable}`, `#set`). The `__modelPrefix__` in filenames is replaced by the `modelPrefix` parameter at generation time.

**Generated Java classes** (3 total):
- `{Prefix}UnmarshallerPool` — thread-safe unmarshaller pool with XSD validation and XXE prevention (uses `ArrayBlockingQueue`, classpath-based `LSResourceResolver`)
- `{Prefix}XsdValidator` — lightweight wrapper delegating to UnmarshallerPool
- `{Prefix}UnmarshallerPoolTest` — JUnit 5 + AssertJ tests for null/empty/invalid XML

**Generated build/config files**: `pom.xml` (parent POM + JAXB plugin profile `generate-xjc`), `Makefile` (sync/build targets), `.xjb` binding file, `owasp-suppressions.xml`, Maven wrapper.

### Generated Project POM

The generated `pom.xml` inherits from `swim-developer` parent POM and includes a `generate-xjc` Maven profile that:
1. Cleans previously generated sources (via `maven-antrun-plugin`)
2. Runs XJC code generation from XSD schemas (via `jaxb-maven-plugin`)
3. Copies generated classes into `src/main/java`

## Coding Rules

- **Inner class exception**: the current `UnmarshallerPool` template contains inner classes `UnmarshalException`, `ClasspathResourceResolver`, `StreamLSInput` — these predate the no-inner-classes rule
- **JSON processing**: `jq` only in shell (not Python/Node)
- **Naming**: every name must be unambiguous and specific — qualify names when siblings exist

## Archetype Sync

When modifying model projects (`swim-aixm-model`, `swim-fixm-model-ed254`), check whether the change affects a class that also exists as a template in this archetype. If so, update the template. After updating, reinstall with `mvn clean install`.

## SWIM Ecosystem Context

This archetype is part of the SWIM (System Wide Information Management) reference architecture built on Red Hat technologies (Quarkus, OpenShift, ActiveMQ Artemis). Generated model projects provide JAXB bindings for aviation data standards (AIXM, FIXM, DNOTAM). The sibling repos include consumer/provider archetypes, the `swim-framework`, and service implementations.
