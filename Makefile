.PHONY: build
build:
	sam build

build-TypeaheadSearchitemsFunction:
	mvn -B clean package
	echo "ARTIFACTS_DIR: $(ARTIFACTS_DIR)"
	mkdir -p $(ARTIFACTS_DIR)/lib
	cp ./target/*.jar $(ARTIFACTS_DIR)/lib/

.PHONY: init
init: build
	sam deploy --guided

.PHONY: deploy
deploy: build
	sam deploy

.PHONY: delete
delete:
	sam delete
