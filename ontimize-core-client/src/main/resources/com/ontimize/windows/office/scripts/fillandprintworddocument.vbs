'==========================================================================
'
' VBScript Source File --
'
' NAME:
'
' AUTHOR: Alejandro Ricart
' DATE  : 02/10/2003
'
' COMMENT: Abre un documento de word y lo imprime. El primer parámetro será el nombre del
' fichero. Los siguientes seran de 2 en 2 interpretados como el texto a substituir y el texto nuevo.
'
'==========================================================================

Option Explicit

' Parametros pasados al script

Dim args
Dim nombrePlantilla
Dim word
Dim rango
Const wdReplaceAll = 2
Const wdFindStop = 0
Const wdDoNotSaveChanges = 0

Dim i
Dim a1
Dim a2
On Error Resume Next

Set args = Wscript.Arguments
If args.Count < 1 Then
	WScript.Echo("Debe especificar el nombre de la plantilla")
	Wscript.Quit(1)
End If

nombrePlantilla = args(0)

Set word = WScript.CreateObject("Word.Application")
'word.Visible = True

word.Documents.Open(nombrePlantilla)
word.Options.PrintBackground = False
Set rango = word.ActiveDocument.Content

For i = 1 To args.Count-2 Step 2
	a1 = args(i)
	a2 = args(i+1)
        'wscript.echo("Sustituyendo " & a1 & " por " & a2)
	rango.Find.Execute a1,True,True,False,False,False,True,False,False, a2,wdReplaceAll
next



'word.ActiveDocument.PrintOut()
'word.Quit(wdDoNotSaveChanges)

word.ActiveDocument.PrintPreview()
word.Visible = True



Set word=Nothing




