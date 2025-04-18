@echo off
echo Finalizando processos do Gradle travados...

:: Lista e finaliza processos java.exe (usados pelo Gradle)
for /f "tokens=2 delims=," %%i in ('tasklist /fi "imagename eq java.exe" /fo csv /nh') do (
    echo Finalizando PID %%i...
    taskkill /F /PID %%i >nul 2>&1
)

:: Remove o arquivo de lock do Gradle
echo Removendo arquivo de lock do Gradle cache...
del /F /Q "%USERPROFILE%\\.gradle\\caches\\journal-1\\journal-1.lock"

echo.
echo Finalizado! Agora tente rodar novamente: ./gradlew bootRun
pause
