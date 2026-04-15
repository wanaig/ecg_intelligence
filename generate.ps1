$initFile = "$pwd/sql/init.sql"
$content = Get-Content $initFile -Raw

# Remove all inserts except dict
$regex = "(?s)INSERT INTO sys_ecg_doctor_info.*?(?=-- ======)"
$content = $content -replace $regex, ""

Set-Content $initFile -Value $content -Encoding UTF8
