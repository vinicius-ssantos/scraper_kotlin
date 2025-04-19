# Caminho do arquivo de lock do Gradle
$lockFile = "$env:USERPROFILE\.gradle\caches\journal-1\journal-1.lock"

# Verifica se o arquivo existe
if (-Not (Test-Path $lockFile)) {
    Write-Host "✅ Nenhum arquivo de lock encontrado em $lockFile."
    exit 0
}

Write-Host "🔍 Verificando processo que está usando o lock..."

# Verifica se o handle.exe está disponível
if (-Not (Get-Command "handle.exe" -ErrorAction SilentlyContinue)) {
    Write-Host "❌ 'handle.exe' não encontrado no PATH. Baixe em https://learn.microsoft.com/sysinternals/downloads/handle e adicione ao PATH."
    exit 1
}

# Captura a saída do handle procurando o lock
$handleOutput = & handle.exe $lockFile 2>&1

# Extrai o PID do processo que está segurando o lock
if ($handleOutput -match "pid: (\d+)") {
    $pid = $Matches[1]
    Write-Host "🔒 Arquivo está sendo usado pelo processo PID: $pid"

    try {
        Stop-Process -Id $pid -Force
        Write-Host "✅ Processo $pid finalizado com sucesso."
    } catch {
        Write-Host "❌ Erro ao finalizar o processo $pid: $_"
        exit 1
    }

    # Espera um pouco e tenta remover o lock
    Start-Sleep -Seconds 1
    try {
        Remove-Item -Path $lockFile -Force
        Write-Host "🧹 Arquivo de lock removido com sucesso!"
    } catch {
        Write-Host "❌ Falha ao remover o arquivo de lock: $_"
        exit 1
    }
} else {
    Write-Host "⚠️ Nenhum processo identificado segurando o lock. Tente remover manualmente."
}
