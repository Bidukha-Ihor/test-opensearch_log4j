#Консольні команди
cd src/main/docker

#старт контейнера зі звільненням терміналу
sudo docker compose up -d
#статус
sudo docker compose ps [-a]
#вивод логів
sudo docker compose logs
#стоп
docker compose down
#images
sudo docker compose images

#не перевірені
docker compose exec [service name] [command]