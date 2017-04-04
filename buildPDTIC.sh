#!/bin/bash

JBOSS_PASS="admin"

JBOSS_PASS='admin'
if [ "$JBOSS_PASS" = "**Random**" ]; then
    unset JBOSS_PASS
fi

if [ ! -f /.jboss_admin_pass_configured ]; then
    sh set_jboss_admin_pass.sh
fi

echo "Vai configurar o nome do HOST de banco de dados"
HOSTNAME="localhost"
# Verifica o valor da variável de ambiente
if [ -n "$HOSTNAMELINK" ]; then
    HOSTNAME=$HOSTNAMELINK
fi


#HOSTNAME="HOSTNAME"
DBNAME="gog"
DBUSERNAME="clelson"
DBPASSWORD="csr1919"
DBSCHEMA="public"





echo "Verificando variáveis de sistema: "
echo "   HOSTNAME: "$HOSTNAME
echo "   DBNAME: "$DBNAME
echo "   DBUSERNAME: "$DBUSERNAME
echo "   DBPASSWORD: "$DBPASSWORD
echo "   DBSCHEMA: "$DBSCHEMA

cp standalone.xml /opt/jboss-as-7.1.1.Final/standalone/configuration/

sed -i -r "s/HOSTNAME/$HOSTNAME/" /opt/jboss-as-7.1.1.Final/standalone/configuration/standalone.xml
sed -i -r "s/DBNAME/$DBNAME/" /opt/jboss-as-7.1.1.Final/standalone/configuration/standalone.xml
sed -i -r "s/DBUSERNAME/$DBUSERNAME/" /opt/jboss-as-7.1.1.Final/standalone/configuration/standalone.xml
sed -i -r "s/DBPASSWORD/$DBPASSWORD/" /opt/jboss-as-7.1.1.Final/standalone/configuration/standalone.xml
sed -i -r "s/DBSCHEMA/$DBSCHEMA/" /opt/jboss-as-7.1.1.Final/standalone/configuration/standalone.xml

cat /opt/jboss-as-7.1.1.Final/standalone/configuration/standalone.xml

# Aguardar a configuração do serviço de bancos para iniciar o jboss...
echo "Vai esperar o serviço de Banco de Dados ser configurado ..."
PORT=5432
export PGPASSWORD=$DBPASSWORD
while true; do
   if psql -lqt -h $HOSTNAME -p $PORT -U $DBUSERNAME $DBNAME | cut -d \| -f 1 | grep -qw $DBNAME; then
       echo "...Banco de Dados pronto"
       break
   else
       echo "...O BD ainda não foi configurado"
       sleep 5
   fi
done




sed -i -r 's/<!-- <property name=\"hibernate.hbm2ddl.auto\" value=\"update\" \/>  -->/<property name=\"hibernate.hbm2ddl.auto\" value=\"update\" \/>/' /opt/GOG/GOG/src/main/resources/META-INF/persistence.xml 
cat /opt/GOG/GOG/src/main/resources/META-INF/persistence.xml






echo "GOG - Monta a build do GOG utilizando o Maven"
cd /opt/GOG/GOG
mvn package

echo "Vai iniciar o Jboss ..."
/opt/jboss-as-7.1.1.Final/bin/standalone.sh -b=0.0.0.0 &
# Aguardar o jboss subir...
sleep 25

echo "Vai realizar o deploy ..."
/opt/jboss-as-7.1.1.Final/bin/jboss-cli.sh --connect --command="deploy /opt/GOG/GOG/target/GOG.war --force"
echo "Jboss iniciado com o depĺoy realizado"

echo "...Agora vamos carregar os dados do sistema..."
sh carregaDados.sh 

echo -e "\n\n\nPRONTO! O GOG está funcionando! Use: http://localhost:8080/GOG"

while :
do
	sleep 1
done
