@echo off

REM ==========================================
REM RUN JMETER LOAD TEST FOR NOTIFICATION SERVICE
REM ==========================================

"C:\apache-jmeter-5.6.3\bin\jmeter.bat" ^
 -n ^
 -t "C:\Users\hp\Documents\workspace-chubb\flight-app-microservices\notification-service\your_test.jmx" ^
 -l "C:\Users\hp\Documents\workspace-chubb\flight-app-microservices\notification-service\notification_results.csv" ^
 -e ^
 -o "C:\Users\hp\Documents\workspace-chubb\flight-app-microservices\notification-service\notification_report"

echo.
echo =====================================================
echo Notification Service Load Test Completed Successfully
echo HTML Report generated at:
echo C:\Users\hp\Documents\workspace-chubb\flight-app-microservices\notification-service\notification_report
echo =====================================================
echo.
pause
