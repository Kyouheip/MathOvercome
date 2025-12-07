# Architecture Evolution

このドキュメントでは MathOvercome がどのようにアーキテクチャ面で成長し、
クラウドに向けてスケールしていったのかを時系列でまとめます。


---

## Phase 1: ローカル環境（2025/5）
- ローカル環境でアプリケーションの基本構成を確立
- アプリMathOvercome完成
- Spring Boot をローカルで起動
- MySQL をローカルで起動
- Next.js をローカルで起動

---

## Phase 2: Docker Compose 化（2025/11）
- 3コンテナ構成へ移行（frontend / backend / db）
- init.sql の導入でclone時の再現性向上

---

## Phase 3: EC2 単体デプロイ（2025/12）
- デフォルト VPC のパブリックサブネットに EC2 を構築し、Docker Compose で frontend / backend / db を起動
- SG にて TCP/3000（Next.js）を 0.0.0.0/0 で公開
- SSH は当初、自宅 PC のグローバル IP のみに制限していたが、IP 変動のため一時的に 0.0.0.0/0 に変更
- 再起動のたびにパブリック IP が変わるため、Elastic IP の利用を検討
- 1 インスタンスで 3 コンテナを稼働させたところメモリ不足でダウン
- データベースを RDS などのマネージドサービスへ切り離す方針を検討

---