'==========================================================================
'
' VBScript Source File -- 
'
' NAME: report2snapshot
'

' Generacion de un archivo excel (xls) a partir de un txt
'
'==========================================================================

Const xlExcel9795 = 43

On Error Resume Next
Dim destino
Dim objshell
Set args = Wscript.Arguments   
If args.Count < 1 Then
	WScript.Echo("Debe especificar  nombre destino")
	Wscript.Quit(1)
End If

destino=args(0)

Set excel = WScript.CreateObject("Excel.Application")
excel.Visible=true
excel.DisplayAlerts = False
Set libro = excel.WorkBooks.Add

'libro.WorkSheets(1).Name="Hoja 1"

Set objShell = CreateObject("WScript.Shell")
libro.activate
objShell.sendkeys "^v"

libro.SaveAs destino

excel.DisplayAlerts = True

'WScript.Echo("Archivo guardado como "+destino)

Set excel=Nothing

If Err.Number <> 0 Then
  WScript.Quit(1)
End If 