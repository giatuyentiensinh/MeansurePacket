cd src
javac *.java

java RecvTCP &
java RecvUDPSendTCP &
java SendUDP && rm *.class
