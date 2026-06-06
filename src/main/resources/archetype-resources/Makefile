#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
SONAR_URL   ?= http://localhost:9000
SONAR_TOKEN ?=

PARENT_DIR  := ${symbol_dollar}(abspath ${symbol_dollar}(dir ${symbol_dollar}(abspath ${symbol_dollar}(lastword ${symbol_dollar}(MAKEFILE_LIST))))/..)
GITHUB_SSH  := git@github.com:swim-developer

SYNC_DEPS := swim-developer-root

.PHONY: help sync pull pull-deps install-deps deps install test sonar security-deps

help:
	@echo ""
	@echo "  ${artifactId} — available targets"
	@echo "  ─────────────────────────────────────────────────────────"
	@echo ""
	@echo "    deps               Show which sibling repos must be installed first"
	@echo "    install            Build + install to local Maven repo"
	@echo "    test               Unit tests"
	@echo "    sonar              SonarQube analysis  (requires SONAR_TOKEN=<token>)"
	@echo "    security-deps      OWASP Dependency-Check"
	@echo ""
	@echo "  Sync:"
	@echo "    sync               Full setup: pull + pull-deps + install-deps"
	@echo "    pull               Pull this project from remote"
	@echo "    pull-deps          Clone missing deps + pull existing ones in ${symbol_dollar}(PARENT_DIR)"
	@echo "    install-deps       Install all deps into local Maven repository"
	@echo ""
	@echo "  Variables: SONAR_URL=${symbol_dollar}(SONAR_URL)"

sync: pull pull-deps install-deps

pull:
	@echo ""
	@echo "  ── Pull this project ────────────────────────────────────────"
	@git pull --ff-only
	@echo ""

pull-deps:
	@echo ""
	@echo "  ── Ensure sibling dependencies in ${symbol_dollar}(PARENT_DIR) ─────────────"
	@for repo in ${symbol_dollar}(SYNC_DEPS); do \
	  dir="${symbol_dollar}(PARENT_DIR)/${symbol_dollar}${symbol_dollar}repo"; \
	  if [ ! -d "${symbol_dollar}${symbol_dollar}dir" ]; then \
	    echo "  CLONE   ${symbol_dollar}${symbol_dollar}repo"; \
	    git clone "${symbol_dollar}(GITHUB_SSH)/${symbol_dollar}${symbol_dollar}repo.git" "${symbol_dollar}${symbol_dollar}dir" --quiet; \
	  else \
	    printf "  PULL    ${symbol_dollar}${symbol_dollar}repo ... "; \
	    git -C "${symbol_dollar}${symbol_dollar}dir" pull --ff-only --quiet 2>&1 && echo "ok" || echo "skipped (local changes or detached HEAD)"; \
	  fi; \
	done
	@echo ""

install-deps:
	@echo ""
	@echo "  ── Install dependencies into local Maven repository ─────────"
	@for repo in ${symbol_dollar}(SYNC_DEPS); do \
	  dir="${symbol_dollar}(PARENT_DIR)/${symbol_dollar}${symbol_dollar}repo"; \
	  if [ ! -d "${symbol_dollar}${symbol_dollar}dir" ]; then \
	    echo "  SKIP    ${symbol_dollar}${symbol_dollar}repo (not found — run: make pull-deps)"; \
	    continue; \
	  fi; \
	  mvn_cmd="mvn"; \
	  [ -f "${symbol_dollar}${symbol_dollar}dir/mvnw" ] && mvn_cmd="${symbol_dollar}${symbol_dollar}dir/mvnw"; \
	  if [ "${symbol_dollar}${symbol_dollar}repo" = "swim-developer-root" ]; then \
	    args="install -N -DskipTests -q"; \
	  else \
	    args="clean install -DskipTests -q"; \
	  fi; \
	  printf "  INSTALL ${symbol_dollar}${symbol_dollar}repo ... "; \
	  "${symbol_dollar}${symbol_dollar}mvn_cmd" -f "${symbol_dollar}${symbol_dollar}dir/pom.xml" ${symbol_dollar}${symbol_dollar}args && echo "ok" || { echo "FAIL"; exit 1; }; \
	done
	@echo ""
	@echo "  Done. Run: make install"
	@echo ""

deps:
	@echo ""
	@echo "  Required sibling repos — install to local Maven repo first:"
	@echo ""
	@echo "    git clone https://github.com/swim-developer/swim-developer-root"
	@echo "    cd swim-developer-root && ./mvnw install -N -DskipTests"
	@echo ""

install:
	./mvnw clean install -DskipTests

test:
	./mvnw test

sonar:
	./mvnw clean verify sonar:sonar \
		-Dsonar.host.url=${symbol_dollar}(SONAR_URL) \
		${symbol_dollar}(if ${symbol_dollar}(SONAR_TOKEN),-Dsonar.login=${symbol_dollar}(SONAR_TOKEN),) \
		-Dsonar.projectKey=${artifactId} \
		-Dsonar.projectName=${artifactId}

security-deps:
	./mvnw org.owasp:dependency-check-maven:aggregate \
		-DfailBuildOnCVSS=7 -Dformats=HTML,JSON -DskipTests \
		-DsuppressionFile=owasp-suppressions.xml
	@echo "Report: target/dependency-check-report.html"
