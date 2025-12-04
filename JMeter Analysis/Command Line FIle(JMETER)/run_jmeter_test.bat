@echo off

REM =============================
REM RUN JMETER LOAD TEST
REM =============================

"C:\apache-jmeter-5.6.3\bin\jmeter.bat" ^
 -n ^
 -t "C:\Users\hp\Documents\workspace-chubb\flight-app-microservices\flight-service\notification_test.jmx" ^
 -l "C:\Users\hp\Documents\workspace-chubb\flight-app-microservices\flight-service\results.csv" ^
 -e ^
 -o "C:\Users\hp\Documents\workspace-chubb\flight-app-microservices\flight-service\report"

echo.
echo =============================
echo Test completed successfully!
echo HTML report generated at:
echo C:\Users\hp\Documents\workspace-chubb\flight-app-microservices\flight-service\report
echo =============================
echo.
pause
