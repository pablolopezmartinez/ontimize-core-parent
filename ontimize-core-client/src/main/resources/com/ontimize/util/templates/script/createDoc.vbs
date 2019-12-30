Option explicit


Dim args
Dim templateName
Dim word
Dim doc
Dim string_operations
Dim operations
Dim actualoperation
Dim debug
Dim hide
Dim i
Dim abookmark
Dim highlight

On Error Resume Next


'Input paramters
'	operations : String that describes the operations to execute in the script(Options: DEBUG,NOTHING,FIELDS,FIELDSWITHOUTLABEL,TABLES,IMAGES)


Set args = WScript.Arguments
If args.Count < 2 Then
	WScript.Echo("It is neccessary to specify the operations to execute and the template name")
	WScript.Quit(1)
End If

debug = False
hide = False
highlight = False

templateName =args(1)
Set word = WScript.CreateObject("Word.Application")
'word.Documents.Open (templateName)
word.Documents.Add


Set doc=word.ActiveDocument

'OptimizePerformance doc


string_operations = args(0)
operations = Split(string_operations ,";")


For i = 0 To UBound(operations) Step 1
	actualoperation = operations(i)	
	If actualoperation = "DEBUG" Then
		debug = True
		word.visible= True
	End If
	
	If actualoperation = "NOTHING" Then
		If (debug) Then
			MsgBox "There is not operations to execute..."
		End If
	ElseIf actualoperation = "FIELDS" Then
		If (debug) Then
			MsgBox "Create the fields with the data file " & args(2)	
		End If
		Call CreateField (args(2),doc,debug,True)
	ElseIf actualoperation = "FIELDSWITHOUTLABEL" Then
		If (debug) Then
			MsgBox "Create fields without labels using the file " & args(2)	
		End If
		Call CreateField (args(2),doc,debug,false)
	ElseIf actualoperation = "TABLES" Then
		If (debug) Then
			MsgBox "Create the tables defined in the file "	& args(3)
		End If
		Call CreateTableField(args(3),word,doc,debug)
	ElseIf actualoperation = "IMAGES" Then
		If (debug) Then		
			MsgBox "Create the images defined in the file " & args(4)	
		End If
		'Call FillImages(args(4),doc,debug)
	ElseIf actualoperation = "HIDE" Then
		If (debug) Then		
			MsgBox "Options:  HIDE"	
		End If
		hide = True
	ElseIf actualoperation = "HIGHLIGHT" Then
		If (debug) Then		
			MsgBox "Options:  HIGHLIGHT"	
		End If
		highlight = True
	End If
Next 

If (highlight) Then
	For Each abookmark In doc.Bookmarks
		'1 Black, 2 Blue, 15 Dark Gray, 16 Light gray, 7 Yellow, 14 Olive
    	abookmark.Range.HighlightColorIndex = 16
	Next
End If 


If (hide=false) Then
	word.visible= True
End If

If Err.Number <> 0 Then
	MsgBox "Error: "& Err.Number &" Description:"& Err.Description  & Err.Source & Err.HelpFile & Err.HelpContext
 WScript.Quit(1)
End If 
'doc.save
doc.SaveAs templateName, 0 


'************************************************************************************************************
'
'Create fields for the word document( This is a text file with the next format attr1$#Label1$#attr2$#Label2$#attr3$#Label3$#attr4$#Label4...)
'
'*************************************************************************************************************

Sub CreateField(nombreOrigenDatos,vDoc,debugI,insertNames)
	
Const ForReading = 1, ForWriting = 2, ForAppending = 3
Dim  fs,f, ts, s

Dim bookmarkName
Dim texto
Dim rango
Dim marker
Dim leer
Dim trozos
Dim i
Dim k
Dim groupTitle
Dim groupText
Dim groupTrozos
Dim groupCount
Dim useGroups
Dim defaultFontSize

Set fs = CreateObject("Scripting.FileSystemObject")
Set f = fs.GetFile(nombreOrigenDatos)
Set ts = f.OpenAsTextStream(ForReading, -2) 


leer = ts.ReadAll
'MsgBox "Los campos son " & leer
groupTrozos = Split(leer, "%#")

groupCount = UBound(groupTrozos) 


If groupCount = 0 Then
	useGroups = false
	groupCount = 1
Else
	useGroups = True
End If

For k = 0 To groupCount Step 2
	
	If (useGroups) Then
		groupTitle = groupTrozos(k)
		groupText = groupTrozos(k+1)
		Set rango = doc.Range(doc.Paragraphs.Last.Range.End - 1,doc.Paragraphs.Last.Range.End)
		rango.Text = groupTitle
		rango.Bold=1
		defaultFontSize = rango.Font.Size 
		rango.Font.Size = defaultFontSize + 4
		doc.Paragraphs.add
		Set rango = doc.Range(doc.Paragraphs.Last.Range.End - 1,doc.Paragraphs.Last.Range.End)
		rango.Bold=0
		rango.Font.Size = defaultFontSize
		'MsgBox "Grupo " & groupTitle
		'MsgBox "Texto " & groupText
	Else
		groupText = leer
	End If
     
        	
	trozos = Split(groupText, "$#")

	For i = 0 To UBound(trozos) Step 2
	
    	bookmarkName = trozos(i)
     	texto = trozos(i+1)
     
     	If (insertNames) Then		
			doc.Content.InsertAfter texto & ": "	
	 	End If
     
     	Set rango = doc.Range(doc.Paragraphs.Last.Range.End - 1,doc.Paragraphs.Last.Range.End)
     	rango.Text = texto
     	Set marker = doc.Bookmarks.Add(bookmarkName, rango) 
     
    	'marker.Range.HighlightColorIndex = 7
     	'marker.Range.Text =  bookmarkName   
     	doc.Paragraphs.add
    Next
Next
ts.Close
End Sub


'************************************************************************************************************
'
'Creamos las tablas de un documento de Word( El formato del fichero tiene que ser Bookmark$#columna1$#NombreColumna1$#columna2$#NombreColumna2$#columna3#NombreColumna3
'
'*************************************************************************************************************

Sub CreateTableField(nombreOrigenDatos, word ,vDoc,debugI)
	
Const ForReading = 1, ForWriting = 2, ForAppending = 3, END_OF_STORY = 6
Dim  fs,f, ts, s

Dim bookmarkName
Dim texto
Dim columna
Dim rango
Dim marker
Dim leer
Dim trozos
Dim tabla
Dim i

Set fs = CreateObject("Scripting.FileSystemObject")
Set f = fs.GetFile(nombreOrigenDatos)
Set ts = f.OpenAsTextStream(ForReading, -2) 


Do While ts.AtEndOfStream <> True
	leer = ts.ReadLine
		'leer = ts.ReadAll
	trozos = Split(leer, "$#")

	If UBound(trozos) > 0 Then
	bookmarkName = trozos(0)
	For i = 1 To UBound(trozos)-1 Step 2
	     columna = trozos(i)
    	 texto = trozos(i+1)
     	If i = 1 Then
     		'Esto inserta la tabla
     		'Primero lo pongo en la última posicion
     		word.selection.EndKey END_OF_STORY
     		word.selection.TypeParagraph
     		Set rango = word.selection.Range
     		'MsgBox ((UBound(trozos)-1) / 2)
       		Set tabla = vDoc.Tables.Add(rango, 2, ((UBound(trozos)-1) / 2))
     		'fin inserta la tabla
     	End If
     	tabla.Cell(1, ((i-1) / 2)+1).Range.Text = texto 
     	tabla.Cell(2, ((i-1) / 2)+1).Range.Text = columna
	Next

	If (debugI) Then 
		MsgBox "Nombre del boolmark a insertar: " & bookmarkName
	End If
	Set marker = vDoc.Bookmarks.Add(bookmarkName, tabla.Rows.Item(2).Range)
	End If
Loop
ts.Close
End Sub



Sub OptimizePerformance(doc)
    doc.Windows(1).View = wdNormalView
    doc.Application.Options.Pagination = False
    doc.Application.ScreenUpdating = False
End Sub


Sub ResetOptimizePerformance(doc)
    doc.Windows(1).View = wdNormalView
    With doc.Application
        .Options.Pagination = True
        .ScreenUpdating = True
    End With
End Sub