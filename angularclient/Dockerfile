FROM node:18-alpine as build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install -g npm@10.8.1
# Устанавливаем зависимости
RUN npm ci
COPY . .

RUN npm run build
#RUN npm run build --prod
FROM nginx:alpine
COPY --from=build /app/dist/my-angular-app /usr/share/nginx/html

