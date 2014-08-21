Set objShell = WScript.CreateObject("WScript.Shell")
objShell.Run "java TengwarConvert"
Wscript.Sleep 500
Wscript.Sleep 4000
objShell.SendKeys "This is some sample text"
