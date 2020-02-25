
Configurare WorkSpace per WEB_ESECUZIONE

1) Utilizzare Eclipse Kepler
-configurare il jre su jdk1.6
-aggiungere tomcat 6 come server
-configurare la parte network mettendo il proxy per accedere a svn
-configurare il proxy anche su maven2 (se manca installare/configurare) nel file 
C:\Documents and Settings\<local user>\.m2\settings.xml
  <proxies>
	<proxy>
		<active>true</active>
		<protocol>http</protocol>
		<host>localhost</host>
		<port>808</port>
		<username></username>
		<password></password>
		<nonProxyHosts></nonProxyHosts>
	</proxy>
  </proxies>


2) Aggiungere il plugin di subclipse
http://subclipse.tigris.org/update_1.6.x

3) Connettersi a SVN
OLD: https://almesl.eng.it/scm/svnrepos/sies
NEW: https://production.eng.it/scm/svnrepos/sies

4) Scaricarsi il progetto WEB_ESECUZIONE
- checkout come progetto semplice
- convertirlo in progetto maven

5) Aggiungerlo come progetto Web su Tomcat