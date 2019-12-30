'==========================================================================
'
' VBScript Source File -- 
'
' NAME: printaccessreport
'
' AUTHOR: Alejandro Ricart
' DATE  : 28/02/2002
'
' COMMENT: Imprime un informe de Access. Se deben pasar los parametros
' siguientes: archivo de base de datos, nombre del informe, preview.
'
'==========================================================================

Option Explicit

' Constantes que indican el modo de apertura del informe. 
Const acNormal = 0
Const acDesign = 1
Const acPreview = 2
Const acExit = 2

On Error Resume Next

' Parametros pasados al script

Dim args
Dim bd
Dim report
Dim preview
Dim condiciones

Set args = Wscript.Arguments   
If args.Count < 2 Then
	WScript.Echo "Debe especificar la base de datos, el nombre del informe, y si debe previsualizarse, y la cadena de condiciones"
	Wscript.Quit(1)
End If

bd=args(0)
report = args(1)

If args.Count > 2 Then
	preview = CBool(args(2))
Else
	preview = False
End If

If args.Count > 3 Then
	condiciones = args(3)
End if

' Objeto access

Dim access

Set access = CreateObject("Access.Application.9")

access.OpenCurrentDatabase bd
' Access Visible si preview
If preview = True Then
	access.DoCmd.OpenReport report,acPreview,,condiciones
	access.Visible = True
Else
	access.DoCmd.OpenReport report,acNormal,,condiciones
	access.CloseCurrentDatabase
	access.Quit acExit
End If
Set access = Nothing



If Err.Number <> 0 Then
  WScript.Quit(1)
End If 


