param(
    [switch]$Headless
)

$ErrorActionPreference = "Stop"
$projectRoot = $PSScriptRoot
Set-Location $projectRoot

$maven = $null
if (Test-Path (Join-Path $projectRoot "mvnw.cmd")) {
    $maven = Join-Path $projectRoot "mvnw.cmd"
} elseif (Get-Command mvn.cmd -ErrorAction SilentlyContinue) {
    $maven = (Get-Command mvn.cmd).Source
} elseif (Get-Command mvn -ErrorAction SilentlyContinue) {
    $maven = (Get-Command mvn).Source
}

if (-not $maven) {
    Write-Error "Maven was not found on PATH and mvnw.cmd is unavailable."
    exit 1
}

$reportRoot = Join-Path $projectRoot "target\cross-browser-reports\TC03"
New-Item -ItemType Directory -Force $reportRoot | Out-Null

$results = [ordered]@{}

foreach ($browser in @("chrome", "firefox", "edge")) {
    Write-Host "" 
    Write-Host "===== Running TC03 in $browser =====" -ForegroundColor Cyan

    $browserReport = Join-Path $reportRoot $browser
    New-Item -ItemType Directory -Force $browserReport | Out-Null

    Remove-Item (Join-Path $projectRoot "target\cucumber-report") -Recurse -Force -ErrorAction SilentlyContinue
    Remove-Item (Join-Path $projectRoot "target\surefire-reports") -Recurse -Force -ErrorAction SilentlyContinue

    $arguments = @("test", "-Dcucumber.filter.tags=@TC03", "-Dbrowser=$browser")
    if ($Headless) {
        $arguments += "-Dheadless=true"
    }

    & $maven @arguments
    $exitCode = $LASTEXITCODE
    $passed = $exitCode -eq 0

    $cucumberReport = Join-Path $projectRoot "target\cucumber-report"
    if (Test-Path $cucumberReport) {
        Copy-Item (Join-Path $cucumberReport "*") $browserReport -Recurse -Force
    }

    $surefireReport = Join-Path $projectRoot "target\surefire-reports"
    if (Test-Path $surefireReport) {
        Copy-Item $surefireReport (Join-Path $browserReport "surefire-reports") -Recurse -Force
    }

    $screenshots = Join-Path $projectRoot "target\screenshots"
    if (Test-Path $screenshots) {
        Copy-Item $screenshots (Join-Path $browserReport "screenshots") -Recurse -Force
    }

    $status = if ($passed) { "PASS" } else { "FAIL" }
    $results[$browser] = $status
    "Browser=$browser`nScenario=TC03 - Prevent adding a user with empty required fields`nStatus=$status`nExitCode=$exitCode" |
        Set-Content (Join-Path $browserReport "result.txt") -Encoding UTF8

    Write-Host "TC03 $browser: $status (exit code $exitCode)"
}

Write-Host "" 
Write-Host "===== TC03 Cross-Browser Summary =====" -ForegroundColor Cyan
foreach ($browser in $results.Keys) {
    Write-Host ("{0}: {1}" -f $browser, $results[$browser])
}

if ($results.Values -contains "FAIL") {
    exit 1
}
exit 0
