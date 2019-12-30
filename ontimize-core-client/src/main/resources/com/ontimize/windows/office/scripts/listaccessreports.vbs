'==========================================================================
'
' VBScript Source File -- 
'
' NAME: Listado de informes disponibles en una base de datos Access
'
' AUTHOR: x , xx
' DATE  : 28/02/2002
'
' COMMENT: Listado de informes disponibles en una base de datos Access. Parametros:
' el nombre del archivo de base de datos
'=========================================================================

Option Explicit

Dim access
Dim bd
Dim reports
Dim report
Dim cadenaReports 

On Error Resume Next

If WScript.Arguments.Count < 1 Then
	WScript.Echo("Debe espeficicar el nombre del archivo de base de datos")
	WScript.Quit(1)
End If

Set access = CreateObject("Access.Application.9")
' coleccion de informes del proyecto actual
bd = WScript.Arguments(0)

access.OpenCurrentDatabase bd
Set reports = access.CurrentProject.AllReports

For Each report In reports
	cadenaReports = cadenaReports & report.Name & ";"
Next

WScript.Echo(cadenaReports)

access.CloseCurrentDatabase
Set access = Nothing

If Err.Number <> 0 Then
  WScript.Quit(1)
End If 


