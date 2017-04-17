# ASHYI


 DESPLIEGUE EN SERVIDOR
 
Instalar Sakai siguiendo estas instrucciones:
https://confluence.sakaiproject.org/pages/viewpage.action?pageId=82249316

Tomcat debe quedar instalado en /opt/tomcat

Configurar Mysql usando la siguiente guía: 

https://confluence.sakaiproject.org/display/BOOT/Install+MySQL+5.1

Crear la BD usando esta guía:
https://confluence.sakaiproject.org/pages/viewpage.action?pageId=65044526

 
 Para habilitar https, agregar esta línea a $CATALINA_HOME/conf/server.xml
 
<Connector port="8080" maxHttpHeaderSize="8192" SSLEnable="true"
           keystoreFile="/opt/Apache/apache-tomcat-7.0.42/conf/keystore.jks"
           maxThreads="150" minSpareThreads="25" maxSpareThreads="75"
           enableLookups="false" disableUploadTimeout="true"
           acceptCount="100" scheme="https" secure="true"
           clientAuth="false" sslProtocol="TLS"
           proxyName="10.2.2.14"
           proxyPort="443"
           ciphers="TLS_RSA_WITH_AES_128_CBC_SHA, TLS_DHE_RSA_WITH_AES_128_CBC_SHA, TLS_DHE_DSS_WITH_AES_128_CBC_SHA, SSL_RSA_WITH_3DES_EDE_CBC_SHA, SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA, SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA"
/>

Para que Sakai inicie más rápido:
http://steveswinsburg.wordpress.com/2012/11/06/reduce-your-sakai-cle-startup-time/

Para iniciar tomcat automáticamente cuando se rebootea el computador
Copiar el archivo ayllu/ASHYI/lessonbuilder-1.4.3/tomcat7 a /etc/init.d
Ejecutar estos comandos:
sudo chmod 755 /etc/init.d/tomcat7
sudo update-rc.d tomcat7 defaults



COMPILACIÓN e INSTALACIóN

Compilar e instalar ayllu. En la carpeta ayllu/AYLLU_Platform
mvn clean install

Luego hay que compilar todos los proyectos ASHYI
En la carpeta ayllu/ASHYI
mvn clean install

Luego compilar y desplegar lessonbuilder

En la carpeta ayllu/ASHYI/lessonbuilder-1.4.3
mvn clean install sakai:deploy  -Dmaven.tomcat.home=/opt/tomcat

En la carpeta ayllu/Chaea
mvn clean install sakai:deploy  -Dmaven.tomcat.home=/opt/tomcat

Copiar el archivo confbesa.xml que está en /ASHYI/BESA.KISSController a la raíz /

Cambiar el dato auto.ddl de sakai.properties a true;

Crear la BD en Mysql usando las siguientes instrucciones: https://confluence.sakaiproject.org/pages/viewpage.action?pageId=65044526

Modificar el archivo /opt/tomcat/bin/setenv.sh agregar "-Duser.language=es -Duser.region=ES" a JAVA_OPTS

Iniciar sakai

Cambiar contraseña usuario admin

Bajar sakai

Cambiar el dato auto.ddl de sakai.properties a false;

Iniciar sakai

---
CONFGURACIÓN
Crear período académico. Ejemplo:
 insert into cm_academic_session_t (VERSION,LAST_MODIFIED_BY, LAST_MODIFIED_DATE, CREATED_BY, CREATED_DATE, ENTERPRISE_ID, TITLE, DESCRIPTION, START_DATE, END_DATE, IS_CURRENT) values('1','', curdate(), '', curdate(), 'PUJ', '2014-1', 'Semestre 2014-1', '2014-01-24', '2014-08-25','1');





Crear un curso usando Sakai -> Worksite Setup. Para crear el Subject, usar la opción "Still cannot find your course/section?"
Añadir dos herramientas de tipo LessonBuilder (Contenidos en Español). 
(a) Una debe llamarse "Conf Meta Datos"
(b) La otra puede tener cualquier nombre


Crear datos de Chaea. El idSite debe ser el mismo que el curso creado anteriormente.

 INSERT INTO `chaea_inquerito` (`idSite`,`inicio`,`fim`) VALUES ('93e96f13-6db8-4e58-8e5e-9d84fc1e86cc','2014-01-30 15:43:39','2015-02-25 16:00:39');
 
Cargar datos básicos de actividades:

 LOAD DATA  INFILE "sakai/Datos/items.csv"     INTO TABLE `sakai`.`item`     FIELDS TERMINATED BY ','    LINES TERMINATED BY '\r\n';
 LOAD DATA  INFILE "sakai/Datos/tipos.csv"     INTO TABLE `sakai`.`tipo`     FIELDS TERMINATED BY ','    LINES TERMINATED BY '\r\n';
 LOAD DATA  INFILE "sakai/Datos/caracteristicas.csv"     INTO TABLE `sakai`.`caracteristica`     FIELDS TERMINATED BY ','    LINES TERMINATED BY '\r\n';
 LOAD DATA  INFILE "sakai/Datos/caracteristicasTipo.csv"     INTO TABLE `sakai`.`caracteristicas_tipo`     FIELDS TERMINATED BY ','    LINES TERMINATED BY '\r\n';
 LOAD DATA  INFILE "sakai/Datos/palabra.csv"     INTO TABLE `sakai`.`palabraclave`     FIELDS TERMINATED BY ','    LINES TERMINATED BY '\r\n';
 
 Los archivos csv deben estar en /var/lib/mysql/sakai/Datos. Si mysql da error de acceso denegado, la BD
 debería abrirse con este comando:
 
 sudo mysql sakai -u sakai -p
 
 
 (1) Crear un nuevo usuario, donde Type = "Ashyi_Administrador"
 (2) Crear un nuevo usuario, donde Type = "Instructor"
 
 añadir ambos usuarios al curso con el rol Instructor
 
 Hacer logout e ingresar como el usuario (1). 
 
 
 Ingresar a (a)
 
 Hacer click en "Registrar Curso, objetivos y recursos"
 
 
  LOAD DATA  INFILE "sakai/Datos/objetivosCurso.csv"     INTO TABLE `sakai`.`objetivo`     FIELDS TERMINATED BY ';'    LINES TERMINATED BY '\r\n';
 LOAD DATA  INFILE "sakai/Datos/objetivosActividadCurso.csv"     INTO TABLE `sakai`.`objetivos_actividad`     FIELDS TERMINATED BY ','    LINES TERMINATED BY '\r\n';
 
 Hacer logout e ingresar como el usuario (2)
 Modificar el curso para que no se despliegue (a) a los estudiantes
 
 Ingresar a (b) 
 - Sakai por debajo registrará datos adicionales del curso y tema.
 Aparecerá el siguiente mensaje "No se ha podido guardar o actualizar el elemento. Error: Batch update returned unexpected row count from update [0]; actual row count: 0; expected: 1"
 ESTO ES NORMAL
 
 
 Crear 3 unidades didácticas en (b)
 
 
 OPCIONAL
 Crear los objetivos de las 3 unidades didácticas
  LOAD DATA  INFILE "sakai/Datos/objetivosActividadUnidades.csv"     INTO TABLE `sakai`.`objetivos_actividad`     FIELDS TERMINATED BY ','    LINES TERMINATED BY '\r\n'; 
 
ADMINISTRACIÓN




Esta aplicación es útil para conectarse de manera más amigable al servidor:http://mobaxterm.mobatek.net/download-home-edition.html
   
   
