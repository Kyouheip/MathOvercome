FROM node:20-alpine
WORKDIR /app

#依存関係を先にインストールしてキャッシュ化
COPY package*.json ./
RUN npm install

#ビルド
COPY . .
RUN npm run build

EXPOSE 3000

#本番用実行
CMD ["npm", "start"]
