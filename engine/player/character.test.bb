;============================
;CHARACTER CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const CHARACTER_MAX%=1

;============================
;GLOBALS
;============================
Global character_ID.character[CHARACTER_MAX%];Primary Key Object Pointer
Global character_Index%;Primary Key ID Management Counter

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type character
	;Purpose: 
	;Properties:
	Field ID%;
	Field weaponID%;
	Field recyclerID;
	Field copies%
	Field filename$;
	Field entity%;
End Type

;============================
;METHODS
;============================
Function characterStart(characterfilename$="character.dat")
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	characterOpen(characterfilename$)
	For this.character=Each character
		this\entity%=LoadAnimMesh(this\filename$)
		HideEntity(this\entity%)
		;create recycler and store object key
		this\recyclerID%=recyclerCreate(this\copies%)
		;create recycler entity copies for stack push/pop 
		recyclerEntityCopy(recycler_ID[this\recyclerID%],this\entity%,this\copies%)
	Next	
End Function

Function characterStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.character=Each character
		characterDelete(this)
	Next
End Function

Function characterNew.character()
	;Purpose: CONSTRUCTOR - Creates character Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: character Instance
	this.character=New character
	this\ID%=0
	this\weaponID%=0
	this\recyclerID%=0	
	this\copies%=0
	this\filename$=""
	this\entity%=0
	character_Index=character_Index+1
	this\id%=character_Index
	character_ID[this\id%]=this
	Return this
End Function

Function characterDelete(this.character)
	;Purpose: DESTRUCTOR - Removes character Instance
	;Parameters: character Instance
	;Return: None
	Freeentity this\entity%
	this\filename$=""
	Delete this
End Function

Function characterUpdate()
	;Purpose: Main Loop Update. Updates all character Instances.
	;Parameters: None
	;Return: None
	For this.character=Each character
		;Do stuff to 'this' here!
	Next
End Function

Function characterWrite(this.character,characterfile)
	;Purpose: Writes character Property Values to File
	;Parameters: character Instance, characterfile=filehandle
	;Return: None
	WriteInt(characterfile,this\ID%)
	WriteInt(characterfile,this\weaponID%)
	WriteInt(characterfile,this\recyclerID%)
	WriteInt(characterfile,this\copies%)
	WriteString(characterfile,this\filename$)
	WriteInt(characterfile,this\entity%)
End Function

Function characterRead.character(characterfile)
	;Purpose: Reads File To character Property Values
	;Parameters: characterfile=filehandle
	;Return: character Instance
	this.character=New character
	this\ID%=ReadInt(characterfile)
	this\weaponID%=ReadInt(characterfile)
	this\recyclerID%=ReadInt(characterfile)	
	this\copies%=ReadInt(characterfile)
	this\filename$=ReadString(characterfile)
	this\entity%=ReadInt(characterfile)
	character_ID[this\id%]=this
	Return this
End Function

Function characterSave(characterfilename$="character.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: characterfilename$= Name of File
	;Return: None
	characterfile=WriteFile(characterfilename$)
	For this.character= Each character
		characterWrite(this,characterfile)
	Next
	CloseFile(characterfile)
End Function

Function characterOpen(characterfilename$="character.dat")
	;Purpose: Opens a Object Property file
	;Parameters: characterfilename$=Name of Property File
	;Return: None
	characterfile=ReadFile(characterfilename$)
	Repeat
		characterRead(characterfile)
	Until Eof(characterfile)
	CloseFile(characterfile)
End Function

Function characterCopy.character(this.character)
	;Purpose: Creates a Copy of a character Instance
	;Parameters: character Instance
	;Return: Copy of character Instance
	copy.character=characterNew()
	copy\ID%=this\ID%
	copy\weaponID%=this\weaponID%
	copy\recyclerID%=this\recyclerID%	
	copy\copies%=this\copies%		
	copy\filename$=this\filename$
	copy\entity%=this\entity%
	Return copy
End Function

Function characterMimic(this.character,mimic.character)
	;Purpose: Copies Property Values from one character Instance To another
	;Parameters: character Instance
	;Return: None
	mimic\ID%=this\ID%
	mimic\weaponID%=this\weaponID%
	mimic\recyclerID%=this\recyclerID%	
	mimic\copies%=this\copies%	
	mimic\filename$=this\filename$
	mimic\entity%=this\entity%
End Function

Function characterCreate%(characterID%,characterweaponID%,characterrecyclerID%,charactercopies%,characterfilename$,characterentity%)
	;Purpose: Creates an Object and returns it ID
	;Parameters: Object's Properties
	;Return: Object's ID%
	this.character=characterNew()
	this\ID%=characterID%
	this\weaponID%=characterweaponID%
	this\recyclerID%=characterrecyclerID%	
	this\copies%=charactercopies%	
	this\filename$=characterfilename$
	this\entity%=characterentity%
	Return this\id
End Function

Function characterDestroy(characterid%)
	;Purpose: Remove object by ID
	;Parameters: characterid%=Object's ID
	;Return: None
	characterDelete(character_ID[characterid%])
End Function

Function characterSet(this.character,characterID%,characterweaponID%,characterrecyclerID%,charactercopies%,characterfilename$,characterentity%)
	;Purpose: Set character Property Values
	;Parameters: character Instance and Properties
	;Return: None
	this\ID%=characterID%
	this\weaponID%=characterweaponID%
	this\recyclerID%=characterrecyclerID%	
	this\copies%=charactercopies%	
	this\filename$=characterfilename$
	this\entity%=characterentity%
End Function

Function characterMethod.character(this.character)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function

Function characterDefaultDatWrite()
	;Purpose: Creates default character dat file
	;Parameters: TBD
	;Return: character Instance
	this.character= characterNew()
	this\weaponID%=1
	this\copies%=PLAYER_MAX%	
	this\filename$="../game/data/3d/players/redbron/redbron.b3d"
	characterSave()
End Function

If gamedatawrite characterDefaultDatWrite
