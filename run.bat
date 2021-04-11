set classpath=lib/mysql-connector-java-5.1.6-bin.jar;lib/jfreechart-1.0.13.jar;lib/jfreechart-1.0.13-experimental.jar;lib/jfreechart-1.0.13-swt.jar;lib/jcommon-1.0.16.jar;lib/stanford-corenlp-3.6.0-models.jar;lib/stanford-corenlp-3.6.0.jar;lib/ejml-0.23.jar;lib/slf4j-api.jar;.;
javac -d . *.java
java -Xmx1000M com.MainScreen
pause