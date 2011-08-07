@echo off

java -cp ..\lib\mckoidb.jar com.mckoi.tools.JDBCQueryTool -url "jdbc:mckoi:local://./db.conf" -u "root" -p "master"