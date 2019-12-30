'==========================================================================
'
' VBScript Source File -- 
'
' NAME: report2snapshot
'
' AUTHOR: Alejandro Ricart
' DATE  : 21/05/2003
'
' Generacion de un archivo excel (xls) a partir de un txt
'
'==========================================================================

Const xlExcel9795 = 43

On Error Resume Next
Dim destino

Set args = Wscript.Arguments   
If args.Count < 2 Then
	WScript.Echo("Debe especificar el nombre del archivo txt y nombre destino")
	Wscript.Quit(1)
End If

destino=args(1)

Set excel = WScript.CreateObject("Excel.Application")
excel.Visible=False
excel.DisplayAlerts = False
Set libro = excel.WorkBooks.Open(args(0))
libro.WorkSheets(1).Name="Hoja 1"

libro.SaveAs destino,xlExcel9795

excel.DisplayAlerts = True
excel.Quit



'WScript.Echo("Archivo guardado como "+destino)

Set excel=Nothing

If Err.Number <> 0 Then
  WScript.Quit(1)
End If 