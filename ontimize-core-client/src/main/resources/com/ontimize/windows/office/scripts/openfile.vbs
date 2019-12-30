'==========================================================================
'
' VBScript Source File --
'
' NAME: openfile
'
' AUTHOR: Alejandro Ricart
' DATE  : 22/07/2003
'
'
'==========================================================================



Dim archivo
Dim doble

On Error Resume next
Set args = Wscript.Arguments
If args.Count < 1 Then
	WScript.Echo("File name is required")
	Wscript.Quit(1)
End If

archivo=chr(34) & args(0) & chr(34)
If args.Count > 1 Then
	doble=args(1)
End If


Set shell = WScript.CreateObject("WScript.Shell")
shell.run archivo,,false

Set shell = Nothing



If doble <> "false" Then
	If Err.Number <> 0 Then
		Err.number = 0
		Set shell2 = WScript.CreateObject("Shell.Application")
		Shell2.ShellExecute archivo, "", "", "openas" , 1
		WScript.sleep 500
		Set shell2 = Nothing
	  If Err.Number <> 0 Then
		    Wscript.echo err.description
		    WScript.Quit(1)
	  End If
End If
End If