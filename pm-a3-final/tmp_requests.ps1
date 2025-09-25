function S($u){ try{ $r=Invoke-WebRequest -UseBasicParsing -Uri $u -TimeoutSec 5; $b=$r.Content; $s = $b.Substring(0,[Math]::Min(200,$b.Length)); return $s } catch { return 'ERR: ' + $_.Exception.Message } }
function P($m,$u){ try{ if($m -eq 'GET'){ $r=Invoke-RestMethod -Uri $u -TimeoutSec 5; return ($r|ConvertTo-Json) } else { $r=Invoke-WebRequest -Method $m -Uri $u -TimeoutSec 5; return $r.Content } } catch { return 'ERR: '+$_.Exception.Message } }
$base='http://localhost:8080'
$urls = @('/','/projects','/api/projects','/api/tasks','/api/tasks/sort','/api/tasks/search?q=Tarefa%201')
foreach($p in $urls){ Write-Output "$p ->"; Write-Output (S($base+$p)); Write-Output '---' }
Write-Output '/demo stack push -> pop'
$push=P('POST',$base+'/api/demo/stack/push?v=alpha'); Write-Output $push
$pop=P('POST',$base+'/api/demo/stack/pop'); Write-Output $pop
Write-Output '---'
Write-Output '/demo queue enqueue -> dequeue'
$enq=P('POST',$base+'/api/demo/queue/enqueue?v=beta'); Write-Output $enq
$deq=P('POST',$base+'/api/demo/queue/dequeue'); Write-Output $deq
