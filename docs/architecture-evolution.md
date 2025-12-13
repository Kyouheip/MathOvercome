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

## Phase 4: RDS 分離・ネットワーク整理（2025/12）

- データベースを EC2 内コンテナから **RDS（MySQL）へ分離**
- RDS の SG にて **ポート 3306 を EC2 の SG のみ許可**
- 新規 VPC を作成し、**RDS をプライベートサブネットに配置する構成を検討**

### ネットワーク / 通信構成
- クライアントコンポーネント構成のため、**EC2 の SG で Backend ポート 8080 を開放**
- **Next.js クライアントから Backend API を直接呼び出す構成**を採用
- 今後、**Next.js Server Component または Route Handler 経由で fetch する構成への移行を検討**

### セキュリティ / 設定管理
- RDS の接続情報は GitHub に公開せず、  
  **EC2 インスタンス上の `.env` から環境変数として設定**
- CORS 設定や環境変数で **グローバル IP を使用する箇所が増えたため Elastic IP を使用**
- HTTPS 未対応環境において secure cookie によりセッションが維持されない問題が発生したため、  
  **HTTP 環境向けにセッション Cookie 設定を変更**

