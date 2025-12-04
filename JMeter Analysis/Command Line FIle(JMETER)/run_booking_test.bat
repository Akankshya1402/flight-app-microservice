@echo off

REM =====================================
REM RUN JMETER LOAD TEST FOR BOOKING SERVICE
REM =====================================

"C:\apache-jmeter-5.6.3\bin\jmeter.bat" ^
 -n ^
 -t "C:\Users\hp\Documents\workspace-chubb\flight-app-microservices\booking-service\your_test.jmx" ^
 -l "C:\Users\hp\Documents\workspace-chubb\flight-app-microservices\booking-service\booking_results.csv" ^
 -e ^
 -o "C:\Users\hp\Documents\workspace-chubb\flight-app-microservices\booking-service\booking_report"

echo.
echo =====================================
echo Test completed successfully!
echo HTML report generated at:
echo C:\Users\hp\Documents\workspace-chubb\flight-app-microservices\booking-service\booking_report
echo =====================================
echo.
pause
