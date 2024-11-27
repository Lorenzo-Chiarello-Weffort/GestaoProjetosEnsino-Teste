# Use a imagem oficial do Payara Server

FROM payara/server-full:6.2024.6



# Copie o arquivo WAR da aplicação para o diretório de deployment do Payara

COPY SGPE.war /opt/payara/deployments/



# Exponha as portas necessárias (4848 para administração e 8080 para a aplicação)

EXPOSE 8080 4848



# Comando para iniciar o domínio padrão

ENTRYPOINT ["/opt/payara/bin/startInForeground.sh"]