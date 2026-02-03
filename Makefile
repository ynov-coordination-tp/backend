.PHONY: dev
dev:
	docker compose -f docker-compose.dev.yml up -d

.PHONY: build
build:
	docker compose -f docker-compose.build.yml build

.PHONY: down
down:
	docker compose -f docker-compose.dev.yml down
