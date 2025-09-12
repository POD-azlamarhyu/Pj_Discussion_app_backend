---
applyTo: "**/*.java"
description: "Javaコーディング規約に従うことを指示する"
---

# Javaコーディング規約


## 技術スタック
このプロジェクトでは，以下の技術スタックを利用している．これらを前提として，コードを生成すること．

- Java21
- Spring Boot 3.2.5
- Spring Web
- Spring Data JPA
- Spring Security
- H2 Database
- Spring JDBC
- JUnit 5
- Mockito
- Lombok
- SpringDoc
- OpenAPI 3.0
- Gradle 8.2.1

## ディレクトリ構成
本プロジェクトはドメイン駆動設計を採用しており，ディレクトリ構成は以下の通り．この構成を前提として，コードを生成すること。
プロンプトで指示をされない限り，勝手にディレクトリ構成を変更しないこと。

```
./src
├── main
│   ├── java
│   │   └── com
│   │       └── application
│   │           └── discussion
│   │               └── project
│   │                   ├── DiscussionAppApplication.java
│   │                   ├── application
│   │                   ├── domain
│   │                   ├── infrastructure
│   │                   └── presentation
│   └── resources
│       ├── application-dev.properties
│       ├── application.properties
│       └── example.properties
└── test
    └── java
        └── com
            └── application
                └── discussion
                    └── project
                        ├── DiscussionAppApplicationTests.java
                        ├── application
                        ├── config
                        ├── domain
                        ├── infrastructure
                        └── presentation
```

## コーディング規約
- コードは、Javaのコーディング規約に従ってください。
- コードは、Spring Bootのベストプラクティスに従ってください。
- コードは、テスト可能な設計を心がけてください。
- インデントは4スペースを使用してください。
- クラス名は大文字で始め、単語の区切りは大文字で表記してください（例：MainTopicController）。
- メソッド名は小文字で始め、単語の区切りは大文字で表記してください（例：getMainTopics）。
- 変数名は小文字で始め、単語の区切りは大文字で表記してください（例：mainTopicService）。
- for文やif文はなるべく使用せず，Stream APIやラムダ式，Optionalを活用してください。
- コメントは必要な箇所にのみ記述し、コードの意図を明確にしてください。
- 関数の役割のみのコメントは無駄に行数が増えるだけなので禁止とします。
- Javadocコメントを使用して、クラス、メソッド、フィールドの説明を記述してください。
- 1つのメソッドは、できるだけ短く、単一の責任を持つようにしてください。
- 例外処理は適切に行い、エラーハンドリングを行ってください。
- SOLIDの原則に従い、クラスは単一の責任を持つようにしてください。
- ドメイン駆動設計（DDD）を意識して、ドメイン層とアプリケーション層を明確に分離してください。
- DRY（Don't Repeat Yourself）の原則に従い、重複コードを避けてください。
- KISS（Keep It Simple, Stupid）の原則に従い、コードはシンプルに保ってください。
- YAGNI（You Aren't Gonna Need It）の原則に従い、不要な機能は実装しないでください。
- 定数はスネークケースで命名し、クラスの先頭にまとめて定義してください（例：MAX_TOPIC_COUNT）。定数は必ずスタティック変数として，再代入を禁止してください。
- ロギングはSLF4Jを使用し、適切なログレベルを設定してください。
- Spring Bootのアノテーションを適切に使用し、コードの可読性を向上させてください。
- RESTful APIを設計する際は、HTTPメソッドとステータスコードを適切に使用してください。
- データベースアクセスはSpring Data JPAを使用し、リポジトリパターンを活用してください。
- セキュリティはSpring Securityを使用し、認証と認可を適切に実装してください。
- テストコードはJUnit 5とMockitoを使用し、ユニットテストと統合テストを適切に実装してください。


