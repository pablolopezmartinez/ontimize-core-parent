'==========================================================================
'
' VBScript Source File -- 
'
' NAME: report2snapshot
'
' AUTHOR: Alejandro Ricart
' DATE  : 28/02/2002
'
' COMMENT: Imprime un informe de Access a un archivo snapshot. 
' Se requiere el visor de snapshots para verlo y/o imprimirlo.
' Se deben pasar los parametros
' siguientes: archivo de base de datos, nombre del informe
'
'==========================================================================

Option Explicit

Const acOutputReport = 3
Const acFormatSNPXP = "Snapshot Format (*.snp)"
Const acFormatSNP =  "Snapshot Format (*.snp)"
Const acFormatRTF = "Rich Text Format (*.rtf)"
Const acExit = 2
Const acNormal = 0
Const acDesign = 1
Const acPreview = 2

' Parametros pasados al script

Dim args
Dim bd
Dim nombreInforme
Dim archivoDestino
Dim condiciones 

On Error Resume Next

Set args = Wscript.Arguments   
If args.Count < 3 Then
	WScript.Echo("Debe especificar la base de datos, el nombre del informe, y el nombre del archivo destino. Opcionalemente puede especificar una condicion SQL")
	Wscript.Quit(1)
End If

bd=args(0)
nombreInforme = args(1)
archivoDestino = args(2)
If(args.Count>3) Then
	condiciones = args(3)
End if

' Objeto access

Dim access
Dim informe

Set access = CreateObject("Access.Application.9")
' Abriendo la base de datos especificada
access.OpenCurrentDatabase bd
access.Visible = False
' Volcamos el contenido del informe en un snapshot
access.DoCmd.OpenReport nombreInforme, acPreview, , condiciones
access.DoCmd.OutputTo acOutputReport, , acFormatRTF, archivoDestino

WScript.Echo("Realizado: "&nombreInforme)
access.Quit acExit
WScript.Echo("Error # " & CStr(Err.Number) & " " & Err.Description)

Set access = Nothing

If Err.Number <> 0 Then
  WScript.Quit(1)
End If 