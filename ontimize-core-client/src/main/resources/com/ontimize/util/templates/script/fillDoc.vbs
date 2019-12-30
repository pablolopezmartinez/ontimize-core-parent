option explicit
' with this option is required to declare the variables with DIM before use them.
' In this way we avoid to write wrong a name and create a new variable doing this 


Dim args

Dim operations
Dim string_operations
Dim actualoperation

Dim templateName
Dim dataFileName
Dim nombreEntidad
Dim nombreFicheroConfig
Dim nombreOrigenDatos
Dim nombreConfigImages
Dim tabla
Dim namesC
Dim word
Dim doc
Dim rowInsertNumber
Dim debug
Dim hide
Dim deleteBookmarks
Dim i

'Dim closeDocument

On Error Resume Next


'Output parameters
'	operations : String that describes the operations to execute in the script(Options: DEBUG, HIDE, KEEPBOOKMARKS, NOTHING, FIELDS, TABLES, IMAGES, PIVOTTABLES)
	

Set args = WScript.Arguments
If args.Count < 2 Then
	WScript.Echo("You need to specify the operations to execute and the template file name")
	WScript.Quit(1)
End If



'If (args.Count>2) Then
'	closeDocument = args(2)
'Else
'	closeDocument = false
'End If

debug = False
hide = False
deleteBookmarks = True


'First of all open the word application
templateName =args(1)
Set word = WScript.CreateObject("Word.Application")
word.Documents.Open (templateName)
Set doc=word.ActiveDocument
OptimizePerformance doc


string_operations = args(0)
operations = Split(string_operations ,";")


For i = 0 To UBound(operations) Step 1
	actualoperation = operations(i)	
	If actualoperation = "DEBUG" Then
		debug = True
		word.visible=true
	End If
	
	If actualoperation = "KEEPBOOKMARKS" Then
		deleteBookmarks = False
	End If
	
	If actualoperation = "NOTHING" Then
		If (debug) Then
			MsgBox "There is any operation to execute..."
		End If
	ElseIf actualoperation = "FIELDS" Then
		If (debug) Then
			MsgBox "Fill template fields with data file " & args(2)	
		End If
		Call FillField (args(2),doc,debug,false)
	ElseIf actualoperation = "FIELDSUNDOHIGHLIGHT" Then
		If (debug) Then
			MsgBox "Fill template fields with data file " & args(2)	
		End If
		Call FillField (args(2),doc,debug, True)
	ElseIf actualoperation = "TABLES" Then
		If (debug) Then
			MsgBox "Fill tables specified in file "	& args(3)
		End If
		Call FillTable(args(3),doc,debug)
	ElseIf actualoperation = "IMAGES" Then
		If (debug) Then		
			MsgBox "Fill the images using file " & args(4)	
		End If
		Call FillImages(args(4),doc,debug)
	ElseIf actualoperation = "PIVOTTABLES" Then
		If (debug) Then		
			MsgBox "Fill the pivot tables specified in file " & args(5)	
		End If
		Call FillPivotTable(args(5),doc,debug)
	ElseIf actualoperation = "HIDE" Then
		If (debug) Then		
			MsgBox "Options:  HIDE"	
		End If
		hide = True
	End If
Next 
 
UpdateALL (doc)

If (debug = False And hide = False) Then
	word.visible= True	
End If

For i = 0 To doc.TablesOfContents.count Step 1 
	doc.TablesOfContents.item(i).UpdatePageNumbers
next

doc.save

If (hide) Then
	word.Quit
End If

If (debug) Then 
	MsgBox "Template ready"
End If

'If Err.Number <> 0 Then
  'MsgBox "Error: "& Err.Number &" Description:"& Err.Description 
' WScript.Quit(1)
'End If 


'*************************************************************************************************************
'
' Update all the fields  in the Word document (headers and footers included). 
' Useful when the same field exist more than once and template uses references
'
'*************************************************************************************************************
Sub UpdateALL(doc)
	
	Dim counter
    
    doc.Fields.Update
    For i = 1 To doc.Sections.Count Step 1
      doc.Sections(i).Headers(1).Range.Fields.Update
      doc.Sections(i).Footers(1).Range.Fields.Update
    Next
    
    if (deleteBookmarks) then 
    	counter = doc.Bookmarks.Count
       	For i = counter To 1 Step -1
      		doc.Bookmarks.Item(i).Delete
    	Next
    End If
End Sub

'************************************************************************************************************
'
'Fill the text fields with bookmarks in a word document
'
'*************************************************************************************************************

Sub FillField(nombreOrigenDatos,vDoc,debugI,undoHighlight)
	
Const ForReading = 1, ForWriting = 2, ForAppending = 3
Dim  fs,f, ts, s

Dim clave
Dim valor
Dim marker
Dim leer
Dim trozos
Dim i
Dim bmRange

Set fs = CreateObject("Scripting.FileSystemObject")
Set f = fs.GetFile(nombreOrigenDatos)
Set ts = f.OpenAsTextStream(ForReading, -2) 


leer = ts.ReadAll
trozos = Split(leer, "$#")

For i = 0 To UBound(trozos) Step 2
	'MsgBox "Indice " & i & "/" & UBound(trozos) 
    clave = trozos(i)
    valor = trozos(i + 1)
	 If (doc.Bookmarks.Exists(clave)) Then
	 	'MsgBox "Bookmark: " & clave & " with DATA: " & valor & "END : " & Len(valor)
	    Set marker = doc.Bookmarks(clave)
	    
	    'MsgBox "Delete data"
	    If Len(valor)> 0 Then
	    	If (undoHighlight) Then
	    		marker.Range.HighlightColorIndex = 0
	    	End If
	    	Set bmRange = marker.Range
	    	bmRange.Text = valor
	    	doc.Bookmarks.Add clave, bmRange 
	    	'marker.range.Text = valor
	    Else 
	    	marker.range.Delete
	    End If	
      	'marker.Delete
      	'MsgBox "Insert field value"
	ElseIf (debugI=true) Then
	    MsgBox "Bookmark not found: " & clave
     End If
Next
ts.Close
End Sub

'*************************************************************************************************************
'
'Rellena las tablas del documento de Word
'
'*************************************************************************************************************

Sub FillTable(nombreFicheroConfig,vDoc,debugI)
	Const ForReading = 1
	Dim  fs,f, ts, s
	Dim dataFileName,entityName
	Dim tableMarker,tabla,rowInsertNumber,namesC,leer,trozos
	Set fs = CreateObject("Scripting.FileSystemObject")
	Set f = fs.GetFile(nombreFicheroConfig)
	Set ts = f.OpenAsTextStream(ForReading, -2) 
	
	
	Do While ts.AtEndOfStream <> True
		leer = ts.ReadLine
		'MsgBox "Leo una linea: " & leer  	
        trozos = Split(leer, "$")
        dataFileName = trozos(0)
		entityName=trozos(1)
		If (debugI) Then 
			MsgBox "Searching bookmark : " & entityName & " with data file : " & dataFileName
		End If
        If (vDoc.Bookmarks.Exists(entityName)) Then
        	Set tableMarker = vDoc.Bookmarks(entityName)  
			Set tabla = tableMarker.range.Tables.Item(1)
			If (IsNull (tabla) Or Tabla is nothing) Then
				WScript.Echo("Table not found")
			Else
				rowInsertNumber = GetNumberRowMarker(tabla, tableMarker)
				If (debugI) Then 
					MsgBox "Row with the marker is " & rowInsertNumber
				End If
				
				If (IsNull (rowInsertNumber) Or rowInsertNumber=-1) = false Then
					namesC = columnas(tabla,rowInsertNumber,debugI)
					If (debugI) Then
							Msgbox "FillTable: Get table data " & entityName & " from file: " & dataFileName
					End If
					Call OpenFile(dataFileName , namesC, tabla,rowInsertNumber,debugI)	
					'MsgBox "Fill table finish"
				End If
			End If
			
   		ElseIf (debugI) Then
			WScript.Echo("Bookmark not found")
		End If
		'MsgBox "Before read a new line"
	Loop
	ts.Close
End Sub

'*****************************************************************************************************************
'
'Rellena las tablas del documento de Word pivotadas
'
'*****************************************************************************************************************

Sub FillPivotTable(nombreFicheroConfig, vDoc, debugI)
    Const ForReading = 1
    Dim fs, f, ts, s
    Dim nombreFicheroDatos, nombreEntidad
    Dim tableMarker, tabla, rowInsertNumber, colInsertNumber, namesR, namesC, leer, trozos
    Set fs = CreateObject("Scripting.FileSystemObject")
    
    Set f = fs.GetFile(nombreFicheroConfig)
    Set ts = f.OpenAsTextStream(ForReading, -2)
    
    
    Do While ts.AtEndOfStream <> True
        leer = ts.ReadLine
        'MsgBox "Leo una linea: " & leer
        trozos = Split(leer, "$")
        nombreFicheroDatos = trozos(0)
        nombreEntidad = trozos(1)
        If (debugI) Then
            MsgBox "Searching for bookmark : " & nombreEntidad & " with data file: " & nombreFicheroDatos
        End If
        If (vDoc.Bookmarks.Exists(nombreEntidad)) Then
     
            Set tableMarker = vDoc.Bookmarks(nombreEntidad)
            Set tabla = tableMarker.Range.Tables.Item(1)
            If (IsNull(tabla) Or tabla Is Nothing) Then
                WScript.Echo ("Table not found")
            Else
                 colInsertNumber = GetNumberColsMarker(tabla, tableMarker)
                  
                If (debugI) Then
                    MsgBox "Column with specified marker is " & colInsertNumber
                End If
                If (IsNull(colInsertNumber) Or colInsertNumber = -1) = False Then
                    namesR = filas(tabla, colInsertNumber, debugI)
                    If (debugI) Then
                            MsgBox "FillTable: Obtenemos los datos para la tabla " & nombreEntidad & " del fichero: " & nombreFicheroDatos
                    End If
                    Call OpenFileCols(nombreFicheroDatos, namesR, tabla, colInsertNumber, debugI)
                    'MsgBox "Table completed"
                End If
            End If
            
        ElseIf (debugI) Then
            WScript.Echo ("Bookmark not found")
        End If
      
    Loop
    ts.Close
End Sub



'****************************************************************************************************************
'
'Fill the images in the Word document
'
'*****************************************************************************************************************


Sub FillImages(nombreConfigImages,doc,debugI)
	Const ForReading = 1
	Dim  fs,f, ts, s
	
	Dim leer,trozos
	Dim imageName
	Dim imageFile
	Dim imageMarker
	Dim bcrange
	Dim picture	
	
	Dim oldPicture
	Dim alto
	Dim ancho
	
	
	Set fs = CreateObject("Scripting.FileSystemObject")
	Set f = fs.GetFile(nombreConfigImages)
	Set ts = f.OpenAsTextStream(ForReading, -2) 

	Do While ts.AtEndOfStream <> True
		leer = ts.ReadLine
	    trozos = Split(leer, "$")
        imageName = trozos(1)
		imageFile=trozos(0)
	
		If (doc.Bookmarks.Exists(imageName)) Then
			Set imageMarker = doc.Bookmarks(imageName)
			Set bcrange = imageMarker.Range
			alto = trozos(3)
			ancho = trozos(2)
			'Primero miro si hay alguna en su sitio
			
			If (bcrange.InlineShapes.Count = 1) Then
				Set oldPicture = bcrange.InlineShapes.Item(1)
				 ancho = oldPicture.Width
     			 alto = oldPicture.Height
     			 oldPicture.Delete
     		Else
     			imageMarker.range.Delete
     		End If

			'MsgBox "Ancho :" & ancho & " Alto: " & alto
			Set picture = Doc.InlineShapes.AddPicture(imageFile,False,True,bcrange)
			picture.Height= alto
			picture.Width = ancho
   		ElseIf (debugI) Then
   			WScript.Echo("No se ha encontrado el bookmark")  		
		End If
	Loop
End Sub



Sub OpenFile(nameFile, columnas, tabla, rowNumber,debug)
    Const ForReading = 1, ForWriting = 2, ForAppending = 3
    Dim fs, f, ts, s
    Dim col 
    Dim leer 
    Dim trozos
    Dim datos
    Dim dimensionar
    Dim i
    dimensionar= False
    
   	Set fs = CreateObject("Scripting.FileSystemObject")
   	Set f = fs.GetFile(nameFile)
    Set ts = f.OpenAsTextStream(ForReading, -2) 
    
    If ts.AtEndOfStream <> True Then
    'Do While ts.AtEndOfStream <> True
        'leer = ts.ReadLine
        leer = ts.ReadAll
        trozos = Split(leer, "$")
      
     	For i=0 To UBound(trozos)-1 step 2
       		'MsgBox "Indice: " & i &"/" & UBound(trozos)
     		If (debug) Then
     			MsgBox "COLUMN NAME: " & trozos(i) & vbCrLf & " DATA: "& trozos(i+1)
     		End If
	        col = GetColumn(columnas, trozos(i))
    	    If (col >= 0) Then
        	    datos = Split(trozos(i+1), "#")
	    	If (dimensionar= false) Then
	   	 		Call DimensionarTabla(tabla, datos,rowNumber)
				dimensionar=true
	     	End If
            	Call InsertRow(tabla, col, datos,rowNumber)
        	End If
        Next
    'Loop
    End If
    ts.Close
    'MsgBox "OpenFile FIN"
 End Sub
 
 
 Sub OpenFileCols(nameFile, filas, tabla, ColNumber,debug)
    Const ForReading = 1, ForWriting = 2, ForAppending = 3
    Dim fs, f, ts, s
    Dim row
    Dim leer
    Dim trozos
    Dim datos
    Dim dimensionar
    Dim i
    dimensionar = False
    Set fs = CreateObject("Scripting.FileSystemObject")
    Set f = fs.GetFile(nameFile)
    Set ts = f.OpenAsTextStream(ForReading, -2)
    
    'Do While ts.AtEndOfStream <> True
        'leer = ts.ReadLine
        leer = ts.ReadAll
        trozos = Split(leer, "$")
      
        For i = 0 To UBound(trozos) - 1 Step 2
            'MsgBox "Indice: " & i &"/" & UBound(trozos)
            If (debug) Then
                MsgBox "ROW NAME: " & trozos(i) & vbCrLf & " DATA: " & trozos(i + 1)
            End If
            row = GetRow(filas, trozos(i))
            'MsgBox "fila " & row
            If (row >= 0) Then
                datos = Split(trozos(i + 1), "#")
            If (dimensionar = False) Then
                Call DimensionarTablaCols(tabla, datos, colNumber)
                dimensionar = True
            End If
                Call InsertCol(tabla, row, datos, ColNumber)
            End If
        Next
    'Loop
    ts.Close
    'MsgBox "OpenFile FIN"
 End Sub

Sub InsertRow(tabla , col , datos ,numRow)
    Dim s
    Dim j 
    j = numRow
    For Each s In datos
        'MsgBox "Insertar en: " & j  & col
        tabla.Cell(j, col).range.Text = datos(j - numRow)
        j = j + 1
    Next
 End Sub

'Insert a new column
 Sub InsertCol(tabla, row, datos, numCol)
    Dim s
    Dim j
    j = numCol
    For Each s In datos
        'MsgBox "Insertar en: "
        tabla.Cell(row, j).Range.Text = datos(j - numCol)
        j = j + 1
    Next
 End Sub

 Sub DimensionarTabla(tabla , datos,rowNumber)
    Dim filas, filasnecesarias 
    Dim rowSelected 
    Dim i

    filasnecesarias = UBound(datos)
    filas = tabla.Rows.Count
  
    Set rowSelected = tabla.Rows.Item(rowNumber)	
    rowSelected.Select()

    For i = 1 To filasnecesarias
		tabla.Rows.Add(rowSelected)
    Next

 End Sub

Sub DimensionarTablaCols(tabla, datos, colNumber)
    Dim columnas, columnasnecesarias
    Dim  colSelected
    Dim i

    columnasnecesarias = UBound(datos)
    columnas = tabla.Columns.Count
  
    Set colSelected = tabla.Columns.Item(colNumber)
    colSelected.Select()

    For i = 1 To columnasnecesarias
        tabla.Columns.Add (colSelected)
    Next

 End Sub
 

 Function GetNumberRowMarker(tabla, marker)
    Dim numeroFilas
    numeroFilas = tabla.Rows.Count
    Dim i
    Dim k
    Dim rowSelected

    For k = 1 To numeroFilas
		Set rowSelected = tabla.Rows.Item(k)
		if (rowSelected.Range.InRange(marker.Range) ) Then
			GetNumberRowMarker = k
			marker.Delete
			Exit Function
		End If
		If (marker.Range.InRange(rowSelected.Range))Then
	   		GetNumberRowMarker = k
	   		marker.Delete
	   		Exit Function
		End If
    Next
    MsgBox "Fin de GetNumberRowMarker sin exito"	
    GetNumberRowMarker = -1
 End Function

Function GetNumberColsMarker(tabla, marker)
    Dim numeroColumnas, cellsNumber
    numeroColumnas = tabla.Columns.Count
    Dim i
    Dim k
    Dim n
    Dim columnSelected
    Dim currentCell

    For k = 1 To numeroColumnas
        Set columnSelected = tabla.Columns.Item(k)
        
        cellsNumber = columnSelected.Cells.Count
        For n = 1 To cellsNumber
        	Set currentCell = columnSelected.Cells.Item(n)
        	If (currentCell.Range.InRange(marker.Range)) Then
        		GetNumberColsMarker = k
            	marker.Delete
            	Exit Function
        	End If
        Next
    Next
    MsgBox "Fin de GetNumberColsMarker sin exito"
    GetNumberColsMarker = -1
 End Function
 
 Function GetColumn(columnas,name)
    Dim size
    Dim i

    size=Ubound(columnas)
    If (size = 0) Then
        GetColumn = -1
        Exit Function
    End If
    
    For i = 0 To size
        If (columnas(i) = name) Then
            GetColumn = i+1
            Exit Function
        End If      
    Next 
  
    GetColumn = -1
 End Function


'Get the row position
Function GetRow(filas, name)
    Dim size
    Dim i

    size = UBound(filas)
    	'MsgBox size
    If (size = 0) Then
        GetRow = -1
        Exit Function
    End If
    For i = 0 To size
        If (filas(i) = name) Then
            GetRow = i + 1
            Exit Function
        End If
    Next
    GetRow = -1
 End Function

Function columnas(tabla,row,debug) 
   	Dim col
 	Dim texto
	Dim datos
	Dim i
   
    col = tabla.Columns.Count
	Dim name
	Dim lista
    
    For i = 1 To col
        texto = tabla.Cell(row, i).range.Text
        Dim lon
        lon = Len(texto) - 2
        texto = Mid(texto, 1, lon)
       	name= name + texto + "$"
       	tabla.Cell(row, i).range.Text = ""
    Next
    If (debug) Then
    	MsgBox "Column names : " & name
    End if
	lista = split(name,"$")
   	columnas = lista
End Function

' devuelve las filas de la tabla

Function filas(tabla,col,debug)
    Dim fila
    Dim texto
    Dim datos
    Dim i
   
    fila = tabla.Rows.Count
    'MsgBox "Numero de columnas " & col
    Dim name
    Dim lista
    
    For i = 1 To fila
        texto = tabla.Cell(i, col).Range.Text
        
        Dim lon
        lon = Len(texto) - 2
        texto = Mid(texto, 1, lon)
        name = name + texto + "$"
        tabla.Cell(i, col).Range.Text = ""
    Next
    If (debug) Then
        MsgBox "Row names : " & name
    End If
    lista = Split(name, "$")
    filas = lista
End Function


Sub OptimizePerformance(doc)
    doc.Windows(1).View = wdNormalView
    With doc.Application
        .Options.Pagination = False
        .ScreenUpdating = False
    End With
End Sub


Sub ResetOptimizePerformance(doc)
    doc.Windows(1).View = wdNormalView
    With doc.Application
        .Options.Pagination = True
        .ScreenUpdating = True
    End With
End Sub
