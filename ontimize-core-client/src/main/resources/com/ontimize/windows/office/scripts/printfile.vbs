'==========================================================================
'
' VBScript Source File --
'
' NAME: printfile
'
'
'
'==========================================================================



Dim archivo
Dim doble

On Error Resume next
Set args = Wscript.Arguments
If args.Count < 1 Then
	WScript.Echo("File name must be specified")
	Wscript.Quit(1)
End If

archivo=args(0)
Set shell2 = WScript.CreateObject("Shell.Application")
Shell2.ShellExecute archivo, "", "","print",  0
WScript.sleep 5000
Set shell2 = Nothing
If Err.Number <> 0 Then
	Wscript.echo err.description
	WScript.Quit(1)
End If

