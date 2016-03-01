;TypeWriter IV CodeWizard v.091905 Applied Object-Based Programming with Blitz3D.
;Author: Frankie  L. Taylor
;Generates a set of object methods from a *.object type files.
;Uses the type name for filename, produces a *.class file

Graphics 800,600,16,2
title$="TypeWriter IV CodeWizard v.091905 Applied Object-Based Programming with Blitz3D"
AppTitle(title$) 

Const PROPERTY_MAX%=255
Const LINE_MAX%=255

Type property
	Field typeid%;1=byte, 2=integer, 4=Float, 8=String, 16=Type, 32=array
	Field name$	
	Field strip$[2]
	Field subtype$
	Field array%
	Field bytesize%
End Type

Type method ;Function
	Field typeid%
	Field name$
	Field parameters%
	Field parameter.property[PROPERTY_MAX%]
End Type

Type typeobject
	Field typeid%
	Field name$
	Field max%
	Field prefix$
	Field properties%
	Field property.property[PROPERTY_MAX%] ;field
	Field includefiles%
	Field includefilename$[PROPERTY_MAX%]
	Field bytesize%
	Field idexists
	Field recycle
End Type

Function typeWriter(typeobjectfilename$)
	If typeobjectfilename$="" typeobjectfilename$=Input("*.object filename (exclude extension) >")
	typeobjectfile=ReadFile(typeobjectfilename$+".object")
	If typeobjectfile
		
		While Not Eof(typeobjectfile)
			
			typeobjectdat$=ReadLine(typeobjectfile)
			
			If Left$(Lower(typeobjectdat$),5)="type "
				typeobject.typeobject=New typeobject
				typeobject\name$=Right$(typeobjectdat$,Len(typeobjectdat$)-5)
			EndIf
			
			If Left$(Lower(typeobjectdat$),6)="field " 
				typeobject\properties%=typeobject\properties+1
				property.property=New property
				typeobject\property.property[typeobject\properties%]=property
				typeobject\property[typeobject\properties%]\name$=Right$(typeobjectdat$,Len(typeobjectdat$)-6)
				If Lower(typeobject\property[typeobject\properties%]\name$)="id%" typeobject\idexists=True
				propertyDatatype(typeobject\property[typeobject\properties%])
				propertyStrip(typeobject\property[typeobject\properties%])				
			EndIf 
					
			If Left$(Lower(typeobjectdat$),4)="max=" 
				typeobject\max%=Right$(typeobjectdat$,Len(typeobjectdat$)-4)
			EndIf
			
			If Left$(Lower(typeobjectdat$),7)="prefix=" 
				typeobject\prefix$=Upper(Right$(typeobjectdat$,Len(typeobjectdat$)-7))+"_"
			EndIf
			
			If Left$(Lower(typeobjectdat$),8)="recycle=" ;1 or 0 
				typeobject\recycle=Right$(typeobjectdat$,Len(typeobjectdat$)-8)
			EndIf
						
		Wend
		CloseFile(typeobjectfile)
		
	Else
		;manual input
		Cls
		Locate 0,0
		Color 0,255,255
		Print "Fields require Data Type declarations:"
		Print "Examples: type.type, byte!, integer%, float#, string$, array%[n]"
		Print "Type 'end type' at the Field Prompt to Write Class"
		
		typeobject.typeobject=New typeobject ;first object is object
		
		Color 255,255,0
		typeobject\name$=Input("Type ")
		
		Restore propertydefaults
		For loop = 1 To 2
			Read propertydefault$
			If propertydefault$<>"end" typeobject\properties%=propertyInput(typeobject,propertydefault$)
		Next
		
		Repeat ;get fields
			typeobject\properties%=typeobject\properties+1
			typeobject\property.property[typeobject\properties%]=New property
			typeobject\property[typeobject\properties%]\name$=Input("Field ")
			propertyDatatype(typeobject\property[typeobject\properties%])
			propertyStrip(typeobject\property[typeobject\properties%])								
		Until Lower(typeobject\property[typeobject\properties%]\name$)="end type"
		typeobject\properties=typeobject\properties-1
		typeobject\max%=Input(Upper(typeobject\name$)+"_MAX%=")
	EndIf
	
	;get include files
	For typeobjectloop= 1 To typeobject\properties
		Select typeobject\property[typeobjectloop]\typeid
			Case 16,48
				includefileexists=False
				For typeobjectloop2 =  1 To typeobject\includefiles ;check for matches
					If typeobject\property[typeobjectloop]\subtype$=typeobject\includefilename[typeobjectloop2] 
						includefileexists=True
						Exit
					EndIf	
				Next
				If Not includefileexists ;add file to include file list
						typeobject\includefiles=typeobject\includefiles+1
						typeobject\includefilename[typeobject\includefiles]=typeobject\property[typeobjectloop]\subtype$
				EndIf
		End Select
	Next 	
	
	typeobjectModuleWrite(First typeobject)
	;typeobjectDump()
	Delete Each property
	Delete Each typeobject
		
End Function

Function typeobjectModuleWrite(this.typeobject)	
	;WRITE FILE
	typeobjectfile=WriteFile(this\name$+".class")
	If typeobjectfile
		Print "Writing "+this\name$+".class"	
		typeobjectDeclarationsWrite(this,typeobjectfile)
		typeobjectTypeobjectWrite(this,typeobjectfile)
		typeobjectStartWrite(this,typeobjectfile)
		typeobjectStopWrite(this,typeobjectfile)
		typeobjectNewWrite(this,typeobjectfile)
		typeobjectDeleteWrite(this,typeobjectfile)
		typeobjectUpdateWrite(this,typeobjectfile)
		typeobjectWriteWrite(this,typeobjectfile)
		typeobjectReadWrite(this,typeobjectfile)
		typeobjectSaveWrite(this,typeobjectfile)
		typeobjectOpenWrite(this,typeobjectfile)
		typeobjectCopyWrite(this,typeobjectfile)
		typeobjectMimicWrite(this,typeobjectfile)
		typeobjectCreateWrite(this,typeobjectfile)
		typeobjectDestroyWrite(this,typeobjectfile)
		typeobjectSetWrite(this,typeobjectfile)
		typeobjectIndexStackWrite(this,typeobjectfile)
		typeobjectMethodWrite(this,typeobjectfile)
		CloseFile(typeobjectfile)
		Print this\name$+".class written sucessfully!"		
	EndIf
End Function	

	
Function typeobjectDeclarationsWrite(this.typeobject,typeobjectfile)
	WriteLine(typeobjectfile,";============================")
	WriteLine(typeobjectfile,";"+Upper(this\name$+" Class"))
	WriteLine(typeobjectfile,";Generated with TypeWriterIV")
	WriteLine(typeobjectfile,";============================")
	
	;INCLUDES
	WriteLine(typeobjectfile,"")
	WriteLine(typeobjectfile,";============================")
	WriteLine(typeobjectfile,";INCLUDES")
	WriteLine(typeobjectfile,";============================")

	For typeobjectloop=  1 To this\includefiles
				WriteLine(typeobjectfile,"Include "+Chr(34)+this\includefilename$[typeobjectloop]+".class"+Chr(34))	
	Next 
	
	;CONST
	WriteLine(typeobjectfile,"")
	WriteLine(typeobjectfile,";============================")
	WriteLine(typeobjectfile,";CONSTANTS")
	WriteLine(typeobjectfile,";============================")
	WriteLine(typeobjectfile,"Const "+Upper(this\prefix$+this\name$)+"_MAX%="+Str(this\max%))
	
	;GLOBALS
	WriteLine(typeobjectfile,"")
	WriteLine(typeobjectfile,";============================")
	WriteLine(typeobjectfile,";GLOBALS")
	WriteLine(typeobjectfile,";============================")
	If this\max%>0 And this\idexists
		;ID
		WriteLine(typeobjectfile,"Global "+this\prefix$+this\name$+"_ID."+this\name$+"["+this\prefix$+Upper(this\name$)+"_MAX%];Primary Key Object Pointer")
		Select this\recycle
			Case 1 ;Index Stack
				WriteLine(typeobjectfile,"Global "+this\prefix$+this\name$+"_Index%["+this\prefix$+Upper(this\name$)+"_MAX%], "+this\prefix$+this\name$+"_IndexPointer%;Primary Key ID Management Stack") 
			Default ;Global Counter
				WriteLine(typeobjectfile,"Global "+this\prefix$+this\name$+"_Index%;Primary Key ID Management Counter")
		End Select	
	EndIf	

	;ARRAYS
	WriteLine(typeobjectfile,"")
	WriteLine(typeobjectfile,";============================")
	WriteLine(typeobjectfile,";ARRAYS")
	WriteLine(typeobjectfile,";============================")	
End Function		
	
Function typeobjectTypeobjectWrite(this.typeobject,typeobjectfile)
	WriteLine(typeobjectfile,"")
	WriteLine(typeobjectfile,";============================")
	WriteLine(typeobjectfile,";OBJECT")
	WriteLine(typeobjectfile,";============================")	
	WriteLine(typeobjectfile,"Type "+this\name$)
	WriteLine(typeobjectfile,"	;Purpose: ")
	WriteLine(typeobjectfile,"	;Properties:")
	For typeobjectloop = 1 To this\properties%
		WriteLine(typeobjectfile,"	Field "+this\property[typeobjectloop]\name$+";")
	Next
	WriteLine(typeobjectfile,"End Type")
	WriteLine(typeobjectfile,"")
End Function

Function typeobjectStartWrite(this.typeobject,typeobjectfile)
	WriteLine(typeobjectfile,";============================")
	WriteLine(typeobjectfile,";METHODS")
	WriteLine(typeobjectfile,";============================")	
	WriteLine(typeobjectfile,"Function "+this\prefix$+this\name$+"Start()")
	WriteLine(typeobjectfile,"	;Purpose: Initialize Class")
	WriteLine(typeobjectfile,"	;Parameters: TBD")
	WriteLine(typeobjectfile,"	;Return: None")
	If this\max%>0 And this\idexists=True And this\recycle=True
		WriteLine(typeobjectfile,"	For "+this\name$+"loop="+this\prefix$+Upper(this\name$)+"_MAX To 1 Step -1; Initialize Primary Key ID Management Stack")
		WriteLine(typeobjectfile,"		"+this\name$+"IndexPush("+this\name$+"loop)")
		WriteLine(typeobjectfile,"	Next")
	EndIf
	WriteLine(typeobjectfile,"End Function")
	WriteLine(typeobjectfile,"")
End Function 	
	
Function typeobjectStopWrite(this.typeobject,typeobjectfile)
	WriteLine(typeobjectfile,"Function "+this\prefix$+this\name$+"Stop()")
	WriteLine(typeobjectfile,"	;Purpose: Shutdown Class")
	WriteLine(typeobjectfile,"	;Parameters: TBD")
	WriteLine(typeobjectfile,"	;Return: None")	
	WriteLine(typeobjectfile,"	For this."+this\name$+"=Each "+this\name$)
	WriteLine(typeobjectfile,"		"+this\name$+"Delete(this)")
	WriteLine(typeobjectfile,"	Next")
	WriteLine(typeobjectfile,"End Function")
	WriteLine(typeobjectfile,"")
End Function
	
Function typeobjectNewWrite(this.typeobject,typeobjectfile)
	WriteLine(typeobjectfile,"Function "+this\prefix$+this\name$+"New."+this\name$+"()")
	WriteLine(typeobjectfile,"	;Purpose: CONSTRUCTOR - Creates "+this\name$+" Instance; Sets Default Property Values")
	WriteLine(typeobjectfile,"	;Parameters: TBD")
	WriteLine(typeobjectfile,"	;Return: "+this\name$+" Instance")	
	WriteLine(typeobjectfile,"	this."+this\name$+"=New "+this\name$)
	For typeobjectloop = 1 To this\properties%
		Select this\property[typeobjectloop]\typeid%
			Case 1,2 WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\name$+"=0")
			Case 4 WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\name$+"=0.0")
			Case 8 WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\name$+"="+Chr(34)+Chr(34))
			Case 16 WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\name$+"="+this\property[typeobjectloop]\subtype$+"New()")
			Case 33,34,36,40,48
				WriteLine(typeobjectfile,"	For "+this\name$+"loop=1 To "+Str(this\property[typeobjectloop]\array%))			
				Select this\property[typeobjectloop]\typeid%
					Case 33,34 WriteLine(typeobjectfile,"		this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]=0")
					Case 36 WriteLine(typeobjectfile,"		this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]=0.0")
					Case 40 WriteLine(typeobjectfile,"		this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]="+Chr(34)+Chr(34))
					Case 48 WriteLine(typeobjectfile,"		this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]="+this\property[typeobjectloop]\subtype$+"New()")
				End Select
				WriteLine(typeobjectfile,"	Next")
		End Select		
	Next
	If this\max%>0 And this\idexists
		If this\recycle
			WriteLine(typeobjectfile,"	this\id%="+this\prefix$+this\name$+"IndexPop()")
		Else
			WriteLine(typeobjectfile,"	"+this\prefix$+this\name$+"_Index="+this\prefix$+this\name$+"_Index+1")
			WriteLine(typeobjectfile,"	this\id%="+this\prefix$+this\name$+"_Index")
		EndIf
		WriteLine(typeobjectfile,"	"+this\prefix$+this\name$+"_ID[this\id%]=this")
	EndIf	
	WriteLine(typeobjectfile,"	Return this")
	WriteLine(typeobjectfile,"End Function")
	WriteLine(typeobjectfile,"")
End Function
	
Function typeobjectDeleteWrite(this.typeobject,typeobjectfile)
	WriteLine(typeobjectfile,"Function "+this\prefix$+this\name$+"Delete(this."+this\name$+")")
	WriteLine(typeobjectfile,"	;Purpose: DESTRUCTOR - Removes "+this\name$+" Instance")
	WriteLine(typeobjectfile,"	;Parameters: "+this\name$+" Instance")
	WriteLine(typeobjectfile,"	;Return: None")	
	If this\max%>0 And this\idexists=True And this\recycle=True
		WriteLine(typeobjectfile,"	"+this\prefix$+this\name$+"_ID[this\id]=Null")
		WriteLine(typeobjectfile,"	"+this\prefix$+this\name$+"IndexPush(this\id%)")
	EndIf
	For typeobjectloop = this\properties% To 1 Step -1
		Select this\property[typeobjectloop]\typeid%
			Case 1 WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\name$+"=0")	
			Case 2
				Select Lower(this\property[typeobjectloop]\strip$[1])
					Case "bank","brush","entity","font","image","sound","texture","timer"
						WriteLine(typeobjectfile,"	Free"+this\property[typeobjectloop]\strip$[1]+" this\"+this\property[typeobjectloop]\name$)
				End Select	
			Case 4 WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\name$+"=0.0")
			Case 8 WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\name$+"="+Chr(34)+Chr(34))
			Case 16 WriteLine(typeobjectfile,"	"+this\property[typeobjectloop]\subtype$+"Delete(this\"+this\property[typeobjectloop]\name$+")")
			Case 33,34,36,40,48
				WriteLine(typeobjectfile,"	For "+this\name$+"loop=1 To "+Str(this\property[typeobjectloop]\array%))			
				Select this\property[typeobjectloop]\typeid%
					Case 33,34 WriteLine(typeobjectfile,"		this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]=0")
					Case 36 WriteLine(typeobjectfile,"		this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]=0.0")
					Case 40 WriteLine(typeobjectfile,"		this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]="+Chr(34)+Chr(34))
					Case 48 WriteLine(typeobjectfile,"		"+this\property[typeobjectloop]\subtype$+"Delete(this\"+this\property[typeobjectloop]\strip$[2]+"["+this\name$+"loop])")
				End Select
				WriteLine(typeobjectfile,"	Next")
		End Select		
	Next
	WriteLine(typeobjectfile,"	Delete this")
	WriteLine(typeobjectfile,"End Function")
	WriteLine(typeobjectfile,"")
End Function
	
Function typeobjectUpdateWrite(this.typeobject,typeobjectfile)
	WriteLine(typeobjectfile,"Function "+this\prefix$+this\name$+"Update()")
	WriteLine(typeobjectfile,"	;Purpose: Main Loop Update. Updates all "+this\name$+" Instances.")
	WriteLine(typeobjectfile,"	;Parameters: None")
	WriteLine(typeobjectfile,"	;Return: None")	
	WriteLine(typeobjectfile,"	For this."+this\name$+"=Each "+this\name$)
	WriteLine(typeobjectfile,"		;Do stuff to 'this' here!")
	WriteLine(typeobjectfile,"	Next")
	WriteLine(typeobjectfile,"End Function")
	WriteLine(typeobjectfile,"")
End Function
	
Function typeobjectWriteWrite(this.typeobject,typeobjectfile)
	WriteLine(typeobjectfile,"Function "+this\prefix$+this\name$+"Write(this."+this\name$+","+this\name$+"file)")
	WriteLine(typeobjectfile,"	;Purpose: Writes "+this\name$+" Property Values to File")
	WriteLine(typeobjectfile,"	;Parameters: "+this\name$+" Instance, "+this\name$+"file=filehandle")
	WriteLine(typeobjectfile,"	;Return: None")	
	For typeobjectloop = 1 To this\properties
		Select this\property[typeobjectloop]\typeid%
			Case 1 WriteLine(typeobjectfile,"	WriteByte("+this\name$+"file,this\"+this\property[typeobjectloop]\name$+")")
			Case 2 WriteLine(typeobjectfile,"	WriteInt("+this\name$+"file,this\"+this\property[typeobjectloop]\name$+")")
			Case 4 WriteLine(typeobjectfile,"	WriteFloat("+this\name$+"file,this\"+this\property[typeobjectloop]\name$+")")
			Case 8 WriteLine(typeobjectfile,"	WriteString("+this\name$+"file,this\"+this\property[typeobjectloop]\name$+")")
			Case 16 WriteLine(typeobjectfile,"	"+this\property[typeobjectloop]\subtype$+"Write(this\"+this\property[typeobjectloop]\name$+","+this\name$+"file)")
			Case 33,34,36,40,48
				WriteLine(typeobjectfile,"	For "+this\name$+"loop=1 To "+Str(this\property[typeobjectloop]\array%))			
				Select this\property[typeobjectloop]\typeid%
					Case 33 WriteLine(typeobjectfile,"		WriteByte("+this\name$+"file,this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop])")
					Case 34 WriteLine(typeobjectfile,"		WriteInt("+this\name$+"file,this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop])")
					Case 36 WriteLine(typeobjectfile,"		WriteFloat("+this\name$+"file,this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop])")
					Case 40 WriteLine(typeobjectfile,"		WriteString("+this\name$+"file,this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop])")
					Case 48 WriteLine(typeobjectfile,"		"+this\property[typeobjectloop]\subtype$+"Write(this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop],"+this\name$+"file)")
				End Select
				WriteLine(typeobjectfile,"	Next")	
		End Select		
	Next
	WriteLine(typeobjectfile,"End Function")
	WriteLine(typeobjectfile,"")
End Function

Function typeobjectReadWrite(this.typeobject,typeobjectfile)
	WriteLine(typeobjectfile,"Function "+this\prefix$+this\name$+"Read."+this\name$+"("+this\name$+"file)")
	WriteLine(typeobjectfile,"	;Purpose: Reads File To "+this\name$+" Property Values")
	WriteLine(typeobjectfile,"	;Parameters: "+this\name$+"file=filehandle")
	WriteLine(typeobjectfile,"	;Return: "+this\name$+" Instance")		
	WriteLine(typeobjectfile,"	this."+this\name$+"=New "+this\name$)
	For typeobjectloop = 1 To this\properties
		Select this\property[typeobjectloop]\typeid%
			Case 1 WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\name$+"=ReadByte("+this\name$+"file)")
			Case 2 WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\name$+"=ReadInt("+this\name$+"file)")
			Case 4 WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\name$+"=ReadFloat("+this\name$+"file)")
			Case 8 WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\name$+"=ReadString("+this\name$+"file)")
			Case 16 WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\name$+"="+this\property[typeobjectloop]\subtype$+"Read("+this\name$+"file)")
			Case 33,34,36,40,48
				WriteLine(typeobjectfile,"	For "+this\name$+"loop=1 To "+Str(this\property[typeobjectloop]\array%))
				Select this\property[typeobjectloop]\typeid%
					Case 33 WriteLine(typeobjectfile,"		this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]=ReadByte("+this\name$+"file)")
					Case 34 WriteLine(typeobjectfile,"		this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]=ReadInt("+this\name$+"file)")
					Case 36 WriteLine(typeobjectfile,"		this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]=ReadFloat("+this\name$+"file)")
					Case 40 WriteLine(typeobjectfile,"		this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]=ReadString("+this\name$+"file)")
					Case 48 WriteLine(typeobjectfile,"		this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]="+this\property[typeobjectloop]\subtype$+"Read("+this\name$+"file)")
				End Select		
				WriteLine(typeobjectfile,"	Next")					
		End Select		
	Next
	If this\max%>0 And this\idexists
		If this\recycle WriteLine(typeobjectfile,"	"+this\prefix$+this\name$+"IndexPop()")
		WriteLine(typeobjectfile,"	"+this\prefix$+this\name$+"_ID[this\id%]=this")		
	EndIf	
	WriteLine(typeobjectfile,"	Return this")
	WriteLine(typeobjectfile,"End Function")
	WriteLine(typeobjectfile,"")
End Function
	
Function typeobjectSaveWrite(this.typeobject,typeobjectfile)
	WriteLine(typeobjectfile,"Function "+this\prefix$+this\name$+"Save("+this\name$+"filename$="+Chr(34)+this\name$+".dat"+Chr(34)+")")
	WriteLine(typeobjectfile,"	;Purpose: Saves all Object Properties to a file")
	WriteLine(typeobjectfile,"	;Parameters: "+this\name$+"filename$= Name of File")
	WriteLine(typeobjectfile,"	;Return: None")	
	WriteLine(typeobjectfile,"	"+this\name$+"file=WriteFile("+this\name$+"filename$)")
	WriteLine(typeobjectfile,"	For this."+this\name$+"= Each "+this\name$)
	WriteLine(typeobjectfile,"		"+this\name$+"Write(this,"+this\name$+"file)")
	WriteLine(typeobjectfile,"	Next")
	WriteLine(typeobjectfile,"	CloseFile("+this\name$+"file)")
	WriteLine(typeobjectfile,"End Function")
	WriteLine(typeobjectfile,"")
End Function	
	
Function typeobjectOpenWrite(this.typeobject,typeobjectfile)
	WriteLine(typeobjectfile,"Function "+this\prefix$+this\name$+"Open("+this\name$+"filename$="+Chr(34)+this\name$+".dat"+Chr(34)+")")
	WriteLine(typeobjectfile,"	;Purpose: Opens a Object Property file")
	WriteLine(typeobjectfile,"	;Parameters: "+this\name$+"filename$=Name of Property File")
	WriteLine(typeobjectfile,"	;Return: None")	
	WriteLine(typeobjectfile,"	"+this\name$+"file=ReadFile("+this\name$+"filename$)")
	WriteLine(typeobjectfile,"	Repeat")
	WriteLine(typeobjectfile,"		"+this\name$+"Read("+this\name$+"file)")
	WriteLine(typeobjectfile,"	Until Eof("+this\name$+"file)")
	WriteLine(typeobjectfile,"	CloseFile("+this\name$+"file)")
	WriteLine(typeobjectfile,"End Function")
	WriteLine(typeobjectfile,"")
End Function	

Function typeobjectCopyWrite(this.typeobject,typeobjectfile)
	WriteLine(typeobjectfile,"Function "+this\prefix$+this\name$+"Copy."+this\name$+"(this."+this\name$+")")
	WriteLine(typeobjectfile,"	;Purpose: Creates a Copy of a "+this\name$+" Instance")
	WriteLine(typeobjectfile,"	;Parameters: "+this\name$+" Instance")
	WriteLine(typeobjectfile,"	;Return: Copy of "+this\name$+" Instance")	
	WriteLine(typeobjectfile,"	copy."+this\name$+"="+this\name$+"New()")
	For typeobjectloop = 1 To this\properties
		If this\property[typeobjectloop]\name%<>"id%"
			Select this\property[typeobjectloop]\typeid%
				Case 1,2,4,8 WriteLine(typeobjectfile,"	copy\"+this\property[typeobjectloop]\name$+"=this\"+this\property[typeobjectloop]\name$)
				Case 16 WriteLine(typeobjectfile,"	copy\"+this\property[typeobjectloop]\name$+"="+this\property[typeobjectloop]\subtype$+"Copy(this\"+this\property[typeobjectloop]\name$+")")
				Case 33,34,36,40,48
					WriteLine(typeobjectfile,"	For "+this\name$+"loop=1 To "+Str(this\property[typeobjectloop]\array%))	
					Select this\property[typeobjectloop]\typeid%
						Case 33 WriteLine(typeobjectfile,"		copy\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]=this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]")
						Case 34,36,40 WriteLine(typeobjectfile,"		copy\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]=this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]")
						Case 48 WriteLine(typeobjectfile,"		copy\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]="+this\property[typeobjectloop]\subtype$+"Copy(this\"+this\property[typeobjectloop]\strip$[2]+"["+this\name$+"loop])")
					End Select
					WriteLine(typeobjectfile,"	Next")	
			End Select
		EndIf
	Next
	WriteLine(typeobjectfile,"	Return copy")
	WriteLine(typeobjectfile,"End Function")
	WriteLine(typeobjectfile,"")
End Function	
	
Function typeobjectMimicWrite(this.typeobject,typeobjectfile)
	WriteLine(typeobjectfile,"Function "+this\prefix$+this\name$+"Mimic(this."+this\name$+",mimic."+this\name$+")")
	WriteLine(typeobjectfile,"	;Purpose: Copies Property Values from one "+this\name$+" Instance To another")
	WriteLine(typeobjectfile,"	;Parameters: "+this\name$+" Instance")
	WriteLine(typeobjectfile,"	;Return: None")	
	For typeobjectloop = 1 To this\properties
		If this\property[typeobjectloop]\name%<>"id%"
			Select this\property[typeobjectloop]\typeid%
				Case 1,2,4,8 WriteLine(typeobjectfile,"	mimic\"+this\property[typeobjectloop]\name$+"=this\"+this\property[typeobjectloop]\name$)
				Case 16 WriteLine(typeobjectfile,"	"+this\property[typeobjectloop]\subtype$+"Mimic(this\"+this\property[typeobjectloop]\name$+",mimic\"+this\property[typeobjectloop]\name$+")")
				Case 33,34,36,40,48		
					WriteLine(typeobjectfile,"	For "+this\name$+"loop=1 To "+Str(this\property[typeobjectloop]\array%))	
					Select this\property[typeobjectloop]\typeid%
						Case 33 WriteLine(typeobjectfile,"		mimic\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]=this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]")
						Case 34,36,40 WriteLine(typeobjectfile,"		mimic\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]=this\"+this\property[typeobjectloop]\strip$[1]+"["+this\name$+"loop]")
						Case 48 WriteLine(typeobjectfile,"		"+this\property[typeobjectloop]\subtype$+"Mimic(this\"+this\property[typeobjectloop]\strip$[2]+"["+this\name$+"loop],mimic\"+this\property[typeobjectloop]\strip$[2]+"["+this\name$+"loop])")
					End Select				
					WriteLine(typeobjectfile,"	Next")
			End Select
		EndIf		
	Next
	WriteLine(typeobjectfile,"End Function")
	WriteLine(typeobjectfile,"")
End Function

Function typeobjectCreateWrite(this.typeobject,typeobjectfile)
	If this\max%>0 And this\idexists 
	WriteLine(typeobjectfile,"Function "+this\prefix$+this\name$+"Create%("+typeobjectParameters$(this)+")")
	WriteLine(typeobjectfile,"	;Purpose: Creates an Object and returns it ID")
	WriteLine(typeobjectfile,"	;Parameters: Object's Properties")
	WriteLine(typeobjectfile,"	;Return: Object's ID%")	
	WriteLine(typeobjectfile,"	this."+this\name$+"="+this\name$+"New()")
	For typeobjectloop = 1 To this\properties
		If this\property[typeobjectloop]\name%<>"id%"
			Select this\property[typeobjectloop]\typeid%
				Case 1,2,4,8,16 WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\name$+"="+this\name$+this\property[typeobjectloop]\name$)
				Case 33,34,36,40,48	
					For typeobjectloop2 =  1 To this\property[typeobjectloop]\array%
						Select this\property[typeobjectloop]\typeid%
							Case 33,34
								WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\strip$[1]+"["+Str(typeobjectloop2)+"]="+typeobjectstripper(this\property[typeobjectloop]\strip$[1],"%")+Str(typeobjectloop2)+"%")
							Case 36
								WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\strip$[1]+"["+Str(typeobjectloop2)+"]="+typeobjectstripper(this\property[typeobjectloop]\strip$[1],"#")+Str(typeobjectloop2)+"#")
							Case 40	
								WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\strip$[1]+"["+Str(typeobjectloop2)+"]="+typeobjectstripper(this\property[typeobjectloop]\strip$[1],"$")+Str(typeobjectloop2)+"$")
							Case 48
								WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\strip$[1]+"["+Str(typeobjectloop2)+"]="+this\property[typeobjectloop]\strip$[2]+Str(typeobjectloop2)+"."+this\property[typeobjectloop]\subtype$)
						End Select		
						Next				
				End Select	
		EndIf			
	Next
	WriteLine(typeobjectfile,"	Return this\id")
	WriteLine(typeobjectfile,"End Function")
	WriteLine(typeobjectfile,"")
	EndIf
End Function

Function typeobjectDestroyWrite(this.typeobject,typeobjectfile)
	If this\max%>0 And this\idexists
	WriteLine(typeobjectfile,"Function "+this\prefix$+this\name$+"Destroy("+this\name$+"id%)")
	WriteLine(typeobjectfile,"	;Purpose: Remove object by ID")
	WriteLine(typeobjectfile,"	;Parameters: "+this\name$+"id%=Object's ID")
	WriteLine(typeobjectfile,"	;Return: None")
	WriteLine(typeobjectfile,"	"+this\prefix+this\name$+"Delete("+this\prefix+this\name$+"_ID["+this\name$+"id%])")
	WriteLine(typeobjectfile,"End Function")
	WriteLine(typeobjectfile,"")	
	EndIf
End Function
	
Function typeobjectSetWrite(this.typeobject,typeobjectfile)
	WriteLine(typeobjectfile,"Function "+this\prefix$+this\name$+"Set(this."+this\name$+","+typeobjectParameters$(this)+")")
	WriteLine(typeobjectfile,"	;Purpose: Set "+this\name$+" Property Values")
	WriteLine(typeobjectfile,"	;Parameters: "+this\name$+" Instance and Properties")
	WriteLine(typeobjectfile,"	;Return: None")	
	For typeobjectloop = 1 To this\properties
		If this\property[typeobjectloop]\name%<>"id%"
			Select this\property[typeobjectloop]\typeid%
				Case 1,2,4,8,16 WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\name$+"="+this\name$+this\property[typeobjectloop]\name$)
				Case 33,34,36,40,48 
					For typeobjectloop2 = 1 To this\property[typeobjectloop]\array%
						Select this\property[typeobjectloop]\typeid%
							Case 33,34
								WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\strip$[1]+"["+Str(typeobjectloop2)+"]="+typeobjectstripper(this\property[typeobjectloop]\strip$[1],"%")+Str(typeobjectloop2)+"%")
							Case 36
								WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\strip$[1]+"["+Str(typeobjectloop2)+"]="+typeobjectstripper(this\property[typeobjectloop]\strip$[1],"#")+Str(typeobjectloop2)+"#")
							Case 40	
								WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\strip$[1]+"["+Str(typeobjectloop2)+"]="+typeobjectstripper(this\property[typeobjectloop]\strip$[1],"$")+Str(typeobjectloop2)+"$")
							Case 48
								WriteLine(typeobjectfile,"	this\"+this\property[typeobjectloop]\strip$[1]+"["+Str(typeobjectloop2)+"]="+this\property[typeobjectloop]\strip$[2]+Str(typeobjectloop2)+"."+this\property[typeobjectloop]\subtype$)
						End Select		
					Next				
			End Select	
		EndIf		
	Next
	WriteLine(typeobjectfile,"End Function")
	WriteLine(typeobjectfile,"")
End Function

Function typeobjectIndexStackWrite(this.typeobject,typeobjectfile)
	If this\max>0 And this\idexists=True And this\recycle=True
		WriteLine(typeobjectfile,"Function "+this\prefix$+this\name$+"IndexPush("+this\name$+"id%)")
		WriteLine(typeobjectfile,"	;Purpose: Pushes ID into Primary Key ID Management Stack")
		WriteLine(typeobjectfile,"	;Parameters: "+this\name$+"id=Object's ID")
		WriteLine(typeobjectfile,"	;Return: None")	
		WriteLine(typeobjectfile,"	"+this\prefix$+this\name$+"_Index["+this\prefix$+this\name$+"_IndexPointer]="+this\name$+"id%")
		WriteLine(typeobjectfile,"	"+this\prefix$+this\name$+"_IndexPointer="+this\prefix$+this\name$+"_IndexPointer+1")
		WriteLine(typeobjectfile,"End Function")
		WriteLine(typeobjectfile,"")
		WriteLine(typeobjectfile,"Function "+this\prefix$+this\name$+"IndexPop()")
		WriteLine(typeobjectfile,"	;Purpose: Pops ID out of Primary Key ID Management Stack")
		WriteLine(typeobjectfile,"	;Parameters: None")
		WriteLine(typeobjectfile,"	;Return: Object ID%")	
		WriteLine(typeobjectfile,"	"+this\prefix$+this\name$+"_IndexPointer="+this\prefix$+this\name$+"_IndexPointer-1")
		WriteLine(typeobjectfile,"	Return "+this\prefix$+this\name$+"_Index["+this\prefix$+this\name$+"_IndexPointer]")		
		WriteLine(typeobjectfile,"End Function")
		WriteLine(typeobjectfile,"")	
	EndIf
End Function

Function typeobjectMethodWrite(this.typeobject,typeobjectfile)
	WriteLine(typeobjectfile,"Function "+this\prefix$+this\name$+"Method."+this\name$+"(this."+this\name$+")")
	WriteLine(typeobjectfile,"	;Purpose: Method Template")
	WriteLine(typeobjectfile,"	;Parameters: TBD")
	WriteLine(typeobjectfile,"	;Return: TBD")
	WriteLine(typeobjectfile,"	;code stuff here...")		
	WriteLine(typeobjectfile,"	Return this")
	WriteLine(typeobjectfile,"End Function")
	WriteLine(typeobjectfile,"")
End Function

Function typeobjectParameters$(this.typeobject)
	For typeobjectloop=1 To this\properties%
		If this\property[typeobjectloop]\name%<>"id%"
			Select this\property[typeobjectloop]\typeid%		
				Case 1,2,4,8 typeobjectpars$=typeobjectpars$+this\name$+this\property[typeobjectloop]\name$  
				Case 16  typeobjectpars$=typeobjectpars$+this\name$+this\property[typeobjectloop]\name$
				Case 33,34,36,40,48
					For typeobjectloop2 = 1 To this\property[typeobjectloop]\array%
						Select this\property[typeobjectloop]\typeid%
							Case 33,34
								typeobjectpars$=typeobjectpars$+this\property[typeobjectloop]\strip$[2]+Str(typeobjectloop2)+"%"
							Case 36
								typeobjectpars$=typeobjectpars$+this\property[typeobjectloop]\strip$[2]+Str(typeobjectloop2)+"#"						
							Case 40						
								typeobjectpars$=typeobjectpars$+this\property[typeobjectloop]\strip$[2]+Str(typeobjectloop2)+"$"					
							Case 48
								typeobjectpars$=typeobjectpars$+this\property[typeobjectloop]\strip$[2]+Str(typeobjectloop2)+"."+this\property[typeobjectloop]\subtype$
						End Select
						If typeobjectloop2<this\property[typeobjectloop]\array% typeobjectpars$=typeobjectpars$+","					
					Next		
					
				End Select
			If typeobjectloop<this\properties% typeobjectpars$=typeobjectpars$+","
		EndIf	
	Next
	Return typeobjectpars$
End Function 

Function typeobjectDump()
	For this.typeobject = Each typeobject
		DebugLog "Type="+this\name$
		DebugLog "Typeid="+Str(this\typeid%)
		DebugLog "Max="+Str(this\max%)
		DebugLog "Properties="+Str(this\properties%)
		For typeobjectloop = 1 To Str(this\properties%)
			DebugLog "	Property["+Str(typeobjectloop)+"]"
			DebugLog "	Name="+this\property[typeobjectloop]\name$ ;field			
			DebugLog "	typeid="+Str(this\property[typeobjectloop]\typeid) ;field			
			DebugLog "	Strip="+this\property[typeobjectloop]\strip$[1]
			DebugLog "	Subtype="+Str(this\property[typeobjectloop]\Subtype)
			DebugLog "	Array="+Str(this\property[typeobjectloop]\Array)
		Next	
		;DebugLog bytesize%		
		DebugLog "----------------------------------------------------------------------------------"
	Next
End Function 

Function typeobjectInclude(typeobjectfilename$)
	typeobjectfile=ReadFile(typeobjectfilename$+".object")

	If typeobjectfile
		this.typeobject=New typeobject
		this\name$=filename$
		While Not Eof(typeobjectfile)
			typeobjectdat$=ReadLine(typeobjectfile)
			If Lower$(Left$(typeobjectdat$,6))="field "
				this\properties%=this\properties%+1
				this\property.property[this\properties%]=New property
				this\property[this\properties%]\name$=Right$(typeobjectdat$,Len(typeobjectdat$)-6)
				propertyDatatype(this\property[this\properties%])
				propertyStrip(this\property[this\properties%])
			EndIf	
		Wend
		CloseFile(typeobjectfile) 
	EndIf
End Function

Function typeobjectstripper$(typeobjecttxt$,typeobjectchar$,typeobjectadditive%=1) 
	;use typeobjectstripper$("apple.apple",".")
	;this function will remove all character after "." to include "."
	;the additive value can be use to strip more characters 
	;After "." (ie +1) Or less characters Before "." (ie -1). 
	;A value of 0 will strip To the "."
	For typeobjectloop=Len(typeobjecttxt$) To 1 Step -1
		If Mid(typeobjecttxt$,typeobjectloop,1)=typeobjectchar$ Return Left(typeobjecttxt$,typeobjectloop-typeobjectadditive%)
	Next	
End Function

Function propertyInput(typeobject.typeobject,propertyname$)
	If 	Input(Lower("Field "+propertyname$+" y/n?"))="y"
		typeobject\properties%=typeobject\properties+1
		typeobject\property.property[typeobject\properties%]=New property
		typeobject\property[typeobject\properties%]\name$=propertyname$
		propertyDatatype(typeobject\property[typeobject\properties%])
		propertyStrip(typeobject\property[typeobject\properties%])			
	EndIf
End Function

Function propertyStrip(this.property)
	Select this\typeid%
		Case 2 this\strip$[1]=typeobjectstripper(this\name$,"%")
		Case 4 this\strip$[1]=typeobjectstripper(this\name$,"#")
		Case 8 this\strip$[1]=typeobjectstripper(this\name$,"$")
		Case 16 this\strip$[1]=typeobjectstripper(this\name$,".")
		Case 33,34,36,40,48 
			this\strip$[1]=typeobjectstripper(this\name$,"[");array
			Select this\typeid%
				Case 33 this\strip$[2]=typeobjectstripper(this\strip$[1],"!")
				Case 34 this\strip$[2]=typeobjectstripper(this\strip$[1],"%")
				Case 36 this\strip$[2]=typeobjectstripper(this\strip$[1],"#")
				Case 40 this\strip$[2]=typeobjectstripper(this\strip$[1],"$")
				Case 48	this\strip$[2]=typeobjectstripper(this\strip$[1],".");type
			End Select
	End Select
End Function

Function propertyDatatype(this.property)
	For propertyloop=Len(this\name$) To 1 Step -1
		char$=Mid(this\name$,propertyloop,1)
		If propertygetelements% elements$=char$+elements$		
		Select char$
			Case "]" ;array
				propertygetelements%=True 
			Case "[" ;array
				this\typeid%=32			 
				;get element value
				this\array%=Right(elements$,Len(elements$)-1)
				propertygetelements%=False								
			Case "." ;type
				this\typeid%=this\typeid%+16
				If this\array
					this\subtype$=typeobjectstripper$(Right(this\name$,Len(this\name$)-propertyloop),"[")
				Else
					this\subtype$=Right(this\name$,Len(this\name$)-propertyloop)
				EndIf
				;If no typeobject created for this subtype, create one
				For typeobject.typeobject = Each typeobject
					If typeobject\name$=this\subtype$ typeobjectcreated%=True					
				Next
				If Not typeobjectcreated% typeobjectInclude(this\subtype$)
				Return 	
			Case "$" ;string
				this\typeid%=this\typeid%+8
				Return
			Case "#" ;float	
				this\typeid%=this\typeid%+4			
			Case "%" ;integer
				this\typeid%=this\typeid%+2
				Return			
			Case "!" ;byte
				this\typeid%=this\typeid%+1
				this\name%=Replace(this\name,"!","%")
				Return
		End Select
	Next
End Function

.propertydefaults
Data "id%","typeid%","end" ;parentid%, entity%, state%, actionid%