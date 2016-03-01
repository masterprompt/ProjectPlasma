;============================
;EDITOROBJECT CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const EDITOROBJECT_MAX%=256

;============================
;GLOBALS
;============================
Global editorobject_ID.editorobject[EDITOROBJECT_MAX%];Primary Key Object Pointer
Global editorobject_Index%[EDITOROBJECT_MAX%], editorobject_IndexPointer%;Primary Key ID Management Stack
;Global EDITOR_ID.EDITOR[EDITOR_MAX%];Primary Key Object Pointer
;Global EDITOR_Index%[EDITOR_MAX%], EDITOR_IndexPointer%;Primary Key ID Management Stack

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type editorobject
	;Purpose: 
	;Properties:
	Field ID%;
	Field name$;
	Field properties%;
	Field propertyname$[256];
	Field propertyvalue$[256];
End Type

;============================
;METHODS
;============================
Function editorobjectStart()
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	For editorobjectloop=EDITOROBJECT_MAX To 1 Step -1; Initialize Primary Key ID Management Stack
		editorobjectIndexPush(editorobjectloop)
	Next
End Function

Function editorobjectStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.editorobject=Each editorobject
		editorobjectDelete(this)
	Next
End Function

Function editorobjectNew.editorobject()
	;Purpose: CONSTRUCTOR - Creates editorobject Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: editorobject Instance
	this.editorobject=New editorobject
	this\ID%=0
	this\name$=""
	this\properties%=0
	For editorobjectloop=1 To 256
		this\propertyname$[editorobjectloop]=""
	Next
	For editorobjectloop=1 To 256
		this\propertyvalue$[editorobjectloop]=""
	Next
	this\id%=editorobjectIndexPop()
	editorobject_ID[this\id%]=this
	Return this
End Function

Function editorobjectDelete(this.editorobject)
	;Purpose: DESTRUCTOR - Removes editorobject Instance
	;Parameters: editorobject Instance
	;Return: None
	editorobject_ID[this\id]=Null
	editorobjectIndexPush(this\id%)
	For editorobjectloop=1 To 256
		this\propertyvalue$[editorobjectloop]=""
	Next
	For editorobjectloop=1 To 256
		this\propertyname$[editorobjectloop]=""
	Next
	this\name$=""
	Delete this
End Function

Function editorobjectUpdate()
	;Purpose: Main Loop Update. Updates all editorobject Instances.
	;Parameters: None
	;Return: None
	For this.editorobject=Each editorobject
		;Do stuff to 'this' here!
	Next
End Function

Function editorobjectWrite(this.editorobject,editorobjectfile)
	;Purpose: Writes editorobject Property Values to File
	;Parameters: editorobject Instance, editorobjectfile=filehandle
	;Return: None
	WriteInt(editorobjectfile,this\ID%)
	WriteString(editorobjectfile,this\name$)
	WriteInt(editorobjectfile,this\properties%)
	For editorobjectloop=1 To 256
		WriteString(editorobjectfile,this\propertyname$[editorobjectloop])
	Next
	For editorobjectloop=1 To 256
		WriteString(editorobjectfile,this\propertyvalue$[editorobjectloop])
	Next
End Function

Function editorobjectRead.editorobject(editorobjectfile)
	;Purpose: Reads File To editorobject Property Values
	;Parameters: editorobjectfile=filehandle
	;Return: editorobject Instance
	this.editorobject=New editorobject
	this\ID%=ReadInt(editorobjectfile)
	this\name$=ReadString(editorobjectfile)
	this\properties%=ReadInt(editorobjectfile)
	For editorobjectloop=1 To 256
		this\propertyname$[editorobjectloop]=ReadString(editorobjectfile)
	Next
	For editorobjectloop=1 To 256
		this\propertyvalue$[editorobjectloop]=ReadString(editorobjectfile)
	Next
	editorobjectIndexPop()
	editorobject_ID[this\id%]=this
	Return this
End Function

Function editorobjectSave(editorobjectfilename$="editorobject.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: editorobjectfilename$= Name of File
	;Return: None
	editorobjectfile=WriteFile(editorobjectfilename$)
	For this.editorobject= Each editorobject
		editorobjectWrite(this,editorobjectfile)
	Next
	CloseFile(editorobjectfile)
End Function

Function editorobjectOpen(editorobjectfilename$="editorobject.dat")
	;Purpose: Opens a Object Property file
	;Parameters: editorobjectfilename$=Name of Property File
	;Return: None
	editorobjectfile=ReadFile(editorobjectfilename$)
	Repeat
		editorobjectRead(editorobjectfile)
	Until Eof(editorobjectfile)
	CloseFile(editorobjectfile)
End Function

Function editorobjectCopy.editorobject(this.editorobject)
	;Purpose: Creates a Copy of a editorobject Instance
	;Parameters: editorobject Instance
	;Return: Copy of editorobject Instance
	copy.editorobject=editorobjectNew()
	copy\ID%=this\ID%
	copy\name$=this\name$
	copy\properties%=this\properties%
	For editorobjectloop=1 To 256
		copy\propertyname$[editorobjectloop]=this\propertyname$[editorobjectloop]
	Next
	For editorobjectloop=1 To 256
		copy\propertyvalue$[editorobjectloop]=this\propertyvalue$[editorobjectloop]
	Next
	Return copy
End Function

Function editorobjectMimic(this.editorobject,mimic.editorobject)
	;Purpose: Copies Property Values from one editorobject Instance To another
	;Parameters: editorobject Instance
	;Return: None
	mimic\ID%=this\ID%
	mimic\name$=this\name$
	mimic\properties%=this\properties%
	For editorobjectloop=1 To 256
		mimic\propertyname$[editorobjectloop]=this\propertyname$[editorobjectloop]
	Next
	For editorobjectloop=1 To 256
		mimic\propertyvalue$[editorobjectloop]=this\propertyvalue$[editorobjectloop]
	Next
End Function

Function editorobjectIndexPush(editorobjectid%)
	;Purpose: Pushes ID into Primary Key ID Management Stack
	;Parameters: editorobjectid=Object's ID
	;Return: None
	editorobject_Index[editorobject_IndexPointer]=editorobjectid%
	editorobject_IndexPointer=editorobject_IndexPointer+1
End Function

Function editorobjectIndexPop()
	;Purpose: Pops ID out of Primary Key ID Management Stack
	;Parameters: None
	;Return: Object ID%
	editorobject_IndexPointer=editorobject_IndexPointer-1
	Return editorobject_Index[editorobject_IndexPointer]
End Function

Function editorObjectDatOpen()
End Function

Function editorObjectDatSave()
End Function

Function editorObjectPropertySet()
End Function

Function editorObjectPropertyGet()
End Function

Function editorImagemapGenerate()
End Function


Function editorObjectPropertyAdd()
End Function

Function editorObjectPropertyRemove()
End Function 

Function editorobjectMethod.editorobject(this.editorobject)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function



