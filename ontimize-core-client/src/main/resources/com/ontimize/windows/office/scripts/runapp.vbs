'==========================================================================
'
' VBScript Source File -- 
'
' NAME: runapp
'
' AUTHOR: Alejandro Ricart
' DATE  : 21/05/2003
'
'
'==========================================================================


On Error Resume Next
Dim app 

Set args = Wscript.Arguments   
If args.Count < 1 Then
	WScript.Echo("Debe especificar el nombre del objeto aplicacion")
	Wscript.Quit(1)
End If

app=args(1)

Set application = WScript.CreateObject(app)
application.Visible=True
Set application=Nothing