# 議論アプリケーションのバックエンド

このリポジトリは、ディスカッションアプリのバックエンドAPIサーバーです。

## プロジェクト構成

```
Pj_Discussion_app_backend/
├── src/
│   ├── main/
│   │   ├── java/~         # Javaソースコード
│   │   └── resources/     # 設定ファイル
│   └── test/              # テストコード
├── .github/               # GitHub関連の設定
├── build.gradle           # Gradleビルド設定
└── README.md
```

## 技術スタック

| カテゴリ | 技術 | バージョン |
|---------|------|------|
| 言語 | Java | 21 |
| フレームワーク | Spring Boot | 3.4.0 |
| ビルドツール | Gradle | 8.11 |
| バージョン管理 | Git / GitHub | - |

### Spring Boot エコシステム

| ライブラリ | 用途 |
|-----------|------|
| Spring Web | REST API構築 |
| Spring Data JPA | データアクセス層 |
| Spring Security | 認証・認可 |
| Spring Validation | バリデーション |
| Lombok | ボイラープレートコード削減 |
| j-jwt | JWTの機能 |
|JUnit | テストフレームワーク |
| OpenAPI | APIドキュメント |

## アーキテクチャ

このプロジェクトは **ドメイン駆動設計（DDD: Domain-Driven Design）** を採用しています。

### レイヤー構成

```
src/main/java/com/application/discussion/project
├── presentation/       # プレゼンテーション層（Controller）
│   └── controllers/
│   └── config/
│   └── exceptions/
│   └── validations/
│   └── security/
├── application/        # アプリケーション層（UseCase / Service）
│   └── services/
│   └── dtos/
├── domain/             # ドメイン層（Entity / ValueObject / Repository Interface）
│   ├── models/
│   ├── repositories/
│   └── services/
│   └── exceptions/
│   └── valueobjects/
└── infrastructure/     # インフラストラクチャ層（Repository実装 / 外部連携）
    ├── repositories/
    └── exceptions/
    └── models/
    
```

### 各レイヤーの責務

| レイヤー | 責務 |
|---------|------|
| **Presentation** | HTTPリクエスト/レスポンスの処理 |
| **Application** | ユースケースの実装、トランザクション管理、DTOの変換 |
| **Domain** | ビジネスロジック、エンティティ、値オブジェクト |
| **Infrastructure** | DB接続、外部API連携、技術的な実装詳細 |

### DDDの主要概念

- **Entity（エンティティ）**: 一意のIDを持つオブジェクト
- **Value Object（値オブジェクト）**: IDを持たない不変のオブジェクト
- **Aggregate（集約）**: 関連するエンティティのまとまり
- **Repository（リポジトリ）**: 永続化の抽象化
- **Domain Service（ドメインサービス）**: エンティティに属さないビジネスロジック

## 環境構築の手順

### 1. リポジトリをクローン

```bash
git clone //url
```

### 2. 依存関係のインストール

```bash
./gradlew build
```

### 3. アプリケーションの起動

```bash
./gradlew bootRun
```

## Docker Composeを使った環境構築

### 前提条件

- Docker がインストールされていること
- Docker Compose がインストールされていること

### Spring boot / PostgreSQL / PGAdmin4 のセットアップ

`docker-compose.yml` を使ってコンテナ起動

```bash
docker-compose build

docker-compose up
```

### 環境変数の設定：
```bash
cp .env.example .env
```

## よく使うコマンド

```bash
docker-compose build --no-cache

docker-compose down

docker-compose stop

docker-compose kill

docker-compose run --rm <サービス名> <コマンド>

docker-compose exec --it <サービス名> bash

docker-compose down --volumes --rmi --remove-orphans

docker-compose logs -f postgres
```