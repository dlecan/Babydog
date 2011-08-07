@echo off

echo Demarrage de la base de donnees
net start MySql

echo Demarrage du programme PhenoChiot
javaw.exe -Djava.library.path=C:\eclipse\plugins\org.eclipse.swt.win32_3.0.0\os\win32\x86 -classpath C:\eclipse\workspace\PhenoChiot;C:\eclipse\workspace\PhenoChiot\images;C:\eclipse\workspace\PhenoChiot\bin;C:\eclipse\plugins\org.eclipse.jface_3.0.0\jface.jar;C:\eclipse\plugins\org.eclipse.swt.win32_3.0.0\ws\win32\swt.jar;C:\eclipse\plugins\org.eclipse.core.runtime_3.0.0\runtime.jar;C:\eclipse\plugins\org.eclipse.core.boot_3.0.0\boot.jar;C:\eclipse\plugins\org.eclipse.ui.workbench_3.0.0\workbench.jar;C:\eclipse\plugins\org.eclipse.jface.text_3.0.0\jfacetext.jar;C:\eclipse\plugins\org.eclipse.text_3.0.0\text.jar;C:\eclipse\workspace\PhenoChiot\lib\log4j-1.2.8.jar;C:\eclipse\workspace\PhenoChiot\lib\mysql-connector-java-3.0.9-stable-bin.jar;C:\eclipse\workspace\PhenoChiot\lib\xercesImpl.jar;C:\eclipse\workspace\PhenoChiot\lib\xml-apis.jar;C:\eclipse\plugins\org.eclipse.osgi_3.0.0\osgi.jar;C:\eclipse\workspace\PhenoChiot\lib\p6spy.jar com.dlecan.phenochiot.admin.Administration

echo Arret de la base de donnees
net stop MySql

pause