# Use a imagem oficial do Payara Server
FROM payara/server-full:6.2024.6

# Copie o arquivo WAR para o diretório de deployment do Payara
COPY SGPE.war $DEPLOY_DIR

# Exponha a porta padrão da aplicação
EXPOSE 8080

# Comando para iniciar o Payara Server
CMD ["start-domain", "--verbose"]
