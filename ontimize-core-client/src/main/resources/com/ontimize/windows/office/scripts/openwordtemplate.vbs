'==========================================================================
'
' VBScript Source File --
'
' NAME: runapp
'
' AUTHOR: Alejandro Ricart
' DATE  : 22/12/2003
'
'
'==========================================================================



Dim archivo
Dim ficheroDatos
On Error Resume next
Set args = Wscript.Arguments
If args.Count < 2 Then
	WScript.Echo("Debe especificar el nombre del archivo word y path del fichero de datos")
	Wscript.Quit(1)
End If

archivo=args(0)
ficheroDatos = args(1)


Set word = WScript.CreateObject("Word.Application")

word.Documents.Open archivo

word.ActiveDocument.MailMerge.OpenDataSource ficheroDatos

word.ActiveDocument.MailMerge.Destination = 0

word.ActiveDocument.MailMerge.Execute

word.Documents(archivo).Visible= false

word.Documents(archivo).close 0

word.ActiveDocument.Visible= true

Set word = nothing



If Err.Number <> 0 Then

  WScript.Quit(1)

End If