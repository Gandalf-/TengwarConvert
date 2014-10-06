Set objShell = WScript.CreateObject("WScript.shell")
objShell.Run "notepad.exe Output_converted.txt"
Wscript.Sleep 300
objShell.SendKeys "%OFTengwar Annatar{TAB}{TAB}26{ENTER}"