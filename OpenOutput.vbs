Set objShell = WScript.CreateObject("WScript.shell")
objShell.Run "notepad.exe Output.txt"
Wscript.Sleep 300
objShell.SendKeys "%OFTengwar Annatar{ENTER}"