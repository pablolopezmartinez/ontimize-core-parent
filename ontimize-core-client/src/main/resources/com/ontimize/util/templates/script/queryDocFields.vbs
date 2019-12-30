option explicit
' all variables must be declared with DIM option
' in this way we can not have a mistake writing a variable name 

Dim args

Dim operations
Dim string_operations
Dim current_operation

Dim templateName
Dim outputDataFields
Dim outputTableFields

Dim word
Dim doc
Dim debug
Dim i

On Error Resume Next

'Input parameters
'operations : String with all operations information(Options: DEBUG,NOTHING,FIELDS,TABLES)
	

Set args = WScript.Arguments
If args.Count < 2 Then
	WScript.Echo("Must specified the operations information and template name")
	WScript.Quit(1)
End If


debug = False
hide = False


'First place open the word document
templateName =args(1)
Set word = WScript.CreateObject("Word.Application")
word.Documents.Open (templateName)
Set doc=word.ActiveDocument

string_operations = args(0)
operations = Split(string_operations ,";")


For i = 0 To UBound(operations) Step 1
	current_operation = operations(i)	
	If current_operation = "DEBUG" Then
		debug = True
		word.visible=true
	End If
	
	If current_operation = "NOTHING" Then
		If (debug) Then
			MsgBox "No operation to execute..."
		End If
	ElseIf current_operation = "FIELDS" Then
		If (debug) Then
			MsgBox "Query the fields"	
		End If
		Call QueryFields (args(2),debug)
	ElseIf current_operation = "TABLES" Then
		If (debug) Then
			MsgBox "Query tables"
		End If
		Call QueryTables(args(3),doc,debug)
	ElseIf current_operation = "HIDE" Then
		If (debug) Then		
			MsgBox "Options:  HIDE"	
		End If
		hide = True
	End If
Next 
 
word.Quit

'If Err.Number <> 0 Then
' MsgBox "Error: "& Err.Number &" Description:"& Err.Description 
' WScript.Quit(1)
'End If 



'************************************************************************************************************
'
'Query all fields (markers) in the word document
'
'*************************************************************************************************************

Sub QueryFields(outputFileName,debugI)
	
Dim marker
Dim fields
Dim i
Dim count


count = doc.Bookmarks.Count


For i = 1 To count Step 1
    fields = fields & doc.Bookmarks.Item(i).Name 
    If (i<count) Then
    	fields = fields & "#"
    End If
Next

If (debug) Then		
			MsgBox "WriteFile: " + outputFileName	
	End If	
Call WriteFile (fields,outputFileName)

End Sub



Sub WriteFile(strText,outputFileName)

Dim objFSO, objFolder, objShell, objTextFile, objFile


' Create the File System Object
Set objFSO = CreateObject("Scripting.FileSystemObject")
Set objFile = objFSO.CreateTextFile(outputFileName)
objFile.Close   


' OpenTextFile Method needs a Const value
' ForAppending = 8 ForReading = 1, ForWriting = 2

Const ForAppending = 8

Set objTextFile = objFSO.OpenTextFile(outputFileName, ForAppending, True)

' Writes strText every time you run this VBScript
objTextFile.WriteLine(strText)
objTextFile.Close

' Bonus or cosmetic section to launch explorer to check file
'If err.number = vbEmpty then
'   Set objShell = CreateObject("WScript.Shell")
'   objShell.run ("Explorer" &" " & strDirectory & "\" )
'Else WScript.echo "VBScript Error: " & err.number
'End If

End Sub




