;============================
;RECYCLER CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const RECYCLER_MAX%=256
Const RECYCLER_LIGHT_MAX=8

;============================
;GLOBALS
;============================
Global recycler_ID.recycler[RECYCLER_MAX%];Primary Key Object Pointer
Global recycler_Index%;Primary Key ID Management Counter
Global recycler_OmniLightID
Global recycler_SpotLightID

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type recycler
	;Purpose: Recycles entities during run-time use.
	;Properties:
	Field ID%;
	Field size%;
	Field PTR%;
	Field stack%;
End Type

;============================
;METHODS
;============================
Function recyclerStart()
	;Purpose: Initialize Class; Create Light Recyclers
	;Parameters: TBD
	;Return: None
	recycler_OmniLightID=recyclerCreate(RECYCLER_LIGHT_MAX)
	recyclerOmnilightCopy(recycler_ID[recycler_OmniLightID],RECYCLER_LIGHT_MAX)
	recycler_SpotLightID=recyclerCreate(RECYCLER_LIGHT_MAX)
	recyclerSpotlightCopy(recycler_ID[recycler_SpotLightID],RECYCLER_LIGHT_MAX)	
End Function

Function recyclerStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.recycler=Each recycler
		recyclerDelete(this)
	Next
End Function

Function recyclerNew.recycler()
	;Purpose: CONSTRUCTOR - Creates recycler Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: recycler Instance
	this.recycler=New recycler
	this\ID%=0
	this\size%=0
	this\PTR%=0
	this\stack%=0
	recycler_Index=recycler_Index+1
	this\id%=recycler_Index
	recycler_ID[this\id%]=this
	Return this
End Function

Function recyclerDelete(this.recycler)
	;Purpose: DESTRUCTOR - Removes recycler Instance
	;Parameters: recycler Instance
	;Return: None
	Delete this
End Function

Function recyclerUpdate()
	;Purpose: Main Loop Update. Updates all recycler Instances.
	;Parameters: None
	;Return: None
	For this.recycler=Each recycler
		;Do stuff to 'this' here!
	Next
End Function

Function recyclerWrite(this.recycler,recyclerfile)
	;Purpose: Writes recycler Property Values to File
	;Parameters: recycler Instance, recyclerfile=filehandle
	;Return: None
	WriteInt(recyclerfile,this\ID%)
	WriteInt(recyclerfile,this\size%)
	WriteInt(recyclerfile,this\PTR%)
	WriteInt(recyclerfile,this\stack%)
End Function

Function recyclerRead.recycler(recyclerfile)
	;Purpose: Reads File To recycler Property Values
	;Parameters: recyclerfile=filehandle
	;Return: recycler Instance
	this.recycler=New recycler
	this\ID%=ReadInt(recyclerfile)
	this\size%=ReadInt(recyclerfile)
	this\PTR%=ReadInt(recyclerfile)
	this\stack%=ReadInt(recyclerfile)
	recycler_ID[this\id%]=this
	Return this
End Function

Function recyclerSave(recyclerfilename$="recycler.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: recyclerfilename$= Name of File
	;Return: None
	recyclerfile=WriteFile(recyclerfilename$)
	For this.recycler= Each recycler
		recyclerWrite(this,recyclerfile)
	Next
	CloseFile(recyclerfile)
End Function

Function recyclerOpen(recyclerfilename$="recycler.dat")
	;Purpose: Opens a Object Property file
	;Parameters: recyclerfilename$=Name of Property File
	;Return: None
	recyclerfile=ReadFile(recyclerfilename$)
	Repeat
		recyclerRead(recyclerfile)
	Until Eof(recyclerfile)
	CloseFile(recyclerfile)
End Function

Function recyclerCopy.recycler(this.recycler)
	;Purpose: Creates a Copy of a recycler Instance
	;Parameters: recycler Instance
	;Return: Copy of recycler Instance
	copy.recycler=recyclerNew()
	copy\ID%=this\ID%
	copy\size%=this\size%
	copy\PTR%=this\PTR%
	copy\stack%=this\stack%
	Return copy
End Function

Function recyclerMimic(this.recycler,mimic.recycler)
	;Purpose: Copies Property Values from one recycler Instance To another
	;Parameters: recycler Instance
	;Return: None
	mimic\ID%=this\ID%
	mimic\size%=this\size%
	mimic\PTR%=this\PTR%
	mimic\stack%=this\stack%
End Function

Function recyclerCreate(recyclersize%)
	;Purpose: Creates an Object and returns it ID
	;Parameters: Object's Properties
	;Return: Object's ID%
	this.recycler=recyclerNew()
	this\size%=recyclersize%
	this\stack%=CreateBank(this\size%*4)
	Return this\id%
End Function

Function recyclerDestroy(recyclerid%)
	;Purpose: Remove object by ID
	;Parameters: recyclerid%=Object's ID
	;Return: None
	FreeBank(recycler_ID[recyclerid%]\stack) 
	recyclerDelete(recycler_ID[recyclerid%])
End Function

Function recyclerSet(this.recycler,recyclerID%,recyclerdatatype%,recyclersize%,recyclerPTR%,recyclerstack%)
	;Purpose: Set recycler Property Values
	;Parameters: recycler Instance and Properties
	;Return: None
	this\ID%=recyclerID%
	this\size%=recyclersize%
	this\PTR%=recyclerPTR%
	this\stack%=recyclerstack%
End Function

Function recyclerPush(this.recycler,recyclerentity%)
	;Purpose: Push value on to Recycler Stack
	;Parameters: TBD
	;Return: TBD
	HideEntity(recyclerentity%) 
	PokeInt(this\stack%,this\PTR%,recyclerentity%)
	this\PTR%=this\PTR%+4		
End Function

Function recyclerPop(this.recycler)
	;Purpose: Pops value off the Recycler Stack
	;Parameters: TBD
	;Return: TBD
	this\PTR%=this\PTR%-4
	recyclerentity%=PeekInt(this\stack%,this\PTR%)
	ShowEntity(recyclerentity%)
	Return recyclerentity%	
End Function

Function recyclerEntityCopy(this.recycler,recyclerentity%,recyclercopies%,recyclerentityradius#=0.0)
	;Purpose: Creates a number of entity copies
	;Parameters: TBD
	;Return: TBD
	For recyclerloop =  1 To recyclercopies%
		recyclercopy%=CopyEntity(recyclerentity%)
		If recyclerentityradius>0 EntityRadius(recyclercopy%,recyclerentityradius#)
		recyclerPush(this,recyclercopy%)
	Next 
End Function

Function recyclerOmniLightPush(this.recycler,recyclerentity%)
	;Purpose: Push value on to Recycler Stack
	;Parameters: TBD
	;Return: TBD
	HideEntity(recyclerentity%) 
	PokeInt(this\stack%,this\PTR%,recyclerentity%)
	this\PTR%=this\PTR%+4		
End Function

Function recyclerOmniLightPop(this.recycler)
	;Purpose: Pops value off the Recycler Stack
	;Parameters: TBD
	;Return: TBD
	this\PTR%=this\PTR%-4
	recyclerentity%=PeekInt(this\stack%,this\PTR%)
	ShowEntity(recyclerentity%)
	Return recyclerentity%	
End Function

Function recyclerOmniLightCopy(this.recycler,recyclercopies%=8)
	;Purpose: Creates a number of entity copies
	;Parameters: TBD
	;Return: TBD
	For recyclerloop = 1 To recyclercopies
		recyclerentity%=CreateLight(2)
		recyclerPush(this,recyclerentity%)
	Next 
End Function

Function recyclerSpotLightPush(this.recycler,recyclerentity%)
	;Purpose: Push value on to Recycler Stack
	;Parameters: TBD
	;Return: TBD
	HideEntity(recyclerentity%) 
	PokeInt(this\stack%,this\PTR%,recyclerentity%)
	this\PTR%=this\PTR%+4		
End Function

Function recyclerSpotLightPop(this.recycler)
	;Purpose: Pops value off the Recycler Stack
	;Parameters: TBD
	;Return: TBD
	this\PTR%=this\PTR%-4
	recyclerentity%=PeekInt(this\stack%,this\PTR%)
	ShowEntity(recyclerentity%)
	Return recyclerentity%	
End Function

Function recyclerSpotLightCopy(this.recycler,recyclercopies%=8)
	;Purpose: Creates a number of entity copies
	;Parameters: TBD
	;Return: TBD
	For recyclerloop = 1 To recyclercopies
		recyclerentity%=CreateLight(3)
		recyclerPush(this,recyclerentity%)
	Next 
End Function

Function recyclerAudioPush(this.recycler,recyclerchannel%)
	;Purpose: Push value on to Recycler Stack specifically for audio management
	;Parameters: TBD
	;Return: TBD
	PokeInt(this\stack%,this\PTR%,recyclerchannel%)
	this\PTR%=this\PTR%+4		
End Function

Function recyclerAudioPop(this.recycler)
	;Purpose: Pops value off the Recycler Stack
	;Parameters: TBD
	;Return: TBD
	this\PTR%=this\PTR%-4
	Return PeekInt(this\stack%,this\PTR%)
End Function

Function recyclerAudioCopy(this.recycler,recyclerfilename$,recyclercopies%)
	;Purpose: Creates a number of entity copies
	;Parameters: TBD
	;Return: TBD
	For recyclerloop = 1 To recyclercopies%
		recyclercopy%=Load3DSound(recyclerfilename$)
		recyclerAudioPush(this,recyclercopy%)
	Next 
End Function

Function recyclerMethod.recycler(this.recycler)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function